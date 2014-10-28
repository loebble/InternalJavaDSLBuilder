package de.htwg.javafluentdsl.creator;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

/**
 * Interface to make sure a {@link DSLGenerationModel} can 
 * be received through a Generator
 *
 */
public interface ICreator {
	
	/**
	 * Returns the created {@link DSLGenationModel} for code generation
	 */
	DSLGenerationModel getGenerationModel();
	
}
