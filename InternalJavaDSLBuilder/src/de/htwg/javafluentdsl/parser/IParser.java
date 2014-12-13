package de.htwg.javafluentdsl.parser;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

/**
 * Interface to make sure a {@link DSLGenerationModel} can be received 
 * through a Parser. The DSLGenerationModel should be fully created 
 * before the {@code getGenerationModel()} can be called.
 */
public interface IParser {

    /**
     * Returns the created {@link DSLGenerationModel} for code generation.
     * Make sure the Model is completely created before this Method can
     * be called.
     * @return the complete created DSLGenerationModel instance.
     */
    DSLGenerationModel getGenerationModel();

    /**
     * Method that has to create the order of the attributes. 
     * This should decide how the correct chain of DSL method calls
     * looks like.
     */
    void createAttributeOrder();

}
