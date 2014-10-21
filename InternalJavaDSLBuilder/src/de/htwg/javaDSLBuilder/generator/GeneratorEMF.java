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

import de.htwg.javaDSLBuilder.DSLBuilder;
import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;
import de.htwg.javaDSLBuilder.dslmodel.ModelClass;

public class GeneratorEMF {

	private static final String currentPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	private static final String TEMPLATES_DIR_PATH = currentPath
			+ "/string_templates/emf_templates/";
	
	private static final String MULTI_TEMPLATE_FILE = "MultipleBuilder.stg";
	private static final String SINGLE_TEMPLATE_FILE = "SingleBuilder.stg";

	public static void buildDSL(CreatorEMF creator, String template,
			String targetPackage) {
		DSLGenerationModel dslModel = creator.getGenerationModel();

		switch (template) {
		case DSLBuilder.SINGLE_BUILDER_TEMPLATE:
			generateSingleBuilderDSL(dslModel, targetPackage);
			break;
		case DSLBuilder.MULTIPLE_BUILDER_TEMPLATE:
			generateMultipleBuilderDSL(dslModel, targetPackage);
			break;
		default:
			break;
		}
	}

	private static void generateMultipleBuilderDSL(DSLGenerationModel dslModel,
			String targetPackage) {
		for (Map.Entry<String,ModelClass> classEntry : dslModel.classes
				.entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			generateBuilderFile(modelClass,targetPackage);

		}
	}

	private static void generateBuilderFile(ModelClass modelClass, String targetPackage) {
		File templateDirecorty = new File(TEMPLATES_DIR_PATH);
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
		File templateDirecorty = new File(TEMPLATES_DIR_PATH);
		String templatePath = templateDirecorty.toString();
		STGroup group = new STGroupFile(templatePath+"/"+SINGLE_TEMPLATE_FILE);
		group.registerRenderer(String.class, new StringRenderer());
		ST simpleBT = group.getInstanceOf("BuilderTemplate");
		simpleBT.add("packageName", targetPackage);
		simpleBT.add("genModel", dslModel);
		writeToFile(targetPackage, dslModel.getModelName(), simpleBT.render());
	}

	private static void writeToFile(String targetPackage, String fileName,
			String generatedCode) {
		try {
			String packagePath = targetPackage.replace(".", "/");
			String pathForDSL = currentPath + "/src/" + packagePath;
			File targetDirectory = new File(pathForDSL);
			if (!targetDirectory.exists()) {
				targetDirectory.mkdirs();
			}
			PrintWriter writer = new PrintWriter(pathForDSL + "/" + fileName
					+ ".java");
			writer.print(generatedCode);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
