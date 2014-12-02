package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a defined attribute in a {@link ModelClass} with all its
 * properties.
 */
public final class ClassAttribute {
    /**
     * Value of the name.
     */
    private String attributeName;
    /**
     * Fully identifying name consists of the class name + the attributes name.
     */
    private String attributeFullName;
    /**
     * the name of the ModelClass the attribute is part of.
     */
    private String className;
    /**
     * a reference to the ModelClass the attribute is part of.
     */
    private ModelClass modelClass;
    /**
     * the type of this ClassAttribute.
     */
    private String type;
    /**
     * True if the attribute is required in the model. But not necessarily mean
     * that the attribute kind is {@link DependencyKind#OPTIONAL_ATTRIBUTE}.
     */
    private boolean optional = false;
    /**
     * True if this is the last attribute of a ModelClass.
     */
    private boolean lastAttribute = false;
    /**
     * True if this attribute is a List.
     */
    private boolean isList = false;

    /**
     * If true this attribute type is another modeled class
     * in this DSLGenerationModel.
     */
    private boolean isReference = false;

    /**
     * True if this attribute has a primitve type.
     */
    private boolean isPrimitive = false;

    /**
     * True if this attribute creates (or assigns the value) of a referencing
     * (opposite) attribute.
     */
    private boolean isCreatorOfOpposite = false;
    /**
     * Holds the kind of this attributes dependency {@link DependencyKind}.
     */
    private DependencyKind kind;
    /**
     * Holds the next mandatory attribute to set in the ModelClass.
     */
    private ClassAttribute nextAttribute;
    /**
     * Holds a List of next optional Attributes. They can be set in the same
     * scope in the method chain.
     */
    private List<ClassAttribute> nextSimpleOptAttr;
    /**
     * Holds the opposite ClassAttribute if there is one.
     */
    private ClassAttribute opposite;

    /**
     * Constructor for creating a new ClassAttribute which always has to have
     * atleast a name, a type and must be part of a {@link ModelClass}. The
     * Attribute is added to the given ModelClasses
     * {@link ModelClass#allAttributes} list. The {@link #attributeFullName} is
     * the combined className and attributeName because this has to be unique in
     * the {@link DSLGenerationModel}.
     * 
     * @param name
     *            the attributes name.
     * @param attrType
     *            the type of the attribute.
     * @param modelClassToSet
     *            the ModelClass the attribute is part of.
     */
    public ClassAttribute(final String name, final String attrType,
            final ModelClass modelClassToSet) {
        this.attributeName = name;
        for (PrimitiveType primType : PrimitiveType.values()) {
            if (attrType.equals(primType.getKeyword())) {
                this.isPrimitive = true;
            }
        }
        this.type = attrType;
        this.modelClass = modelClassToSet;
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
     * Sets {@link #type}.
     * 
     * @param attrType
     *            the type to set
     */
    public void setType(final String attrType) {
        this.type = attrType;
    }

    /**
     * Returns {@link #optional}.
     * 
     * @return true if attribute is optional
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Sets {@link #optional}.
     * 
     * @param optionalValue
     *            value to set
     */
    public void setOptional(final boolean optionalValue) {
        this.optional = optionalValue;
    }

    /**
     * @return {@link #nextAttribute}
     */
    public ClassAttribute getNextAttribute() {
        return nextAttribute;
    }

    /**
     * Sets {@link #nextAttribute} value. Which declares which attribute comes
     * next in the method chain.
     * 
     * @param nextAttributeToSet
     *            the next {@link ClassAttribute}.
     */
    public void setNextAttribute(final ClassAttribute nextAttributeToSet) {
        this.nextAttribute = nextAttributeToSet;
    }

    /**
     * @return {@link #nextSimpleOptAttr} value.
     */
    public List<ClassAttribute> getNextSimpleOptAttr() {
        return nextSimpleOptAttr;
    }

    /**
     * Adds a {@link ClassAttribute} to the {@link #nextSimpleOptAttr} list.
     * 
     * @param nextOptionalAttributeToAdd
     *            the attribute to add
     */
    public void addNextSimpleOptAttr(
            final ClassAttribute nextOptionalAttributeToAdd) {
        this.nextSimpleOptAttr.add(nextOptionalAttributeToAdd);
    }

    /**
     * Sets the List {@link #nextSimpleOptAttr}.
     * 
     * @param nextOptionalAttributes
     *            value
     */
    public void setNextSimpleOptAttr(
            final List<ClassAttribute> nextOptionalAttributes) {
        this.nextSimpleOptAttr = nextOptionalAttributes;
    }

    /**
     * @return {@link #attributeName}.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Sets the Name of the {@link #attributeName}.
     * 
     * @param attributeNameToSet
     *            value to set
     */
    public void setAttributeName(final String attributeNameToSet) {
        this.attributeName = attributeNameToSet;
    }

    /**
     * @return {link #kind}.
     */
    public DependencyKind getDependencyKind() {
        return kind;
    }

    /**
     * Sets the value of the dependency kind {@link #kind}.
     * 
     * @param kindToSet
     *            value to set
     */
    public void setDependencyKind(final DependencyKind kindToSet) {
        this.kind = kindToSet;
    }

    /**
     * @return {@link #attributeFullName}.
     */
    public String getAttributeFullName() {
        return attributeFullName;
    }

    /**
     * Sets {@link #attributeFullName}.
     * 
     * @param fullName
     *            {@link #attributeFullName}
     */
    public void setAttributeFullName(final String fullName) {
        this.attributeFullName = fullName;
    }

    /**
     * @return {@link #lastAttribute}.
     */
    public boolean isLastAttribute() {
        return lastAttribute;
    }

    /**
     * Sets {@link #lastAttribute}.
     * Use this to set the attribute as the last one to set
     * by a DSL in this attirbutes ModelClass.
     * 
     * @param lastAttributeValue
     *            value to set
     */
    public void setLastAttribute(final boolean lastAttributeValue) {
        this.lastAttribute = lastAttributeValue;
    }

    /**
     * @return {@link #isReference} value.
     */
    public boolean isReference() {
        return isReference;
    }

    /**
     * Sets {@link #isReference}.
     * 
     * @param isReferenceValue
     *            value to set
     */
    public void setReference(final boolean isReferenceValue) {
        this.isReference = isReferenceValue;
    }

    /**
     * Sets {@link #opposite} value.
     * 
     * @param opp
     *            {@link #opposite}
     */
    public void setOpposite(final ClassAttribute opp) {
        this.opposite = opp;
    }

    /**
     * @return {@link #opposite} value.
     */
    public ClassAttribute getOpposite() {
        return this.opposite;
    }

    /**
     * Adds the given ClassAttribute to this {@link ModelClass#oppositesToSet}
     * List.
     * 
     * @param oppToSet
     *            the ClassAttribute to add
     */
    public void addOppositeToSet(final ClassAttribute oppToSet) {
        this.getModelClass().addOppositeToSet(oppToSet);
    }

    /**
     * @return {@link #className} value.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets {@link #className} value.
     * 
     * @param classNameToSet
     *            value to set
     */
    public void setClassName(final String classNameToSet) {
        this.className = classNameToSet;
    }

    /**
     * @return {@link #isList} value.
     */
    public boolean isList() {
        return isList;
    }

    /**
     * Sets {@link #isList} value.
     * 
     * @param isListValue
     *            value to set
     */
    public void setList(final boolean isListValue) {
        this.isList = isListValue;
    }

    /**
     * @return {@link #modelClass} value.
     */
    public ModelClass getModelClass() {
        return modelClass;
    }

    /**
     * Sets {@link #modelClass} value.
     * 
     * @param modelClassToSet
     *            value to set
     */
    public void setModelClass(final ModelClass modelClassToSet) {
        this.modelClass = modelClassToSet;
    }

    /**
     * Gets {@link #isCreatorOfOpposite} value.
     * 
     * @return true if is creator of Opposite
     */
    public boolean isCreatorOfOpposite() {
        return isCreatorOfOpposite;
    }

    /**
     * Sets {@link #isCreatorOfOpposite} value.
     * 
     * @param isCreatorOfOppositeValue
     *            boolean value to set
     */
    public void setCreatorOfOpposite(final boolean isCreatorOfOppositeValue) {
        this.isCreatorOfOpposite = isCreatorOfOppositeValue;
    }

    /**
     * Gets {@link #isPrimitive}.
     * 
     * @return true if its primitve
     */
    public boolean isPrimitive() {
        return isPrimitive;
    }
}
