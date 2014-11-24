package de.htwg.javafluentdsl.parser;

import java.util.regex.Pattern;

/**
 * Util Class for the {@link ParserRegex} Class.
 * Contains Regular Expressions for the Model Description {@link #MODEL_DESCRIPTION}
 * and defines therefore the Models Meta Model through regular expression.
 * Also provides the Patterns for matching purposes.
 *
 */
public final class RegexUtil {
	
	// Util Class therefore No Instantiation
	public RegexUtil(){}
	
	//Parts of regular expressions
	public static final String CLASS_START = ".class";
	public static final String CLASS_NAME = CLASS_START+"=\\w+";
	public static final String ATTR_START = ".A";
	public static final String OPT_START = ".OA";
	public static final String LIST_START = ".LA";
	public static final String OPPOSITE_START = ".OP";
	public static final String OPPOSITE_OPERATOR = "->";
	public static final String NAMING_OPERATOR = "=";
	public static final String TYPING_OPERATOR = ":";
	public static final String NAMING = NAMING_OPERATOR+"\\w+";
	public static final String TYPING = TYPING_OPERATOR+"\\w+";
	public static final String OPPOSITE = OPPOSITE_OPERATOR+"\\w+";
	public static final String IMPORT_START= ".imp=";
	public static final String IMPORT_PARAMETER = "(\\w+(\\.)?)+";
	
	/**
	 * Defines the start attribute
	 */
	public static final String SINGLE_ATTRIBUTE_WITHOUT_OP = "((.A|.OA|.LA)=\\w+:\\w+)";
	
	/**
	 * Defines a single attribute (except opposite attribute)
	 */
	public static final String FOLLOWING_SINGLE_ATTRIBUTE = "(\\s?\\,\\s?(.A|.OA|.LA)=\\w+:\\w+)";
	
	/**
	 * Defines the opposite attribute
	 */
	public static final String OPPOSITE_ATTRIBUTE = "(\\.OP=\\w+:\\w+"+OPPOSITE_OPERATOR+"\\w+)";
	
	/**
	 * Defines the opposite attribute
	 */
	public static final String FOLLOWING_OPPOSITE_ATTRIBUTE = "(\\s?\\,\\s? \\.OP=\\w+:\\w+"+OPPOSITE_OPERATOR+"\\w+)";
	
	
	/**
	 * Defines an attribute OR an Opposite attribute
	 */
	public static final String ATTRIBUTE_OR_OPATTRIBUTE = "("+SINGLE_ATTRIBUTE_WITHOUT_OP+"|"+OPPOSITE_ATTRIBUTE+")";
	
	/**
	 * Defines the imports
	 */
	public static final String IMPORT = "(\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\})";
	
	/**
	 * Regular expression for the attributes in a class definition
	 * Defines that the class has to start with an mandatory, optional attribute or a list of attributes
	 * and that a opposite attribute has to be at the end
	 * @see {@link #SINGLE_ATTRIBUTE_WITHOUT_OP} {@link#FOLLOWING_SINGLE_ATTRIBUTE} {@link#FOLLOWING_OPPOSITE_ATTRIBUTE} 
	 */
	public static final String ALL_ATTR_DEFINITION = "\\{"
			+ SINGLE_ATTRIBUTE_WITHOUT_OP
			+ FOLLOWING_SINGLE_ATTRIBUTE +"*"
			+ FOLLOWING_OPPOSITE_ATTRIBUTE + "*"
			+"\\}";
	
	/**
	 * Regular expression for a complete Class definition.
	 * Has to start with .class and go on with attribute declaration {@link #ALL_ATTR_DEFINITION}
	 */
	public static final String CLASS_DEFINITION = "("+CLASS_NAME+""+ALL_ATTR_DEFINITION +")";
	
	/**
	 * Regular Expression for the complete model description 
	 */
	public static final String MODEL_DESCRIPTION= 
			CLASS_DEFINITION + "+"
			+ IMPORT + "?"
			;
	
	//Patterns for matching
	public static final Pattern CLASS_DEFINITION_PATTERN = Pattern.compile(CLASS_DEFINITION, Pattern.CASE_INSENSITIVE);
	public static final Pattern CLASS_NAME_PATTERN= Pattern.compile(CLASS_NAME, Pattern.CASE_INSENSITIVE);
	public static final Pattern CLASS_ATTRIBUTES_PATTERN= Pattern.compile(ALL_ATTR_DEFINITION, Pattern.CASE_INSENSITIVE);
	public static final Pattern ATTRIBUTE_ALL_PATTERN = Pattern.compile(ATTRIBUTE_OR_OPATTRIBUTE, Pattern.CASE_INSENSITIVE);
	public static final Pattern NAMING_PATTERN = Pattern.compile(NAMING, Pattern.CASE_INSENSITIVE);
	public static final Pattern IMPORT_PATTERN = Pattern.compile(IMPORT, Pattern.CASE_INSENSITIVE);
	public static final Pattern OPPOSITE_ATTRIBUTE_PATTERN = Pattern.compile(OPPOSITE_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
	public static final Pattern TYPING_PATTERN= Pattern.compile(TYPING, Pattern.CASE_INSENSITIVE);
	public static final Pattern OPPOSITE_PATTERN= Pattern.compile(OPPOSITE, Pattern.CASE_INSENSITIVE);
	public static final Pattern IMPORT_PARAMETER_PATTERN = Pattern.compile(IMPORT_PARAMETER, Pattern.CASE_INSENSITIVE);
	
	//Errors
	public static final String MODEL_DOESNT_MATCH = "Given String does not match the regular "
			+ "expression for a model description: \n" +RegexUtil.MODEL_DESCRIPTION;
	
	/**
	 * Method for checking if given description String is a valid one.
	 * @param modelDescription the description for the model and therefore for the DSL
	 * @return true if it is a valid description
	 */
	public static boolean isDescriptionMatching(String modelDescription){
		return modelDescription.matches(RegexUtil.MODEL_DESCRIPTION);
	}
	
}
