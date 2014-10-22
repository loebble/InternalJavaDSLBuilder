package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.creator.CreatorRegex;
import de.htwg.javafluentdsl.generator.Generator;


/**
 * Class for starting the Application via the {@link #startDSLGenerationProcess(String, String, String)} method.
 * With a model description as source defined by {@link de.htwg.javafluentdsl.creator.RegexUtil#MODEL_DESCRIPTION ModelDescriptionRegex}
 *
 */
public class StartRegex implements IStart{

	/**
	 * @param source The complete model description defined by 
	 * {@link de.htwg.javafluentdsl.creator.RegexUtil#MODEL_DESCRIPTION ModelDescriptionRegex}
	 */
	@Override
	public void startDSLGenerationProcess(String source, String templateOption,
			String targetPackage) {
		CreatorRegex creator = CreatorRegex.getInstance(source);
		Generator.buildDSL(creator, templateOption,
				targetPackage);
	}

}
