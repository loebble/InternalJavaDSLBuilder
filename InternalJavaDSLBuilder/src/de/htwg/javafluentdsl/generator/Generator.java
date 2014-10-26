package de.htwg.javafluentdsl.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import de.htwg.javafluentdsl.creator.CreatorEMF;
import de.htwg.javafluentdsl.creator.CreatorRegex;
import de.htwg.javafluentdsl.creator.ICreator;
import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * Class for generating the fluent DSL with defined StringTemplate files
 *
 */
public class Generator {

	/*
	 * Template options
	 */
	public static final String SINGLE_BUILDER_OPTION = "singleBuilder";
	public static final String MULTIPLE_BUILDER_OPTION = "multiBuilder"; // TODO
	public static final String INTERN_MODEL_OPTION = "internModel";
	public static final String SEPARATED_MODEL_OPTION = "separatedModel"; // TODO

	/*
	 * Template paths
	 */
	private static final String currentPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	private static final String TEMPLATES_DIR_PATH = currentPath
			+ "/string_templates/";
	private static final String EMF_TEMPLATES_DIR_PATH = TEMPLATES_DIR_PATH
			+ "/emf_templates";
	private static final String REGEX_TEMPLATES_DIR_PATH = TEMPLATES_DIR_PATH
			+ "/regex_templates";

	/*
	 * Template fileNames
	 */
	private static final String MULTI_TEMPLATE_FILE = "MultipleBuilder.stg";
	private static final String SINGLE_TEMPLATE_FILE = "SingleBuilder.stg";
	private static final String INTERN_BUILDER_FILE = "InternBuilder.stg";
	private static final String SEPARATED_BUILDER_FILE = "SeparatedBuilder.stg";

	/**
	 * No instantiation needed
	 */
	private Generator() {
	}

	/**
	 * Starts generation of files with given template.
	 * 
	 * @param creator
	 *            the creator which holds the DSLGenerationModel
	 * @param template
	 *            which template to use
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @return list of paths where the files where created
	 */
	public static void buildDSL(ICreator creator, String template,
			String targetPackage) {
		DSLGenerationModel dslModel = creator.getGenerationModel();
		List<String> filesCreated = new ArrayList<>();
		String filePath = null;
		switch (template) {
		case SINGLE_BUILDER_OPTION:
			filePath = emfGenerateSingleBuilderDSL(dslModel, targetPackage);
			break;
		case MULTIPLE_BUILDER_OPTION:
			emfGenerateMultipleBuilderDSL(dslModel, targetPackage, filesCreated);
			break;
		case INTERN_MODEL_OPTION:
			filePath = regexGenerateModelInternDSL(dslModel, targetPackage);
			break;
		case SEPARATED_MODEL_OPTION:
			regexGenerateModelSeperatedDSL(dslModel, targetPackage,
					filesCreated);
			break;
		default:
			break;
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
		STGroup group = getStringGroup(REGEX_TEMPLATES_DIR_PATH,
				INTERN_BUILDER_FILE);
		ST simpleBT = group.getInstanceOf("BuilderTemplate");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		return writeToJavaFile(targetPackage, dslModel.getModelName(),
				simpleBT.render());
	}

	/**
	 * Generates a DSL with a separated Model. This means the Builder is
	 * seperated from the Model (or vice versa) which can be desired in terms of
	 * separation of concerns. This method is created to be used with
	 * DSLGenerationModels created by a {@link CreatorRegex} instance, but can
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
	private static String regexGenerateModelSeperatedDSL(
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
		// String modelPackage = targetPackage+".model";
		// STGroup group = new STGroupFile(templatePath);
		// group.registerRenderer(String.class, new StringRenderer());
		// ST modelTemplate = group.getInstanceOf("ModelTemplate");
		// modelTemplate.add("packageName",modelPackage);
		// modelTemplate.add("genModel",dslModel);
		// String modelCode = modelTemplate.render();
		// writeToFile(modelPackage, dslModel.getModelName(), modelCode);
		//
		// String builderPackage = targetPackage+".builder";
		// ST builderTemplate = group.getInstanceOf("BuilderTemplate");
		// builderTemplate.add("packageName",builderPackage);
		// builderTemplate.add("genModel",dslModel);
		// String builderCode = modelTemplate.render();
		// return writeToJavaFile(builderPackage, dslMoel.getModelName(),
		// builderCode);
		return null;
	}

	private static String regexGenerateSeperatedModel(DSLGenerationModel dslModel,
			String targetPackage) {
		STGroup group = getStringGroup(REGEX_TEMPLATES_DIR_PATH,
				SEPARATED_BUILDER_FILE);
		group.registerRenderer(String.class, new StringRenderer());
		ST modelTemplate = group.getInstanceOf("Model");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("genModel", dslModel);
		String modelCode = modelTemplate.render();
		return Generator.writeToJavaFile(targetPackage,
				dslModel.getModelName(), modelCode);
	}
	
	private static String regexGenerateSeperatedBuilder(DSLGenerationModel dslModel,
			String targetPackage) {
		STGroup group = getStringGroup(REGEX_TEMPLATES_DIR_PATH,
				SEPARATED_BUILDER_FILE);
		group.registerRenderer(String.class, new StringRenderer());
		ST modelTemplate = group.getInstanceOf("Builder");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("genModel", dslModel);
		String modelCode = modelTemplate.render();
		return Generator.writeToJavaFile(targetPackage,
				dslModel.getModelName() + "Builder", modelCode);
	}

	/**
	 * Generates a DSL with multiple Builders. For each ModelClassin the
	 * DSLGenerationModel a Builder in generated in its own java file. Is
	 * designed for DSLGenerationModels created by {@link CreatorEMF}, but can
	 * be used with every DSLGenerationModel if the template is suited for it.
	 * In case of CreatorEMF generated DSLGenerationModel every EClass gets its
	 * own Builder.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @param filesCreated
	 *            a list to which the paths of created files are added
	 */
	private static void emfGenerateMultipleBuilderDSL(
			DSLGenerationModel dslModel, String targetPackage,
			List<String> filesCreated) {
		for (Map.Entry<String, ModelClass> classEntry : dslModel.classes
				.entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			String filePath = emfGenerateMultiBuilderFile(modelClass,
					targetPackage);
			if (filePath != null)
				filesCreated.add(filePath);
		}
	}

	/**
	 * Method for generating a single Builder file.
	 * 
	 * @param modelClass
	 * @param targetPackage
	 * @return {@link #writeToJavaFile(String, String, String) same as
	 *         writeToJavaFile}
	 */
	private static String emfGenerateMultiBuilderFile(ModelClass modelClass,
			String targetPackage) {
		STGroup group = getStringGroup(EMF_TEMPLATES_DIR_PATH,
				MULTI_TEMPLATE_FILE);
		group.registerRenderer(String.class, new StringRenderer());
		ST modelTemplate = group.getInstanceOf("ClassBuilder");
		modelTemplate.add("packageName", targetPackage);
		modelTemplate.add("modelClass", modelClass);
		String modelCode = modelTemplate.render();
		return Generator.writeToJavaFile(targetPackage,
				modelClass.getClassName() + "Builder", modelCode);
	}

	/**
	 * Generates a DSL with a Single Builder file. Is designed for
	 * DSLGenerationModels created by {@link CreatorEMF}, but can be used with
	 * every DSLGenerationModel if the template is suited for it.
	 * 
	 * @param dslModel
	 *            {@link DSLGenerationModel}
	 * @param targetPackage
	 *            the target package the files are generated at
	 * @param filesCreated
	 *            a list to which the paths of created files are added
	 */
	private static String emfGenerateSingleBuilderDSL(
			DSLGenerationModel dslModel, String targetPackage) { // TODO
		STGroup group = getStringGroup(EMF_TEMPLATES_DIR_PATH,
				SINGLE_TEMPLATE_FILE);
		ST simpleBT = group.getInstanceOf("SingleBuilder");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		return Generator.writeToJavaFile(targetPackage,
				dslModel.getModelName()+"Builder", simpleBT.render());
	}

	/**
	 * Looks for StringTemplate file in directory dir and with name fileName if
	 * found it creates a {@link STGroup} object and returns it.
	 * 
	 * @param dir
	 *            Directory path
	 * @param fileName
	 *            name of the template file
	 * @return STGroup object for accessing the template file
	 */
	private static STGroup getStringGroup(String dir, String fileName) {
		File templateDirecorty = new File(dir);
		String templatePath = templateDirecorty.toString();
		STGroup group = new STGroupFile(templatePath + "/" + fileName);
		group.registerRenderer(String.class, new StringRenderer());
		return group;
	}

	/**
	 * Writes generatedCode to a java file located at the targetPackage
	 * 
	 * @param targetPackage
	 *            package name with package naming conventions (domain
	 *            seperation with '.')
	 * @param fileName
	 *            name of the generated file (without .java)
	 * @param generatedCode
	 *            the java code
	 * @return
	 */
	public static String writeToJavaFile(String targetPackage, String fileName,
			String generatedCode) {
		try {
			String packagePath = targetPackage.replace(".", "/");
			String pathForDSL = currentPath + "/src/" + packagePath;
			File targetDirectory = new File(pathForDSL);
			if (!targetDirectory.exists()) {
				targetDirectory.mkdirs();
			}
			String filePath = pathForDSL + "/" + fileName + ".java";
			PrintWriter writer = new PrintWriter(filePath);
			writer.print(generatedCode);
			writer.close();
			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
