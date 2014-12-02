package de.htwg.javafluentdsl.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwg.javafluentdsl.dslmodel.DependencyKind;
import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * Class for Creating a {@link DSLGenerationModel} from a model description
 * defined by 
 * {@link de.htwg.javafluentdsl.parser.RegexDefinitions#MODEL_DESCRIPTION_REGEX
 * model description}.
 * 
 * @see {@link IParser}
 */
public final class ParserRegex extends AbstractIParserBasic {

    // Error Messages
    /**
     * Error Message if same class is defined multiple times.
     */
    private static final String CLASS_DEFINED_MULTIPLE_TIMES = 
            "The class was defined more than once";
    /**
     * Error Message if same attribute in class is defined multiple times.
     */
    private static final String SAME_ATTRIBUTE_MULTIPLE_TIMES = 
            "An Attribute can only be declared once in the same class!";
    /**
     * Error Message if same class was not defined which is referenced.
     */
    private static final String CLASS_NOT_DEFINED = 
            "class is not defined!";
    /**
     * Error Message if attributes opposite has not the needed type.
     */
    private static final String OPPOSITE_DIFFERENT_TYPE = 
            "The defined opposite attribute and its "
            + "referencing attribute have different types.";
    /**
     * Error Message if the attributes defined opposite attribute
     * does not exist.
     */
    private static final String OPPOSITE_ATTRIBUTE_NOT_DEFINED = 
            "Defined opposite attributes other end was not found. ";
    /**
     * Error Message if the attributes opposite is the attribute 
     * itself.
     */
    private static final String OPPOSITE_ATTRIBUTE_THE_SAME = 
        "The other end of a opposite attribute can not be the same attribute!";
    /**
     * Error Message if a list of attributes 
     * is defined with a primitive type.
     */
    private static final String PRIMITVES_NOT_ALLOWED_FOR_LIST = 
            "For a List the type cannot be a primitive one.";

    // Matcher used for regular expression matching
    /**
     * Matches the definition of a class.
     * 
     * @see RegexDefinitions#CLASS_DEFINITION_PATTERN
     */
    private final Matcher classDefinitionMatcher;
    /**
     * Matches import whole import expression.
     * 
     * @see RegexDefinitions#IMPORT_PATTERN
     */
    private final Matcher importMatcher;

    /**
     * List for temporarily saving the classes that need to be imported.
     */
    private List<String> imports;
    /**
     * List for temporarily saving the classes defined in the description.
     */
    private Map<String, String> definedClasses = new LinkedHashMap<>();
    /**
     * defines the number of characters that are shown 
     * from the error backwards.
     */
    private static final int NUMBER_OF_CHARACTER_TO_SHOW = 10;

    /**
     * Private Constructor. This class should be instantiated via its
     * {@link #getInstance(String)} method
     * @param modelDescription The string with the complete 
     * Regex model description.
     */
    private ParserRegex(final String modelDescription) {
        super();
        this.classDefinitionMatcher = RegexDefinitions.CLASS_DEFINITION_PATTERN
                .matcher(modelDescription);
        this.importMatcher = RegexDefinitions.IMPORT_PATTERN
                .matcher(modelDescription);
    }

    /**
     * Creates a new Instance of this class {@link ParserRegex}. An ParserRegex
     * need a model description. This description is defined by the
     * {@link RegexDefinitions#MODEL_DESCRIPTION_REGEX}. All the necessary
     * methods are called in this method so that after it the Creators genModel
     * can be used.
     * 
     * @param modelDescription
     *            the String with the whole model Description
     * @return new Instance of the CreatorRegex class
     */
    public static ParserRegex getInstance(final String modelDescription) {
        if (!RegexDefinitions.isDescriptionMatching(modelDescription)) {
            // try to find class description which failed
            Pattern p = Pattern.compile(RegexDefinitions.CLASS_DEFINITION);
            Matcher m = p.matcher(modelDescription);
            int endOfLastCorrectClass = 0;
            while (m.find()) {
                endOfLastCorrectClass = m.end();
            }
            String appendedErrorMsg = "";
            if (endOfLastCorrectClass == 0) {
                appendedErrorMsg = "\n First class definition has errors";
            } else {
                // class definitions are always greater than 10 characters
                appendedErrorMsg = "\n Error in class definition after :'"
                        + modelDescription.substring(
                                endOfLastCorrectClass 
                                    - NUMBER_OF_CHARACTER_TO_SHOW,
                                endOfLastCorrectClass) + "'...";
            }
            throw new IllegalArgumentException(
                    RegexDefinitions.MODEL_DOESNT_MATCH + appendedErrorMsg);
        }
        ParserRegex parser = new ParserRegex(modelDescription);
        parser.retrieveDefinedClasses();
        parser.retrieveImports();
        parser.createAttributeOrder();
        return parser;
    }

    /**
     * Retrieves the value (the name) after the naming operator
     * {@link RegexDefinitions#NAMING_OPERATOR}.
     * 
     * @param def
     *            the definition defined by {@link RegexDefinitions#REGEX}
     * @return the defined Name as String, or "" if none found
     */
    private String getNameOfDefinition(final String def) {
        Matcher namingMatcher = RegexDefinitions.NAMING_PATTERN.matcher(def);
        if (namingMatcher.find()) {
            String naming = namingMatcher.group();
            return naming.substring(RegexDefinitions.NAMING_OPERATOR.length());
        } else {
            return "";
        }
    }

    /**
     * Retrieves the value (the type) after the typing operator
     * {@link RegexDefinitions#TYPING_OPERATOR}.
     * 
     * @param def
     *            the definition defined by
     *            {@link RegexDefinitions#ATTRIBUTE_OR_OPATTRIBUTE}
     * @return the defined type as String, or "" if none found.
     */
    private String getTypeOfDefinition(final String def) {
        Matcher typingMatcher = RegexDefinitions.TYPING_PATTERN.matcher(def);
        if (typingMatcher.find()) {
            String naming = typingMatcher.group();
            return naming.substring(RegexDefinitions.TYPING_OPERATOR.length());
        } else {
            return "";
        }
    }

    /**
     * Retrieves the name of the opposite attribute. defined afer the opposite
     * operator {@link RegexDefinitions#OPPOSITE_OPERATOR}
     * 
     * @param def the definition
     * @return the defined name of the opposite attribute, or "" if none found
     */
    private String getOppositeNameOfDefinition(final String def) {
        Matcher opMatcher = RegexDefinitions.OPPOSITE_PATTERN.matcher(def);
        if (opMatcher.find()) {
            String naming = opMatcher.group();
            return naming
                    .substring(RegexDefinitions.OPPOSITE_OPERATOR.length());
        } else {
            return "";
        }
    }

    /**
     * Retrieves the classes and their attributes defined in the language
     * description and adds them to the {@link DSLGenerationModel}. instance
     */
    private void retrieveDefinedClasses() {
        while (this.classDefinitionMatcher.find()) {
            String classDef = this.classDefinitionMatcher.group();
            String className = retrieveClassName(classDef);
            // a className's first letter should be upper case
            className = Character.toUpperCase(className.charAt(0))
                    + className.substring(1);
            // set modelName to first Class found, since its the root class
            if (this.getGenerationModel().getModelName() == null) {
                this.getGenerationModel().setModelName(className);
            }
            addClassDef(className, classDef);
        }
        for (Map.Entry<String, String> classEntry : definedClasses.entrySet()) {
            ModelClass modelClass = this.getGenerationModel()
                    .addModelClass(classEntry.getKey());
            retrieveAttributes(classEntry.getValue(), modelClass);
        }
    }

    /**
     * Adds a defined class to the DSLGenerationModel {@link #genModel}. Throws
     * an IllegalArgumentException if the class is defined multiple times.
     * 
     * @param className
     *            unique class name
     * @param classDef
     *            class definition from the language description
     */
    private void addClassDef(final String className, final String classDef) {
        if (isClassDefined(className)) {
            throw new IllegalArgumentException(
                    "Failed to define class of name: " + className + ". "
                            + CLASS_DEFINED_MULTIPLE_TIMES);
        } else {
            this.definedClasses.put(className, classDef);
        }
    }

    /**
     * Checks if a class is defined and saved in the DSLGenerationModel.
     * 
     * @param className name of the class
     * @return true if its defined and found otherwise false is returned
     */
    private boolean isClassDefined(final String className) {
        String capitalClassName = Character.toUpperCase(className.charAt(0))
                + className.substring(1);
        return this.definedClasses.containsKey(capitalClassName);
    }

    /**
     * Retrieves the attributes and their properties from the class definition
     * and creates a ClassAttribute representation in the given modelClass. All
     * created Attributes for the ModelClass are internally added its
     * allAttributes list.
     * 
     * @param classDef
     *            String with class definition
     * @param modelClass
     *            the corresponding ModelClass
     */
    private void retrieveAttributes(final String classDef,
            final ModelClass modelClass) {
        Matcher attrDefMatcher = RegexDefinitions.CLASS_ATTRIBUTES_PATTERN
                .matcher(classDef);
        while (attrDefMatcher.find()) {
            Matcher singleAttrMatcher = RegexDefinitions.ATTRIBUTE_ALL_PATTERN
                    .matcher(attrDefMatcher.group());
            while (singleAttrMatcher.find()) {
                String attrDef = singleAttrMatcher.group();
                String attrName = getNameOfDefinition(attrDef);
                // attributes start with lower case letter
                attrName = Character.toLowerCase(attrName.charAt(0))
                        + attrName.substring(1);
                // check if same name is already in ModelClass
                if (modelClass.getSpefificAttribute(attrName) != null) {
                    throw new IllegalArgumentException(
                            SAME_ATTRIBUTE_MULTIPLE_TIMES + " Attribute '"
                                    + attrName + "' in class "
                                    + modelClass.getClassName());
                }
                String attrType = getTypeOfDefinition(attrDef);
                DependencyKind kind = getKind(attrDef);
                boolean isRef = false;
                // if type is a class thats defined in the model
                if (isClassDefined(attrType)) {
                    // make sure capital letter is used
                    attrType = Character.toUpperCase(attrType.charAt(0))
                            + attrType.substring(1);
                    isRef = true;
                }
                ClassAttribute currentAttr = new ClassAttribute(attrName,
                        attrType, modelClass);
                currentAttr.setReference(isRef);
                if (kind == DependencyKind.OPTIONAL_ATTRIBUTE) {
                    currentAttr.setOptional(true);
                    if (isRef) {
                        // optional declared in Regex Model only means optional
                        // in the model
                        kind = DependencyKind.ATTRIBUTE;
                    }
                }
                currentAttr.setDependencyKind(kind);
                if (kind.equals(DependencyKind.LIST_OF_ATTRIBUTES)) {
                    currentAttr.setList(true);
                    if (currentAttr.isPrimitive()) {
                        throw new IllegalArgumentException(
                                PRIMITVES_NOT_ALLOWED_FOR_LIST
                                        + " Attribute name: "
                                        + currentAttr.getAttributeName()
                                        + " In class: "
                                        + currentAttr.getClassName());
                    }
                    this.getGenerationModel().setHasList(true);
                    modelClass.setHasList(true);
                }
                if (currentAttr.getDependencyKind() 
                        == DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET) {
                    setOppositeAttribute(currentAttr, attrDef);
                }
            }
        }
    }

    /**
     * Retrieves Opposite definitions and sets the corresponding opposite
     * attribute to the given ClassAttribute.
     * 
     * @param currentAttr
     *            the attribute which needs a opposite reference
     * @param attrDef
     *            String with attribute definition which is defined by
     *            {@link REGEX_ATTRIBUTE}
     */
    private void setOppositeAttribute(final ClassAttribute currentAttr,
            final String attrDef) {
        Matcher oppositeMatcher = RegexDefinitions.OPPOSITE_ATTRIBUTE_PATTERN
                .matcher(attrDef);
        while (oppositeMatcher.find()) {
            String opDef = oppositeMatcher.group();
            String opType = getTypeOfDefinition(opDef);
            opType = Character.toUpperCase(opType.charAt(0))
                    + opType.substring(1);
            String nameOfOpposite = getOppositeNameOfDefinition(opDef);
            nameOfOpposite = Character.toLowerCase(nameOfOpposite.charAt(0))
                    + nameOfOpposite.substring(1);
            // class which is referenced exists
            if (isClassDefined(opType)) {
                ModelClass oppModelClass = getGenerationModel()
                        .getModelClass(opType);
                ClassAttribute oppositeAttribute = oppModelClass
                        .getSpefificAttribute(nameOfOpposite);
                // attribute which is referenced exists
                if (oppositeAttribute == null) {
                    throw new IllegalArgumentException(
                            OPPOSITE_ATTRIBUTE_NOT_DEFINED
                                    + "Given OP Attribute: " + opDef);
                } else if (oppositeAttribute.getAttributeFullName().equals(
                        currentAttr.getAttributeFullName())) {
                    throw new IllegalArgumentException(
                            OPPOSITE_ATTRIBUTE_THE_SAME
                                    + " Given OP Attribute: " + opDef);
                }
                checkForMatchingType(currentAttr, oppositeAttribute);
                // set bidirectional relation
                currentAttr.setOpposite(oppositeAttribute);
                oppositeAttribute.setOpposite(currentAttr);
                oppositeAttribute.setCreatorOfOpposite(true);
            } else {
                throw new IllegalArgumentException(opType + " "
                        + CLASS_NOT_DEFINED + " but referenced by " + opDef
                        + " . An opposite attributes type has "
                        + "to be defined before the attribute itself.");
            }
        }
    }

    /**
     * Checks if the attribute's opposite ClassAttribute has the correct type.
     * throws IllegalArgumentException
     *             if the types do not match.
     * @param attribute
     *            one side of the opposite relation
     * @param oppositeAttribute
     *            the other side of the relation
     */
    private void checkForMatchingType(final ClassAttribute attribute,
            final ClassAttribute oppositeAttribute) {
        if (!attribute.getType().equals(oppositeAttribute.getClassName())
                || !oppositeAttribute.getType()
                        .equals(attribute.getClassName())) {
            throw new IllegalArgumentException(OPPOSITE_DIFFERENT_TYPE
                    + " OP attr '" + oppositeAttribute.getAttributeName() + ":"
                    + oppositeAttribute.getType() + "' referencing attribute '"
                    + attribute.getAttributeName() + ":" + attribute.getType()
                    + "'");
        }
    }

    /**
     * Retrieves the class name of a modeled class from a definition.
     * 
     * @param classDef
     *            the class definition as defined by
     *            {@link RegexDefinitions#CLASS_DEFINITION}
     * @return the class name as a String
     */
    private String retrieveClassName(final String classDef) {
        Matcher classNameMatcher = RegexDefinitions.CLASS_NAME_PATTERN
                .matcher(classDef);
        String className = "";
        if (classNameMatcher.find()) {
            className = getNameOfDefinition(classNameMatcher.group());
        }
        return className;
    }

    /**
     * Returns the matching DependencyKind depending on the Start of the
     * attribute definition.
     * 
     * @param attrDef
     *            the attribute defined in the language description
     * @return the matching AttributeKind or null if none is found
     */
    private DependencyKind getKind(final String attrDef) {
        if (attrDef.startsWith(RegexDefinitions.ATTR_START)) {
            return DependencyKind.ATTRIBUTE;
        } else if (attrDef.startsWith(RegexDefinitions.OPT_START)) {
            return DependencyKind.OPTIONAL_ATTRIBUTE;
        } else if (attrDef.startsWith(RegexDefinitions.LIST_START)) {
            return DependencyKind.LIST_OF_ATTRIBUTES;
        } else if (attrDef.startsWith(RegexDefinitions.OPPOSITE_START)) {
            return DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the defined import Classes from the language description and
     * adds them to the import list of the DSGenerationModel {@link #genModel}.
     */
    private void retrieveImports() {
        if (this.imports == null && this.importMatcher.find()) {
            imports = new ArrayList<>();
            String importsString = this.importMatcher.group().substring(
                    RegexDefinitions.IMPORT_START.length());
            // initialize importParameterMatcher with found imports
            Matcher importParameterMatcher = 
                    RegexDefinitions.IMPORT_PARAMETER_PATTERN
                        .matcher(importsString);
            while (importParameterMatcher.find()) {
                String toImport = importParameterMatcher.group();
                if (!toImport.equals("")) {
                    this.getGenerationModel().addImport(toImport);
                    for (ModelClass modelClass 
                            : this.getGenerationModel().getClasses()) {
                        modelClass.addImport(toImport);
                    }
                }
            }
        }
    }

}
