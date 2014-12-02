package de.htwg.javafluentdsl.generator;

import de.htwg.javafluentdsl.parser.IParser;

/**
 * Type Interface for defining a generator.
 *
 */
public interface ITemplateGenerator {

    /**
     * Generating the Files needed for the DSL.
     * 
     * @param parser
     *            a parser Object which implements the {@link IParser} Interface
     * @param templateOption
     *            a valid template option for the DSLGenerationModel the parser
     *            creates
     * @param targetPackage
     *            the package where the files should be created in
     */
    void generateDSL(IParser parser, String templateOption,
            String targetPackage);

}
