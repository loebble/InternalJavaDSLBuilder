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
		className = name;
		attributes = new ArrayList<ClassAttribute>();
		simpleOptAttr = new ArrayList<ClassAttribute>();
		createdByOpposite = new ArrayList<ClassAttribute>();
		this.imports = new ArrayList<String>();
	}

	/**
	 * Searches {@link #attributes} for the given fullAttributeName. The
	 * fullAttributeName is defined by the class name + the attribute name.
	 * 
	 * @param fullAttributeName
	 *            name to search for
	 * @return the found ClassAttribute or null if none found
	 * @see ClassAttribute#getAttributeFullName()
	 */
	public ClassAttribute getSpefificAttributeByFullName(
			String fullAttributeName) {
		for (ClassAttribute attr : this.attributes) {
			if (attr.getAttributeFullName().equals(fullAttributeName))
				return attr;
		}
		return null;
	}

	/**
	 * Searches {@link #attributes} for the given attribute name.
	 * 
	 * @param attributeName
	 *            name to search for
	 * @return the found ClassAttribute or null if none found
	 * @see ClassAttribute#getAttributeName()
	 */
	public ClassAttribute getSpefificAttribute(String attributeName) {
		for (ClassAttribute attr : this.attributes) {
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
	 * Holds the mandatory, list and reference attributes of this ModelClass
	 */
	private List<ClassAttribute> attributes;
	/**
	 * Holds the simple optional attributes this ModelClass has
	 */
	private List<ClassAttribute> simpleOptAttr;
	/**
	 * Holds the opposite Attributes which are set in the DSL by this ModelClass
	 */
	private List<ClassAttribute> createdByOpposite;
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
	 * Gets {@link #attributes}.
	 */
	public List<ClassAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * Gets {@link #simpleOptAttr}.
	 */
	public List<ClassAttribute> getSimpleOptAttr() {
		return simpleOptAttr;
	}

	/**
	 * Adds a {@link ClassAttribute} to this ModelClass
	 * 
	 * @param attribute
	 *            the attribute to be added
	 */
	public void addAttribute(ClassAttribute attribute) {
		attribute.setModelClass(this);
		attribute.setClassName(this.className);
		attribute.setAttributeFullName(this.className
				+ attribute.getAttributeName());
		this.attributes.add(attribute);
	}

	/**
	 * Adds a Attribute to {@link #simpleOptAttr}
	 * 
	 * @param attribute
	 *            the attribute to be added
	 */
	public void addSimpleOptionalAttribute(ClassAttribute attribute) {
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
	 * Gets {@link #createdByOpposite}.
	 */
	public List<ClassAttribute> getCreatedByOpposite() {
		return createdByOpposite;
	}

	/**
	 * Adds ClassAttribute to {@link #createdByOpposite}.
	 * 
	 * @param createdByOpposite
	 *            the ClassAttribute to be added
	 */
	public void addCreatedByOpposite(ClassAttribute createdByOpposite) {
		this.createdByOpposite.add(createdByOpposite);
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
