package de.htwg.javafluentdsl.parser.regex.creation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.htwg.javafluentdsl.parser.ParserRegex;
/**
 * Test class for wrong language descriptions
 *
 */
public class RegexCreation_WrongDescription {
	/*
	 * Wrong descriptions
	 */
	
	public final static String USER_DESCRIPTION_OPP_SAME_ATTR=
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int, .A=nickName:String, .A=address:Address, .OP=oppUser:User->oppUser}"
			;
	
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
	
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Boolean =
			".class=SomeClass{.LA=list:boolean}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Byte =
			".class=SomeClass{.LA=list:byte}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Short =
			".class=SomeClass{.LA=list:short}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Int =
			".class=SomeClass{.LA=list:int}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Long =
			".class=SomeClass{.LA=list:long}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Char =
			".class=SomeClass{.LA=list:char}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Float =
			".class=SomeClass{.LA=list:float}"
			;
	public final static String LIST_PRIM_DESCRIPTION_Wrong_Double =
			".class=SomeClass{.LA=list:double}"
			;
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSimpleDeclFails() {
		try{
			ParserRegex.getInstance(FORUM_WRONG_DECL);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateClasses() {
		try{
			ParserRegex.getInstance(FORUM_DUPL_CLASSES);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateAttribute() {
		try{
			ParserRegex.getInstance(FORUM_DUPL_ATTRIBUTENAME);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_SameAttr() {
		try{
			ParserRegex.getInstance(USER_DESCRIPTION_OPP_SAME_ATTR);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_NoRefAttr() {
		try{
			ParserRegex.getInstance(FORUM_OP_NO_REFERENCE);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongRefType() {
		try{
			ParserRegex.getInstance(FORUM_OP_WRONG_REFERENCE_TYPE);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_bool() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Boolean);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_byte() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Byte);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_short() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Short);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_int() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Int);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_long() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Long);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_float() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Float);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
	@Test(expected = IllegalArgumentException.class)
	public void testOP_WrongListPrim_double() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Double);
		}catch(IllegalArgumentException ex){
			assertTrue(ex.getMessage().startsWith("For a List the type cannot be a primitive one"));
			throw ex;
		}
	}
}
