package de.htwg.javafluentdsl.creator;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;

public interface ICreator {
	
	/**
	 * Returns the created {@link DSLGenationModel} for code generation
	 */
	DSLGenerationModel getGenerationModel();
	
}
