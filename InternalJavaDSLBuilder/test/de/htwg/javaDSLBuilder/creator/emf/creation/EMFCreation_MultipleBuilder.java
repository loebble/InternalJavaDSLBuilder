package de.htwg.javaDSLBuilder.creator.emf.creation;

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
	public final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	
	
	@Test
	public void simpleForum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, Generator.MULTIPLE_BUILDER_OPTION, SIMPLE_FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(SIMPLE_FORUM_DEST+".multiBuilder");
	}
	
	@Test
	public void forum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, Generator.MULTIPLE_BUILDER_OPTION, FORUM_DEST+".multiBuilder");
		EMFCreation.fileExists(FORUM_DEST+".multiBuilder");
	}
	
	
	
}
