package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.generator.Generator;
import de.htwg.javafluentdsl.parser.ParserRegex;


/**
 * Class for starting the Application via the {@link #startDSLGenerationProcess(String, String, String)} method.
 * With a model description as source defined by {@link de.htwg.javafluentdsl.parser.RegexUtil#MODEL_DESCRIPTION ModelDescriptionRegex}
 *
 */
public class StartRegex implements IStart{

	/**
	 * @param source The complete model description defined by 
	 * {@link de.htwg.javafluentdsl.parser.RegexUtil#MODEL_DESCRIPTION ModelDescriptionRegex}
	 */
	@Override
	public void startDSLGenerationProcess(String source, String templateOption,
			String targetPackage) {
		ParserRegex creator = ParserRegex.getInstance(source);
		Generator.generateDSL(creator, templateOption,
				targetPackage);
		System.out.println(creator.getGenerationModel().printModel());
	}

}
