package de.htwg.javafluentdsl.parser;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

/**
 * Interface to make sure a {@link DSLGenerationModel} can be received in a
 * Parser implementing this interface by calling the specified {@code getGenerationModel()} method.
 *
 */
public interface IParser {

	/**
	 * Returns the created {@link DSLGenerationModel} for code generation
	 */
	DSLGenerationModel getGenerationModel();

}
