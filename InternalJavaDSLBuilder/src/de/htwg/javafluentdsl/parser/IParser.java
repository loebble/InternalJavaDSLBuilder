package de.htwg.javafluentdsl.parser;

import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
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
	
	/**
	 * Method that has to create the order the attributes will be set in the DSL.
	 * It therefore has to create a chain by using {@link ClassAttribute#setNextAttribute(ClassAttribute)}
	 * and {@link ClassAttribute#setNextSimpleOptAttr(java.util.List)} inside the ModelClasses.
	 */
	void createAttributeOrder();

}
