package de.htwg.javafluentdsl.parser;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

/**
 * Interface to make sure a {@link DSLGenerationModel} can be received in a
 * Parser. The DSLGenerationModel should be fully created before the
 * get {@code getGenerationModel()} can be called.
 *
 */
public interface IParser {

    /**
     * Returns the created {@link DSLGenerationModel} for code generation.
     * @return the complete created DSLGenerationModel.
     */
    DSLGenerationModel getGenerationModel();

    /**
     * Method that has to create the order the attributes will be set in the
     * DSL. It therefore has to create a chain by using
     * {@link ClassAttribute#setNextAttribute(ClassAttribute)} and
     * {@link ClassAttribute#setNextSimpleOptAttr(java.util.List)} inside the
     * ModelClasses.
     */
    void createAttributeOrder();

}
