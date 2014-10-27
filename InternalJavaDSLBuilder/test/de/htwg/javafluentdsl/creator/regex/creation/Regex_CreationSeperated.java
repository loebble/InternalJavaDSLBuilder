package de.htwg.javafluentdsl.creator.regex.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.Generator;

/**
 * Test class for creation of a DSL using Regular Expression
 * and the separated model template
 *
 */
public class Regex_CreationSeperated {
	
	/*
	 * Package Declarations
	 */
	public final static String PACKAGE_DEST = "de.htwg.generated.regex.separated";
	public final static String USER_PACKAGE_DEST = PACKAGE_DEST+".user";
	public final static String USER_OPT_DEST = PACKAGE_DEST+".useropt";
	public final static String SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST+".simpleforum";
	public final static String FORUM_PACKAGE_DEST = PACKAGE_DEST+".forum";
	
	
	
	/*
	 * Right declarations
	 */
	@Test
	public void testCreateUserDSLSeperated() {
		RegexCreation.createDSL(RegexCreation.USER_DESCRIPTION, Generator.SEPARATED_MODEL_OPTION,USER_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateUserOPTOnlyDSLSeperated() {
		RegexCreation.createDSL(RegexCreation.USER_OPT_ONLY_DESCRIPTION, Generator.SEPARATED_MODEL_OPTION,USER_OPT_DEST);
	}
	
	@Test
	public void testCreateSimpleForumDSLSeperated() {
		RegexCreation.createDSL(RegexCreation.SIMPLE_FORUM_DESCRIPTION, Generator.SEPARATED_MODEL_OPTION,SIMPLE_FORUM_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateForumDSLSeperated() {
		RegexCreation.createDSL(RegexCreation.FORUM_DESCRIPTION, Generator.SEPARATED_MODEL_OPTION,FORUM_PACKAGE_DEST);
	}
	
}
