package de.htwg.javafluentdsl.main;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Test for the Main Class.
 * Holds little tests for the Main Class.
 */
public class MainTest {

	@Test
	public void testMain_wrongSourcePackage() {
		String[] args = new String[]{"someString","someString","someString."};
		try{
			Main.main(args);
		}catch(IllegalArgumentException e){
			assertTrue(e.getMessage().equals(Main.WRONG_SOURCE));
		}
	}
	
	@Test
	public void testMain_GetStart() {
		String source = "usr/bin/local/MyModel.genmodel";
		IStart start = Main.getStartForSource(source);
		
		assertTrue(start instanceof StartEMF);
		start = Main.getStartForSource(".class={.OA=name}");
		assertTrue(start instanceof StartRegex);
		start = Main.getStartForSource("rnd");
		assertTrue(start == null);
	}
	

}
