package de.htwg.javafluentdsl.parser.emf.using;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import de.htwg.generated.emf.dsl.optonly.multiBuilder.OptOnlyBuilder;
import de.htwg.generated.emf.dsl.optonly.multiBuilder.RefBuilder;
import de.htwg.generated.emf.dsl.simpleForum.multiBuilder.PostBuilder;
import de.htwg.generated.emf.dsl.simpleForum.multiBuilder.SimpleForumBuilder;
import de.htwg.generated.emf.dsl.simpleForum.multiBuilder.UserBuilder;
import de.htwg.generated.emf.dsl.testmodel.multiBuilder.AClassBuilder;
import de.htwg.generated.emf.dsl.testmodel.multiBuilder.BClassBuilder;
import de.htwg.generated.emf.dsl.testmodel.multiBuilder.TestModelBuilder;
import de.htwg.generated.emf.model.OptOnly.OptOnly;
import de.htwg.generated.emf.model.SimpleForum.Post;
/*
 * Imports for EMF Model and DSL Builders
 */
import de.htwg.generated.emf.model.SimpleForum.SimpleForum;
import de.htwg.generated.emf.model.SimpleForum.User;
import de.htwg.generated.emf.model.TestModel.TestModel;
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
		//No static import here to show that multiple Builders are used
		User user1 = UserBuilder.createUser().optionalFirstName(firstName).optionalLastName(lastName).nickName(nickName)
					.optionalAge(age).email(email)
						.addPosts(
							PostBuilder.createPost().optionalText(postText1).optionalViews(5).title(postTitle1)
							.buildPost()
						)
						.noPosts()
					.buildUser();
		Post post1 = user1.getPosts().get(0);
		Post post2 = PostBuilder.createPost().optionalText(postText2).optionalViews(10).title(postTitle2)
				 .buildPost();
		user1.getPosts().add(post2);
		
		User user2 = UserBuilder.createUser().optionalFirstName(firstName2).optionalLastName(lastName2).nickName(nickName2)
				.optionalAge(age2).email(email2)
					.noPosts()
				.buildUser();
		
		Post post3 = PostBuilder.createPost().optionalText(postText2).optionalViews(10).title(postTitle2)
				.buildPost();
		
		SimpleForum simpleForum = SimpleForumBuilder.createSimpleForum().name(forumName).url(new URL(urlString))
									.addUsers(user1).addUsers(user2)
									.noUsers()
							 .buildSimpleForum();
		
		assertTrue(EMFUsing.validateObject(user1));
		assertTrue(EMFUsing.validateObject(post1));
		assertTrue(EMFUsing.validateObject(post2));
		assertTrue(EMFUsing.validateObject(post3));
		assertTrue(EMFUsing.validateObject(simpleForum));
		//user2 has no posts, but at least one post is required
		assertTrue(!EMFUsing.validateObject(user2));
		user2.getPosts().add(post3);
		assertTrue(EMFUsing.validateObject(user2));
		
		// check for list sizes
		assertTrue(user1.getPosts().size() == 2);
		assertTrue(user2.getPosts().size() == 1);
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
	
	@Test
	public void TestModelMultiBuilderTest(){
		TestModel testModel = TestModelBuilder.createTestModel()
			.a(
				AClassBuilder.createAClass().bClassRef(
						//Has only an opposite attr to the aClass which is set by buildAClass
						BClassBuilder.createBClass()
						.buildBClass()
				).buildAClass()
			).buildTestModel();
		
		assertTrue(EMFUsing.validateObject(testModel));
		assertTrue(EMFUsing.validateObject(testModel.getA()));
		assertTrue(EMFUsing.validateObject(testModel.getA().getBClassRef()));
	}
	
}
