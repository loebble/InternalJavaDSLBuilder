package de.htwg.javaDSLBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import de.htwg.javaDSLBuilder.creator.ICreator;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public class Builder {
	
	public static final String MODEL_MIXED_IN_TEMPLATE = "mixedIn";
	public static final String MODEL_SEPERATED_TEMPLATE = "asd"; //TODO
	private static final String projectPath = Paths.get(".").toAbsolutePath().normalize().toString();
	
	public static void buildDSL(ICreator creator, String template, String targetPackage){
		DSLGenerationModel builderModel = creator.getGenerationModel();
		ST simpleBT = getTemplate(template);
		simpleBT.add("packageName",targetPackage);
		simpleBT.add("genModel",builderModel);
		String res = simpleBT.render();
//		System.out.println(res);
		try {
			String packagePath = targetPackage.replace(".", "\\");
			String pathForDSL = projectPath+"\\src\\"+packagePath;
			File targetDirectory = new File(pathForDSL);
			if(!targetDirectory.exists()){
				targetDirectory.mkdirs();
			}
			PrintWriter writer = new PrintWriter(pathForDSL+"\\"+builderModel.getModelName()+".java");
			writer.print(res);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static ST getTemplate(String template){
		String templatePath = "";
		File templateDirecorty = new File(projectPath+"/templates");
		System.out.println("exists "+templateDirecorty.getAbsolutePath());
		templatePath = templateDirecorty.toString();
		switch (template) {
		case MODEL_MIXED_IN_TEMPLATE:
			templatePath += "/ModelMixedInTemplate.stg";
			break;
		default:
			break;
		}
		STGroup group = new STGroupFile(templatePath);
		group.registerRenderer(String.class, new StringRenderer());
		ST simpleBT = group.getInstanceOf("BuilderTemplate");
		return simpleBT;
	}

}
