package de.htwg.javafluentdsl.parser;

import java.util.ArrayList;
import java.util.List;

import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.DependencyKind;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * Util Class for Parsers.
 * Helps to set the Attribute Order in a DSLGenerationModel.
 * Used by Parsers if they retrieved the Classes and Attributes of a Model and
 * created a DSLGenerationModel with them.
 *
 */
public final class ParserUtil {
	/**
	 * Util Class, no insantiation needed
	 */
	private ParserUtil(){}
	
	/**
	 * Handles the order of the ClassAttributes for the generated ModelClasses in the given DSLGenerationModel.
	 * @see #createAttributeOrder(ModelClass) createAttributeOrder(ModelClass) - for ordering details
	 */
	public static void createAttributeOrder(DSLGenerationModel genModel) {
		for (ModelClass modelClass : genModel.getClasses()) {
			createAttributeOrder(modelClass);
		}
	}
	
	/**
	 * Handles the order of ClassAttributes in a ModelClass.
	 * It separates the attributes to be set from the simple optional ones,
	 * so the corresponding lists in the DSLGenerationModel can be filled with them.
	 * Attributes with DepencyKind {@link DependencyKind} OPPOSITE_ATTRIBUTE_TO_SET are not
	 * processed because they are set by their opposite.
	 * @param modelClass The ModelClass object to order attributes in
	 * @return a List with the simple optional attributes
	 */
	private static void createAttributeOrder(ModelClass modelClass) {
		// a list for the case that the first attributes are optional
		List<ClassAttribute> firstOptAttr= new ArrayList<>();
		List<ClassAttribute> simpleOptionalAttrs = new ArrayList<>();
		List<ClassAttribute> attributesToSet = new ArrayList<>();
		ClassAttribute previousRequiredAttr = null;
		for (ClassAttribute currentAtt : modelClass.getAllAttributes()) {
			if(currentAtt.getDependencyKind() == DependencyKind.ATTRIBUTE || // mandatory attribute
			currentAtt.getDependencyKind() == DependencyKind.LIST_OF_ATTRIBUTES){ // lists are handled as mandatory attributes
				attributesToSet.add(currentAtt);
				if(previousRequiredAttr==null){
					previousRequiredAttr = currentAtt;
					// if first attributes are optional add them to the first mandatory attribute(scope)
					currentAtt.setNextSimpleOptAttr(firstOptAttr);
				}
				else if(previousRequiredAttr!=null){
					previousRequiredAttr.setNextAttribute(currentAtt);
					previousRequiredAttr = currentAtt;
				}
			}else if(currentAtt.getDependencyKind() == DependencyKind.OPTIONAL_ATTRIBUTE){
				currentAtt.setOptional(true);
				if(previousRequiredAttr!=null){
					//If its a reference to another modeled class it has also to be set
					//even if its an optional attribute.
					if(currentAtt.isReference()){
						attributesToSet.add(currentAtt);
						previousRequiredAttr.setNextAttribute(currentAtt);
						previousRequiredAttr = currentAtt;
					}else{
						// type is not a modeled Class so it is a simple dependency
						previousRequiredAttr.addNextSimpleOptAttr(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}else{
					if(currentAtt.isReference()){
						attributesToSet.add(currentAtt);
						previousRequiredAttr = currentAtt;
						currentAtt.setNextSimpleOptAttr(firstOptAttr);
					}else{
						//add it to the list of the first optional attributes
						firstOptAttr.add(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}
			}
		}
		
		//Special case if no required Attribute in Class
		if(previousRequiredAttr == null){
			boolean simpleOptionalsOnly = true;
			for (ClassAttribute attr : modelClass.getSimpleOptAttr()) {
				if(attr.isReference())
					simpleOptionalsOnly = false;
			}
			modelClass.setSimpleOptionalsOnly(simpleOptionalsOnly);
			
		}
		else{
			previousRequiredAttr.setLastAttribute(true);
		}
		
		for (ClassAttribute attrToSet : attributesToSet) {
			modelClass.addAttributeToSet(attrToSet);
		}
		for (ClassAttribute simpleOptAttr : simpleOptionalAttrs) {
			modelClass.addSimpleOptionalAttribute(simpleOptAttr);
		}
	}

}
