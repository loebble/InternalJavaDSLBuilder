package de.htwg.javafluentdsl.parser;

import java.util.regex.Pattern;

/**
 * Util Class for the {@link ParserRegex} Class. Contains Regular Expressions
 * for the Model Description {@link #MODEL_DESCRIPTION_REGEX} and defines
 * therefore the Models Meta Model through regular expression. Also provides the
 * Patterns for matching purposes.
 *
 */
public final class RegexDefinitions {

    /**
     *  Util Class therefore No Instantiation.
     */
    private RegexDefinitions() {
    }

    // Parts of regular expressions
    /**
     * Start of a class definition.
     */
    public static final String CLASS_START = ".class";
    /**
     * Definition of a class name.
     */
    public static final String CLASS_NAME = CLASS_START + "=\\w+";
    /**
     * Start of a mandatory attribute definition.
     */
    public static final String ATTR_START = ".A";
    /**
     * Start of a optional attribute definition.
     */
    public static final String OPT_START = ".OA";
    /**
     * Start of a list attribute definition.
     */
    public static final String LIST_START = ".LA";
    /**
     * Start of an opposite attribute definition.
     */
    public static final String OPPOSITE_START = ".OP";
    /**
     * Operator for setting a name.
     */
    public static final String NAMING_OPERATOR = "=";
    /**
     * Operator for setting a type.
     */
    public static final String TYPING_OPERATOR = ":";
    /**
     * the valid values after a naming operator.
     */
    public static final String VALID_NAMING = NAMING_OPERATOR + "\\w+";
    /**
     * the valid values after a typing operator.
     */
    public static final String VALID_TYPING = TYPING_OPERATOR + "\\w+";
    /**
     * Operator for opposite attributes.
     */
    public static final String OPPOSITE_OPERATOR = "->";
    /**
     * the valid values after an opposite operator.
     */
    public static final String VALID_OPPOSITE = OPPOSITE_OPERATOR + "\\w+";
    /**
     * Start of an import definition.
     */
    public static final String IMPORT_START = ".imp=";
    /**
     * Valid Single Parameter inside the import.
     */
    public static final String IMPORT_PARAMETER = "(\\w+(\\.)?)+";

    /**
     * Defines the start attribute of a class definition.
     */
    public static final String SINGLE_ATTRIBUTE_WITHOUT_OP = 
            "((.A|.OA|.LA)=\\w+:\\w+)";

    /**
     * Defines a single attribute (except opposite attribute).
     */
    public static final String FOLLOWING_SINGLE_ATTRIBUTE = 
            "(\\s?\\,\\s?(.A|.OA|.LA)=\\w+:\\w+)";

    /**
     * Defines the opposite attribute.
     */
    public static final String OPPOSITE_ATTRIBUTE = "(\\.OP=\\w+:\\w+"
            + OPPOSITE_OPERATOR + "\\w+)";

    /**
     * Defines the opposite attribute.
     */
    public static final String FOLLOWING_OPPOSITE_ATTRIBUTE = 
            "(\\s?\\,\\s? \\.OP=\\w+:\\w+"
            + OPPOSITE_OPERATOR + "\\w+)";

    /**
     * Defines an attribute OR an Opposite attribute.
     */
    public static final String ATTRIBUTE_OR_OPATTRIBUTE = "("
            + SINGLE_ATTRIBUTE_WITHOUT_OP + "|" + OPPOSITE_ATTRIBUTE + ")";

    /**
     * Defines whole imports definition. If an import is given it has to have at
     * least one import parameter.
     * 
     */
    public static final String IMPORT = 
            "(" + IMPORT_START + "\\{"
            + IMPORT_PARAMETER 
            + "(\\s?\\,\\s? " 
                + IMPORT_PARAMETER 
            + ")*\\})";

    /**
     * Regular expression for the attributes in a class definition Defines that
     * the class has to start with an mandatory, optional attribute or a list of
     * attributes and that a opposite attribute has to be at the end.
     * 
     * @see {@link #SINGLE_ATTRIBUTE_WITHOUT_OP}
     *      {@link#FOLLOWING_SINGLE_ATTRIBUTE}
     *      {@link#FOLLOWING_OPPOSITE_ATTRIBUTE}
     */
    public static final String ALL_ATTR_DEFINITION = "\\{"
            + SINGLE_ATTRIBUTE_WITHOUT_OP + FOLLOWING_SINGLE_ATTRIBUTE + "*"
            + FOLLOWING_OPPOSITE_ATTRIBUTE + "*" + "\\}";

    /**
     * Regular expression for a complete Class definition. Has to start with
     * {@link #CLASS_NAME} and followed by an attribute declaration
     * {@link #ALL_ATTR_DEFINITION}.
     */
    public static final String CLASS_DEFINITION = "(" + CLASS_NAME + ""
            + ALL_ATTR_DEFINITION + ")";

    /**
     * Regular Expression for the complete model description.
     */
    public static final String MODEL_DESCRIPTION_REGEX = CLASS_DEFINITION + "+"
            + IMPORT + "?";

    // Patterns for match using
    /**
     * Pattern for {@link #VALID_NAMING}.
     */
    public static final Pattern NAMING_PATTERN = Pattern.compile(VALID_NAMING,
            Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #VALID_TYPING}.
     */
    public static final Pattern TYPING_PATTERN = Pattern.compile(VALID_TYPING,
            Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #IMPORT}.
     */
    public static final Pattern IMPORT_PATTERN = Pattern.compile(IMPORT,
            Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #OPPOSITE_ATTRIBUTE}.
     */
    public static final Pattern OPPOSITE_ATTRIBUTE_PATTERN = Pattern.compile(
            OPPOSITE_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #VALID_OPPOSITE}.
     */
    public static final Pattern OPPOSITE_PATTERN = Pattern.compile(
            VALID_OPPOSITE, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #ATTRIBUTE_OR_OPATTRIBUTE}.
     */
    public static final Pattern ATTRIBUTE_ALL_PATTERN = Pattern.compile(
            ATTRIBUTE_OR_OPATTRIBUTE, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #CLASS_NAME}.
     */
    public static final Pattern CLASS_NAME_PATTERN = Pattern.compile(
            CLASS_NAME, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #ALL_ATTR_DEFINITION}.
     */
    public static final Pattern CLASS_ATTRIBUTES_PATTERN = Pattern.compile(
            ALL_ATTR_DEFINITION, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #CLASS_DEFINITION}.
     */
    public static final Pattern CLASS_DEFINITION_PATTERN = Pattern.compile(
            CLASS_DEFINITION, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #CLASS_DEFINITION}.
     */
    public static final Pattern IMPORT_PARAMETER_PATTERN = Pattern.compile(
            IMPORT_PARAMETER, Pattern.CASE_INSENSITIVE);
    /**
     * Pattern for {@link #MODEL_DESCRIPTION_REGEX}.
     */
    public static final Pattern MODEL_DESCRIPTION_REGEX_PATTERN = Pattern
            .compile(MODEL_DESCRIPTION_REGEX, Pattern.CASE_INSENSITIVE);

    /**
     * Error Message for description that does not match
     * {@link #MODEL_DESCRIPTION_REGEX}.
     */
    public static final String MODEL_DOESNT_MATCH = 
            "Given String does not match the regular "
            + "expression for a model description: \n"
            + RegexDefinitions.MODEL_DESCRIPTION_REGEX;

    /**
     * Method for checking if given description String is a valid one.
     * 
     * @param modelDescription
     *            the description for the model and therefore for the DSL
     * @return true if it is a valid description
     */
    public static boolean isDescriptionMatching(final String modelDescription) {
        return MODEL_DESCRIPTION_REGEX_PATTERN.matcher(modelDescription)
                .matches();
    }

}
