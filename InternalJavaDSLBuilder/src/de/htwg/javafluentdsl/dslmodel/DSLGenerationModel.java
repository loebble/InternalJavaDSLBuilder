package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Model for the generation of a internal Java DSL.
 * An Instance of this class describes the structure of a another model 
 * (or more specifically the classes and attributes) which can be used
 * to generate a internal DSL for the model instantiation.
 *
 */
public class DSLGenerationModel {
	
	/**
	 * Constructor for the DSLGenerationModel
	 */
	public DSLGenerationModel(){
		imports = new ArrayList<String>();
		classes = new LinkedHashMap<String,ModelClass>();
	}
	
	/**
	 * The name of the model
	 */
	private String modelName;
	
	/**
	 * A list of imports the model has
	 */
	private List<String> imports;
	
	/**
	 * Indicator if the model uses Lists of attributes
	 */
	private boolean hasList = false;
	
	/**
	 * the name of the factory if the model is a emf model
	 */
	private String emfFactoryName;
	
	/**
	 * Map of classes the model has.
	 * Key of map is the ModelClass name and value the ModelClass object.
	 */
	private final Map<String,ModelClass> classes;
	
	
	/**
	 * Handles the order of the ClassAttributes for the generated ModelClasses in {@link #classes}.
	 * Call this Method after all Classes an their attributes are defined correctly and added to {@link #classes}
	 * @see #setAttributeOrderInClass(ModelClass) setAttributeOrderInClass(ModelClass) - for order algorithm
	 */
	public void setAttributeOrder() {
		for (Map.Entry<String,ModelClass> classEntry : this.getClasses().entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			List<ClassAttribute> optionalAttrs = setAttributeOrderInClass(modelClass);
			handleOptionalAttributes(optionalAttrs, modelClass);
		}
	}
	
	/**
	 * Sets the order of ClassAttributes in a ModelClass
	 * @param modelClass The ModelClass object to order attributes in
	 * @return a List<ClassAttribute> with the simpleOptionalAttributes
	 */
	private List<ClassAttribute> setAttributeOrderInClass(ModelClass modelClass) { //TODO refactor with attributeKind SIMPLE:OPTIONAL_ATTRIBUTE, to reduce complexity
		List<ClassAttribute> firstOptAttr= new ArrayList<>();
		List<ClassAttribute> simpleOptionalAttrs = new ArrayList<>();
		ClassAttribute previousRequiredAttr = null;
		for (ClassAttribute currentAtt : modelClass.getAttributes()) {
			if(currentAtt.getAttributeKind() == AttributeKind.ATTRIBUTE || // mandatory attribute
			currentAtt.getAttributeKind() == AttributeKind.LIST_OF_ATTRIBUTES){ // lists are handled as mandatory attributes
				if(previousRequiredAttr==null){
					previousRequiredAttr = currentAtt;
					// if first attributes are optional add them to the first mandatory attribute(scope)
					currentAtt.setNextSimpleOptAttr(firstOptAttr);
				}
				else if(previousRequiredAttr!=null){
					previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
					previousRequiredAttr = currentAtt;
				}
			}else if(currentAtt.getAttributeKind() == AttributeKind.OPTIONAL_ATTRIBUTE){ // optional attribute
				currentAtt.setOptional(true);
				if(previousRequiredAttr!=null){
					if(currentAtt.isReference()){
						previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
						previousRequiredAttr = currentAtt;
					}else{
						// type is not a modeled Class e.g. a simple types
						previousRequiredAttr.addNextSimpleOptAttr(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}else{
					//reference to another ModelClass?
					if(currentAtt.isReference()){
						previousRequiredAttr = currentAtt;
						currentAtt.setNextSimpleOptAttr(firstOptAttr);
					}else{
						firstOptAttr.add(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}
				
			}
		}
		//Special Case if no required Attribute in Class
		if(previousRequiredAttr == null){
			boolean simpleOptionalsOnly = true;
			for (ClassAttribute attr : modelClass.getSimpleOptAttr()) {
				if(attr.isReference())
					simpleOptionalsOnly = false;
			}
			modelClass.setSimpleOptionalsOnly(simpleOptionalsOnly);
			return simpleOptionalAttrs;
		}
		else{
			previousRequiredAttr.setLastAttribute(true);
			return simpleOptionalAttrs;
		}
	}
	
	/**
	 * Handles the simple optionalAttributes of a ModelClass.
	 * It removes them from the attributes list
	 * and adds them to the optionalAttrubutes list for separation purpose
	 * @param optionalAttrs List of simple optional attributes 
	 * @param modelClass the ModelClass in which the attribute is defined
	 */
	private void handleOptionalAttributes(List<ClassAttribute> optionalAttrs, ModelClass modelClass) {
		for (ClassAttribute optAttr : optionalAttrs) {
			modelClass.addOptionalAttribute(optAttr);
			modelClass.getAttributes().remove(optAttr);
		}
		
	}
	
	/**
	 * Searches the whole DSLGenerationModel for the a ClassAttribute
	 * @param className the name of the Class from
	 * @return the found ClassAttribute in the DSLGenerationModel or null if none has been found
	 */
	public ClassAttribute findAttribute(String className, String attrName){
		ModelClass modelClass= this.classes.get(className);
		if(modelClass != null){
			ClassAttribute foundAttr = modelClass.getSpefificAttributeByFullName(className+attrName);
			if(foundAttr != null){
				return foundAttr;
			}
		}
		return null;
	}
	
	/**
	 * Returns the value of {@link#modelName}
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * Sets the value of {@link#modelName}
	 * @param modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * Returns List of imports the model has
	 * @see {@link #imports}
	 */
	public List<String> getImports() {
		return imports;
	}

	/**
	 * Adds a single import to the list of imports
	 * @param importString the full name of an import
	 * @see {@link #imports}
	 */
	public void addImport(String importString) {
		if(!imports.contains(importString))
			imports.add(importString);
	}
	
	/**
	 * Adds a single ModelClass to this generation model.
	 * If a ModelClass with the same name already was in the list {@link #classes}
	 * it will not be added or replaced.
	 * @param className the ModelClass obj to be added
	 * @return the ModelClass object which is finally in the list {@link #classes}
	 */
	public ModelClass addModelClass(String className) {
		if(!classes.containsKey(className)){
			classes.put(className,new ModelClass(className,this));
			if(classes.size() == 1)
				classes.get(className).setRootModelClass(true);
		}
		return classes.get(className);
	}
	
	/**
	 * Retrieves a ModelClass object from the list {@link #classes} by the given className.
	 * Can throw an Exception if a className which isnt in the list 
	 * @param className the name of the ModelClass
	 * @return
	 */
	public ModelClass getModelClass(String className){
		return classes.get(className);
	}
	
	/**
	 * Returns the Map {@link #classes} which has all defined classes of this Model.
	 * Classes can be added {@link #addModelClass(String)}
	 */
	public Map<String,ModelClass> getClasses() {
		return classes;
	}
	
	/**
	 * Returns the {@link #hasList} value
	 * @return true if model uses at least one list of attributes
	 */
	public boolean isHasList() {
		return hasList;
	}

	/**
	 * Sets the {@link #hasList} value.
	 * @param hasList true if model uses at least one list of attributes
	 */
	public void setHasList(boolean hasList) {
		this.hasList = hasList;
	}

	/**
	 * Returns the {@link #emfFactoryName} value
	 * @return the excact name of the factory
	 */
	public String getEmfFactoryName() {
		return emfFactoryName;
	}

	/**
	 * Sets the {@link #emfFactoryName} value.
	 * @param the excact name of the factory
	 */
	public void setEmfFactoryName(String emfFactoryName) {
		this.emfFactoryName = emfFactoryName;
	}
	
	/**
	 * Creates a String representation of this generation
	 * model with all the classes and attributes
	 * as well as the attributes properties.
	 * @return the created String
	 */
	public String printModel() {
		StringBuffer sb = new StringBuffer();
		sb.append("DSL for: "+this.modelName +"\n");
		sb.append("IMPORTS: ");
		for (String importString : this.imports) {
			sb.append(importString + "; ");
		}
		sb.append("\n"+"CLASSES: ");
		for (Map.Entry<String, ModelClass> entry : this.classes.entrySet()) {
			ModelClass modelClass = (ModelClass) entry.getValue();
			sb.append("\n" + "ModelClass: "+modelClass.getClassName() +"\n");
			sb.append("Imports: ");
			for (String imp : modelClass.getImports()) {
				sb.append(imp + "; ");
			}
			sb.append("\n");
			for (ClassAttribute attr : modelClass.getAttributes()) {
				sb.append("\t" + "Name: " +attr.getAttributeName() + " type: " +attr.getType() 
						+ " kind: " +attr.getAttributeKind()+" " + " reference: " +attr.isReference()
						+ " optional: " +attr.isOptional() +" list:" + attr.isList());
				if(attr.getOpposite()!=null)
					sb.append(" opposite: " +attr.getOpposite().getAttributeFullName()
							+ " isRefBy: " +attr.isReferencedByAttribute()+
							" isCreatorOfOpp: " +attr.isCreatorOfOpposite());
				sb.append("\n");
			}
			for (ClassAttribute attr : modelClass.getSimpleOptAttr()) {
				sb.append("\t" + "Name: " +attr.getAttributeName() + " type: " +attr.getType() 
						+ " kind: " +attr.getAttributeKind()+" " + " reference: " +attr.isReference()
						+ " optional: " +attr.isOptional()+ "\n");
			}
			for (ClassAttribute referencedCl : modelClass.getCreatedByOpposite()) {
				sb.append("\t" + referencedCl.getOpposite().getClassName() + "." + referencedCl.getOpposite().getAttributeName() +" referenced by nestedClassAttr: "+referencedCl.getClassName() + "." + referencedCl.getAttributeName() +"\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Creates a String representation of the order the classes and attributes 
	 * that are in this model.
	 * 
	 * @return the created String with the corresponding order
	 */
	public String printOrder(){
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, ModelClass> entry : this.classes.entrySet()) {
			ModelClass modelClass = (ModelClass) entry.getValue();
			sb.append("\n" + "ModelClass: "+modelClass.getClassName() +"\n");
			int i = 0;
			for (ClassAttribute attr : modelClass.getAttributes()) {
				if(i == 0)
					sb.append(" "+attr.getAttributeName() +":" +attr.getType());
				if(attr.getNextAttribute() != null)
					sb.append("-> "+ attr.getNextAttribute().getAttributeName() +":" +attr.getNextAttribute().getType());
				for (ClassAttribute nextOptional : attr.getNextSimpleOptAttr()) {
					sb.append("(->"+nextOptional.getAttributeName()+":" +nextOptional.getType()+")");
				}
				i++;
			}
		}
		return sb.toString();
	}
	
}
