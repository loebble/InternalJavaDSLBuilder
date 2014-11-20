package de.htwg.javafluentdsl.parser.regex.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorRegex;

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
	public final static String LIST_PRIM_PACKAGE_DEST = PACKAGE_DEST+".listprim";
	public final static String MA_SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST+".masimpleforum";
	public final static String FORUM_PACKAGE_DEST = PACKAGE_DEST+".forum";
	
	@Test
	public void testCreateUserDSL() {
		RegexCreation.createDSL(RegexCreation.USER_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,USER_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateUserDSLOPInClass() {
		RegexCreation.createDSL(RegexCreation.USER_DESCRIPTION_OPP_IN_SAME_CLASS, GeneratorRegex.INTERN_MODEL_OPTION,USER_PACKAGE_DEST+".oppinclass");
	}
	
	@Test
	public void testCreateUserOPTOnlyDSL() {
		RegexCreation.createDSL(RegexCreation.USER_OPT_ONLY_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,USER_OPT_DEST);
	}
	
	@Test
	public void testCreateSimpleForumDSL() {
		RegexCreation.createDSL(RegexCreation.SIMPLE_FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,SIMPLE_FORUM_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateSimpleForumListPrimDSL() {
		RegexCreation.createDSL(RegexCreation.LIST_PRIM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,LIST_PRIM_PACKAGE_DEST);
	}
	
	@Test
	public void testMACreateSimpleForumDSL() {
		RegexCreation.createDSL(RegexCreation.MA_SIMPLE_FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,MA_SIMPLE_FORUM_PACKAGE_DEST+".oneuser");
	}
	
	@Test
	public void testMA_BIREF_CreateSimpleForumDSL() {
		RegexCreation.createDSL(RegexCreation.MA_OPT_FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,MA_SIMPLE_FORUM_PACKAGE_DEST +".opt");
	}
	
	@Test
	public void testCreateForumDSL() {
		RegexCreation.createDSL(RegexCreation.FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_OPTION,FORUM_PACKAGE_DEST);
	}
	
//	@Test
//	public void testCreateUserDSL() {
//		RegexCreation.createDSL(RegexCreation.USER_DESCRIPTION, GeneratorRegex.INTERN_MODEL_NEW_OPTION,USER_PACKAGE_DEST +".NEW");
//	}
//	
//	@Test
//	public void testCreateUserOPTOnlyDSL() {
//		RegexCreation.createDSL(RegexCreation.USER_OPT_ONLY_DESCRIPTION, GeneratorRegex.INTERN_MODEL_NEW_OPTION,USER_OPT_DEST+".NEW");
//	}
//	
//	@Test
//	public void testCreateSimpleForumDSL() {
//		RegexCreation.createDSL(RegexCreation.SIMPLE_FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_NEW_OPTION,SIMPLE_FORUM_PACKAGE_DEST+".NEW");
//	}
//	
//	@Test
//	public void testCreateForumDSL() {
//		RegexCreation.createDSL(RegexCreation.FORUM_DESCRIPTION, GeneratorRegex.INTERN_MODEL_NEW_OPTION,FORUM_PACKAGE_DEST+".NEW");
//	}
	
}
