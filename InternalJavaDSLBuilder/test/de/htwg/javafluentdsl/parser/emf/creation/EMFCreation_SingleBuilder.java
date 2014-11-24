package de.htwg.javafluentdsl.parser.emf.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

public class EMFCreation_SingleBuilder {
	
	public static final String EMFGenModelPath = EMFCreation.projectPath + "/emfmodel/";
	
	private final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	private final static String FORUM_DEST = PACKAGE_DEST+".forum";
	private final static String EXCMODEL_DEST = PACKAGE_DEST+".exceptionCase";
	private static final String EXC_LISTMODEL_DEST =PACKAGE_DEST+".exceptionCaseList";
	private final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	private final static String OPTONLY_DEST = PACKAGE_DEST+".optonly";
	private final static String ENUM_DEST = PACKAGE_DEST+".enumtest";
	
	
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
	public void exceptionModel_SingleBuilderTest() {
		String genModelPath = EMFGenModelPath + "ExceptionCase.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, EXCMODEL_DEST+".singleBuilder");
		EMFCreation.fileExists(EXCMODEL_DEST+".singleBuilder.ExceptionCaseBuilder","java");
	}
	
	@Test
	public void exceptionListModel_SingleBuilderTest() {
		String genModelPath = EMFGenModelPath + "ExceptionCaseList.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION, EXC_LISTMODEL_DEST+".singleBuilder");
		EMFCreation.fileExists(EXC_LISTMODEL_DEST+".singleBuilder.ExceptionCaseListBuilder","java");
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
//	@Test
//	public void Expression_SingleBuilderTest(){
//		String genModelPath = EMFGenModelPath + "Expression.genmodel";
//		new StartEMF().startDSLGenerationProcess(genModelPath, GeneratorEcore.SINGLE_BUILDER_OPTION,"de.htwg.expression.dsl.singleBuilder");
//		EMFCreation.fileExists("de.htwg.expression.dsl.singleBuilder.ExpressionBuilder","java");
//	}
	
}
