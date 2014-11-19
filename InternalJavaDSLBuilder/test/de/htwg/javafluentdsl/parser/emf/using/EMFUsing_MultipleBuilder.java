package de.htwg.javafluentdsl.parser.emf.using;

import static de.htwg.generated.emf.dsl.simpleForum.multiBuilder.PostBuilder.createPost;
import static de.htwg.generated.emf.dsl.simpleForum.multiBuilder.SimpleForumBuilder.createSimpleForum;
import static de.htwg.generated.emf.dsl.simpleForum.multiBuilder.UserBuilder.createUser;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import de.htwg.generated.emf.dsl.optonly.multiBuilder.OptOnlyBuilder;
import de.htwg.generated.emf.dsl.optonly.multiBuilder.RefBuilder;
import de.htwg.generated.emf.model.OptOnly.OptOnly;
import de.htwg.generated.emf.model.SimpleForum.Post;
/*
 * Imports for EMF Model and DSL Builders
 */
import de.htwg.generated.emf.model.SimpleForum.SimpleForum;
import de.htwg.generated.emf.model.SimpleForum.User;
import de.htwg.javafluentdsl.parser.emf.creation.EMFCreation_SingleBuilder;

/**
 * Test For Using the EMF DSL.
 * If imports are not correct pls make sure emf genmodel has 
 * created EMF models and {@link EMFCreation_SingleBuilder} was run
 *
 */
public class EMFUsing_MultipleBuilder {
	
	/*
	 * User Data
	 * user 1
	 */
	String firstName = "Max";
	String lastName = "Mueller";
	int age = 25;
	String nickName = "MaMue";
	String street = "someStreet";
	int houseNumber = 12;
	String zipCode = "78462";
	String email ="MaMue@smth.com";
	
	/*
	* user 2
	 */
	String firstName2 = "Moritz";
	String lastName2 = "Bauer";
	int age2 = 31;
	String nickName2 = "MoBau";
	String street2 = "someOtherStreet";
	int houseNumber2 = 14;
	String zipCode2 = "78467";
	String email2 ="MoBau@smth.com";
	
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
	public void SimpleForumMultiBuilderTest() throws MalformedURLException {
		
		User user1 = createUser().optionalFirstName(firstName).optionalLastName(lastName).nickName(nickName)
					.optionalAge(age).email(email)
						.noPosts()
						.noForum()
					.buildUser();
		
		User user2 = createUser().optionalFirstName(firstName2).optionalLastName(lastName2).nickName(nickName2)
				.optionalAge(age2).email(email2)
					.noPosts()
					.noForum()
				.buildUser();
		
		Post post1 = createPost().optionalText(postText1).optionalViews(5).title(postTitle1)
							.creator(user1)
					 .buildPost();
		
		
		Post post2 = createPost().optionalText(postText2).optionalViews(10).title(postTitle2)
				.creator(user1)
		 .buildPost();
		
		SimpleForum simpleForum = createSimpleForum().name(forumName).url(new URL(urlString))
									.addUsers(user1).addUsers(user2)
									.noUsers()
							 .buildSimpleForum();
		
		// check for list sizes
		assertTrue(user1.getPosts().size() == 2);
		assertTrue(user2.getPosts().size() == 0);
		assertTrue(simpleForum.getUsers().size() == 2);
		// check for same opposite objects, regardless from which side they were set
		assertTrue(EcoreUtil.equals(user1.getPosts().get(0) , post1));
		assertTrue(EcoreUtil.equals(user1.getPosts().get(1) , post2));
		assertTrue(EcoreUtil.equals(post2.getCreator() , post1.getCreator()));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(0) , user1));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(1) , user2));
		assertTrue(EcoreUtil.equals(simpleForum , user2.getForum()));
		assertTrue(EcoreUtil.equals(simpleForum , user2.getForum()));
		assertTrue(EcoreUtil.equals(user1.getForum() , simpleForum));
	}
	
	@Test
	public void OptOnlyMultiBuilderTest(){
		OptOnly opt = OptOnlyBuilder.createOptOnly().optionalName("ad").optionalSomeOtherAttr(12)
					.ref(RefBuilder.createRef().optionalOtherName("otherName").optionalSomeNumber(true).buildRef())
				.buildOptOnly();
		
		assertTrue(opt.getName().equals("ad"));
		assertTrue(opt.getSomeOtherAttr() == 12);
		assertTrue(opt.getRef() != null);
		assertTrue(opt.getRef().getOtherName().equals("otherName"));
		assertTrue(opt.getRef().isSomeNumber() ==true);
		
	}
	
}
