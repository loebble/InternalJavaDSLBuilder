package de.htwg.javafluentdsl.parser;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

/**
 * Interface to make sure a {@link DSLGenerationModel} can be received in a
 * Generator by calling {@link #getGenerationModel()}.
 *
 */
public interface IParser {

	/**
	 * Returns the created {@link DSLGenationModel} for code generation
	 */
	DSLGenerationModel getGenerationModel();

}
