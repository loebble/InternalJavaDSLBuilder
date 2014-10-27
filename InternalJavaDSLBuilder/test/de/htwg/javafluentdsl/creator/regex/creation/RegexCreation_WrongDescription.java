package de.htwg.javafluentdsl.creator.regex.creation;

import org.junit.Test;

import de.htwg.javafluentdsl.creator.CreatorRegex;
/**
 * Test class for wrong language descriptions
 *
 */
public class RegexCreation_WrongDescription {
	/*
	 * Wrong descriptions
	 */
	public final static String FORUM_WRONG_DECL =
			".class=Forum{.A=name:String, .A=url:URL, .a=asd=int}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DUPL_CLASSES =
			".class=Forum{.A=name:String, .A=url:URL, .A=asd:int}"
			+ ".class=Forum{.A=asd:String}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_DUPL_ATTRIBUTENAME =
			".class=Forum{.A=name:String, .A=url:URL, .OA=name:int}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_OP_IN_SAME_CLASS =
			".class=Forum{.A=name:String, .A=url:URL, .LA=users:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=user:User, .OP=userABC:User->user}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_OP_NO_REFERENCE =
			".class=Forum{.A=name:String, .A=url:URL, .OA=userABC:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OP=forum:Forum->user}"
			+ ".imp={java.net.URL}"
			;
	
	public final static String FORUM_OP_WRONG_REFERENCE_TYPE =
			".class=Forum{.A=name:String, .A=url:URL, .OA=user:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .OP=forum:Forum->user}"
			+ ".class=Post{.A=title:String, .A=text:String, .A=views:int, .OP=creator:User->forum}"
			+ ".imp={java.net.URL}"
			;
	
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSimpleDeclFails() {
		try{
			CreatorRegex.getInstance(FORUM_WRONG_DECL);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateClasses() {
		try{
			CreatorRegex.getInstance(FORUM_DUPL_CLASSES);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateAttribute() {
		try{
			CreatorRegex.getInstance(FORUM_DUPL_ATTRIBUTENAME);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_InSameClass() {
		try{
			CreatorRegex.getInstance(FORUM_OP_IN_SAME_CLASS);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_NoRefAttr() {
		try{
			CreatorRegex.getInstance(FORUM_OP_NO_REFERENCE);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongRefType() {
		try{
			CreatorRegex.getInstance(FORUM_OP_WRONG_REFERENCE_TYPE);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
}
