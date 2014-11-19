package de.htwg.javafluentdsl.main;

import de.htwg.javafluentdsl.parser.RegexUtil;

/**
 * Starting class for the application.
 *
 */
public class Main {
	
	/**
	 * File Ending for a EMF Generator Model file
	 */
	public static final String GENMODEL_FILE = ".genmodel";
	/**
	 * Message that occurs if not a valid source was given
	 */
	public static final String WRONG_SOURCE = "The first Parameter is not a valid source. "
			+ "It either has to the abolute path to a EMF GenModel File or a String describing the dsl model "
			+ "(as defined in the de.htwg.javafluentdsl.creator.RegexUtil.MODEL_DESCRIPTION)";
	
	/**
	 * Requires three String parameters as arguments (Source, TemplateOption, TargetPackage).
	 * First parameter (args[0]) decides which DSL generation process to start.
	 * Second parameter (args[1]) decides template is used.
	 * Third parameter (args[2]) decides the targetPackage (String will converted to lower case).
	 * @param args array with 3 paramters 
	 * @see IStart#startDSLGenerationProcess(String, String, String) StartDSLProcess method for parameters
	 */
	public static void main(String[] args) {
		if(args.length != 3 ){ 
			System.err.println("Excactly three parameters are required "
					+ "(source, templateOption, targetPackage). Exiting...");
			return;
		}
		String source = args[0];
		String templateOption = args[1];
		String targetPackage = args[2].toLowerCase();
		if(targetPackage.endsWith("."))
			targetPackage = targetPackage.substring(0, targetPackage.length()-1);
		if(source.endsWith(GENMODEL_FILE)){
			new StartEMF().startDSLGenerationProcess(source, templateOption, targetPackage);
		}else if (source.startsWith(RegexUtil.CLASS_START)) {
			new StartRegex().startDSLGenerationProcess(source, templateOption, targetPackage);
		}
		else
			System.err.println(WRONG_SOURCE);
	}

	
}
