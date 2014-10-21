package de.htwg.javaDSLBuilder.creator;

import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public interface ICreator {
	
	/**
	 * Returns the created {@link DSLGenationModel} for code generation
	 */
	DSLGenerationModel getGenerationModel();
	
}
