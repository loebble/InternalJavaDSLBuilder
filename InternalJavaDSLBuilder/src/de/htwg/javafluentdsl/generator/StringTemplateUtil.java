package de.htwg.javafluentdsl.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

/**
 * Utility class for generating java files with StringTemplate files.
 * 
 */
public final class StringTemplateUtil {

    /**
     * Path to the current directory where either the tool is started at or the
     * project is located.
     */
    private static final String CURRENT_PATH = Paths.get(".").toAbsolutePath()
            .normalize().toString();
    /**
     * Location of the String Templates relative to the CURRENT_PATH.
     */
    public static final String STRING_TEMPLATES_DIR_PATH = "string_templates/";

    /**
     * Utility class therefore no instantiation needed.
     */
    private StringTemplateUtil() {
    };

    /**
     * Looks for StringTemplate by the given template path. If found it creates
     * and returns a {@link STGroup} object.
     * 
     * @param templatePath
     *            relative path to template file
     * @return STGroup object for accessing the template file
     */
    public static STGroup getStringTemplateFromPath(final String templatePath) {
        STGroup group = new STGroupFile(templatePath);
        group.registerRenderer(String.class, new StringRenderer());
        return group;
    }

    /**
     * Writes generatedCode to a java file located at src/targetPackage.
     * throws IOException
     *             if the java file or 
     *             the target directory could not be created
     * @param targetPackage
     *            package name with package naming conventions (domain
     *            seperation with '.')
     * @param fileName
     *            name of the generated file (without .java)
     * @param generatedCode
     *            the java code
     * @return the generated files name or null of exception is thrown
     */
    public static String writeToJavaFile(final String targetPackage,
            final String fileName, final String generatedCode) {
        try {
            String packagePath = targetPackage.replace(".", "/");
            String pathForDSL = CURRENT_PATH + "/" + "generated" + "/"
                    + packagePath;
            File targetDirectory = new File(pathForDSL);
            if (!targetDirectory.exists()) {
                if (!targetDirectory.mkdirs()) {
                    throw new IOException("Targe Directory at "
                            + targetDirectory + " could not be created.");
                }
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
