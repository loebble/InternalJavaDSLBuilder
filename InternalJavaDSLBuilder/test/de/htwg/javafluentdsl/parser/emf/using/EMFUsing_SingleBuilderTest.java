package de.htwg.javafluentdsl.parser.emf.using;

import static de.htwg.generated.emf.dsl.exceptionCase.singleBuilder.ExceptionCaseBuilder.createExceptionCase;
import static de.htwg.generated.emf.dsl.exceptionCase.singleBuilder.ExceptionCaseBuilder.createOppositeOnly;
import static de.htwg.generated.emf.dsl.exceptionCase.singleBuilder.ExceptionCaseBuilder.createOppositeWithMand;
import static de.htwg.generated.emf.dsl.exceptionCase.singleBuilder.ExceptionCaseBuilder.createOppositeWithOPT;
import static de.htwg.generated.emf.dsl.exceptionCaseList.singleBuilder.ExceptionCaseListBuilder.createExceptionCaseList;
import static de.htwg.generated.emf.dsl.exceptionCaseList.singleBuilder.ExceptionCaseListBuilder.createOppositeOnlyList;
import static de.htwg.generated.emf.dsl.exceptionCaseList.singleBuilder.ExceptionCaseListBuilder.createOppositeWithMandList;
import static de.htwg.generated.emf.dsl.exceptionCaseList.singleBuilder.ExceptionCaseListBuilder.createOppositeWithOPTList;
import static de.htwg.generated.emf.dsl.forum.singleBuilder.ForumBuilder.createForum;
import static de.htwg.generated.emf.dsl.forum.singleBuilder.ForumBuilder.createPost;
import static de.htwg.generated.emf.dsl.forum.singleBuilder.ForumBuilder.createUser;
import static de.htwg.generated.emf.dsl.complexforum.singleBuilder.ComplexForumBuilder.*;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

/*
 * Imports for EMF Model and DSL Builders
 */
import de.htwg.generated.emf.dsl.optonly.singleBuilder.OptOnlyBuilder;
import de.htwg.generated.emf.model.ComplexForum.ComplexForum;
import de.htwg.generated.emf.model.ComplexForum.ComplexUser;
import de.htwg.generated.emf.model.ExceptionCase.ExceptionCase;
import de.htwg.generated.emf.model.ExceptionCaseList.ExceptionCaseList;
import de.htwg.generated.emf.model.Forum.Forum;
import de.htwg.generated.emf.model.Forum.User;
import de.htwg.generated.emf.model.OptOnly.OptOnly;
import de.htwg.javafluentdsl.parser.emf.creation.EMFCreation_SingleBuilderTest;

/**
 * Test For Using the EMF DSL. The testmethods
 * have the names of the Ecore model and dsl they test. 
 * If imports are not correct please make
 * sure EMF genmodel has created EMF models and
 * {@link EMFCreation_SingleBuilderTest} has run.
 *
 */
public class EMFUsing_SingleBuilderTest {

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
    String postTitle3 = "MyThirdPost";
    String postText3 = "MyThirdPostText";
    String urlString = "http://MyForum.com";

    @Test
    public void ForumSingleBuilderTest() throws MalformedURLException {
        Forum forum = createForum()
                .name(forumName)
                .url(new URL(urlString))
                .addUsers(
                    createUser()
                        .optFirstName(firstName)
                        .optLastName(lastName)
                        .optAge(age)
                        .email(email)
                        .addPosts(
                            createPost().optText(postText1)
                                .optViews(5).title(postTitle1)
                                .buildPost())
                        .addPosts(
                            createPost().optText(postText2)
                                .optViews(10).title(postTitle2)
                                .buildPost()).noPosts()
                    .buildUser())
                .addUsers(
                    createUser()
                        .optFirstName(firstName2)
                        .optLastName(lastName2)
                        .optAge(age2)
                        .email(email2)
                        .addPosts(
                            createPost().optText(postText1)
                                .optViews(11).title(postTitle2)
                                .buildPost()).noPosts()
                        .buildUser())
                .noUsers()
        .buildForum();

        User user1 = forum.getUsers().get(0);
        User user2 = forum.getUsers().get(1);

        // check for list sizes
        assertTrue(user1.getPosts().size() == 2);
        assertTrue(user2.getPosts().size() == 1);
        assertTrue(forum.getUsers().size() == 2);
        // check for same opposite objects
        assertTrue(EcoreUtil
                .equals(user1.getPosts().get(0).getCreator(), user1));
        assertTrue(EcoreUtil
                .equals(user1.getPosts().get(1).getCreator(), user1));
        assertTrue(EcoreUtil
                .equals(user2.getPosts().get(0).getCreator(), user2));
        // calling EMFs validateObject for validating
        assertTrue(EMFUsingTest.validateEObject(forum));
        forum.getUsers().forEach(
                u -> assertTrue(EMFUsingTest.validateEObject(u)));
        forum.getUsers().forEach(
                u -> u.getPosts().forEach(
                        p -> assertTrue(EMFUsingTest.validateEObject(p))));
    }

    @Test
    public void ComplexForumSingleBuilderTest() throws MalformedURLException {
        ComplexForum complexForum = createComplexForum()
                .name(forumName)
                .url(new URL(urlString))
                .addSections(
                    createSection()
                        .addModerators(
                            createComplexUser()
                                .email("dummy")
                                .nickName("dummy")
                                .rating(createRating()
                                    .buildRating())
                                .noPosts().buildComplexUser())
                        .noModerators().buildSection())
                .noSections()
                .addUsers(
                    createComplexUser()
                        .optFirstName(firstName)
                        .optLastName(lastName)
                        .optAge(age)
                        .email(email)
                        .nickName(nickName)
                        .rating(createRating().optUpps(5).buildRating())
                        .addPosts(
                            createComplexPost()
                                .title(postTitle1)
                                .optViews(5)
                                .text(postText1)
                                .noRepliers()
                                .rating(createRating()
                                        .buildRating())
                                .buildComplexPost())
                        .addPosts(
                            createComplexPost()
                                .title(postTitle2)
                                .text(postText2)
                                .noRepliers()
                                .rating(createRating()
                                        .buildRating())
                            .buildComplexPost())
                        .noPosts()
                    .buildComplexUser())
                .addUsers(
                    createComplexUser()
                        .optFirstName(firstName2)
                        .optLastName(lastName2)
                        .optAge(age2)
                        .email(email2)
                        .nickName(nickName2)
                        .rating(createRating().buildRating())
                        .addPosts(
                            createComplexPost()
                                .title(postTitle3)
                                .optViews(1)
                                .text(postText3)
                                .noRepliers()
                                .rating(createRating()
                                        .buildRating())
                                .buildComplexPost()).noPosts()
                    .buildComplexUser()
                )
                .noUsers()
        .buildComplexForum();

        ComplexUser user1 = complexForum.getUsers().get(0);
        ComplexUser user2 = complexForum.getUsers().get(1);
        // check for list sizes
        assertTrue(user1.getPosts().size() == 2);
        assertTrue(user2.getPosts().size() == 1);
        assertTrue(complexForum.getSections().size() == 1);
        assertTrue(complexForum.getSections().get(0).getModerators().size() == 1);
        assertTrue(complexForum.getUsers().size() == 2);
        // some set values
        assertTrue(user1.getFirstName().equals(firstName));
        assertTrue(user2.getFirstName().equals(firstName2));
        assertTrue(user1.getPosts().get(0).getTitle().equals(postTitle1));
        assertTrue(user1.getPosts().get(1).getTitle().equals(postTitle2));
        assertTrue(user2.getPosts().get(0).getTitle().equals(postTitle3));
        // check for same opposite objects, regardless
        // from which side they were set
        assertTrue(EcoreUtil.equals(complexForum.getUsers().get(0), user1));
        assertTrue(EcoreUtil.equals(complexForum.getUsers().get(1), user2));
        assertTrue(EcoreUtil
                .equals(user1, user1.getPosts().get(0).getCreator()));
        assertTrue(EcoreUtil
                .equals(user1.getPosts().get(1).getCreator(), user1));
        assertTrue(EcoreUtil
                .equals(user2, user2.getPosts().get(0).getCreator()));
        // Calling EMFs validateObject for validating
        assertTrue(EMFUsingTest.validateEObject(complexForum));
        complexForum.getSections().forEach(
                s -> assertTrue(EMFUsingTest.validateEObject(s)));
        complexForum.getUsers().forEach(
                u -> assertTrue(EMFUsingTest.validateEObject(u)));
        complexForum.getUsers().forEach(
                u -> u.getPosts().forEach(
                        p -> assertTrue(EMFUsingTest.validateEObject(p))));
        complexForum.getUsers().forEach(
                u -> u.getPosts().forEach(
                        p -> assertTrue(EMFUsingTest.validateEObject(p
                                .getRating()))));
        complexForum.getUsers().forEach(
                u -> assertTrue(EMFUsingTest.validateEObject(u.getRating())));
    }

    @Test
    public void optOnly_SingleBuilderTest() {
        OptOnly opt = OptOnlyBuilder
                .createOptOnly()
                .optName("ad")
                .optSomeOtherAttr(12)
                .ref(OptOnlyBuilder.createRef().optOtherName("otherName")
                        .optSomeNumber(true).buildRef()).buildOptOnly();

        assertTrue(EMFUsingTest.validateEObject(opt));
        assertTrue(EMFUsingTest.validateEObject(opt.getRef()));
        assertTrue(opt.getName().equals("ad"));
        assertTrue(opt.getSomeOtherAttr() == 12);
        assertTrue(opt.getRef() != null);
        assertTrue(opt.getRef().getOtherName().equals("otherName"));
        assertTrue(opt.getRef().isSomeNumber() == true);

    }

    @Test
    public void ExceptionCase_SingleBuilderTest() {
        ExceptionCase exCase = createExceptionCase()
                .a(createOppositeWithOPT().optStringValue("OptionalA")
                        .buildOppositeWithOPT())
                .b(createOppositeWithMand().stringValue("MandatoryString")
                        .buildOppositeWithMand())
                .c(createOppositeOnly().buildOppositeOnly())
                .buildExceptionCase();

        assertTrue(EMFUsingTest.validateEObject(exCase));
        assertTrue(EMFUsingTest.validateEObject(exCase));
        assertTrue(EMFUsingTest.validateEObject(exCase.getA()));
        assertTrue(EMFUsingTest.validateEObject(exCase.getB()));
        assertTrue(EMFUsingTest.validateEObject(exCase.getC()));
        // String values has been set
        assertTrue("OptionalA".equals(exCase.getA().getStringValue()));
        assertTrue("MandatoryString".equals(exCase.getB().getStringValue()));
        // EMF Equals
        assertTrue(EcoreUtil.equals(exCase, exCase.getA().getAOPRef()));
        assertTrue(EcoreUtil.equals(exCase, exCase.getB().getBOPRef()));
        assertTrue(EcoreUtil.equals(exCase, exCase.getC().getCOPRef()));
    }

    @Test
    public void exceptionCaseList_SingleBuilderTest() {
        ExceptionCaseList exCaseList = createExceptionCaseList()
                .addA(createOppositeWithOPTList().optStringValue("OptionalA")
                        .buildOppositeWithOPTList())
                .noA()
                .addB(createOppositeWithMandList().stringValue(
                        "MandatoryString").buildOppositeWithMandList()).noB()
                .addC(createOppositeOnlyList().buildOppositeOnlyList()).noC()
                .buildExceptionCaseList();
        assertTrue(EMFUsingTest.validateEObject(exCaseList));
        assertTrue(EMFUsingTest.validateEObject(exCaseList.getA().get(0)));
        assertTrue(EMFUsingTest.validateEObject(exCaseList.getB().get(0)));
        assertTrue(EMFUsingTest.validateEObject(exCaseList.getC().get(0)));
        // sizes of each list and opposites list
        assertTrue(exCaseList.getA().size() == 1
                && exCaseList.getA().get(0).getAOPRef().size() == 1);
        assertTrue(exCaseList.getB().size() == 1
                && exCaseList.getB().get(0).getBOPRef().size() == 1);
        assertTrue(exCaseList.getC().size() == 1
                && exCaseList.getC().get(0).getCOPRef().size() == 1);
        // String values has been set
        assertTrue("OptionalA"
                .equals(exCaseList.getA().get(0).getStringValue()));
        assertTrue("MandatoryString".equals(exCaseList.getB().get(0)
                .getStringValue()));
        // EMF Equals
        assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getA().get(0)
                .getAOPRef().get(0)));
        assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getB().get(0)
                .getBOPRef().get(0)));
        assertTrue(EcoreUtil.equals(exCaseList, exCaseList.getC().get(0)
                .getCOPRef().get(0)));
    }
}
