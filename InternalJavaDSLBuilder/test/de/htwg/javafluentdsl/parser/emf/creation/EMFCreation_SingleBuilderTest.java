package de.htwg.javafluentdsl.parser.emf.creation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

/**
 * Test class for creating Ecore DSLs with template option singleBuilder.
 */
public class EMFCreation_SingleBuilderTest {

	/**
	 * Method for testing a simple Forum as described by the SimpleForum.ecore
	 * and SimpleForum.genmodel files.
	 */
	@Test
	public void forum_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				EMFCreationTest.FORUM_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.FORUM_DEST
				+ ".singleBuilder.ForumBuilder", "java"));
	}

	/**
	 * Method for testing a complex Forum Model as described by the Forum.ecore
	 * and Forum.genmodel files.
	 */
	@Test
	public void complexForum_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ComplexForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				EMFCreationTest.COMPLEX_FORUM_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".singleBuilder.ComplexForumBuilder", "java"));
	}

	/**
	 * Method for testing a exception Model as described by the
	 * ExceptionModel.ecore and ExceptionModel.genmodel files. This Model was
	 * created for testing special Attribute and Reference cases.
	 */
	@Test
	public void exceptionModel_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ExceptionCase.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				EMFCreationTest.EXCMODEL_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXCMODEL_DEST
				+ ".singleBuilder.ExceptionCaseBuilder", "java"));
	}

	/**
	 * Method for testing a ExceptionList Model as described by the
	 * ExceptionList.ecore and ExceptionList.genmodel files. This Model was
	 * created for testing special List cases.
	 */
	@Test
	public void exceptionListModel_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ExceptionCaseList.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				EMFCreationTest.EXC_LISTMODEL_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXC_LISTMODEL_DEST
				+ ".singleBuilder.ExceptionCaseListBuilder", "java"));
	}

	/**
	 * Method for testing an OptOnly Model as described by the OptOnly.ecore and
	 * OptOnly.genmodel files. This Model was created for testing the optional
	 * only case if only simple optional attributes are in the ecore model.
	 */
	@Test
	public void optOnly_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "OptOnly.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				EMFCreationTest.OPTONLY_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.OPTONLY_DEST
				+ ".singleBuilder.OptOnlyBuilder", "java"));
	}

	/**
	 * Method for testing a SimpleTypes Model as described by the
	 * SimpleTypes.ecore and SimpleTypes.genmodel files. The model was created
	 * to test simple types and Enums.
	 */
	@Test
	public void Enum_SingleBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "SimpleTypes.genmodel";
		new StartEMF()
				.startDSLGenerationProcess(genModelPath,
						GeneratorEcore.SINGLE_BUILDER_OPTION,
						EMFCreationTest.ENUM_DEST+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.ENUM_DEST
				+ ".singleBuilder.SimpleTypesBuilder", "java"));
	}
}
