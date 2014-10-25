package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

public class ClassAttribute{
	/**
	 * Value of the name
	 */
	private String attributeName;
	/**
	 * Fully identifying name consists of the
	 * class name + the attributes name
	 */
	private String attributeFullName;
	/**
	 * the name of the ModelClass the attribute
	 * is part of
	 */
	private String className;
	/**
	 * a reference to the ModelClass the attribute
	 * is part of
	 */
	private ModelClass modelClass;
	/**
	 * the type of this ClassAttribute
	 */
	private String type;
	/**
	 * True if the attribute is not mandatory
	 * in the model
	 */
	private boolean optional = false;
	/**
	 * True if this is the last attribute of
	 * a ModelClass
	 */
	private boolean lastAttribute = false;
	/**
	 * True if this attribute is a List
	 */
	private boolean isList = false;
	
	/**
	 * True if this attribute is a reference of an ModelClass
	 */
	private boolean isReference;
	
	/**
	 * True if this attribute is Referenced by a opposite attribute
	 */
	private boolean isReferencedByAttribute = false;
	
	/**
	 * True if this attribute creates (or assigns the value)
	 * of a referencing (opposite) attribute
	 */
	private boolean isCreatorOfOpposite = false;
	/**
	 * Holds the kind of this attribute {@link AttributeKind}
	 */
	private AttributeKind kind;
	/**
	 * Holds the next mandatory attribute to set in the
	 * ModelClass
	 */
	private ClassAttribute nextAttribute;
	/**
	 * Holds a List of optional Attributes which can be
	 * set in the same scope
	 */
	private List<ClassAttribute> nextSimpleOptAttr;
	/**
	 * Holds the opposite ClassAttribute if there is one
	 */
	private ClassAttribute opposite;
	/**
	 * Holds the opposite Attribute which references to this one
	 * @see #isReferencedByAttribute
	 */
	private ClassAttribute referencedBy = null;
	
	/**
	 * Constructor for creating a new ClassAttribute
	 * which always has to have at least a name, a type and a className
	 * @param name
	 * @param type
	 * @param className
	 */
	public ClassAttribute(String name, String type, String className){
		this.attributeName = name;
		this.type = type;
		this.className = className;
		this.attributeFullName = className + name;
		this.nextSimpleOptAttr = new ArrayList<ClassAttribute>();
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isOptional() {
		return optional;
	}
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	public ClassAttribute getNextAttribute() {
		return nextAttribute;
	}
	public void setNextAttribute(ClassAttribute nextAttribute) {
		this.nextAttribute = nextAttribute;
	}
	public List<ClassAttribute> getNextSimpleOptAttr() {
		return nextSimpleOptAttr;
	}
	public void addNextSimpleOptAttr(ClassAttribute nextOptionalAttribute) {
		this.nextSimpleOptAttr.add(nextOptionalAttribute);
	}
	public void setNextSimpleOptAttr(
			List<ClassAttribute> nextOptionalAttributes) {
		this.nextSimpleOptAttr = nextOptionalAttributes;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public AttributeKind getAttributeKind() {
		return kind;
	}

	public void setAttributeKind(AttributeKind kind) {
		this.kind = kind;
	}

	public String getAttributeFullName() {
		return attributeFullName;
	}
	
	public String setAttributeFullName(String fullName) {
		return fullName;
	}

	public boolean isLastAttribute() {
		return lastAttribute;
	}

	public void setLastAttribute(boolean lastAttribute) {
		this.lastAttribute = lastAttribute;
	}

	public boolean isReference() {
		return isReference;
	}

	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}

	public void setOpposite(ClassAttribute opp) {
		this.opposite=opp;
	}
	
	public ClassAttribute getOpposite() {
		return this.opposite;
	}

	public ClassAttribute getReferencedBy() {
		return referencedBy;
	}

	public void setReferencedBy(ClassAttribute referencedBy) {
		this.referencedBy = referencedBy;
		this.getModelClass().addCreatedByOpposite(referencedBy);
	}

	public boolean isReferencedByAttribute() {
		return isReferencedByAttribute;
	}

	public void setReferencedByAttribute(boolean isReferencedByAttribute) {
		this.isReferencedByAttribute = isReferencedByAttribute;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public ModelClass getModelClass() {
		return modelClass;
	}

	public void setModelClass(ModelClass modelClass) {
		this.modelClass = modelClass;
	}

	public boolean isCreatorOfOpposite() {
		return isCreatorOfOpposite;
	}

	public void setCreatorOfOpposite(boolean isCreatorOfOpposite) {
		this.isCreatorOfOpposite = isCreatorOfOpposite;
	}
	
}