package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.parser.RegexDefinitions;

/**
 * Starting class for the application.
 *
 */
public final class Main {
    
    /**
     * No Instantiation needed.
     */
    private Main() { };

    /**
     * File Ending for a EMF Generator Model file.
     */
    public static final String GENMODEL_FILE = ".genmodel";
    /**
     * Message that occurs if not a valid source was given.
     */
    public static final String WRONG_SOURCE = 
            "The first Parameter is not a valid source. "
            + "It either has to the abolute path to a EMF GenModel "
            + "File or a String describing the dsl model "
            + "(as defined by de.htwg.javafluentdsl.creator"
            + ".RegexDefinitions.MODEL_DESCRIPTION)";

    /**
     * Defines the number of parameters the application requires.
     */
    public static final int NUMBER_OF_PARAMETER = 3;

    /**
     * Requires three String parameters as arguments (Source, TemplateOption,
     * TargetPackage). First parameter (args[0]) decides which DSL generation
     * process to start. Second parameter (args[1]) decides template is used.
     * Third parameter (args[2]) decides the targetPackage (String will
     * converted to lower case).
     * 
     * @param args
     *            array with 3 paramters
     * @see IStart#startDSLGenerationProcess(String, String, String)
     *      StartDSLProcess method for parameters
     */
    public static void main(final String[] args) {
        if (args.length != NUMBER_OF_PARAMETER) {
            System.err.println("Excactly three parameters are required "
                    + "(source, templateOption, targetPackage). Exiting...");
            return;
        }
        String source = args[0];
        String templateOption = args[1];
        String targetPackage = args[2].toLowerCase();
        if (targetPackage.endsWith(".")) {
            targetPackage = targetPackage.substring(0,
                    targetPackage.length() - 1);
        }
        IStart start = getStartForSource(source);
        if (start != null) {
            start.startDSLGenerationProcess(source, templateOption,
                    targetPackage);
        } else {
            throw new IllegalArgumentException(WRONG_SOURCE);
        }
    }

    /**
     * Decides which IStart Implementation to use by the given source.
     * 
     * @param source
     *            The String which represents a source.
     * @return IStart Implementation to use for further processing or null
     *         source was not a correct one
     */
    static IStart getStartForSource(final String source) {
        if (source.endsWith(GENMODEL_FILE)) {
            return new StartEMF();
        } else if (source.startsWith(RegexDefinitions.CLASS_START)) {
            return new StartRegex();
        } else {
            return null;
        }
    }

}
