package de.htwg.javafluentdsl.creator.emf.creation;

import java.nio.file.Paths;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.Generator;
import de.htwg.javafluentdsl.main.StartEMF;

public class EMFCreation_MultipleBuilder {
	public static final String projectPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	public static final String EMFGenModelPath = projectPath + "/emfmodel/";
	
	public final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	public final static String FORUM_DEST = PACKAGE_DEST+".forum";
	public final static String TESTMODEL_DEST = PACKAGE_DEST+".testmodel";
	public final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	
	
	@Test
	public void simpleForum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "SimpleForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, Generator.MULTIPLE_BUILDER_OPTION, SIMPLE_FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.SimpleForumBuilder","java");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.UserBuilder","java");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.PostBuilder","java");
	}
	
	@Test
	public void forum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, Generator.MULTIPLE_BUILDER_OPTION, FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.ForumBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.SectionBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.UserBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.PostBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.RatingBuilder","java");
		
	}
	
	@Test
	public void testModel_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "TestModel.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, Generator.MULTIPLE_BUILDER_OPTION, TESTMODEL_DEST+".multiBuilder");
		EMFCreation.fileExists(TESTMODEL_DEST+".multiBuilder.TestModelBuilder","java");
		EMFCreation.fileExists(TESTMODEL_DEST+".multiBuilder.AClassBuilder","java");
		EMFCreation.fileExists(TESTMODEL_DEST+".multiBuilder.BClassBuilder","java");
	}
	
	
}
