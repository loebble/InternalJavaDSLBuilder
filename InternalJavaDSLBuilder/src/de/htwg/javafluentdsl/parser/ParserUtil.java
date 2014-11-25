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
	 * It separates the attributes to be set in their own scope
	 * from the simple optional ones which are set in the scope of a mandatory attribute.
	 * Also tells the ModelClass which opposite Attribute it has to set when an instance is created.
	 * For each dependency kind a list is available in the ModelClass and thus the
	 * attributes are added to the suited list.
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
			//Lists and attributes to set in dsl
			if(currentAtt.getDependencyKind() == DependencyKind.ATTRIBUTE || 
			currentAtt.getDependencyKind() == DependencyKind.LIST_OF_ATTRIBUTES){
				attributesToSet.add(currentAtt);
				if(previousRequiredAttr==null){
					previousRequiredAttr = currentAtt;
					// if first attributes are optional add them to the first mandatory attribute(scope)
					currentAtt.setNextSimpleOptAttr(firstOptAttr);
				}
				else{
					previousRequiredAttr.setNextAttribute(currentAtt);
					previousRequiredAttr = currentAtt;
				}
			}
			//optional_attribute dependency is a simple optional attributes in the dsl model
			else if(currentAtt.getDependencyKind() == DependencyKind.OPTIONAL_ATTRIBUTE){
				currentAtt.setOptional(true);
				simpleOptionalAttrs.add(currentAtt);
				if(previousRequiredAttr!=null){
					previousRequiredAttr.addNextSimpleOptAttr(currentAtt);
				}else{
					//add it to the list of the first optional attributes
					firstOptAttr.add(currentAtt);
				}
			}
			else if(currentAtt.getDependencyKind() == DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET){
				//adds the current attribute to its creators oppositesToSetList
				currentAtt.getOpposite().addOppositeToSet(currentAtt);
			}
		}
		for (ClassAttribute attrToSet : attributesToSet) {
			attrToSet.setDependencyKind(DependencyKind.ATTRIBUTE);
			modelClass.addAttributeToSet(attrToSet);
		}
		for (ClassAttribute simpleOptAttr : simpleOptionalAttrs) {
			modelClass.addSimpleOptionalAttribute(simpleOptAttr);
		}
		//Special case if no required attribute in class
		if(previousRequiredAttr == null){
			modelClass.setSimpleOptionalsOnly(true);
		}
		else{
			previousRequiredAttr.setLastAttribute(true);
		}
	}

}
