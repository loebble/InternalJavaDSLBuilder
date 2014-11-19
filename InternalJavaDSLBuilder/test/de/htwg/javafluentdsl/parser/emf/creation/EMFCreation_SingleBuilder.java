package de.htwg.javafluentdsl.parser.emf.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

public class EMFCreation_SingleBuilder {
	
	public static final String EMFGenModelPath = EMFCreation.projectPath + "/emfmodel/";
	
	public final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	public final static String FORUM_DEST = PACKAGE_DEST+".forum";
	public final static String TESTMODEL_DEST = PACKAGE_DEST+".testmodel";
	public final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	public final static String OPTONLY_DEST = PACKAGE_DEST+".optonly";
	public final static String ENUM_DEST = PACKAGE_DEST+".enumtest";
	
	
	@Test
	public void simpleForum_SingleBuilderTest() {
		String genModelPath = EMFGenModelPath + "SimpleForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, SIMPLE_FORUM_DEST +".singleBuilder");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST + ".singleBuilder.SimpleForumBuilder","java");
	}
	
	@Test
	public void forum_SingleBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, FORUM_DEST+".singleBuilder");
		EMFCreation.fileExists(FORUM_DEST+".singleBuilder.ForumBuilder","java");
	}
	
	@Test
	public void testModel_SINGLEBuilderTest() {
		String genModelPath = EMFGenModelPath + "TestModel.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, TESTMODEL_DEST+".singleBuilder");
		EMFCreation.fileExists(TESTMODEL_DEST+".singleBuilder.TestModelBuilder","java");
	}
	
	@Test
	public void OptOnly_SingleBuilderTest(){
		String genModelPath = EMFGenModelPath + "OptOnly.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, OPTONLY_DEST+".singleBuilder");
		EMFCreation.fileExists(OPTONLY_DEST+".singleBuilder.OptOnlyBuilder","java");
	}
	@Test
	public void Enum_SingleBuilderTest(){
		String genModelPath = EMFGenModelPath + "SimpleTypes.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, ENUM_DEST+".singleBuilder");
		EMFCreation.fileExists(ENUM_DEST+".singleBuilder.SimpleTypesBuilder","java");
	}
}
