package de.htwg.javaDSLBuilder.creator.regex;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.omg.CORBA.RepositoryIdHelper;

import de.htwg.generated.regex.Forum.*;
import de.htwg.generated.regex.Forum;
import de.htwg.generated.regex.SimpleForum;
import de.htwg.generated.regex.SimpleForum.SimplePostBuilder;
import de.htwg.generated.regex.SimpleForum.SimpleUserBuilder;
import de.htwg.generated.regex.User.AddressBuilder;
import de.htwg.generated.regex.User.CountryBuilder;
import de.htwg.generated.regex.User.UserBuilder;
import de.htwg.generated.regex.UserOPT.AddressOPTBuilder;
import de.htwg.generated.regex.UserOPT.CountryOPTBuilder;
import de.htwg.generated.regex.UserOPT.UserOPTBuilder;

/**
 * Test class for using created DSL from CreatorRegex.
 * 
 *
 */
public class CreatorRegexTest_Using {
	/*
	 * User Data
	 */
	String firstName = "Max";
	String lastName = "Mueller";
	int age = 25;
	String nickName = "MaMue";
	String street = "someStreet";
	int houseNumber = 12;
	String zipCode = "78462";
	String countryName = "Germany";
	boolean unMemer = true;
	
	/*
	 * Forum Data
	 */
	String forumName = "MyForum";
	String postTitle1 = "MyPost";
	String postText1 = "MyPostText";
	int views = 1;
	String postTitle2 = "MySecondPost";
	String postText2 = "MySecondPostText";
	String urlString = "http://MyForum.com";
	

	@Test
	public void testUserDSL() {
		de.htwg.generated.regex.User user = UserBuilder.createUser().firstName(firstName).optionalAge(age).lastName(lastName).nickName(nickName)
				.address(
					AddressBuilder.createAddress().street(street).houseNumber(houseNumber).zipCode(zipCode)
						.country(
								CountryBuilder.createCountry().name(countryName).uN_Member(unMemer).buildCountry()
						)
					.buildAddress()
				)
			.buildUser();
		
		assertTrue(user.getFirstName().equals(firstName) && user.getLastName().equals(lastName) && user.getAge() == age
				&& user.getNickName().equals(nickName) && user.getAddress().getZipCode().equals(zipCode)
				&& user.getAddress().getCountry().getName().equals(countryName));
	}
	
	@Test
	public void testUserOPTOnlyDSL() throws MalformedURLException {
		de.htwg.generated.regex.UserOPT user = UserOPTBuilder.createUserOPT().firstName(firstName).optionalAge(age).lastName(lastName).nickName(nickName)
				.address(
					AddressOPTBuilder.createAddressOPT().optionalStreet(street).optionalHouseNumber(houseNumber).optionalZipCode(zipCode)
						.country(
								CountryOPTBuilder.createCountryOPT().optionalName(countryName).optionalUnMember(unMemer).buildCountryOPT()
						)
					.buildAddressOPT()
				)
			.buildUserOPT();
		
		assertTrue(user.getFirstName().equals(firstName) && user.getLastName().equals(lastName) && user.getAge() == age
				&& user.getNickName().equals(nickName) && user.getAddress().getZipCode().equals(zipCode)
				&& user.getAddress().getCountry().getName().equals(countryName)
				&& user.getAddress().getUser().getFirstName().equals(firstName));
	}
	
	@Test
	public void testSimpleForumDSL() throws MalformedURLException {
		SimpleForum simpleForum = SimpleForum.SimpleForumBuilder
		.createSimpleForum().name(forumName).url(new URL(urlString))
				.addUser(
					SimpleUserBuilder.createSimpleUser().optionalAge(age).optionalFirstName(firstName).lastName(lastName).nickName(nickName).post(
								SimplePostBuilder.createSimplePost().title(postTitle1).text(postText1).views(views).buildSimplePost()
					).buildSimpleUser()
				)
				.noUser()
		.buildSimpleForum();
		assertTrue(simpleForum.getUser().get(0).getNickName().equals(nickName) && simpleForum.getUser().get(0).getPost().getTitle().equals(postTitle1)
				&& simpleForum.getUser().get(0).getPost().getCreator().getNickName().equals(nickName));
	}
	
	@Test
	public void testForumDSL() throws MalformedURLException {
		Forum.User replierWithoutPosts = Forum.UserBuilder.createUser().nickName("nick").rating(
				Forum.RatingBuilder.createRating().optionalUpps(1).optionalDowns(0).buildRating()
				).noPosts().buildUser();
		Forum.User userWithPost = Forum.UserBuilder.createUser().nickName(nickName).rating(
				Forum.RatingBuilder.createRating().optionalDowns(5).buildRating()
				).addPosts(Forum.PostBuilder.createPost().title("Introduction").text("Hello my Nickname is "+nickName).addReplier(replierWithoutPosts).noReplier().rating(
						Forum.RatingBuilder.createRating().buildRating()
						)
				 .buildPost()
				).noPosts()
				.buildUser();
		
		Forum forum = Forum.ForumBuilder
		.createForum().name(forumName).url(new URL(urlString))
			.addSections(Forum.SectionBuilder.createSection().name("mainSection").addModerators(userWithPost).noModerators().buildSection())
			.noSections()
			.addUser(userWithPost)
			.addUser(replierWithoutPosts)
				.noUser()
			.buildForum();
			
		assertTrue(forum.getUser().get(0).getNickName().equals(nickName));
		assertTrue(forum.getUser().get(0).getPosts().get(0).getText().equals("Hello my Nickname is "+nickName));
		assertTrue( forum.getUser().get(0).getPosts().get(0).getCreator().getNickName().equals(nickName));
		assertTrue( forum.getUser().get(0).getPosts().get(0).getReplier().get(0).getNickName().equals(replierWithoutPosts.getNickName()));
		assertTrue( forum.getSections().get(0).getName().equals("mainSection"));
		
		// because the rating was created by a User object the opposite Reference to Post is null
		assertTrue( forum.getUser().get(0).getRating().getForPost() == null);
		assertTrue( forum.getUser().get(0).getRating().getForUser().getNickName()
					.equals(forum.getUser().get(0).getNickName()));
		
		// Rating was created by PostObj
		assertTrue( forum.getUser().get(0).getPosts().get(0).getRating().getForUser() == null);
		assertTrue( forum.getUser().get(0).getPosts().get(0).getRating().getForPost().getText()
					.equals(forum.getUser().get(0).getPosts().get(0).getText()));
		//TODO more to come
	}
	
}
