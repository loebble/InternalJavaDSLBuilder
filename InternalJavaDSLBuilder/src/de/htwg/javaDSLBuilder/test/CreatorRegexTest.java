
package de.htwg.javaDSLBuilder.test;

import java.net.MalformedURLException;
import java.net.URL;

import de.htwg.generated.regex.Forum;
import de.htwg.generated.regex.Forum.PostBuilder;
import de.htwg.generated.regex.Forum.UserBuilder;
//import de.htwg.generated.regex.Forum;
//import de.htwg.generated.regex.Forum.UserBuilder;

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
			+ ".class=Forum{.A=name:String, .A=url:URL , .A=users:User}"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OA=post:Post}"
			+ ".class=Post{.A=title:String, .A=text:int, .A=views:int, .OP=creator:User->post}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String SECOND_FORUM_DESCRIPTION = "modelName=User"
			+ ".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address, .A=country:Country}"
			+ ".class=Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
			+ ".class=Country{.A=name:String, .A=isoCode:int, .OA=UN_Member:boolean}"
			+ ".imp={de.htwg.javaDSLBuilder.test.TestImport}"
			;
	
	
	public static void main(String[] args) throws MalformedURLException {
//		CreatorRegex regexCreator= CreatorRegex.getInstance(FORUM_DESCRIPTION);
//		DSLGenerationModel builderModel = regexCreator.getGenerationModel();
//		System.out.println(builderModel.printedModel());
//		System.out.println();
//		System.out.println(builderModel.printedOrder());
//		
//		Builder.buildDSL(regexCreator, Builder.MODEL_MIXED_IN_TEMPLATE, "de.htwg.generated.regex");
		
//		User user = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad").noAddress().buildUser();
//		User user2 = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad")
//						.address().street("asda").houseNumber(123).zipCode("adwad")
//							.country().name("fewfe").optionalUN_Member(true).isoCode(12345).buildUser();
		
//		User userTmp = 	createUser().firstName("Steven").lastName("Böckle").nickName("sboeckle")
//								.address(createAddress().street("Zasius").houseNumber(11).zipCode("78462")
//										.country(createCountry().name("Deutschland").optionalUN_Member(true).isoCode(12345)
//										.buildCountry())
//								.buildAddress())
//						.buildUser();
		
//		System.out.println(userTmp.firstName + userTmp.address.houseNumber + userTmp.address.country.name);
		
//		ForumTMP forum = ForumTMP.ForumTMPBuilder
//						.createForumTMP().name("facebook").url(new URL("http://facebook.com")).users(
//								UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle")
//									.post(
//											PostBuilder.createPost().title("introduction").text(12345).views(1)
//												.nested(NestedBuilder.createNested().name("THE NESTED STRING").referencedCreator().buildNested())
//											.buildPost()
//									).buildUser()
//								).buildForumTMP();
//		
//		System.out.println(forum.users.nickName + forum.users.post.title +forum.users.post.nested.name + " from Creator:" + forum.users.post.nested.creator.nickName);
		
		Forum forum = Forum.ForumBuilder
		.createForum().name("facebook").url(new URL("http://facebook.com")).users(
				UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle")
					.post(
							PostBuilder.createPost().title("introduction").text(12345).views(1).creator().buildPost()
					).buildUser()
				).buildForum();
		
		System.out.println(forum.users.nickName + forum.users.post.title +forum.users.post.text+ " from Creator:" + forum.users.post.creator.nickName);
	}

}
