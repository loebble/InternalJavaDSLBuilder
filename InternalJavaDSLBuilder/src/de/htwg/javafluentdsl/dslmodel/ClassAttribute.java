package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a defined attribute in a {@link ModelClass}
 * with all its properties
 *
 */
public final class ClassAttribute{
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
	private boolean isReference = false;
	
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
	 * Holds a List of next optional Attributes. They can be
	 * set in the same scope in the method chain.
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
	 * which always has to have at least a name, a type and a className.
	 * The {@link #attributeFullName} is the combined className and name
	 * because this has to be unique in the {@link DSLGenerationModel}
	 * @param name the attribues name
	 * @param type the type of the attribute
	 * @param className the name of the ModelClass the attribute is part of
	 */
	public ClassAttribute(String name, String type, String className){
		this.attributeName = name;
		this.type = type;
		this.className = className;
		this.attributeFullName = className + name;
		this.nextSimpleOptAttr = new ArrayList<ClassAttribute>();
	}
	
	/**
	 * @return {@link #type}
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets {@link #type}
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Returns {@link #optional}
	 * @return true if attribute is optional
	 */
	public boolean isOptional() {
		return optional;
	}
	/**
	 * Sets {@link #optional}
	 * @param optional
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	public ClassAttribute getNextAttribute() {
		return nextAttribute;
	}
	/**
	 * Sets {@link #nextAttribute} value.
	 * Which declares which attribute comes 
	 * next in the method chain.
	 * @param nextAttribute the next {@link ClassAttribute}
	 */
	public void setNextAttribute(ClassAttribute nextAttribute) {
		this.nextAttribute = nextAttribute;
	}
	/**
	 * Returns {@link #nextSimpleOptAttr} value.
	 */
	public List<ClassAttribute> getNextSimpleOptAttr() {
		return nextSimpleOptAttr;
	}
	/**
	 * Adds a {@link ClassAttribute} to the {@link #nextSimpleOptAttr} list.
	 * @param nextOptionalAttribute the attribute to add
	 */
	public void addNextSimpleOptAttr(ClassAttribute nextOptionalAttribute) {
		this.nextSimpleOptAttr.add(nextOptionalAttribute);
	}
	/**
	 * Sets the List {@link #nextSimpleOptAttr}.
	 * @param nextOptionalAttributes
	 */
	public void setNextSimpleOptAttr(
			List<ClassAttribute> nextOptionalAttributes) {
		this.nextSimpleOptAttr = nextOptionalAttributes;
	}
	/**
	 * Gets {@link #attributeName}.
	 */
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * Sets {@link #attributeName}.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	/**
	 * Gets {@link #kind}.
	 */
	public AttributeKind getAttributeKind() {
		return kind;
	}
	/**
	 * Sets {@link #kind}.
	 */
	public void setAttributeKind(AttributeKind kind) {
		this.kind = kind;
	}
	/**
	 * Gets {@link #attributeFullName}.
	 */
	public String getAttributeFullName() {
		return attributeFullName;
	}
	/**
	 * Sets {@link #attributeFullName}.
	 */
	public String setAttributeFullName(String fullName) {
		return fullName;
	}
	/**
	 * Gets {@link #lastAttribute}.
	 */
	public boolean isLastAttribute() {
		return lastAttribute;
	}
	/**
	 * Sets {@link #lastAttribute}.
	 */
	public void setLastAttribute(boolean lastAttribute) {
		this.lastAttribute = lastAttribute;
	}
	/**
	 * Gets {@link #isReference} value.
	 */
	public boolean isReference() {
		return isReference;
	}
	/**
	 * Sets {@link #isReference}.
	 */
	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}
	/**
	 * Sets {@link #opposite} value.
	 */
	public void setOpposite(ClassAttribute opp) {
		this.opposite=opp;
	}
	/**
	 * Gets {@link #opposite} value.
	 */
	public ClassAttribute getOpposite() {
		return this.opposite;
	}
	/**
	 * Gets {@link #referencedBy} value.
	 */
	public ClassAttribute getReferencedBy() {
		return referencedBy;
	}
	/**
	 * Sets {@link #referencedBy} value.
	 * And adds the given ClassAttribute to this 
	 * {@link ModelClass#referencedByOpposite} List.
	 */
	public void setReferencedBy(ClassAttribute referencedBy) {
		this.referencedBy = referencedBy;
		this.getModelClass().addCreatedByOpposite(referencedBy);
	}
	/**
	 * Gets {@link #isReferencedByAttribute} value.
	 */
	public boolean isReferencedByAttribute() {
		return isReferencedByAttribute;
	}
	/**
	 * Sets {@link #referencedBy} value.
	 */
	public void setReferencedByAttribute(boolean isReferencedByAttribute) {
		this.isReferencedByAttribute = isReferencedByAttribute;
	}
	/**
	 * Gets {@link #className} value.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Sets {@link #className} value.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * Gets {@link #isList} value.
	 */
	public boolean isList() {
		return isList;
	}
	/**
	 * Sets {@link #isList} value.
	 */
	public void setList(boolean isList) {
		this.isList = isList;
	}
	/**
	 * Gets {@link #modelClass} value.
	 */
	public ModelClass getModelClass() {
		return modelClass;
	}
	/**
	 * Sets {@link #modelClass} value.
	 */
	public void setModelClass(ModelClass modelClass) {
		this.modelClass = modelClass;
	}
	/**
	 * Gets {@link #isCreatorOfOpposite} value.
	 */
	public boolean isCreatorOfOpposite() {
		return isCreatorOfOpposite;
	}
	/**
	 * Sets {@link #isCreatorOfOpposite} value.
	 */
	public void setCreatorOfOpposite(boolean isCreatorOfOpposite) {
		this.isCreatorOfOpposite = isCreatorOfOpposite;
	}
	
}