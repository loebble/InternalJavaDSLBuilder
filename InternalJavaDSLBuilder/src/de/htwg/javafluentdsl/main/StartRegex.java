package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.generator.GeneratorRegex;
import de.htwg.javafluentdsl.generator.IGenerator;
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
		ParserRegex parser = ParserRegex.getInstance(source);
		IGenerator generator = new GeneratorRegex();
		generator.generateDSL(parser, templateOption, targetPackage);
		System.out.println(parser.getGenerationModel().printModel());
		System.out.println(parser.printOrder());
	}

}
