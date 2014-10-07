
package de.htwg.javaDSLBuilder.test;

import java.net.MalformedURLException;
import java.net.URL;

import de.htwg.generated.regex.Forum;
//import de.htwg.generated.regex.Forum;
//import de.htwg.generated.regex.Forum.UserBuilder;
import de.htwg.javaDSLBuilder.GeneratorRegex;
import de.htwg.javaDSLBuilder.creator.CreatorRegex;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public class CreatorRegexTest {
	
//	public final static String FORUM_DESCRIPTION = "MoDeLnaME=Forum"
//			+ ".A=name:String"
//			+ ".A=url:String"
//			+ ".OA=genre:String"
//			+ ".LA=users:User"
//			+ ".class:User{.A=firstName:String, .A=lastName:String, .LA=posts:Post, .OA=age:int, .OA=address:Address}"
//			+ ".class:Post{.A=header:String, .A=text:String, .OA=likes:int, .OLA=replier:User}"
//			+ ".class:Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
//			;
	
	public final static String FORUM_DESCRIPTION = "modelName=Forum"
			+ ".class=Forum{.A=name:String, .A=url:URL , .LA=user:User, .A=post:Post}"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OA=post:Post}"
			+ ".class=Post{.A=title:String, .A=text:String, .A=views:int, .OP=creator:User->post, .OP=forum:Forum->post}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String SECOND_FORUM_DESCRIPTION = "modelName=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address, .A=country:Country}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
			+ ".class=Country{.A=name:String, .A=isoCode:int, .OA=UN_Member:boolean}"
			+ ".imp={de.htwg.javaDSLBuilder.test.TestImport}"
			;
	
	
	public static void main(String[] args) throws MalformedURLException {
		CreatorRegex regexCreator= CreatorRegex.getInstance(FORUM_DESCRIPTION);
		DSLGenerationModel builderModel = regexCreator.getGenerationModel();
		System.out.println(builderModel.printModel());
		System.out.println();
		System.out.println(builderModel.printOrder());
		
		GeneratorRegex.buildDSL(regexCreator, GeneratorRegex.MODEL_MIXED_IN_TEMPLATE, "de.htwg.generated.regex");
		
	}

}
