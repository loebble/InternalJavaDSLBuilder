
package de.htwg.javaDSLBuilder.test;

import de.htwg.javaDSLBuilder.model.ModelCreatorRegex;

public class modelTest {
	
	public final static String FORUM_DESCRIPTION = "modelName=Forum"
			+ ".A=name:String"
			+ ".A=url:String"
			+ ".OA=genre:String"
			+ ".LA=users:User{.A=firstName:String, .A=lastName:String, .LA=posts:Post, .OA=age:int, .OA=address:Address}"
			+ ".def:Post{.A=header:String, .A=text:String, .OA=likes:int, .OLA=replier:User}"
			+ ".def:Address{.A=street:String, .A=houseNumber:int, .A=zipCode:String}"
			;
	
	public static void main(String[] args) {
		ModelCreatorRegex regexCreator= ModelCreatorRegex.getInstance(FORUM_DESCRIPTION);
	}

}
