package de.htwg.javafluentdsl.creator.regex.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.Generator;

/**
 * Test class for wrong language descriptions
 *
 */
public class Regex_CreationIntern {
	
	/*
	 * Package Declarations
	 */
	public final static String PACKAGE_DEST = "de.htwg.generated.regex.intern";
	public final static String USER_PACKAGE_DEST = PACKAGE_DEST+".user";
	public final static String USER_OPT_DEST = PACKAGE_DEST+".useropt";
	public final static String SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST+".simpleforum";
	public final static String FORUM_PACKAGE_DEST = PACKAGE_DEST+".forum";
	
	
	@Test
	public void testCreateUserDSL() {
		RegexCreation.createDSL(RegexCreation.USER_DESCRIPTION, Generator.INTERN_MODEL_OPTION,USER_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateUserOPTOnlyDSL() {
		RegexCreation.createDSL(RegexCreation.USER_OPT_ONLY_DESCRIPTION, Generator.INTERN_MODEL_OPTION,USER_OPT_DEST);
	}
	
	@Test
	public void testCreateSimpleForumDSL() {
		RegexCreation.createDSL(RegexCreation.SIMPLE_FORUM_DESCRIPTION, Generator.INTERN_MODEL_OPTION,SIMPLE_FORUM_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateForumDSL() {
		RegexCreation.createDSL(RegexCreation.FORUM_DESCRIPTION, Generator.INTERN_MODEL_OPTION,FORUM_PACKAGE_DEST);
	}
	
}
