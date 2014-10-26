package de.htwg.javaDSLBuilder.creator.emf.using;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;




/*
 * Imports for EMF Model and DSL Builders
 */
import de.htwg.generated.emf.model.SimpleForum.SimpleForum;
import de.htwg.generated.emf.model.SimpleForum.Post;
import de.htwg.generated.emf.model.SimpleForum.User;
import de.htwg.javaDSLBuilder.creator.emf.creation.EMFCreation_SingleBuilder;
import static de.htwg.generated.emf.dsl.simpleForum.singleBuilder.SimpleForumBuilder.*;

/**
 * Test For Using the EMF DSL.
 * If imports are not correct pls make sure emf genmodel has 
 * created EMF models and {@link EMFCreation_SingleBuilder} was run
 *
 */
public class EMFUsing_SingleBuilder {
	
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
		
		
		SimpleForum simpleForum = createSimpleForum().name(forumName).url(new URL(urlString))
				.addUsers(
						createUser().optionalFirstName(firstName).optionalLastName(lastName).nickName(nickName)
						.optionalAge(age).email(email)
							.addPosts(createPost().optionalText(postText1).optionalViews(5).title(postTitle1)
										.noCreator()
										.buildPost()
									 )
							 .addPosts(createPost().optionalText(postText2).optionalViews(10).title(postTitle2)
									 	 .noCreator()
										 .buildPost())
							 .noPosts()
							 .noForum()
						.buildUser()
				).addUsers(
					createUser().optionalFirstName(firstName2).optionalLastName(lastName2).nickName(nickName2)
					.optionalAge(age2).email(email2)
						.noPosts()
						.noForum()
					.buildUser()
						)
				.noUsers()
		.buildSimpleForum();
		
		User user1 = simpleForum.getUsers().get(0); 
		User user2 = simpleForum.getUsers().get(1); 
		
		// check for list sizes
		assertTrue(user1.getPosts().size() == 2);
		assertTrue(user2.getPosts().size() == 0);
		assertTrue(simpleForum.getUsers().size() == 2);
		// check for same opposite objects, regardless from which side they were set
		assertTrue(EcoreUtil.equals(user1 , user1.getPosts().get(0).getCreator()));
		assertTrue(EcoreUtil.equals(user1.getPosts().get(1).getCreator() , user1));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(0) , user1));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(1) , user2));
		assertTrue(EcoreUtil.equals(simpleForum , user1.getForum()));
		assertTrue(EcoreUtil.equals(simpleForum , user2.getForum()));
		assertTrue(EcoreUtil.equals(user1.getForum() , simpleForum));
	}
	
}
