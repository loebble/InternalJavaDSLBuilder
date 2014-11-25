package de.htwg.javafluentdsl.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

/**
 * Util class for generating.
 * 
 */
public class GeneratorUtil_StringTemplate {

	/**
	 * Path to the project root. Used to write to the location of this project
	 * or later the jar.
	 */
	private static final String currentPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	
	public static final String STRING_TEMPLATES_DIR_PATH = "string_templates/";

	/**
	 * Looks for StringTemplate by the given template path. If found it creates
	 * and returns a {@link STGroup} object.
	 * 
	 * @param templatePath
	 *            relative path to template file
	 * @return STGroup 
	 * 			  object for accessing the template file
	 */
	public static STGroup getStringTemplateFromPath(String templatePath) {
		STGroup group = new STGroupFile(templatePath);
		group.registerRenderer(String.class, new StringRenderer());
		return group;
	}

	/**
	 * Writes generatedCode to a java file located at src/targetPackage
	 * 
	 * @param targetPackage
	 *            package name with package naming conventions (domain
	 *            seperation with '.')
	 * @param fileName
	 *            name of the generated file (without .java)
	 * @param generatedCode
	 *            the java code
	 * @return the generated files name or null of exception is thrown
	 * @throws IOException
	 *             if the java file or the target directory could not be created
	 */
	public static String writeToJavaFile(String targetPackage, String fileName,
			String generatedCode) {
		try {
			String packagePath = targetPackage.replace(".", "/");
			String pathForDSL = currentPath + "/generated/" + packagePath; //TODO
			File targetDirectory = new File(pathForDSL);
			if (!targetDirectory.exists()) {
				if(!targetDirectory.mkdirs())
					throw new IOException("Targe Directory at "
							+targetDirectory+" could not be created.");
			}
			String filePath = pathForDSL + "/" + fileName + ".java";
			PrintWriter writer = new PrintWriter(filePath);
			writer.print(generatedCode);
			writer.close();
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
