package de.htwg.javafluentdsl.parser;

import java.util.ArrayList;
import java.util.List;

import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.DependencyKind;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * 
 * Abstract class that implements {@link IParser}. When implementing a new
 * IParser check if this {@link #createAttributeOrder()} already creates the
 * correct chaining of ClassAttributes. If so extend this Class and use the
 * {@link #createAttributeOrder()} Method AFTER the Parser has created the
 * complete {@link DSLGenerationModel} with all ModelClasses and
 * ClassAttributes.
 *
 */
public abstract class AbstractIParserBasic implements IParser {

    /**
     * Holds the created {@link DSLGenerationModel} which this Parser creates.
     */
    private final DSLGenerationModel genModel;

    /**
     * Constructor which ensures the DSLGenerationModel is created.
     */
    AbstractIParserBasic() {
        this.genModel = new DSLGenerationModel();
    }

    @Override
    public final DSLGenerationModel getGenerationModel() {
        return genModel;
    }

    /**
     * Handles the order of the ClassAttributes for the generated ModelClasses.
     * 
     * @see #createAttributeOrder(ModelClass) createAttributeOrder(ModelClass) -
     *      for ordering details
     */
    @Override
    public final void createAttributeOrder() {
        for (ModelClass modelClass : genModel.getClasses()) {
            createAttributeOrder(modelClass);
        }
    }

    /**
     * Creates the order of ClassAttributes in a ModelClass by setting their
     * {@code setNextAttribute(ClassAttribute)} and
     * {@code setNextSimpleOptAttr(java.util.List)}. For each dependency kind
     * a list is available in the ModelClass and thus the attributes are added
     * to the suited list.
     * 
     * @param modelClass
     *            The ModelClass to order attributes in
     */
    private void createAttributeOrder(final ModelClass modelClass) {
        // a list for the case that the first attributes are optional
        List<ClassAttribute> firstOptAttr = new ArrayList<>();
        /*
         * last found required attribute, therefore the one nextAttribute and
         * nextSimpleOptAttr has to be set
         */
        ClassAttribute previousRequiredAttr = null;
        for (ClassAttribute currentAtt : modelClass.getAllAttributes()) {
            // Lists and attributes to set in dsl
            if (currentAtt.getDependencyKind() == DependencyKind.ATTRIBUTE
                    || currentAtt.getDependencyKind() 
                    == DependencyKind.LIST_OF_ATTRIBUTES) {
                modelClass.addAttributeToSet(currentAtt);
                if (previousRequiredAttr == null) {
                    previousRequiredAttr = currentAtt;
                    // if first attributes are optional add them to the first
                    // mandatory attribute(scope)
                    currentAtt.setNextSimpleOptAttr(firstOptAttr);
                } else {
                    previousRequiredAttr.setNextAttribute(currentAtt);
                    previousRequiredAttr = currentAtt;
                }
            } else if (currentAtt.getDependencyKind() 
                    == DependencyKind.OPTIONAL_ATTRIBUTE) {
                currentAtt.setOptional(true);
                modelClass.addSimpleOptionalAttribute(currentAtt);
                if (previousRequiredAttr != null) {
                    previousRequiredAttr.addNextSimpleOptAttr(currentAtt);
                } else {
                    // add it to the list of the first optional attributes
                    firstOptAttr.add(currentAtt);
                }
            } else if (currentAtt.getDependencyKind() 
                    == DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET) {
                // adds the current attribute to its creators oppositesToSetList
                currentAtt.getOpposite().addOppositeToSet(currentAtt);
            }
        }
        // Special case if no required attribute in class
        if (previousRequiredAttr == null) {
            modelClass.setSimpleOptionalsOnly(true);
        } else {
            previousRequiredAttr.setLastAttribute(true);
        }
    }

    /**
     * Creates a String representation of the attribute order of this
     * {@link #genModel} , if the attributesToSet List in each ModelClass is
     * filled accordingly.
     * 
     * @return the created String with the corresponding order
     */
    public final String printOrder() {
        StringBuffer sb = new StringBuffer();
        for (ModelClass modelClass : genModel.getClasses()) {
            sb.append("\n" + "ModelClass: " + modelClass.getClassName() + "\n");
            int i = 0;
            for (ClassAttribute attr : modelClass.getAttributesToSet()) {
                if (i == 0) {
                    sb.append(" " + attr.getAttributeName() + ":"
                            + attr.getType());
                }
                if (attr.getNextAttribute() != null) {
                    sb.append("-> "
                            + attr.getNextAttribute().getAttributeName() + ":"
                            + attr.getNextAttribute().getType());
                }
                for (ClassAttribute nextOptional 
                        : attr.getNextSimpleOptAttr()) {
                    sb.append("(->" + nextOptional.getAttributeName() + ":"
                            + nextOptional.getType() + ")");
                }
                i++;
            }
        }
        return sb.toString();
    }

}
