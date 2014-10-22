package de.htwg.javaDSLBuilder.creator.emf;

import org.junit.Test;

import de.htwg.javafluentdsl.main.StartEMF;


public class CreatorEMFTest_Creation {
	public final static String PACKAGE_DEST = "de.htwg.generated.emf.dsl";
	public final static String SIMPLE_FORUM_DEST = PACKAGE_DEST+".simpleForum";
	@Test
	public void SimpleForum_MultiBuilderTest() {
		String genModelPath = "C:/Users/loebble/git/InternalJavaDSLBuilder/InternalJavaDSLBuilder/emfmodel/SimpleForum.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath, "multi", SIMPLE_FORUM_DEST);
	}
	
}
