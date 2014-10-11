package de.htwg.javaDSLBuilder.test;

import java.net.MalformedURLException;

import de.htwg.generated.regex.User;
import de.htwg.generated.regex.User.AddressBuilder;
import de.htwg.generated.regex.User.UserBuilder;


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
//					.addUser(
//						UserBuilder.createUser().firstName("steven").lastName("boeckle").nickName("sboeckle").post(
//									PostBuilder.createPost().title("introduction").text("Some Text").views(1).creator().forum().buildPost()
//						).buildUser()
//					)
//					.noUser()
//					.post(PostBuilder.createPost().title("getStarted").text("Other Text").views(0).creator().forum().buildPost())
//			.buildForum();
//			System.out.println(forum.getUser().get(0).getNickName() +"Post 1:" +forum.getUser().get(0).getPost().getTitle() 
//					+ " from Creator:" + forum.getUser().get(0).getPost().getCreator().getNickName());
//			System.out.println("ForumPost:"+forum.getPost().getTitle()+ " from Forum: "+forum.getPost().getForum().getName());
//			System.out.println("forumPost has UserCreator? " + forum.getPost().getCreator());
		
		User user = UserBuilder.createUser().firstName("Steven").lastName("Boeckle").nickName("asd").
						address(
							AddressBuilder.createAddress().optionalHouseNumber(23).optionalStreet("saystreet").buildAddress()
						)
					.buildUser();
		
		System.out.println(user.getLastName() + user.getNickName() + user.getAddress().getHouseNumber());
		
	}

}
