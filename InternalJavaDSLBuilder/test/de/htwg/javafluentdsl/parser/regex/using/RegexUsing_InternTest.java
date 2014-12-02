package de.htwg.javafluentdsl.parser.regex.using;

import static de.htwg.generated.regex.intern.forum.Forum.ForumBuilder.*;
import static de.htwg.generated.regex.intern.complexforum.ComplexForum.ComplexForumBuilder.createComplexPost;
import static de.htwg.generated.regex.intern.complexforum.ComplexForum.ComplexForumBuilder.createRating;
import static de.htwg.generated.regex.intern.complexforum.ComplexForum.ComplexForumBuilder.createSection;
import static de.htwg.generated.regex.intern.simpleforum.SimpleForum.SimpleForumBuilder.createSimplePost;
import static de.htwg.generated.regex.intern.simpleforum.SimpleForum.SimpleForumBuilder.createSimpleUser;
import static de.htwg.generated.regex.intern.user.User.UserBuilder.createAddress;
import static de.htwg.generated.regex.intern.user.User.UserBuilder.createCountry;
import static de.htwg.generated.regex.intern.useropt.UserOPT.UserOPTBuilder.createAddressOPT;
import static de.htwg.generated.regex.intern.useropt.UserOPT.UserOPTBuilder.createCountryOPT;
import static de.htwg.generated.regex.intern.useropt.UserOPT.UserOPTBuilder.createUserOPT;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import de.htwg.generated.regex.intern.complexforum.ComplexForum;
import de.htwg.generated.regex.intern.complexforum.ComplexForum.ComplexForumBuilder;
import de.htwg.generated.regex.intern.forum.Forum;
import de.htwg.generated.regex.intern.simpleforum.SimpleForum;
import de.htwg.generated.regex.intern.user.User;
import de.htwg.generated.regex.intern.user.User.UserBuilder;
import de.htwg.generated.regex.intern.useropt.UserOPT;
import de.htwg.javafluentdsl.parser.regex.creation.RegexCreation_InternTest;

/**
 * Test For Using the Regex DSL.
 * If imports are not correct pls make sure {@link RegexCreation_InternTest} was run
 *
 */
public class RegexUsing_InternTest {
	/*
	 * User1 Data
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
	String postTitle2 = "MyPost2";
	String postText2 = "MyPostText2";
	int views = 1;
	String urlString = "http://MyForum.com";
	
	@Test
	public void testMASimpleForumDSL() throws MalformedURLException {
		de.htwg.generated.regex.intern.masimpleforum.oneuser.Forum forum =
		de.htwg.generated.regex.intern.masimpleforum.oneuser.Forum.ForumBuilder
		.createForum().name(forumName).url(new URL(urlString)).user(
				de.htwg.generated.regex.intern.masimpleforum.oneuser.Forum.ForumBuilder
				.createUser().firstName(firstName).lastName(lastName).buildUser())
		.buildForum();

		assertTrue(forum.getName().equals(forumName)
				&& forum.getUser().getFirstName().equals(firstName)
				&& forum.getUser().getAge() == 0);
		
	}
	
	@Test
	public void testForumDSL() throws MalformedURLException {
		Forum forum =
		Forum.ForumBuilder
		.createForum().name(forumName).url(new URL(urlString)).addUser(
				Forum.ForumBuilder
				.createUser().optFirstName(firstName).optLastName(lastName).email(email)
					.addPost(
							createPost().title(postTitle1)
								.text(postText1).addReplier(
										createUser().email(otherEMail).noPost().buildUser()
							).noReplier().buildPost())
					.addPost(createPost().title(postTitle2)
								.text(postText2).noReplier().buildPost())
					.noPost()
				.buildUser())
				.noUser()
		.buildForum();
		//list sizes
		assertTrue(forum.getUser().size() == 1);
		assertTrue(forum.getUser().get(0).getPost().size() == 2);
		assertTrue(forum.getUser().get(0).getPost().get(0).getReplier().size() == 1);
		assertTrue(forum.getUser().get(0).getPost().get(1).getReplier().size() == 0);
		//values
		assertTrue(forum.getUser().get(0).getFirstName().equals(firstName));
		assertTrue(forum.getUser().get(0).getLastName().equals(lastName));
		assertTrue(forum.getUser().get(0).getPost().get(0).getText().equals(postText1));
		assertTrue(forum.getUser().get(0).getPost().get(0).getTitle().equals(postTitle1));
		assertTrue(forum.getUser().get(0).getPost().get(1).getTitle().equals(postTitle2));
		assertTrue(forum.getUser().get(0).getPost().get(1).getText().equals(postText2));
		assertTrue(forum.getUser().get(0).getPost().get(0).getReplier().get(0).getEmail().equals(otherEMail));
		//opposite attribute
		assertTrue(forum.getUser().get(0).getPost().get(0).getCreator().getFirstName().equals(firstName));
	}
	

	@Test
	public void testUserDSL() {
		User user = UserBuilder.createUser().firstName(firstName).optAge(age).lastName(lastName).nickName(nickName)
				.address(
					createAddress().street(street).houseNumber(houseNumber).zipCode(zipCode)
						.country(
								createCountry().name(countryName).uN_Member(unMemer).buildCountry()
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
		UserOPT user = createUserOPT().firstName(firstName).optAge(age).lastName(lastName).nickName(nickName)
				.address(
					createAddressOPT().optStreet(street).optHouseNumber(houseNumber).optZipCode(zipCode)
						.country(
								createCountryOPT().optName(countryName).optUnMember(unMemer).buildCountryOPT()
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
					createSimpleUser().optAge(age).optFirstName(firstName).lastName(lastName).nickName(nickName).post(
								createSimplePost().title(postTitle1).text(postText1).views(views).buildSimplePost()
					).buildSimpleUser()
				)
				.noUser()
		.buildSimpleForum();
		assertTrue(simpleForum.getUser().get(0).getNickName().equals(nickName) && simpleForum.getUser().get(0).getPost().getTitle().equals(postTitle1)
				&& simpleForum.getUser()
				.get(0)
				.getPost()
				.getCreator()
				.getNickName()
				.equals(nickName));
	}
	
	@Test
	public void testComplexForumDSL() throws MalformedURLException {
		// ForumBuilder needed because User DSLs createUser is used otherwise
		ComplexForum.ComplexUser replierWithoutPosts = ComplexForumBuilder.createComplexUser().email(email).nickName("nick").rating(
				createRating().optUpps(1).optDowns(0).buildRating()
				).noPosts().buildComplexUser();
		ComplexForum.ComplexUser userWithPost = ComplexForumBuilder.createComplexUser().email(otherEMail).nickName(nickName).rating(
				createRating().optDowns(5).buildRating()
				).addPosts(createComplexPost().title("Introduction").text("Hello my Nickname is "+nickName).addRepliers(replierWithoutPosts).noRepliers().rating(
						createRating().buildRating()
						)
				 .buildComplexPost()
				).noPosts()
				.buildComplexUser();
		
		ComplexForum forum = ComplexForum.ComplexForumBuilder
		.createComplexForum().name(forumName).url(new URL(urlString))
			.addSections(createSection().name("mainSection").addModerators(userWithPost).noModerators().buildSection())
			.noSections()
			.addUser(userWithPost)
			.addUser(replierWithoutPosts)
				.noUser()
			.buildComplexForum();
			
		assertTrue(forum.getUser().get(0).getNickName().equals(nickName));
		assertTrue(forum.getUser().get(0).getPosts().get(0).getText().equals("Hello my Nickname is "+nickName));
		assertTrue(forum.getUser().get(0).getPosts().get(0).getCreator().getNickName().equals(nickName));
		assertTrue(forum.getUser().get(0).getPosts().get(0).getRepliers().get(0).getNickName().equals(replierWithoutPosts.getNickName()));
		assertTrue(forum.getSections().get(0).getName().equals("mainSection"));
		
		// because the rating was created by a User object the opposite Reference to Post is null
		assertTrue( forum.getUser().get(0).getRating().getForPost() == null);
		assertTrue( forum.getUser().get(0).getRating().getForUser().getNickName()
					.equals(forum.getUser().get(0).getNickName()));
		
		// Rating was created by PostObj
		assertTrue( forum.getUser().get(0).getPosts().get(0).getRating().getForUser() == null);
		assertTrue( forum.getUser().get(0).getPosts().get(0).getRating().getForPost().getText()
					.equals(forum.getUser().get(0).getPosts().get(0).getText()));
	}
	
}
