package de.htwg.javafluentdsl.parser.regex.creation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.javafluentdsl.generator.GeneratorRegex;

/**
 * Test class for wrong language descriptions
 *
 */
public class RegexCreation_InternTest {

	/*
	 * Package Declarations
	 */
	private final static String PACKAGE_DEST = "de.htwg.generated.regex.intern";
	private final static String USER_PACKAGE_DEST = PACKAGE_DEST + ".user";
	private final static String USER_MULTI_IMP_PACKAGE_DEST = PACKAGE_DEST
			+ ".usermultiimp";
	private final static String USER_OPT_DEST = PACKAGE_DEST + ".useropt";
	private final static String SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST
			+ ".simpleforum";
	private final static String LIST_PRIM_PACKAGE_DEST = PACKAGE_DEST
			+ ".listprim";
	private final static String MA_SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST
			+ ".masimpleforum";
	private final static String FORUM_PACKAGE_DEST = PACKAGE_DEST + ".forum";
	private final static String FORUM_COMPLEX_PACKAGE_DEST = PACKAGE_DEST
			+ ".complexforum";

	@Test
	public void testCreateUserDSLMultiImport() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.USER_MULTI_IMPORT_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION,
				USER_MULTI_IMP_PACKAGE_DEST));
	}

	@Test
	public void testCreateUserDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.USER_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION, USER_PACKAGE_DEST));
	}

	@Test
	public void testCreateUserDSLOPInClass() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.USER_DESCRIPTION_OPP_IN_SAME_CLASS,
				GeneratorRegex.INTERN_BUILDER_OPTION, USER_PACKAGE_DEST
						+ ".oppinclass"));
	}

	@Test
	public void testCreateUserOPTOnlyDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.USER_OPT_ONLY_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION, USER_OPT_DEST));
	}

	@Test
	public void testCreateSimpleForumDSL() {
		assertTrue(RegexCreationTest
				.createDSL(RegexCreationTest.SIMPLE_FORUM_DESCRIPTION,
						GeneratorRegex.INTERN_BUILDER_OPTION,
						SIMPLE_FORUM_PACKAGE_DEST));
	}

	@Test
	public void testCreateSimpleForumListPrimDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.LIST_PRIM_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION, LIST_PRIM_PACKAGE_DEST));
	}

	@Test
	public void testMACreateSimpleForumDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.MA_SIMPLE_FORUM_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION,
				MA_SIMPLE_FORUM_PACKAGE_DEST + ".oneuser"));
	}

	@Test
	public void testCreateForumDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.FORUM_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION, FORUM_PACKAGE_DEST));
	}

	@Test
	public void testComplexCreateForumDSL() {
		assertTrue(RegexCreationTest.createDSL(
				RegexCreationTest.COMPLEXFORUM_DESCRIPTION,
				GeneratorRegex.INTERN_BUILDER_OPTION,
				FORUM_COMPLEX_PACKAGE_DEST));
	}

}
