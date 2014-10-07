package de.htwg.javaDSLBuilder.test;

import java.net.MalformedURLException;
import java.net.URL;

import de.htwg.generated.regex.Forum;
import de.htwg.generated.regex.Forum.PostBuilder;
import de.htwg.generated.regex.Forum.UserBuilder;

public class GeneratedDSLsTest {
	
	
	public static void main(String[] args) throws MalformedURLException {
		//	User user = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad").noAddress().buildUser();
		//	User user2 = User.UserBuilder.createUser().firstName("asd").lastName("asd").nickName("ad")
		//					.address().street("asda").houseNumber(123).zipCode("adwad")
		//						.country().name("fewfe").optionalUN_Member(true).isoCode(12345).buildUser();
			
		//	User userTmp = 	createUser().firstName("Steven").lastName("Böckle").nickName("sboeckle")
		//							.address(createAddress().street("Zasius").houseNumber(11).zipCode("78462")
		//									.country(createCountry().name("Deutschland").optionalUN_Member(true).isoCode(12345)
		//									.buildCountry())
		//							.buildAddress())
		//					.buildUser();
			
		//	System.out.println(userTmp.firstName + userTmp.address.houseNumber + userTmp.address.country.name);
			
		//	ForumTMP forum = ForumTMP.ForumTMPBuilder
		//					.createForumTMP().name("facebook").url(new URL("http://facebook.com")).users(
		//							UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle")
		//								.post(
		//										PostBuilder.createPost().title("introduction").text(12345).views(1)
		//											.nested(NestedBuilder.createNested().name("THE NESTED STRING").referencedCreator().buildNested())
		//										.buildPost()
		//								).buildUser()
		//							).buildForumTMP();
		//	
		//	System.out.println(forum.users.nickName + forum.users.post.title +forum.users.post.nested.name + " from Creator:" + forum.users.post.nested.creator.nickName);
			
//			Forum forum = Forum.ForumBuilder
//			.createForum().name("facebook").url(new URL("http://facebook.com"))
//					.user(
//						UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle").post(
//									PostBuilder.createPost().title("introduction").text("Some Text").views(1).creator().forum().buildPost()
//						).buildUser()
//					)
//					.post(PostBuilder.createPost().title("getStarted").text("Other Text").views(0).creator().forum().buildPost())
//			.buildForum();
//			
//			System.out.println(forum.user.nickName + forum.user.post.title +forum.user.post.text+ " from Creator:" + forum.user.post.creator.nickName
//					+ forum.post.forum.name);
//			
//			Forum forum2 = Forum.ForumBuilder
//					.createForum().name("facebook").url(new URL("http://facebook.com"))
//							.addUser(
//								UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle").noPost().buildUser()
//							)
//							.post(PostBuilder.createPost().title("getStarted").text("Other Text").views(0).creator().forum().buildPost())
//					.buildForum();
//			System.out.println(forum.user.getPost());
	}

}
