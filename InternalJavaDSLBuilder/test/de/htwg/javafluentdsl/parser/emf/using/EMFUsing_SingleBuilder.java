package de.htwg.javafluentdsl.parser.emf.using;

import static de.htwg.generated.emf.dsl.exceptionCase.singleBuilder.ExceptionCaseBuilder.*;
import static de.htwg.generated.emf.dsl.exceptionCaseList.singleBuilder.ExceptionCaseListBuilder.*;
import static de.htwg.generated.emf.dsl.simpleForum.singleBuilder.SimpleForumBuilder.createPost;
import static de.htwg.generated.emf.dsl.simpleForum.singleBuilder.SimpleForumBuilder.createSimpleForum;
import static de.htwg.generated.emf.dsl.simpleForum.singleBuilder.SimpleForumBuilder.createUser;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/*
 * Imports for EMF Model and DSL Builders
 */
import de.htwg.generated.emf.dsl.optonly.singleBuilder.OptOnlyBuilder;
import de.htwg.generated.emf.model.ExceptionCase.ExceptionCase;
import de.htwg.generated.emf.model.ExceptionCaseList.ExceptionCaseList;
import de.htwg.generated.emf.model.OptOnly.OptOnly;
import de.htwg.generated.emf.model.SimpleForum.SimpleForum;
import de.htwg.generated.emf.model.SimpleForum.User;
import de.htwg.javafluentdsl.parser.emf.creation.EMFCreation_SingleBuilder;

/**
 * Test For Using the EMF DSL. If imports are not correct pls make sure emf
 * genmodel has created EMF models and {@link EMFCreation_SingleBuilder} was run
 *
 */
public class EMFUsing_SingleBuilder {

	/*
	 * User Data user 1
	 */
	String firstName = "Max";
	String lastName = "Mueller";
	int age = 25;
	String nickName = "MaMue";
	String email = "MaMue@smth.com";

	/*
	 * user 2
	 */
	String firstName2 = "Moritz";
	String lastName2 = "Bauer";
	int age2 = 31;
	String nickName2 = "MoBau";
	String email2 = "MoBau@smth.com";

	/*
	 * Forum Data
	 */
	String forumName = "MyForum";
	String postTitle1 = "MyPost";
	String postText1 = "MyPostText";
	String postTitle2 = "MySecondPost";
	String postText2 = "MySecondPostText";
	String urlString = "http://MyForum.com";

	@Test
	public void simpleForumSingleBuilderTest() throws MalformedURLException {
		SimpleForum simpleForum = createSimpleForum()
				.name(forumName)
				.url(new URL(urlString))
				.addUsers(
						createUser()
								.optionalFirstName(firstName)
								.optionalLastName(lastName)
								.nickName(nickName)
								.optionalAge(age)
								.email(email)
								.addPosts(
										createPost().optionalText(postText1)
												.optionalViews(5)
												.title(postTitle1).buildPost())
								.addPosts(
										createPost().optionalText(postText2)
												.optionalViews(10)
												.title(postTitle2).buildPost())
								.noPosts().buildUser())
				.addUsers(
						createUser()
								.optionalFirstName(firstName2)
								.optionalLastName(lastName2)
								.nickName(nickName2)
								.optionalAge(age2)
								.email(email2)
								.addPosts(
										createPost().optionalText(postText1)
												.optionalViews(11)
												.title(postTitle2).buildPost())
								.noPosts().buildUser())
				.noUsers()
				.buildSimpleForum();

		User user1 = simpleForum.getUsers().get(0);
		User user2 = simpleForum.getUsers().get(1);

		// check for list sizes
		assertTrue(user1.getPosts().size() == 2);
		assertTrue(user2.getPosts().size() == 1);
		assertTrue(simpleForum.getUsers().size() == 2);
		// check for same opposite objects, regardless from which side they were
		// set
		assertTrue(EcoreUtil
				.equals(user1, user1.getPosts().get(0).getCreator()));
		assertTrue(EcoreUtil
				.equals(user1.getPosts().get(1).getCreator(), user1));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(0), user1));
		assertTrue(EcoreUtil.equals(simpleForum.getUsers().get(1), user2));
		assertTrue(EcoreUtil.equals(simpleForum, user1.getForum()));
		assertTrue(EcoreUtil.equals(simpleForum, user2.getForum()));
		assertTrue(EcoreUtil.equals(user1.getForum(), simpleForum));
		// Calling EMFs validateObject for validating
		assertTrue(EMFUsing.validateObject(simpleForum));
		simpleForum.getUsers().forEach(
				u -> assertTrue(EMFUsing.validateObject(u)));
		simpleForum.getUsers().forEach(
				u -> u.getPosts().forEach(
						p -> assertTrue(EMFUsing.validateObject(p))));

	}

	@Test
	public void optOnly_SingleBuilderTest() {
		OptOnly opt = OptOnlyBuilder
				.createOptOnly()
				.optionalName("ad")
				.optionalSomeOtherAttr(12)
				.ref(OptOnlyBuilder.createRef().optionalOtherName("otherName")
						.optionalSomeNumber(true).buildRef()).buildOptOnly();

		assertTrue(EMFUsing.validateObject(opt));
		assertTrue(EMFUsing.validateObject(opt.getRef()));
		assertTrue(opt.getName().equals("ad"));
		assertTrue(opt.getSomeOtherAttr() == 12);
		assertTrue(opt.getRef() != null);
		assertTrue(opt.getRef().getOtherName().equals("otherName"));
		assertTrue(opt.getRef().isSomeNumber() == true);

	}

	@Test
	public void ExceptionCase_SingleBuilderTest() {
		ExceptionCase exCase = createExceptionCase()
				.a(createOppositeWithOPT().optionalStringValue("OptionalA")
						.buildOppositeWithOPT())
				.b(createOppositeWithMand().stringValue("MandatoryString")
						.buildOppositeWithMand())
				.c(createOppositeOnly().buildOppositeOnly())
				.buildExceptionCase();

		assertTrue(EMFUsing.validateObject(exCase));
		assertTrue(EMFUsing.validateObject(exCase));
		assertTrue(EMFUsing.validateObject(exCase.getA()));
		assertTrue(EMFUsing.validateObject(exCase.getB()));
		assertTrue(EMFUsing.validateObject(exCase.getC()));
		//String values has been set
		assertTrue("OptionalA".equals(exCase.getA().getStringValue()));
		assertTrue("MandatoryString".equals(exCase.getB().getStringValue()));
		//EMF Equals
		assertTrue(EcoreUtil.equals(exCase, exCase.getA().getAOPRef()));
		assertTrue(EcoreUtil.equals(exCase, exCase.getB().getBOPRef()));
		assertTrue(EcoreUtil.equals(exCase, exCase.getC().getCOPRef()));
	}

	@Test
	public void exceptionCaseList_SingleBuilderTest() {
		ExceptionCaseList exCaseList = createExceptionCaseList()
				.addA(createOppositeWithOPTList().optionalStringValue(
						"OptionalA").buildOppositeWithOPTList())
				.noA()
				.addB(createOppositeWithMandList().stringValue(
						"MandatoryString").buildOppositeWithMandList())
				.noB()
				.addC(createOppositeOnlyList().buildOppositeOnlyList())
				.noC()
				.buildExceptionCaseList();
		assertTrue(EMFUsing.validateObject(exCaseList));
		assertTrue(EMFUsing.validateObject(exCaseList.getA().get(0)));
		assertTrue(EMFUsing.validateObject(exCaseList.getB().get(0)));
		assertTrue(EMFUsing.validateObject(exCaseList.getC().get(0)));
		//sizes of each list and opposites list
		assertTrue(exCaseList.getA().size() == 1 && exCaseList.getA().get(0).getAOPRef().size() == 1);
		assertTrue(exCaseList.getB().size() == 1 && exCaseList.getB().get(0).getBOPRef().size() == 1);
		assertTrue(exCaseList.getC().size() == 1 && exCaseList.getC().get(0).getCOPRef().size() == 1);
		//String values has been set
		assertTrue("OptionalA".equals(exCaseList.getA().get(0).getStringValue()));
		assertTrue("MandatoryString".equals(exCaseList.getB().get(0).getStringValue()));
		//EMF Equals
		assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getA().get(0).getAOPRef().get(0)));
		assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getB().get(0).getBOPRef().get(0)));
		assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getC().get(0).getCOPRef().get(0)));
	}
}
