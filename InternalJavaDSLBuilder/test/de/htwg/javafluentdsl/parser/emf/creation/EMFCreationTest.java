package de.htwg.javafluentdsl.parser.emf.creation;

import java.io.File;
import java.nio.file.Paths;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Class for Creation of DSLs for EMFs Ecore Models. Starts the test
 * classes EMFCreation_SingleBuilderTest and EMFCreation_MultipleBuilderTest
 *
 */

@RunWith(Suite.class)
@SuiteClasses({ EMFCreation_SingleBuilderTest.class,
		EMFCreation_MultipleBuilderTest.class, EMFCreation_WrongModels.class })
public class EMFCreationTest {
	/**
	 * Holds the absolute Path to the current directory.
	 */
	public static final String projectPath = 
	        Paths.get(".").toAbsolutePath()
			.normalize().toString();
	/**
	 * Path to the ECore Models.
	 */
	public static final String EMFGenModelPath = 
	        projectPath + "/emfmodel/";
	/**
	 * Root package for all generated Ecore DSLs.
	 */
	public static final String PACKAGE_DEST = 
	        "de.htwg.generated.emf.dsl";

	/**
	 * Package for Forum DSL.
	 */
	public static final String FORUM_DEST = 
	        PACKAGE_DEST + ".forum";
	/**
     * Package for complex Forum DSL.
     */
	public static final String COMPLEX_FORUM_DEST = 
	        PACKAGE_DEST + ".complexforum";
	/**
	 * Package for exception DSL.
	 */
	public static final  String EXCMODEL_DEST = 
	        PACKAGE_DEST + ".exceptionCase";
	/**
	 * Package for exceptionList DSL.
	 */
	public final static  String EXC_LISTMODEL_DEST = 
	        PACKAGE_DEST
			+ ".exceptionCaseList";
	/**
	 * Package for OptOnly DSL.
	 */
	public static final  String OPTONLY_DEST = 
	        PACKAGE_DEST + ".optonly";
	/**
	 * Package for SimpleType DSL.
	 */
	public static final  String ENUM_DEST = 
	        PACKAGE_DEST + ".simplyType";

	/**
	 * Helper Method to check if a File is was actually generated.
	 * 
	 * @param path
	 *            The absolute Path the generated File should be located at
	 * @param ending
	 *            the ending the file should have in this case most likely "java"
	 * @return
	 *         true if file could be created, false if not
	 */
	public static boolean fileExists(final String path, final String ending) {
		String modelPath = projectPath + "/generated/" + path.replace('.', '/')
				+ "." + ending;
		try {
			File file = new File(modelPath);
			return file.exists();
		} catch (Exception e) {
			System.err.println("Class at " + modelPath + " doesnt exist");
			return false;
		}
	}

}
