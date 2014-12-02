package de.htwg.javafluentdsl.parser.regex.creation;

import java.io.File;
import java.nio.file.Paths;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.htwg.javafluentdsl.main.StartRegex;
/**
 * Util Class for language descriptions.
 * Also test class forwrong language descriptions
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ RegexCreation_WrongDescriptionTest.class, RegexCreation_InternTest.class, RegexCreation_SeparatedTest.class })
public class RegexCreationTest {
	
	/*
	 * Correct descriptions
	 */
	public final static String USER_MULTI_IMPORT_DESCRIPTION =
			".class=User{.A=firstName:String, .A=lastName:String, .A=email:URL, .A=born:Date}"
			+ ".imp={java.net.URL, java.util.Date}"
			;
	
	public final static String USER_DESCRIPTION =
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String, .A=country:Country}"
			+ ".class=Country{.A=name:String, .A=UN_Member:boolean}"
			;
	
	public final static String USER_DESCRIPTION_OPP_IN_SAME_CLASS=
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OA=otherUser:User, .OP=oppUser:User->otherUser}"
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
	
	public final static String LIST_PRIM_DESCRIPTION =
			".class=SomeClass{.LA=booleanList:Boolean,.LA=charList:Character,.LA=byteList:Byte,"
				+ ".LA=shortList:Short,.LA=intList:Integer,.LA=longList:Long,.LA=floatList:Float,.LA=doubleList:Double}"
			;
	
	public final static String MA_SIMPLE_FORUM_DESCRIPTION =
			".class=Forum{.A=name:String, .A=url:URL , .A=user:User}"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String MA_BIREF_FORUM_DESCRIPTION =
			".class=Forum{.A=name:String, .A=url:URL , .LA=user:User}"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=email:String, .LA=post:Post, .OP=forum:Forum->user}"
			+ ".class=Post{.A=title:String, .A=text:String, .LA=replier:User, .OP=creator:User->post}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DESCRIPTION =
			".class=Forum{.A=name:String, .A=url:URL , .LA=user:User}"
			+ ".class=User{.OA=firstName:String, .OA=lastName:String, .OA=age:int, .A=email:String, .LA=post:Post}"
			+ ".class=Post{.A=title:String, .A=text:String, .LA=replier:User, .OP=creator:User->post}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String COMPLEXFORUM_DESCRIPTION =
			".class=ComplexForum{.A=name:String, .A=url:URL, .LA=sections:Section, .LA=user:ComplexUser}"
			+ ".class=Section{.A=name:String, .LA=moderators:ComplexUser}"
			+ ".class=ComplexUser{.OA=firstName:String, .OA=lastName:String, .OA=age:int, .A=email:String,.A=nickName:String, .A=rating:Rating, .LA=posts:ComplexPost, .OP=moderatorOfSection:Section->moderators}"
			+ ".class=ComplexPost{.A=title:String, .A=text:String, .OA=views:int, .LA=repliers:ComplexUser, .OA=rating:Rating, .OP=creator:ComplexUser->posts}"
			+ ".class=Rating{.OA=upps:int, .OA=downs:int, .OP=forUser:ComplexUser->rating, .OP=forPost:ComplexPost->rating}"
			+ ".imp={java.net.URL}"
			;
	
	public static boolean createDSL(String modelDescription, String templateOption, String targetPackage) {
		new StartRegex().startDSLGenerationProcess(modelDescription, templateOption, targetPackage);
		String modelPath =  Paths.get(".").toAbsolutePath().normalize().toString() + "\\generated\\" +targetPackage.replace('.', '\\');
		try {
			File file = new File(modelPath);
			return file.exists();
		} catch(Exception e) {
			System.err.println("Class at "+modelPath+" doesnt exist");
		    return false;
		}
	}

}
