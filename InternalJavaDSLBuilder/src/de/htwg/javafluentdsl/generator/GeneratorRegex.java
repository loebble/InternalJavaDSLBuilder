package de.htwg.javafluentdsl.generator;

import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;
import de.htwg.javafluentdsl.parser.ParserRegex;

public class GeneratorRegex {

	/*
	 * Template options
	 */
	public static final String INTERN_MODEL_OPTION = "internBuilder";
	public static final String INTERN_MODEL_NEW_OPTION = "internBuilderNew";//TODO
	public static final String SEPARATED_MODEL_OPTION = "separatedBuilder";

	/*
	 * Template paths
	 */
	private static final String TEMPLATES_DIR_PATH = "string_templates/";
	private static final String REGEX_TEMPLATES_DIR_PATH = TEMPLATES_DIR_PATH
			+ "regex_templates/";

	/*
	 * Template fileNames
	 */
	private static final String INTERN_BUILDER_FILE = "InternBuilder.stg";
	private static final String INTERN_BUILDER_FILE_NEW = "InternBuilder_New.stg";
	private static final String SEPARATED_BUILDER_FILE = "SeparatedBuilder.stg";
	private static final String WRONG_OPTION_REGEX = "Not a valid templateOption given. "
			+ "For a string model description only:"
			+ INTERN_MODEL_OPTION
			+ " OR " + SEPARATED_MODEL_OPTION + " allowed.";

	/**
	 * No instantiation needed
	 */
	private GeneratorRegex() {
	}

	/**
	 * Starts generation of files with given template.
	 * 
	 * @param creator
	 *            the {@link ParserRegex} which holds the DSLGenerationModel
	 * @param templateOption
	 *            which template to use
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @return list of paths where the files where created
	 */
	public static void generateDSL(ParserRegex parser, String templateOption,
			String targetPackage) {
		DSLGenerationModel dslModel = parser.getGenerationModel();
		List<String> filesCreated = new ArrayList<>();
		String filePath = null;
		switch (templateOption) {
		case INTERN_MODEL_OPTION:
			filePath = regexGenerateModelInternDSL(dslModel, targetPackage);
			break;
		case SEPARATED_MODEL_OPTION:
			regexGenerateSeparateBuilderDSL(dslModel, targetPackage,
					filesCreated);
			break;
		case INTERN_MODEL_NEW_OPTION:
			regexGenerateModelInternDSL_NEW(dslModel, targetPackage,filesCreated);
			break;
		default:
			throw new IllegalArgumentException(WRONG_OPTION_REGEX);
		}
		if (filePath != null)
			filesCreated.add(filePath);
		if (filesCreated.size() > 0) {
			for (String path : filesCreated) {
				System.out.println("File created under path: " + path);
			}
			System.out.println();
		}

	}

	private static void regexGenerateModelInternDSL_NEW(
			DSLGenerationModel dslModel, String targetPackage, List<String> filesCreated) {
		for (ModelClass modelClass : dslModel.getClasses()) {
			String fileCreated = regexGenerateModelInternDSL_NEW_SingleFile(modelClass,targetPackage);
			if(fileCreated != null)
				filesCreated.add(fileCreated);
		}
	}
	
	private static String regexGenerateModelInternDSL_NEW_SingleFile(ModelClass modelClass,
			String targetPackage){
				STGroup group = GeneratorUtil.getStringGroup(REGEX_TEMPLATES_DIR_PATH
						+ INTERN_BUILDER_FILE_NEW);
				ST modelTemplate = group.getInstanceOf("ModelTemplate");
				modelTemplate.add("packageName", targetPackage);
				modelTemplate.add("modelClass", modelClass);
				String modelCode = modelTemplate.render();
				return GeneratorUtil.writeToJavaFile(targetPackage,
						modelClass.getClassName(), modelCode);
	}

	/**
	 * Creates a DSL in one File. This means the Builder is inside the Model and
	 * thus the model can only be instantiated by the Builder.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @return path of DSL File which was created
	 */
	private static String regexGenerateModelInternDSL(
			DSLGenerationModel dslModel, String targetPackage) {
		STGroup group = GeneratorUtil.getStringGroup(REGEX_TEMPLATES_DIR_PATH
				+ INTERN_BUILDER_FILE);
		ST simpleBT = group.getInstanceOf("ModelTemplate");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		return GeneratorUtil.writeToJavaFile(targetPackage, dslModel.getModelName(),
				simpleBT.render());
	}

	/**
	 * Generates a DSL with a separated Model. This means the Builder is
	 * seperated from the Model (or vice versa) which can be desired in terms of
	 * separation of concerns. This method is created to be used with
	 * DSLGenerationModels created by a {@link ParserRegex} instance, but can
	 * be used with every DSLGenerationModel while it still generates a correct
	 * model for the template.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param templatePath
	 *            String with the template location
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @return path of DSL File which was created
	 */
	private static String regexGenerateSeparateBuilderDSL(
			DSLGenerationModel dslModel, String targetPackage,
			List<String> filesCreated) {
		String modelFilePath = regexGenerateSeperatedModel(dslModel,
				targetPackage);
		if (modelFilePath != null)
			filesCreated.add(modelFilePath);

		String builderFilePath = regexGenerateSeperatedBuilder(dslModel,
				targetPackage);
		if (builderFilePath != null)
			filesCreated.add(builderFilePath);
		return null;
	}

	/**
	 * Generates a single model file for separated builder.
	 * 
	 * @param dslModel
	 *            the generation model
	 * @param targetPackage
	 *            the target package the file is be written to
	 * @return @return path of DSL File which was created
	 * @see {@link #regexGenerateSeparateBuilderDSL(DSLGenerationModel, String, List)}
	 */
	private static String regexGenerateSeperatedModel(
			DSLGenerationModel dslModel, String targetPackage) {
		STGroup group = GeneratorUtil.getStringGroup(REGEX_TEMPLATES_DIR_PATH
				+ SEPARATED_BUILDER_FILE);
		ST modelTemplate = group.getInstanceOf("Model");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("genModel", dslModel);
		String modelCode = modelTemplate.render();
		return GeneratorUtil.writeToJavaFile(targetPackage,
				dslModel.getModelName(), modelCode);
	}

	/**
	 * Generates a single builder file for separated builder.
	 * 
	 * @param dslModel
	 *            the generation model
	 * @param targetPackage
	 *            the target package the file is be written to
	 * @return @return path of DSL File which was created
	 * @see {@link #regexGenerateSeparateBuilderDSL(DSLGenerationModel, String, List)}
	 */
	private static String regexGenerateSeperatedBuilder(
			DSLGenerationModel dslModel, String targetPackage) {
		STGroup group = GeneratorUtil.getStringGroup(REGEX_TEMPLATES_DIR_PATH
				+ SEPARATED_BUILDER_FILE);
		ST modelTemplate = group.getInstanceOf("Builder");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("genModel", dslModel);
		String modelCode = modelTemplate.render();
		return GeneratorUtil.writeToJavaFile(targetPackage, dslModel.getModelName()
				+ "Builder", modelCode);
	}

}
