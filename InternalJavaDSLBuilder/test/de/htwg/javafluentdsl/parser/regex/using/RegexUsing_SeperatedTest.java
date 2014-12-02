package de.htwg.javafluentdsl.parser.regex.using;

import static de.htwg.generated.regex.separated.complexforum.ComplexForumBuilder.ComplexPostBuilder.createComplexPost;
import static de.htwg.generated.regex.separated.complexforum.ComplexForumBuilder.ComplexUserBuilder.createComplexUser;
import static de.htwg.generated.regex.separated.complexforum.ComplexForumBuilder.RatingBuilder.createRating;
import static de.htwg.generated.regex.separated.complexforum.ComplexForumBuilder.SectionBuilder.createSection;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import de.htwg.generated.regex.separated.complexforum.ComplexForum;
import de.htwg.generated.regex.separated.complexforum.ComplexForumBuilder;
import de.htwg.generated.regex.separated.simpleforum.SimpleForum;
import de.htwg.generated.regex.separated.simpleforum.SimpleForumBuilder;
import de.htwg.generated.regex.separated.simpleforum.SimpleForumBuilder.SimplePostBuilder;
import de.htwg.generated.regex.separated.simpleforum.SimpleForumBuilder.SimpleUserBuilder;
import de.htwg.generated.regex.separated.user.User;
import de.htwg.generated.regex.separated.user.UserBuilder;
import de.htwg.generated.regex.separated.user.UserBuilder.AddressBuilder;
import de.htwg.generated.regex.separated.user.UserBuilder.CountryBuilder;
import de.htwg.generated.regex.separated.useropt.UserOPT;
import de.htwg.generated.regex.separated.useropt.UserOPTBuilder;
import de.htwg.generated.regex.separated.useropt.UserOPTBuilder.AddressOPTBuilder;
import de.htwg.generated.regex.separated.useropt.UserOPTBuilder.CountryOPTBuilder;
import de.htwg.javafluentdsl.parser.regex.creation.RegexCreation_InternTest;

/**
 * Test For Using the Regex DSL.
 * If imports are not correct pls make sure {@link RegexCreation_InternTest} was run
 *
 */
public class RegexUsing_SeperatedTest {
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
	String urlString = "http://MyForum.com";
	

	@Test
	public void testUserDSL() {
		User user = UserBuilder.createUser().firstName(firstName).optAge(age).lastName(lastName).nickName(nickName)
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
		UserOPT user = UserOPTBuilder.createUserOPT().firstName(firstName).optAge(age).lastName(lastName).nickName(nickName)
				.address(
					AddressOPTBuilder.createAddressOPT().optStreet(street).optHouseNumber(houseNumber).optZipCode(zipCode)
						.country(
								CountryOPTBuilder.createCountryOPT().optName(countryName).optUnMember(unMemer).buildCountryOPT()
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
		SimpleForum simpleForum = SimpleForumBuilder
		.createSimpleForum().name(forumName).url(new URL(urlString))
				.addUser(
					SimpleUserBuilder.createSimpleUser().optAge(age).optFirstName(firstName).lastName(lastName).nickName(nickName).post(
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
	// ForumBuilder needed because User DSLs createUser is used otherwise
			ComplexForum.ComplexUser replierWithoutPosts = createComplexUser().email(email).nickName("nick").rating(
					createRating().optUpps(1).optDowns(0).buildRating()
					).noPosts().buildComplexUser();
			ComplexForum.ComplexUser userWithPost = createComplexUser().email(otherEMail).nickName(nickName).rating(
					createRating().optDowns(5).buildRating()
					).addPosts(createComplexPost().title("Introduction").text("Hello my Nickname is "+nickName).addRepliers(replierWithoutPosts).noRepliers().rating(
							createRating().buildRating()
							)
					 .buildComplexPost()
					).noPosts()
					.buildComplexUser();
			
			ComplexForum forum = ComplexForumBuilder
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
	
	@Test
	public void testUserOPInSameClass() {
		de.htwg.generated.regex.separated.user.oppinclass.User testingUser = 
				de.htwg.generated.regex.separated.user.oppinclass.UserBuilder
				.createUser().firstName("first").lastName("first").nickName("first").otherUser(
						de.htwg.generated.regex.separated.user.oppinclass.UserBuilder
						.createUser().firstName("second").lastName("second").nickName("second").noOtherUser().buildUser()
				).buildUser();
		//first user is also the opUser of the second user
		assertTrue(testingUser.getFirstName().equals(testingUser.getOtherUser().getOppUser().getFirstName()));
	}
	
}
