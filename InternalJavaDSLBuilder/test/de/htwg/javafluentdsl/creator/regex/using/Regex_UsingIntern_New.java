package de.htwg.javafluentdsl.creator.regex.using;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import de.htwg.generated.regex.intern.forum.NEW.*;
import static de.htwg.generated.regex.intern.forum.NEW.User.UserBuilder.*;
import static de.htwg.generated.regex.intern.forum.NEW.Forum.ForumBuilder.*;
import static de.htwg.generated.regex.intern.forum.NEW.Rating.RatingBuilder.*;
import static de.htwg.generated.regex.intern.forum.NEW.Section.SectionBuilder.*;
import de.htwg.generated.regex.intern.simpleforum.NEW.SimpleForum;
import de.htwg.generated.regex.intern.simpleforum.NEW.SimplePost;
import de.htwg.generated.regex.intern.simpleforum.NEW.SimpleUser;
import de.htwg.generated.regex.intern.user.NEW.User;
import de.htwg.generated.regex.intern.user.NEW.Address;
import de.htwg.generated.regex.intern.user.NEW.Country;
import de.htwg.generated.regex.intern.user.NEW.User.UserBuilder;
import de.htwg.generated.regex.intern.useropt.NEW.UserOPT;
import de.htwg.generated.regex.intern.useropt.NEW.AddressOPT;
import de.htwg.generated.regex.intern.useropt.NEW.CountryOPT;
import de.htwg.generated.regex.intern.useropt.NEW.UserOPT.UserOPTBuilder;
import de.htwg.javafluentdsl.creator.regex.creation.Regex_CreationIntern;

/**
 * Test For Using the Regex DSL.
 * If imports are not correct pls make sure {@link Regex_CreationIntern} was run
 *
 */
public class Regex_UsingIntern_New {
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
	String email = "MaMue@asd.com";
	String otherEMail = "ROBA@asd.com";
	
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
		User user = UserBuilder.createUser().firstName(firstName).optionalAge(age).lastName(lastName).nickName(nickName)
				.address(
					Address.AddressBuilder.createAddress().street(street).houseNumber(houseNumber).zipCode(zipCode)
						.country(
								Country.CountryBuilder.createCountry().name(countryName).uN_Member(unMemer).buildCountry()
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
		UserOPT user = UserOPTBuilder.createUserOPT().firstName(firstName).optionalAge(age).lastName(lastName).nickName(nickName)
				.address(
						AddressOPT.AddressOPTBuilder.createAddressOPT().optionalStreet(street).optionalHouseNumber(houseNumber).optionalZipCode(zipCode)
						.country(
								CountryOPT.CountryOPTBuilder.createCountryOPT().optionalName(countryName).optionalUnMember(unMemer).buildCountryOPT()
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
						SimpleUser.SimpleUserBuilder.createSimpleUser().optionalAge(age).optionalFirstName(firstName).lastName(lastName).nickName(nickName).post(
								SimplePost.SimplePostBuilder.createSimplePost().title(postTitle1).text(postText1).views(views).buildSimplePost()
					).buildSimpleUser()
				)
				.noUser()
		.buildSimpleForum();
		assertTrue(simpleForum.getUser().get(0).getNickName().equals(nickName) && simpleForum.getUser().get(0).getPost().getTitle().equals(postTitle1)
				&& simpleForum.getUser().get(0).getPost().getCreator().getNickName().equals(nickName));
	}
	
	@Test
	public void testForumDSL() throws MalformedURLException {
		de.htwg.generated.regex.intern.forum.NEW.User replierWithoutPosts = createUser().email(email).nickName("nick").rating(
					createRating().optionalUpps(1).optionalDowns(0).buildRating()
				).noPosts().buildUser();
		de.htwg.generated.regex.intern.forum.NEW.User userWithPost = createUser().email(otherEMail).nickName(nickName).rating(
					createRating().optionalDowns(5).buildRating()
				).addPosts(Post.PostBuilder.createPost().title("Introduction").text("Hello my Nickname is "+nickName).addRepliers(replierWithoutPosts).noRepliers().rating(
						createRating().buildRating()
						)
				 .buildPost()
				).noPosts()
				.buildUser();
		
		Forum forum = 
			createForum().name(forumName).url(new URL(urlString))
			.addSections(createSection().name("mainSection").addModerators(userWithPost).noModerators().buildSection())
			.noSections()
			.addUser(userWithPost)
			.addUser(replierWithoutPosts)
				.noUser()
			.buildForum();
			
		assertTrue(forum.getUser().get(0).getNickName().equals(nickName));
		assertTrue(forum.getUser().get(0).getPosts().get(0).getText().equals("Hello my Nickname is "+nickName));
		assertTrue( forum.getUser().get(0).getPosts().get(0).getCreator().getNickName().equals(nickName));
		assertTrue( forum.getUser().get(0).getPosts().get(0).getRepliers().get(0).getNickName().equals(replierWithoutPosts.getNickName()));
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
