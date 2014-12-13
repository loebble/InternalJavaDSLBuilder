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
     * @param genModel
     *            The corresponding DSLGenerationModel
     */
    ModelClass(final String name, final DSLGenerationModel genModel) {
        this.model = genModel;
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
    public final ClassAttribute getSpefificAttributeByFullName(
            final String fullAttributeName) {
        for (ClassAttribute attr : this.allAttributes) {
            if (attr.getAttributeFullName().equals(fullAttributeName)) {
                return attr;
            }
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
    public final ClassAttribute getSpefificAttribute(
            final String attributeName) {
        for (ClassAttribute attr : this.allAttributes) {
            if (attr.getAttributeName().equals(attributeName)) {
                return attr;
            }
        }
        return null;
    }

    /**
     * Reference to the {@link DSLGenerationModel} this ModelClass is part of.
     */
    private DSLGenerationModel model;
    /**
     * The unique class name.
     */
    private String className;
    /**
     * True if this ModelClass is the root Model Class for the Generation Model.
     * 
     */
    private boolean rootModelClass;

    /**
     * A List which holds all Attributes of the this ModelClass. All Attributes
     * means regardless of their dependency kind and other values.
     */
    private ArrayList<ClassAttribute> allAttributes;

    /**
     * Holds the mandatory, list and reference attributes of this ModelClass.
     */
    private List<ClassAttribute> attributesToSet;
    /**
     * Holds the simple optional attributes this ModelClass has.
     */
    private List<ClassAttribute> simpleOptAttr;
    /**
     * Holds the opposite Attributes of this ModelClass which has to set the
     * other ends (i.e. their opposite) reference to complete a bidirectional
     * relation.
     */
    private List<ClassAttribute> oppositesToSet;
    /**
     * True if this ModelClass only consists of simple optional attributes.
     * 
     * @see #simpleOptAttr
     */
    private boolean simpleOptionalsOnly;
    /**
     * Holds the imports this ModelClass needs.
     */
    private List<String> imports;
    /**
     * True if this ModelClass has at least one list attribute.
     * 
     * @see ClassAttribute#isList()
     */
    private boolean hasList = false;

    /**
     * @return {@link #simpleOptionalsOnly}.
     */
    public final boolean isSimpleOptionalsOnly() {
        return simpleOptionalsOnly;
    }

    /**
     * @return {@link #attributesToSet}.
     */
    public final List<ClassAttribute> getAttributesToSet() {
        return attributesToSet;
    }

    /**
     * @return {@link #simpleOptAttr}.
     */
    public final List<ClassAttribute> getSimpleOptAttr() {
        return simpleOptAttr;
    }

    /**
     * @return {@link #allAttributes}
     * 
     */
    public final ArrayList<ClassAttribute> getAllAttributes() {
        return allAttributes;
    }

    /**
     * Adds an attribute to the {@link #allAttributes} List.
     * 
     * @param attribute
     *            attribut to add
     */
    public final void addToAllAttributes(final ClassAttribute attribute) {
        this.allAttributes.add(attribute);
    }

    /**
     * Adds a {@link ClassAttribute} to this ModelClass.
     * 
     * @param attribute
     *            the attribute to be added
     */
    public final void addAttributeToSet(final ClassAttribute attribute) {
        if (attribute.getDependencyKind() != DependencyKind.ATTRIBUTE
        && attribute.getDependencyKind() != DependencyKind.LIST_OF_ATTRIBUTES) {
            throw new IllegalArgumentException(
                    "Not the right DependencyKind for list!" + "Given: "
                            + attribute.getDependencyKind() + " expected: "
                            + DependencyKind.ATTRIBUTE + " OR "
                            + DependencyKind.LIST_OF_ATTRIBUTES);
        }
        this.attributesToSet.add(attribute);
    }

    /**
     * Adds a Attribute to {@link #simpleOptAttr}.
     * @param attribute
     *            the attribute to be added
     */
    public final void addSimpleOptionalAttribute(
            final ClassAttribute attribute) {
        if (attribute.getDependencyKind() 
                != DependencyKind.OPTIONAL_ATTRIBUTE) {
            throw new IllegalArgumentException(
                    "Not the right DependencyKind for list!" + "Given: "
                            + attribute.getDependencyKind() + " expected: "
                            + DependencyKind.OPTIONAL_ATTRIBUTE);
        }
        this.simpleOptAttr.add(attribute);
    }

    /**
     * @return {@link #className}.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Sets {@link #className}.
     * @param classNameString name of the class
     */
    public final void setClassName(final String classNameString) {
        this.className = classNameString;
    }

    /**
     * Returns the value of {@link #rootModelClass}.
     * @return true if this class is rootModelClass
     */
    public final boolean isRootModelClass() {
        return rootModelClass;
    }

    /**
     * Sets the value of {@link #rootModelClass}.
     * @param rootModelClassRef the root model class
     */
    public final void setRootModelClass(final boolean rootModelClassRef) {
        this.rootModelClass = rootModelClassRef;
    }

    /**
     * @return {@link #oppositesToSet}.
     */
    public final List<ClassAttribute> getOppositesToSet() {
        return oppositesToSet;
    }

    /**
     * Adds ClassAttribute to {@link #oppositesToSet}.
     * 
     * @param opToSet
     *            the ClassAttribute to be added
     */
    public final void addOppositeToSet(final ClassAttribute opToSet) {
        if (opToSet.getDependencyKind() 
                != DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET) {
            throw new IllegalArgumentException(
                    "Not the right DependencyKind for list!" + "Given: "
                            + opToSet.getDependencyKind() + " expected: "
                            + DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET);
        }
        this.oppositesToSet.add(opToSet);
    }

    /**
     * Sets {@link #simpleOptionalsOnly}.
     * @param b boolean value
     */
    public final void setSimpleOptionalsOnly(final boolean b) {
        this.simpleOptionalsOnly = b;
    }

    /**
     * @return {@link #imports}.
     */
    public final List<String> getImports() {
        return imports;
    }

    /**
     * Adds a String to {@link #imports}.
     * @param importStringValue String to add
     */
    public final void addImport(final String importStringValue) {
        if (!imports.contains(importStringValue)) {
            imports.add(importStringValue);
        }
    }

    /**
     * @return {@link #model}.
     */
    public final DSLGenerationModel getModel() {
        return model;
    }

    /**
     * @return the {@link #hasList} value.
     */
    public final boolean isHasList() {
        return hasList;
    }

    /**
     * Sets the {@link #hasList} value.
     * @param hasListValue true if it has a list
     */
    public final void setHasList(final boolean hasListValue) {
        this.hasList = hasListValue;
    }

}
