package de.htwg.javafluentdsl.parser.emf.creation;

import java.nio.file.Paths;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

public class EMFCreation_MultipleBuilder {
	private static final String projectPath = Paths.get(".").toAbsolutePath()
			.normalize().toString();
	private static final String EMFGenModelPath = projectPath + "/emfmodel/";
	
	private final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	private final static String FORUM_DEST = PACKAGE_DEST+".forum";
	private final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	private final static String OPTONLY_DEST = PACKAGE_DEST+".optonly";
	private static final String EXCMODEL_DEST = PACKAGE_DEST+".exceptionCase";
	private static final String EXC_LISTMODEL_DEST =PACKAGE_DEST+".exceptionCaseList";
	
	
	@Test
	public void simpleForum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "SimpleForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.MULTIPLE_BUILDER_OPTION, SIMPLE_FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.SimpleForumBuilder","java");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.UserBuilder","java");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder.PostBuilder","java");
	}
	
	@Test
	public void forum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.MULTIPLE_BUILDER_OPTION, FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.ForumBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.SectionBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.UserBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.PostBuilder","java");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder.RatingBuilder","java");
		
	}
	
	@Test
	public void exceptionModel_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "ExceptionCase.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.MULTIPLE_BUILDER_OPTION, EXCMODEL_DEST+".multiBuilder");
		EMFCreation.fileExists(EXCMODEL_DEST+".multiBuilder.ExceptionCaseBuilder","java");
		EMFCreation.fileExists(EXCMODEL_DEST+".multiBuilder.OppositeOnlyBuilder","java");
		EMFCreation.fileExists(EXCMODEL_DEST+".multiBuilder.OppositeWithMandBuilder","java");
		EMFCreation.fileExists(EXCMODEL_DEST+".multiBuilder.OppositeWithOPTBuilder","java");
	}
	
	@Test
	public void exceptionListModel_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "ExceptionCaseList.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.MULTIPLE_BUILDER_OPTION, EXC_LISTMODEL_DEST+".multiBuilder");
		EMFCreation.fileExists(EXC_LISTMODEL_DEST+".multiBuilder.ExceptionCaseListBuilder","java");
		EMFCreation.fileExists(EXC_LISTMODEL_DEST+".multiBuilder.OppositeOnlyListBuilder","java");
		EMFCreation.fileExists(EXC_LISTMODEL_DEST+".multiBuilder.OppositeWithMandListBuilder","java");
		EMFCreation.fileExists(EXC_LISTMODEL_DEST+".multiBuilder.OppositeWithOPTListBuilder","java");
	}
	
	@Test
	public void OptOnly_MultiBuilderTest(){
		String genModelPath = EMFGenModelPath + "OptOnly.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.MULTIPLE_BUILDER_OPTION, OPTONLY_DEST+".multiBuilder");
		EMFCreation.fileExists(OPTONLY_DEST+".multiBuilder.OptOnlyBuilder","java");
		EMFCreation.fileExists(OPTONLY_DEST+".multiBuilder.RefBuilder","java");
	}
	
	
}
