package de.htwg.javaDSLBuilder.creator.emf;

import java.nio.file.Paths;

import org.junit.Test;

import de.htwg.javafluentdsl.main.StartEMF;

public class CreatorEMFTest_Creation {
	
	private static final String EMFGenModelPath = Paths.get(".").toAbsolutePath()
	.normalize().toString()+ "/emfmodel/";
	public final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	public final static String FORUM_DEST = PACKAGE_DEST+".forum";
	public final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	
	
	@Test
	public void simpleForum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "SimpleForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, "multi", SIMPLE_FORUM_DEST);
	}
	
	@Test
	public void forum_MultiBuilderTest() {
		String genModelPath = EMFGenModelPath + "Forum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, "multi", FORUM_DEST);
	}
	
}
