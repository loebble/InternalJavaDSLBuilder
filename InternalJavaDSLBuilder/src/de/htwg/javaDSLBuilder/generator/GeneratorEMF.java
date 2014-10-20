package de.htwg.javaDSLBuilder.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel.ModelClass;

public class GeneratorEMF {

	public static final String SINGLE_BUILDER_TEMPLATE = "single";
	public static final String MULTIPLE_BUILDER_TEMPLATE = "multi"; // TODO
	private static final String currentPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	private static final String templatesPath = currentPath
			+ "/string_templates/emf_templates/";
	
	private static final String MULTI_TEMPLATE_FILE = "MultipleBuilder.stg";

	public static void buildDSL(CreatorEMF creator, String template,
			String targetPackage) {
		DSLGenerationModel dslModel = creator.getGenerationModel();

		switch (template) {
		case SINGLE_BUILDER_TEMPLATE:
			generateSingleBuilderDSL(dslModel, targetPackage);
			break;
		case MULTIPLE_BUILDER_TEMPLATE:
			generateMultipleBuilderDSL(dslModel, targetPackage);
			break;
		default:
			break;
		}
	}

	private static void generateMultipleBuilderDSL(DSLGenerationModel dslModel,
			String targetPackage) {
		// String modelPackage = targetPackage+".model";
		// group.registerRenderer(String.class, new StringRenderer());
		// ST modelTemplate = group.getInstanceOf("ModelTemplate");
		// modelTemplate.add("packageName",modelPackage);
		// modelTemplate.add("genModel",dslModel);
		// String modelCode = modelTemplate.render();
		// writeToFile(modelPackage, dslModel.getModelName(), modelCode);

		for (Map.Entry<String, DSLGenerationModel.ModelClass> classEntry : dslModel.classes
				.entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			generateBuilderFile(modelClass,targetPackage);

		}

		// String builderPackage = targetPackage+".builder";
		// ST builderTemplate = group.getInstanceOf("BuilderTemplate");
		// builderTemplate.add("packageName",builderPackage);
		// builderTemplate.add("genModel",dslModel);
		// String builderCode = modelTemplate.render();
		// writeToFile(builderPackage, dslModel.getModelName(), builderCode);
	}

	private static void generateBuilderFile(ModelClass modelClass, String targetPackage) {
		File templateDirecorty = new File(templatesPath);
		String templatePath = templateDirecorty.toString();
		STGroup group = new STGroupFile(templatePath +"/"+MULTI_TEMPLATE_FILE);
		String modelPackage = targetPackage;
		group.registerRenderer(String.class, new StringRenderer());
		ST modelTemplate = group.getInstanceOf("ClassBuilder");
		modelTemplate.add("packageName", modelPackage);
		modelTemplate.add("modelClass", modelClass);
		String modelCode = modelTemplate.render();
		writeToFile(modelPackage, modelClass.getClassName()+"Builder", modelCode);


	}

	private static void generateSingleBuilderDSL(DSLGenerationModel dslModel,
			String targetPackage) {
		STGroup group = new STGroupFile(templatesPath);
		group.registerRenderer(String.class, new StringRenderer());
		ST simpleBT = group.getInstanceOf("BuilderTemplate");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		writeToFile(targetPackage, dslModel.getModelName(), simpleBT.render());
	}

	private static void writeToFile(String targetPackage, String fileName,
			String generatedCode) {
		try {
			String packagePath = targetPackage.replace(".", "\\");
			String pathForDSL = currentPath + "\\src\\" + packagePath;
			File targetDirectory = new File(pathForDSL);
			if (!targetDirectory.exists()) {
				targetDirectory.mkdirs();
			}
			PrintWriter writer = new PrintWriter(pathForDSL + "\\" + fileName
					+ ".java");
			writer.print(generatedCode);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
