package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.generator.GeneratorRegex;
import de.htwg.javafluentdsl.parser.ParserRegex;

/**
 * Class for starting the Application via the
 * {@link #startDSLGenerationProcess(String, String, String)} method. With a
 * model description as source defined by
 * {@link de.htwg.javafluentdsl.parser.RegexDefinitions#MODEL_DESCRIPTION_REGEX
 * ModelDescriptionRegex}
 *
 */
public class StartRegex implements IStart {

    /**
     * @param source
     *            The complete model description defined by
     * {@link de.htwg.javafluentdsl.parser
     * .RegexDefinitions#MODEL_DESCRIPTION_REGEX
     *            ModelDescriptionRegex}
     * @param templateOption
     *            the template which should be used for generation
     * @param targetPackage
     *            the relative target package. Preferred with typical package
     *            naming conventions domains seperated by '.' and all in small
     *            letters.
     *            
     */
    @Override
    public final void startDSLGenerationProcess(final String source,
            final String templateOption, final String targetPackage) {
        ParserRegex parser = ParserRegex.getInstance(source);
        new GeneratorRegex().generateDSL(parser, templateOption, targetPackage);
    }

}
