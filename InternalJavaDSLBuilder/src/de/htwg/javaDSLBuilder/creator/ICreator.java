package de.htwg.javaDSLBuilder.creator;

import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public interface ICreator {
	
	/**
	 * Returns the DSLGenationModel created by this creator
	 */
	DSLGenerationModel getGenerationModel();

}
