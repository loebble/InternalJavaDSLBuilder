package de.htwg.javaDSLBuilder.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import de.htwg.javaDSLBuilder.creator.CreatorRegex;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public class GeneratorRegex {
	
	public static final String MODEL_MIXED_IN_TEMPLATE = "internModel";
	public static final String MODEL_SEPERATED_TEMPLATE = "seperatedModel"; //TODO
	private static final String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
	
	public static void buildDSL(CreatorRegex creator, String template, String targetPackage){
		DSLGenerationModel dslModel = creator.getGenerationModel();
		String templatePath = "";
		File templateDirecorty = new File(currentPath+"/templates/regex_templates");
		templatePath = templateDirecorty.toString();
		switch (template) {
		case MODEL_MIXED_IN_TEMPLATE:
			templatePath += "/ModelIntern.stg";
			generateModelInternDSL(dslModel,templatePath,targetPackage);
			break;
		case MODEL_SEPERATED_TEMPLATE:
			templatePath += "/ModelSeperated.stg";
			generateModelSeperatedDSL(dslModel,templatePath,targetPackage);
			break;
		default:
			break;
		}
	}
	
	private static void generateModelSeperatedDSL(DSLGenerationModel dslModel,
			String templatePath, String targetPackage) {
		String modelPackage = targetPackage+".model";
		STGroup group = new STGroupFile(templatePath);
		group.registerRenderer(String.class, new StringRenderer());
		ST modelTemplate = group.getInstanceOf("ModelTemplate");
		modelTemplate.add("packageName",modelPackage);
		modelTemplate.add("genModel",dslModel);
		String modelCode = modelTemplate.render();
		writeToFile(modelPackage, dslModel.getModelName(), modelCode);
		
		String builderPackage = targetPackage+".builder";
		ST builderTemplate = group.getInstanceOf("BuilderTemplate");
		builderTemplate.add("packageName",builderPackage);
		builderTemplate.add("genModel",dslModel);
		String builderCode = modelTemplate.render();
		writeToFile(builderPackage, dslModel.getModelName(), builderCode);
	}

	private static void generateModelInternDSL(DSLGenerationModel dslModel,
			String templatePath, String targetPackage) {
		STGroup group = new STGroupFile(templatePath);
		group.registerRenderer(String.class, new StringRenderer());
		ST simpleBT = group.getInstanceOf("BuilderTemplate");
		simpleBT.add("packageName",targetPackage);
		simpleBT.add("genModel",dslModel);
		writeToFile(targetPackage, dslModel.getModelName(), simpleBT.render());
	}
	
	private static void writeToFile(String targetPackage, String fileName, String generatedCode){
		try {
			String packagePath = targetPackage.replace(".", "\\");
			String pathForDSL = currentPath+"\\src\\"+packagePath;
			File targetDirectory = new File(pathForDSL);
			if(!targetDirectory.exists()){
				targetDirectory.mkdirs();
			}
			PrintWriter writer = new PrintWriter(pathForDSL+"\\"+fileName+".java");
			writer.print(generatedCode);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
