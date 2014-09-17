
package de.htwg.javaDSLBuilder.test;

import de.htwg.javaDSLBuilder.creator.CreatorRegex;
import de.htwg.javaDSLBuilder.model.DSLGenerationModel;

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
	
	public final static String FORUM_DESCRIPTION = "MoDeLnaME=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .LA=posts:Post, .OA=age:int, .OA=address:Address}"
			+ ".class=Post{.A=header:String, .A=text:String, .OA=likes:int, .OLA=replier:User}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
			;
	
	public final static String SECOND_FORUM_DESCRIPTION = "modelName=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .A=address:Address, .OA=age:int, .A=nickName:String}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String, .A=country:Country}"
			+ ".class=Country{.A=name:String, .A=isoCode:int, .OA=UN_Member:boolean}"
			;
	
	public static void main(String[] args) {
		CreatorRegex regexCreator= CreatorRegex.getInstance(SECOND_FORUM_DESCRIPTION);
		DSLGenerationModel builderModel = regexCreator.getGenerationModel();
		System.out.println(builderModel);
		System.out.println();
		System.out.println(builderModel.printOrder());
	}

}
