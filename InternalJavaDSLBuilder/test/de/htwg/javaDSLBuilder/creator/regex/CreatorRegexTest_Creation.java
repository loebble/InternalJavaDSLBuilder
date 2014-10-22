package de.htwg.javaDSLBuilder.creator.regex;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

import de.htwg.javafluentdsl.creator.CreatorRegex;
import de.htwg.javafluentdsl.generator.Generator;
import de.htwg.javafluentdsl.main.StartEMF;
import de.htwg.javafluentdsl.main.StartRegex;

/**
 * Test class for wrong language descriptions
 *
 */
public class CreatorRegexTest_Creation {
	
	/*
	 * Package Declarations
	 */
	public final static String PACKAGE_DEST = "de.htwg.generated.regex";
	public final static String USER_PACKAGE_DEST = PACKAGE_DEST+".user";
	public final static String USER_OPT_DEST = PACKAGE_DEST+".useropt";
	public final static String SIMPLE_FORUM_PACKAGE_DEST = PACKAGE_DEST+".simpleforum";
	public final static String FORUM_PACKAGE_DEST = PACKAGE_DEST+".forum";
	
	
	/*
	 * Wrong declarations
	 */
	public final static String FORUM_WRONG_DECL =
			".class=Forum{.A=name=String, url:URL, .a=asd:int}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DUPL_CLASSES =
			".class=Forum{.A=name=String, url:URL, .A=asd:int}"
			+ ".class=Forum{.A=asd=String}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DUPL_ATTRIBUTENAME =
			".class=Forum{.A=name=String, url:URL, .OA=name:int}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_OP_NO_REFERENCE =
			".class=Forum{.A=name=String, url:URL, .OA=userABC:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OP=forum:Forum->user}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_OP_WRONG_REFERENCE_TYPE =
			".class=Forum{.A=name=String, url:URL, .OA=user:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OP=forum:Forum->user}"
			+ ".class=Post{.A=title:String, .A=text:String, .A=views:int, .OP=creator:User->forum}"
			+ ".imp={java.net.URL}"
			;
	
	
	/*
	 * Correct declarations
	 */
	
	public final static String USER_DESCRIPTION =
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String, .A=country:Country}"
			+ ".class=Country{.A=name:String, .A=UN_Member:boolean}"
			;
	
	public final static String USER_OPT_ONLY_DESCRIPTION =
			".class=UserOPT{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:AddressOPT}"
			+ ".class=AddressOPT{.OA=street:String, .OA=houseNumber:int, .OA=zipCode:String, .OA=country:CountryOPT, .OP=user:UserOPT->address}"
			+ ".class=CountryOPT{.OA=name:String, .OA=unMember:boolean}"
			;
	
	public final static String SIMPLE_FORUM_DESCRIPTION =
			".class=SimpleForum{.A=name:String, .A=url:URL , .LA=user:SimpleUser}"
			+ ".class=SimpleUser{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OA=post:SimplePost, .OP=forum:SimpleForum->user}"
			+ ".class=SimplePost{.A=title:String, .A=text:String, .A=views:int, .OP=creator:SimpleUser->post}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DESCRIPTION =
			".class=Forum{.A=name:String, .A=url:URL, .LA=sections:Section, .LA=user:User}"
			+ ".class=Section{.A=name:String, .LA=moderators:User}"
			+ ".class=User{.OA=firstName:String, .OA=lastName:String, .OA=age:int, .A=email:String,.A=nickName:String, .A=rating:Rating, .LA=posts:Post, .OP=moderatorOfSection:Section->moderators}"
			+ ".class=Post{.A=title:String, .A=text:String, .OA=views:int, .LA=repliers:User, .A=rating:Rating, .OP=creator:User->posts}"
			+ ".class=Rating{.OA=upps:int, .OA=downs:int, .OP=forUser:User->rating, .OP=forPost:Post->rating}"
			+ ".imp={java.net.URL}"
			;
	
	@Test(expected = IllegalArgumentException.class)
	public void testSimpleDeclFails() {
		CreatorRegex.getInstance(FORUM_WRONG_DECL);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateClasses() {
		CreatorRegex.getInstance(FORUM_DUPL_CLASSES);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateAttribute() {
		CreatorRegex.getInstance(FORUM_DUPL_ATTRIBUTENAME);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_NoRefAttr() {
		CreatorRegex.getInstance(FORUM_OP_NO_REFERENCE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongRefType() {
		CreatorRegex.getInstance(FORUM_OP_WRONG_REFERENCE_TYPE);
	}
	
	
	/*
	 * Right declarations
	 */
	@Test
	public void testCreateUserDSL() {
		createDSL(USER_DESCRIPTION, Generator.INTERN_MODEL_OPTION,USER_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateUserOPTOnlyDSL() {
		createDSL(USER_OPT_ONLY_DESCRIPTION, Generator.INTERN_MODEL_OPTION,USER_OPT_DEST);
	}
	
	@Test
	public void testCreateSimpleForumDSL() {
		createDSL(SIMPLE_FORUM_DESCRIPTION, Generator.INTERN_MODEL_OPTION,SIMPLE_FORUM_PACKAGE_DEST);
	}
	
	@Test
	public void testCreateForumDSL() {
		createDSL(FORUM_DESCRIPTION, Generator.INTERN_MODEL_OPTION,FORUM_PACKAGE_DEST);
	}
	
	public static void createDSL(String modelDescription, String templateOption, String targetPackage) {
		new StartRegex().startDSLGenerationProcess(modelDescription, templateOption, targetPackage);
		String modelPath =  Paths.get(".").toAbsolutePath().normalize().toString() + "\\src\\" +targetPackage.replace('.', '\\');
		try {
			File file = new File(modelPath);
			assert(file.exists());
		} catch(Exception e) {
			System.err.println("Class at "+modelPath+" doesnt exist");
		    fail();
		}
	}
	
}
