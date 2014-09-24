
package de.htwg.javaDSLBuilder.test;

import de.htwg.generated.regex.User;
import de.htwg.generated.regex.UserTMP;
import static de.htwg.generated.regex.UserTMP.UserBuilder.*;
import de.htwg.javaDSLBuilder.Builder;
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
	
	public final static String FORUM_DESCRIPTION = "modelName=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OA=address:Address}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String, .A=country:Country}"
			+ ".class=Country{.A=name:String, .A=isoCode:int, .OA=UN_Member:boolean}"
			+ ".imp={de.htwg.javaDSLBuilder.test.TestImport}"
			;
	
	public final static String SECOND_FORUM_DESCRIPTION = "modelName=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address, .A=country:Country}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
			+ ".class=Country{.A=name:String, .A=isoCode:int, .OA=UN_Member:boolean}"
			+ ".imp={de.htwg.javaDSLBuilder.test.TestImport}"
			;
	
	public static void main(String[] args) {
		CreatorRegex regexCreator= CreatorRegex.getInstance(FORUM_DESCRIPTION);
		DSLGenerationModel builderModel = regexCreator.getGenerationModel();
		System.out.println(builderModel.printedModel());
		System.out.println();
		System.out.println(builderModel.printedOrder());
		
		Builder.buildDSL(regexCreator, Builder.MODEL_MIXED_IN_TEMPLATE, "de.htwg.generated.regex");
		
//		User user = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad").noAddress().buildUser();
//		User user2 = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad")
//						.address().street("asda").houseNumber(123).zipCode("adwad")
//							.country().name("fewfe").optionalUN_Member(true).isoCode(12345).buildUser();
//		
//		UserTMP userTmp = 	createUser().firstName("Steven").lastName("Böckle").nickName("sboeckle")
//								.address(createAddress().street("Zasius").houseNumber(11).zipCode("78462")
//										.country(createCountry().name("Deutschland").optionalUN_Member(true).isoCode(12345)
//											.buildCountry())
//									.buildAddress())
//							.buildUser();
//		
//		System.out.println(userTmp.firstName + userTmp.address.houseNumber + userTmp.address.country.name);
		
	}

}
