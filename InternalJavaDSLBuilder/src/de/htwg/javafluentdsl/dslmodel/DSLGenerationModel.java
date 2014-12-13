package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the generation of a internal Java DSL. An Instance of this class
 * describes the structure of a another model (or more specifically the classes
 * and attributes) which can be used to generate a internal DSL for the model
 * instantiation.
 *
 */
public class DSLGenerationModel {

    /**
     * Constructor for the DSLGenerationModel.
     */
    public DSLGenerationModel() {
        imports = new ArrayList<String>();
        classes = new ArrayList<ModelClass>();
    }

    /**
     * The name of the model.
     */
    private String modelName;

    /**
     * A list of imports the model has to have.
     */
    private List<String> imports;

    /**
     * Indicator if the model uses Lists of attributes.
     */
    private boolean hasList = false;

    /**
     * Holds the name of the factory which has a create method for creating an
     * object.
     */
    private String factoryName;

    /**
     * List of classes the model has described.
     */
    private final List<ModelClass> classes;

    /**
     * Searches the whole DSLGenerationModel for the a ClassAttribute.
     * 
     * @param className
     *            the name of the class
     * @param attrName
     *            name of the attribute
     * @return the found ClassAttribute in the DSLGenerationModel or null if
     *         none has been found
     */
    public final ClassAttribute findAttribute(final String className,
            final String attrName) {
        ModelClass existingModelClass = getModelClass(className);
        if (existingModelClass != null) {
            ClassAttribute foundAttr = existingModelClass
                    .getSpefificAttribute(attrName);
            if (foundAttr != null) {
                return foundAttr;
            }
        }
        return null;
    }

    /**
     * Returns the value of {@link#modelName}.
     * @return {@link#modelName}
     */
    public final String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of {@link#modelName}.
     * 
     * @param modelNameString name of the model
     */
    public final void setModelName(final String modelNameString) {
        this.modelName = modelNameString;
    }

    /**
     * Returns List of imports the model has.
     * 
     * @return {@link #imports}
     */
    public final List<String> getImports() {
        return imports;
    }

    /**
     * Adds a single import to the list of {@link #imports}.
     * 
     * @param importString
     *            the full name of an import
     */
    public final void addImport(final String importString) {
        if (!imports.contains(importString)) {
            imports.add(importString);
        }
    }

    /**
     * Adds a single ModelClass to this generation model. If a ModelClass with
     * the same name already was in the list {@link #classes} it will not be
     * added or replaced.
     * 
     * @param className
     *            the ModelClass obj to be added
     * @return the ModelClass object which is now in the list {@link #classes}
     */
    public final ModelClass addModelClass(final String className) {
        ModelClass existingModelClass = getModelClass(className);
        if (existingModelClass != null) {
            throw new IllegalArgumentException("ModelClass with name "
                    + className + " already exists!");
        }
        ModelClass newModelClass = new ModelClass(className, this);
        // First Class is always the rootclass
        if (classes.isEmpty()) {
            newModelClass.setRootModelClass(true);
        }
        classes.add(newModelClass);
        return newModelClass;
    }

    /**
     * Retrieves a ModelClass object from the list {@link #classes} by the given
     * className.
     * 
     * @param className
     *            the name of the ModelClass
     * @return ModelClass the found ModelClass Object or null if none found
     */
    public final ModelClass getModelClass(final String className) {
        for (ModelClass modelClass : classes) {
            if (modelClass.getClassName().equals(className)) {
                return modelClass;
            }
        }
        return null;
    }

    /**
     * Returns the List {@link #classes} which has all defined classes of this
     * Model. Classes can be added {@link #addModelClass(String)}
     * @return {@link #classes}
     */
    public final List<ModelClass> getClasses() {
        return classes;
    }

    /**
     * Returns the {@link #hasList} value.
     * 
     * @return true if model uses at least one list of attributes
     */
    public final boolean isHasList() {
        return hasList;
    }

    /**
     * Sets the {@link #hasList} value.
     * 
     * @param hasListValue
     *            true if model uses at least one list of attributes
     */
    public final void setHasList(final boolean hasListValue) {
        this.hasList = hasListValue;
    }

    /**
     * Returns the {@link #factoryName} value.
     * 
     * @return the excact name of the factory
     */
    public final String getFactoryName() {
        return factoryName;
    }

    /**
     * Sets the {@link #factoryName} value.
     * 
     * @param factoryNameString the
     *            excact name of the factory
     */
    public final void setFactoryName(final String factoryNameString) {
        this.factoryName = factoryNameString;
    }

    /**
     * Creates a String representation of this generation model with all the
     * classes and attributes as well as the attributes properties.
     * 
     * @return the created String
     */
    public final String printModel() {
        StringBuffer sb = new StringBuffer();
        sb.append("DSL Model for: " + this.modelName + "\n");
        sb.append("IMPORTS: ");
        for (String importString : this.imports) {
            sb.append(importString + "; ");
        }
        sb.append("\n" + "CLASSES: ");
        for (ModelClass modelClass : classes) {
            sb.append("\n" + "ModelClass: " + modelClass.getClassName() + "\n");
            sb.append("Imports: ");
            for (String imp : modelClass.getImports()) {
                sb.append(imp + "; ");
            }
            sb.append("\n");
            for (ClassAttribute attr : modelClass.getAllAttributes()) {
                sb.append("\t" + "Name: " + attr.getAttributeName() + " type: "
                        + attr.getType() + " kind: " + attr.getDependencyKind()
                        + " " + " reference: " + attr.isReference()
                        + " optional: " + attr.isOptional() + " list:"
                        + attr.isList());
                if (attr.getOpposite() != null) {
                    sb.append("\n\t" + " opposite: "
                            + attr.getOpposite().getClassName() + "."
                            + attr.getOpposite().getAttributeName()
                            + " isCreatorOfOpp: " + attr.isCreatorOfOpposite());
                }
                sb.append("\n");
            }
            for (ClassAttribute referencedCl : modelClass.getOppositesToSet()) {
                sb.append("\t" + referencedCl.getOpposite().getClassName()
                        + "." + referencedCl.getOpposite().getAttributeName()
                        + " is creator opposite of "
                        + referencedCl.getClassName() + "."
                        + referencedCl.getAttributeName() + "\n");
            }
        }
        return sb.toString();
    }
}
