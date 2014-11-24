package de.htwg.javafluentdsl.generator;

import de.htwg.javafluentdsl.parser.ParserEcore;
import de.htwg.javafluentdsl.parser.ParserRegex;

/**
 * Abstract generator class which delegates the call of the specific Generator
 * depending on the given Parser.
 *
 */
public abstract class Generator {
	
	/**
	 * Delegation method to call the generator for ecore models {@link GeneratorEcore}
	 * @see {@link GeneratorEcore#generateDSL(parser, templateOption, targetPackage)}
	 */
	public static void generateDSL(ParserEcore parser, String templateOption,
			String targetPackage){
		GeneratorEcore.generateDSL(parser, templateOption, targetPackage);
	}
	
	/**
	 * Delegation method to call the generator for ecore models {@link GeneratorRegex}
	 * @see {@link GeneratorRegex#generateDSL(parser, templateOption, targetPackage)}
	 */
	public static void generateDSL(ParserRegex parser, String templateOption,
			String targetPackage){
		GeneratorRegex.generateDSL(parser, templateOption, targetPackage);
	}
	
}
