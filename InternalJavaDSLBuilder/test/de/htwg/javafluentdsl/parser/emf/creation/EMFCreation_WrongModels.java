package de.htwg.javafluentdsl.parser.emf.creation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.main.StartEMF;

public class EMFCreation_WrongModels {

	@Test(expected = IllegalArgumentException.class)
	public void wrong_PackageNameTest() {
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "Wrong_NoEClass.genmodel";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				"de.htwg.generated.wrong.Wrong_NoEClass");
		assertFalse(EMFCreationTest.fileExists(
				"de.htwg.generated.wrong.Wrong_NoEClass.Wrong_NoEClassBuilder", "java"));
	}
	
	@Test
	public void wrong_FirstEClassTest() throws IOException{
		String genModelPath = EMFCreationTest.EMFGenModelPath
				+ "Wrong_FirstEClass.genmodel";
		String packageString = "de.htwg.generated.wrong";
		String pathString = packageString+".Wrong_FirstEClassName";
		new StartEMF().startDSLGenerationProcess(genModelPath,
				GeneratorEcore.SINGLE_BUILDER_OPTION,
				pathString);
		assertTrue(EMFCreationTest.fileExists(pathString +".Wrong_FirstEClassBuilder","java"));
		//Delete generated files
		Files.deleteIfExists(Paths.get(EMFCreationTest.projectPath 
				+ "/generated/" + pathString.replace('.', '/')
				+ "/Wrong_FirstEClassBuilder." + "java"));
		Files.deleteIfExists(Paths.get(EMFCreationTest.projectPath 
				+ "/generated/" + pathString.replace('.', '/')));
		Files.deleteIfExists(Paths.get(EMFCreationTest.projectPath
				+ "/generated/" + packageString.replace('.', '/')));
	}
	
}
