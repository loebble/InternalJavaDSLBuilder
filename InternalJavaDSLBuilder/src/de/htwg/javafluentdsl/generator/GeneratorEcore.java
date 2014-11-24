package de.htwg.javafluentdsl.generator;

import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;
import de.htwg.javafluentdsl.parser.ParserEcore;

public class GeneratorEcore{

	/*
	 * Template options
	 */
	public static final String SINGLE_BUILDER_OPTION = "singleBuilder";
	public static final String MULTIPLE_BUILDER_OPTION = "multiBuilder";

	/*
	 * Template paths
	 */
	private static final String STRING_TEMPLATES_DIR_PATH = "string_templates/";
	private static final String ECORE_TEMPLATES_DIR_PATH = STRING_TEMPLATES_DIR_PATH
			+ "ecore_templates/";

	/*
	 * Template fileNames
	 */
	private static final String MULTI_TEMPLATE_FILE = "MultipleBuilder.stg";
	private static final String SINGLE_TEMPLATE_FILE = "SingleBuilder.stg";
	private static final String WRONG_OPTION_ECORE = "Not a valid templateOption given. "
			+ "For a genmodel source only:"
			+ SINGLE_BUILDER_OPTION
			+ " OR "
			+ MULTIPLE_BUILDER_OPTION + " allowed.";

	/**
	 * No instantiation needed
	 */
	private GeneratorEcore() {
	}

	/**
	 * Starts generation of files with given template.
	 * 
	 * @param parser
	 *            the {@link ParserEcore} which holds the DSLGenerationModel
	 * @param templateOption
	 *            which template to use
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @return list of paths where the files where created
	 */
	public static void generateDSL(ParserEcore parser, String templateOption,
			String targetPackage) {
		DSLGenerationModel dslModel = parser.getGenerationModel();
		List<String> filesCreated = new ArrayList<>();
		String filePath = null;
		switch (templateOption) {
		case SINGLE_BUILDER_OPTION:
			filePath = ecoreGenerateSingleBuilderDSL(dslModel, targetPackage);
			break;
		case MULTIPLE_BUILDER_OPTION:
			ecoreGenerateMultipleBuilderDSL(dslModel, targetPackage, filesCreated);
			break;
		default:
			throw new IllegalArgumentException(WRONG_OPTION_ECORE);
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

	/**
	 * Generates a DSL with multiple Builders. For each ModelClassin the
	 * DSLGenerationModel a Builder in generated in its own java file. Is
	 * designed for DSLGenerationModels created by {@link ParserEcore}, but can
	 * be used with every DSLGenerationModel if the template is suited for it.
	 * In case of ParserEcore generated DSLGenerationModel every EClass gets its
	 * own Builder.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @param filesCreated
	 *            a list to which the paths of created files are added
	 */
	private static void ecoreGenerateMultipleBuilderDSL(
			DSLGenerationModel dslModel, String targetPackage,
			List<String> filesCreated) {
		for (ModelClass modelClass : dslModel.getClasses()) {
			String filePath = ecoreGenerateMultiBuilderFile(modelClass,
					targetPackage);
			if (filePath != null)
				filesCreated.add(filePath);
		}
	}

	/**
	 * Method for generating a single Builder file for ecore multi builder option.
	 * 
	 * @param modelClass
	 * @param targetPackage
	 * @return path of File which was created
	 * @see #ecoreGenerateMultipleBuilderDSL(DSLGenerationModel, String, List)
	 */
	private static String ecoreGenerateMultiBuilderFile(ModelClass modelClass,
			String targetPackage) {
		STGroup group = GeneratorUtil.getStringTemplateFromPath(ECORE_TEMPLATES_DIR_PATH
				+ MULTI_TEMPLATE_FILE);
		ST modelTemplate = group.getInstanceOf("ClassBuilder");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("modelClass", modelClass);
		String modelCode = modelTemplate.render();
		return GeneratorUtil.writeToJavaFile(targetPackage,
				modelClass.getClassName() + "Builder", modelCode);
	}

	/**
	 * Generates a DSL with a Single Builder file. Is designed for
	 * DSLGenerationModels created by {@link ParserEcore}, but can be used with
	 * every DSLGenerationModel if the template is suited for it.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @param filesCreated
	 *            a list to which the paths of created files are added
	 */
	private static String ecoreGenerateSingleBuilderDSL(
			DSLGenerationModel dslModel, String targetPackage) {
		STGroup group = GeneratorUtil.getStringTemplateFromPath(ECORE_TEMPLATES_DIR_PATH
				+ SINGLE_TEMPLATE_FILE);
		ST simpleBT = group.getInstanceOf("SingleBuilder");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		return GeneratorUtil.writeToJavaFile(targetPackage, dslModel.getModelName()
				+ "Builder", simpleBT.render());
	}
}
