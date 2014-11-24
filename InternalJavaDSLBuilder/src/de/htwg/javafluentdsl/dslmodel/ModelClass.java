package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Class the model defines. Also contains all the Attributes the
 * model defined for this class {@link ClassAttribute}
 *
 */
public class ModelClass {

	/**
	 * Constructor for a ModelClass. The ModelClass needs a name which is unique
	 * in the model and a {@link DSLGenerationModel} instance it belongs to
	 * 
	 * @param name
	 *            The unique name of this ModelClass
	 * @param model
	 *            The correponding DSLGenerationModel
	 */
	public ModelClass(String name, DSLGenerationModel model) {
		this.model = model;
		this.className = name;
		this.allAttributes = new ArrayList<ClassAttribute>();
		this.attributesToSet = new ArrayList<ClassAttribute>();
		this.simpleOptAttr = new ArrayList<ClassAttribute>();
		this.oppositesToSet = new ArrayList<ClassAttribute>();
		this.imports = new ArrayList<String>();
	}

	/**
	 * Searches {@link #attributesToSet} for the given fullAttributeName. The
	 * fullAttributeName is defined by the class name + the attribute name.
	 * 
	 * @param fullAttributeName
	 *            name to search for
	 * @return the found ClassAttribute or null if none found
	 * @see ClassAttribute#getAttributeFullName()
	 */
	public ClassAttribute getSpefificAttributeByFullName(
			String fullAttributeName) {
		for (ClassAttribute attr : this.allAttributes) {
			if (attr.getAttributeFullName().equals(fullAttributeName))
				return attr;
		}
		return null;
	}

	/**
	 * Searches {@link #attributesToSet} for the given attribute name.
	 * 
	 * @param attributeName
	 *            name to search for
	 * @return the found ClassAttribute or null if none found
	 * @see ClassAttribute#getAttributeName()
	 */
	public ClassAttribute getSpefificAttribute(String attributeName) {
		for (ClassAttribute attr : this.allAttributes) {
			if (attr.getAttributeName().equals(attributeName))
				return attr;
		}
		return null;
	}

	/**
	 * Reference to the {@link DSLGenerationModel} this ModelClass is part of
	 */
	private DSLGenerationModel model;
	/**
	 * The unique class name
	 */
	private String className;
	/**
	 * True if this ModelClass is the root Model Class for the Generation Model
	 * 
	 */
	private boolean rootModelClass;
	
	/**
	 * A List which holds all Attributes of the this ModelClass.
	 * All Attributes means regardless of their dependency kind and other values.
	 */
	private ArrayList<ClassAttribute> allAttributes;
	
	/**
	 * Holds the mandatory, list and reference attributes of this ModelClass
	 */
	private List<ClassAttribute> attributesToSet;
	/**
	 * Holds the simple optional attributes this ModelClass has
	 */
	private List<ClassAttribute> simpleOptAttr;
	/**
	 * Holds the opposite Attributes of this ModelClass which has to set the other ends
	 * (i.e. their opposite) reference to complete a bidirectional relation. 
	 */
	private List<ClassAttribute> oppositesToSet;
	/**
	 * True if this ModelClass only consists of simple optional attributes
	 * 
	 * @see #simpleOptAttr
	 */
	private boolean simpleOptionalsOnly;
	/**
	 * Holds the imports this ModelClass needs
	 */
	private List<String> imports;
	/**
	 * True if this ModelClass has at least one list attribute
	 * 
	 * @see ClassAttribute#isList()
	 */
	private boolean hasList = false;

	/**
	 * Gets {@link #simpleOptionalsOnly}.
	 */
	public boolean isSimpleOptionalsOnly() {
		return simpleOptionalsOnly;
	}

	/**
	 * Gets {@link #attributesToSet}.
	 */
	public List<ClassAttribute> getAttributesToSet() {
		return attributesToSet;
	}

	/**
	 * Gets {@link #simpleOptAttr}.
	 */
	public List<ClassAttribute> getSimpleOptAttr() {
		return simpleOptAttr;
	}
	
	/**
	 * gets All attributes of this ModelClass
	 * @see {@link #allAttributes}
	 */
	public ArrayList<ClassAttribute> getAllAttributes() {
		return allAttributes;
	}

	/**
	 * 
	 * @param allAttributes
	 */
	public void addToAllAttributes(ClassAttribute attribute) {
		this.allAttributes.add(attribute);
	}

	/**
	 * Adds a {@link ClassAttribute} to this ModelClass
	 * 
	 * @param attribute
	 *            the attribute to be added
	 */
	public void addAttributeToSet(ClassAttribute attribute) {
//		if(attribute.getDependencyKind() != DependencyKind.ATTRIBUTE
//				&& attribute.getDependencyKind() != DependencyKind.LIST_OF_ATTRIBUTES)
//			throw new IllegalArgumentException("Not the right DependencyKind for list!"
//					+ "Given: " +attribute.getDependencyKind() +" expected: " +DependencyKind.ATTRIBUTE 
//					+ " OR "+DependencyKind.LIST_OF_ATTRIBUTES);
		this.attributesToSet.add(attribute);
	}

	/**
	 * Adds a Attribute to {@link #simpleOptAttr}
	 * 
	 * @param attribute
	 *            the attribute to be added
	 */
	public void addSimpleOptionalAttribute(ClassAttribute attribute) {
//		if(attribute.getDependencyKind() != DependencyKind.OPTIONAL_ATTRIBUTE)
//			throw new IllegalArgumentException("Not the right DependencyKind for list!"
//					+ "Given: " +attribute.getDependencyKind() +" expected: " +DependencyKind.OPTIONAL_ATTRIBUTE);
		this.simpleOptAttr.add(attribute);
	}

	/**
	 * Gets {@link #className}.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets {@link #className}.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Returns the value of {@link #rootModelClass}
	 * @return true if this class is rootModelClass.
	 */
	public boolean isRootModelClass() {
		return rootModelClass;
	}

	/**
	 * Sets the value of {@link #rootModelClass}
	 */
	public void setRootModelClass(boolean rootModelClass) {
		this.rootModelClass = rootModelClass;
	}

	/**
	 * Gets {@link #oppositesToSet}.
	 */
	public List<ClassAttribute> getOppositesToSet() {
		return oppositesToSet;
	}

	/**
	 * Adds ClassAttribute to {@link #oppositesToSet}.
	 * 
	 * @param opToSet
	 *            the ClassAttribute to be added
	 */
	public void addOppositeToSet(ClassAttribute opToSet) {
//		if(opToSet.getDependencyKind() != DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET)
//			throw new IllegalArgumentException("Not the right DependencyKind for list!"
//					+ "Given: " +opToSet.getDependencyKind() +" expected: " +DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET);
		this.oppositesToSet.add(opToSet);
	}

	/**
	 * Sets {@link #simpleOptionalsOnly}.
	 */
	public void setSimpleOptionalsOnly(boolean b) {
		this.simpleOptionalsOnly = b;
	}

	/**
	 * Gets {@link #imports}.
	 */
	public List<String> getImports() {
		return imports;
	}

	/**
	 * Adds a String to {@link #imports}.
	 */
	public void addImport(String importString) {
		if (!imports.contains(importString))
			imports.add(importString);
	}

	/**
	 * Gets {@link #model}.
	 */
	public DSLGenerationModel getModel() {
		return model;
	}

	/**
	 * Gets the {@link #hasList} value.
	 */
	public boolean isHasList() {
		return hasList;
	}

	/**
	 * Sets the {@link #hasList} value.
	 */
	public void setHasList(boolean hasList) {
		this.hasList = hasList;
	}

}
