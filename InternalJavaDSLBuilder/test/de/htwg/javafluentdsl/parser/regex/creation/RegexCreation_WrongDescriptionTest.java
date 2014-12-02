package de.htwg.javafluentdsl.parser.regex.creation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.htwg.javafluentdsl.parser.ParserRegex;
/**
 * Test class for wrong language descriptions
 *
 */
public class RegexCreation_WrongDescriptionTest {
	/*
	 * Wrong descriptions
	 */
	
	private final static String WRONG_EMPH=
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .OP=oppUser:User->oppUser"
			;
	
	private final static String WRONG_CLASS=
			".clas=User{.A=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .OP=oppUser:User->oppUser}"
			;
	
	private final static String WRONG_ATTR=
			".class=User{.=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .OP=oppUser:User->oppUser}"
			;
	
	public final static String WRONG_SECOND_CLASS =
			".class=User{.A=firstName:String, .A=lastName:String, .A=email:URL, .A=born:Date}"
			+".class=B{.=b:String}"
			+ ".imp={java.net.URL, java.util.Date}"
			;
	
	private final static String WRONG_OP_CLASS=
			".class=User{.A=firstName:String, .A=lastName:String, .A=email:URL, .A=born:Date, .OP=b:A->b}"
			+".class=B{.A=b:User}"
			+ ".imp={java.net.URL, java.util.Date}"
			;
	
	private final static String USER_DESCRIPTION_OPP_SAME_ATTR=
			".class=User{.A=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .A=address:Address, .OP=oppUser:User->oppUser}"
			;
	
	private final static String FORUM_WRONG_DECL =
			".class=Forum{.A=name:String, .A=url:URL, .a=asd=int}"
			+ ".imp={java.net.URL}"
			;
	
	private final static String FORUM_DUPL_CLASSES =
			".class=Forum{.A=name:String, .A=url:URL, .A=asd:int}"
			+ ".class=Forum{.A=asd:String}"
			+ ".imp={java.net.URL}"
			;
	
	private final static String FORUM_DUPL_ATTRIBUTENAME =
			".class=Forum{.A=name:String, .A=url:URL, .OA=name:int}"
			+ ".imp={java.net.URL}"
			;
	
	private final static String FORUM_OP_NO_REFERENCE =
			".class=Forum{.A=name:String, .A=url:URL, .OA=userABC:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .OP=forum:Forum->user}"
			+ ".imp={java.net.URL}"
			;
	
	private final static String FORUM_OP_WRONG_REFERENCE_TYPE =
			".class=Forum{.A=name:String, .A=url:URL, .OA=user:User}"
			+ ".class=User{.OA=firstName:String, .A=lastName:String, .OA=age:int"
			+ ", .A=nickName:String, .OP=forum:Forum->user}"
			+ ".class=Post{.A=title:String, .A=text:String, .A=views:int, .OP=creator:User->forum}"
			+ ".imp={java.net.URL}"
			;
	
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Boolean =
			".class=SomeClass{.LA=list:boolean}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Byte =
			".class=SomeClass{.LA=list:byte}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Short =
			".class=SomeClass{.LA=list:short}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Int =
			".class=SomeClass{.LA=list:int}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Long =
			".class=SomeClass{.LA=list:long}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Char =
			".class=SomeClass{.LA=list:char}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Float =
			".class=SomeClass{.LA=list:float}"
			;
	private final static String LIST_PRIM_DESCRIPTION_Wrong_Double =
			".class=SomeClass{.LA=list:double}"
			;
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmphFails() {
		try{
			ParserRegex.getInstance(WRONG_EMPH);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongClassFails() {
		try{
			ParserRegex.getInstance(WRONG_CLASS);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongAttrFails() {
		try{
			ParserRegex.getInstance(WRONG_ATTR);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongSecondClassFails() {
		try{
			ParserRegex.getInstance(WRONG_SECOND_CLASS);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongOPWrongClassFails() {
		try{
			ParserRegex.getInstance(WRONG_OP_CLASS);
		}catch(IllegalArgumentException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
	}
	
	
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
	public void testOP_WrongListPrim_char() {
		try{
			ParserRegex.getInstance(LIST_PRIM_DESCRIPTION_Wrong_Char);
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
