package de.htwg.javaDSLBuilder.creator.emf;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.javaDSLBuilder.DSLBuilder;

public class CreatorEMFTest_Creation {
	
	@Test
	public void SimpleForum_MultiBuilderTest() {
		String genModelPath = "C:/Users/loebble/git/InternalJavaDSLBuilder/InternalJavaDSLBuilder/emfmodel/SimpleForum.genmodel";
		DSLBuilder.createDSLForEMF(genModelPath, "multi", "de.htwg.generated.emfdsl.simpleForum");
	}
	
	@Test
	public void SimpleForum_SingleBuilderTest() {
		fail("Not yet implemented");
	}
	
}
