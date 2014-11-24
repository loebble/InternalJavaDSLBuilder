package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

import de.htwg.javafluentdsl.parser.PrimitiveType;

/**
 * Represents a defined attribute in a {@link ModelClass} with all its
 * properties
 *
 */
public final class ClassAttribute {
	/**
	 * Value of the name
	 */
	private String attributeName;
	/**
	 * Fully identifying name consists of the class name + the attributes name
	 */
	private String attributeFullName;
	/**
	 * the name of the ModelClass the attribute is part of
	 */
	private String className;
	/**
	 * a reference to the ModelClass the attribute is part of
	 */
	private ModelClass modelClass;
	/**
	 * the type of this ClassAttribute
	 */
	private String type;
	/**
	 * True if the attribute is not mandatory in the model
	 */
	private boolean optional = false;
	/**
	 * True if this is the last attribute of a ModelClass
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
	 * True if this attribute has a primitve type
	 */
	private boolean isPrimitive = false;

	/**
	 * True if this attribute creates (or assigns the value) of a referencing
	 * (opposite) attribute
	 */
	private boolean isCreatorOfOpposite = false;
	/**
	 * Holds the kind of this attributes dependency {@link DependencyKind}
	 */
	private DependencyKind kind;
	/**
	 * Holds the next mandatory attribute to set in the ModelClass
	 */
	private ClassAttribute nextAttribute;
	/**
	 * Holds a List of next optional Attributes. They can be set in the same
	 * scope in the method chain.
	 */
	private List<ClassAttribute> nextSimpleOptAttr;
	/**
	 * Holds the opposite ClassAttribute if there is one
	 */
	private ClassAttribute opposite;

	/**
	 * Constructor for creating a new ClassAttribute which always has to have at
	 * least a name, a type and must be part of a {@link ModelClass}.
	 * The Attribute is added to the given ModelClasses {@link ModelClass#allAttributes} list. 
	 * The {@link #attributeFullName} is the combined className and attributeName because
	 * this has to be unique in the {@link DSLGenerationModel}.
	 * 
	 * @param name
	 *            the attributes name
	 * @param type
	 *            the type of the attribute
	 * @param modelClass
	 *            the ModelClass the attribute is part of
	 */
	public ClassAttribute(String name, String type, ModelClass modelClass) {
		this.attributeName = name;
		for (PrimitiveType primType : PrimitiveType.values()) {
			if (type.equals(primType.getKeyword()))
				this.isPrimitive = true;
		}
		this.type = type;
		this.modelClass = modelClass;
		this.className = modelClass.getClassName();
		this.attributeFullName = className + name;
		this.nextSimpleOptAttr = new ArrayList<ClassAttribute>();
		modelClass.addToAllAttributes(this);
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
	 * 
	 * @return true if attribute is optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Sets {@link #optional}
	 * 
	 * @param optional
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public ClassAttribute getNextAttribute() {
		return nextAttribute;
	}

	/**
	 * Sets {@link #nextAttribute} value. Which declares which attribute comes
	 * next in the method chain.
	 * 
	 * @param nextAttribute
	 *            the next {@link ClassAttribute}
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
	 * 
	 * @param nextOptionalAttribute
	 *            the attribute to add
	 */
	public void addNextSimpleOptAttr(ClassAttribute nextOptionalAttribute) {
		this.nextSimpleOptAttr.add(nextOptionalAttribute);
	}

	/**
	 * Sets the List {@link #nextSimpleOptAttr}.
	 * 
	 * @param nextOptionalAttributes
	 */
	public void setNextSimpleOptAttr(List<ClassAttribute> nextOptionalAttributes) {
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
	public DependencyKind getDependencyKind() {
		return kind;
	}

	/**
	 * Sets {@link #kind}.
	 */
	public void setDependencyKind(DependencyKind kind) {
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
		this.opposite = opp;
	}

	/**
	 * Gets {@link #opposite} value.
	 */
	public ClassAttribute getOpposite() {
		return this.opposite;
	}

	/**
	 * Adds the given ClassAttribute to this {@link ModelClass#oppositesToSet}
	 * List.
	 */
	public void addOppositeToSet(ClassAttribute referencedBy) {
		this.getModelClass().addOppositeToSet(referencedBy);
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

	public boolean isPrimitive() {
		return isPrimitive;
	}
}