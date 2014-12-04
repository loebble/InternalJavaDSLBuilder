package de.htwg.javafluentdsl.parser.emf.creation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

/**
 * Test class for Ecore DSLs with template option multiBuilder.
 *
 */
public class EMFCreation_MultipleBuilderTest {

	/**
	 * Method for testing a Forum as described by the Forum.ecore
	 * and Forum.genmodel files.
	 */
	@Test
	public void forum_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.MULTIPLE_BUILDER_OPTION,
				EMFCreationTest.FORUM_DEST + ".multiBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.FORUM_DEST
				+ ".multiBuilder.ForumBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.FORUM_DEST
				+ ".multiBuilder.UserBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.FORUM_DEST
				+ ".multiBuilder.PostBuilder", "java"));
	}

	/**
	 * Method for testing a complex Forum Model as described by the Forum.ecore
	 * and ComplexForum.genmodel files.
	 */
	@Test
	public void complexForum_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ComplexForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.MULTIPLE_BUILDER_OPTION,
				EMFCreationTest.COMPLEX_FORUM_DEST + ".multiBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".multiBuilder.ComplexForumBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".multiBuilder.SectionBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".multiBuilder.ComplexUserBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".multiBuilder.ComplexPostBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.COMPLEX_FORUM_DEST
				+ ".multiBuilder.RatingBuilder", "java"));

	}

	/**
	 * Method for testing a exception Model as described by the
	 * ExceptionModel.ecore and ExceptionModel.genmodel files. This Model was
	 * created for special cases.
	 */
	@Test
	public void exceptionModel_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ExceptionCase.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.MULTIPLE_BUILDER_OPTION,
				EMFCreationTest.EXCMODEL_DEST + ".multiBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXCMODEL_DEST
				+ ".multiBuilder.ExceptionCaseBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXCMODEL_DEST
				+ ".multiBuilder.OppositeOnlyBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXCMODEL_DEST
				+ ".multiBuilder.OppositeWithMandBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXCMODEL_DEST
				+ ".multiBuilder.OppositeWithOPTBuilder", "java"));
	}

	/**
	 * Method for testing a ExceptionList Model as described by the
	 * ExceptionList.ecore and ExceptionList.genmodel files. This Model was
	 * created for special List cases.
	 */
	@Test
	public void exceptionListModel_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "ExceptionCaseList.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.MULTIPLE_BUILDER_OPTION,
				EMFCreationTest.EXC_LISTMODEL_DEST + ".multiBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXC_LISTMODEL_DEST
				+ ".multiBuilder.ExceptionCaseListBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXC_LISTMODEL_DEST
				+ ".multiBuilder.OppositeOnlyListBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXC_LISTMODEL_DEST
				+ ".multiBuilder.OppositeWithMandListBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.EXC_LISTMODEL_DEST
				+ ".multiBuilder.OppositeWithOPTListBuilder", "java"));
	}

	/**
	 * Method for testing a OptOnly Model as described by the OptOnly.ecore and
	 * OptOnly.genmodel files. This Model was created for testing the optional
	 * only case if only simple optional attributes are in the ecore model.
	 */
	@Test
	public void OptOnly_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "OptOnly.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.MULTIPLE_BUILDER_OPTION,
				EMFCreationTest.OPTONLY_DEST + ".multiBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.OPTONLY_DEST
				+ ".multiBuilder.OptOnlyBuilder", "java"));
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.OPTONLY_DEST
				+ ".multiBuilder.RefBuilder", "java"));
	}

	/**
	 * Method for testing a SimpleTypes Model as described by the
	 * SimpleTypes.ecore and SimpleTypes.genmodel files. The model was created
	 * to test simple types and Enums.
	 */
	@Test
	public void Enum_MultiBuilderTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "SimpleTypes.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION, EMFCreationTest.ENUM_DEST
						+ ".singleBuilder");
		assertTrue(EMFCreationTest.fileExists(EMFCreationTest.ENUM_DEST
				+ ".singleBuilder.SimpleTypesBuilder", "java"));
	}

}
