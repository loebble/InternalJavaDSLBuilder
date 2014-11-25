package de.htwg.javafluentdsl.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwg.javafluentdsl.dslmodel.DependencyKind;
import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * Class for Creating a {@link DSLGenerationModel} from a model description 
 * defined by {@link de.htwg.javafluentdsl.parser.RegexUtil#MODEL_DESCRIPTION model description}
 * 
 * @see {@link IParser}
 */
public final class ParserRegex extends AbstractIParserBasic{

	//Error Messages
	private static final String CLASS_DEFINED_MULTIPLE_TIMES = "The class was defined more than once";
	private static final String SAME_ATTRIBUTE_MULTIPLE_TIMES = "An Attribute can only be declared once in the same class!";
	private static final String CLASS_NOT_DEFINED = "class is not defined!";
	private static final String OPPOSITE_DIFFERENT_TYPE = "The defined opposite attribute and its referencing attribute have different types.";
	private static final String OPPOSITE_ATTRIBUTE_NOT_DEFINED = "Defined opposite attributes other end was not found. ";
	private static final String OPPOSITE_ATTRIBUTE_THE_SAME = "The other end of a opposite attribute can not be the same attribute!";
	private static final String PRIMITVES_NOT_ALLOWED_FOR_LIST = "For a List the type cannot be a primitive one.";
	
	//Matcher used for regular expression matching
	/**
	 * Matches the definition of a class
	 * @see RegexUtil#CLASS_DEFINITION_PATTERN
	 */
	private Matcher classDefinitionMatcher;
	/**
	 * Matches the naming of an attribute or class
	 * @see RegexUtil#NAMING_PATTERN
	 */
	private Matcher namingMatcher;
	/**
	 * Matches the type of an attribute
	 * @see RegexUtil#TYPING_PATTERN
	 */
	private Matcher typingMatcher;
	/**
	 * Matches import whole import expression
	 * @see RegexUtil#IMPORT_PATTERN
	 */
	private Matcher importMatcher;
	/**
	 * Matches a single import inside the RegexUtil#IMPORT_PATTERN
	 * @see RegexUtil#IMPORT_PARAMETER_PATTERN
	 */
	private Matcher importParameterMatcher;
	
	/**
	 * List for temporarily saving the classes that need to be imported.
	 */
	private List<String> imports;
	/**
	 * List for temporarily saving the classes defined in the description.
	 */
	private Map<String,String> definedClasses= new LinkedHashMap<>();
	
	/**
	 * Private Constructor. This class should  be instantiated via 
	 * its {@link #getInstance(String)} method
	 */
	private ParserRegex(){
		super();
	}
	
	/**
	 * Creates a new Instance of this class {@link ParserRegex}.
	 * An ParserRegex need a model description. This description is defined
	 * by the {@link RegexUtil#MODEL_DESCRIPTION}.
	 * All the necessary methods are called in this method so that after it the Creators genModel can be used.
	 * @param modelDescription the String with the whole model Description
	 * @return new Instance of the CreatorRegex class
	 */
	public static ParserRegex getInstance(String modelDescription){
		if(!RegexUtil.isDescriptionMatching(modelDescription)){
			//try to find class description which failed
			Pattern p = Pattern.compile(RegexUtil.CLASS_DEFINITION);
			Matcher m = p.matcher(modelDescription);
			int endOfLastCorrectClass = 0;
			while(m.find())
				endOfLastCorrectClass = m.end();
			String appendedErrorMsg = "";
			if(endOfLastCorrectClass == 0)
				appendedErrorMsg = "\n First class definition has errors";
			else //class definitions are always greater than 10 characters
				appendedErrorMsg = "\n Error in class definition after :'"
						+ modelDescription.substring(endOfLastCorrectClass -10, endOfLastCorrectClass) + "'...";
			throw new IllegalArgumentException(RegexUtil.MODEL_DOESNT_MATCH + appendedErrorMsg);
		}
		ParserRegex parser = new ParserRegex();
		parser.classDefinitionMatcher = RegexUtil.CLASS_DEFINITION_PATTERN.matcher(modelDescription);
		parser.importMatcher = RegexUtil.IMPORT_PATTERN.matcher(modelDescription);
		parser.retrieveDefinedClasses();
		parser.retrieveImports();
		parser.createAttributeOrder();
		return parser;
	}
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}
	
	/**
	 * Retrieves the value (the name) after the naming operator {@link RegexUtil#NAMING_OPERATOR}
	 * @param def the definition defined by {@link RegexUtil#REGEX}
	 * @return
	 */
	private String getNameOfDefinition(String def){
		this.namingMatcher = RegexUtil.NAMING_PATTERN.matcher(def);
		if(this.namingMatcher.find()){
				String naming = namingMatcher.group();
				return naming.substring(RegexUtil.NAMING_OPERATOR.length());
		} 
		else{
			return "";
		}
	}
	
	/**
	 * Retrieves the value (the type) after the typing operator {@link RegexUtil#TYPING_OPERATOR}
	 * @param def the definition defined by {@link RegexUtil#ATTRIBUTE_OR_OPATTRIBUTE}
	 * @return
	 */
	private String getTypeOfDefinition(String def){
		this.typingMatcher = RegexUtil.TYPING_PATTERN.matcher(def);
		if(this.typingMatcher.find()){
				String naming = typingMatcher.group();
				return naming.substring(RegexUtil.TYPING_OPERATOR.length());
		} 
		else{
			return "";
		}
	}
	
	/** Retrieves the name of the opposite attribute. defined afer the opposite operator 
	 * {@link RegexUtil#OPPOSITE_OPERATOR}
	 * @param def 
	 * @return
	 */
	private String getOppositeNameOfDefinition(String def){
		Matcher opMatcher = RegexUtil.OPPOSITE_PATTERN.matcher(def);
		if(opMatcher.find()){
				String naming = opMatcher.group();
				return naming.substring(RegexUtil.OPPOSITE_OPERATOR.length());
		} 
		else{
			return "";
		}
	}
	
	/**
	 * Retrieves the classes and their attributes defined in the language description and
	 * adds them to the {@link DSLGenerationModel}. instance
	 */
	private void retrieveDefinedClasses(){
		while(this.classDefinitionMatcher.find()){
			String classDef = this.classDefinitionMatcher.group();
			String className = retrieveClassName(classDef);
			//a className's first letter should be upper case
			className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
			//set modelName to first Class found, since its the root class
			if(this.genModel.getModelName() == null){
				this.genModel.setModelName(className);
			}
			addClassDef(className,classDef);
		}
		for (Map.Entry<String,String> classEntry: definedClasses.entrySet()) {
			ModelClass modelClass = this.genModel.addModelClass(classEntry.getKey());
			retrieveAttributes(classEntry.getValue(),modelClass);
		}
	}
	
	/**
	 * Adds a defined class to the DSLGenerationModel {@link #genModel}.
	 * Throws an IllegalArgumentException if the class is defined multiple times.
	 * @param className unique class name
	 * @param classDef class definition from the language description
	 */
	private void addClassDef(String className, String classDef){
		if(isClassDefined(className))
			throw new IllegalArgumentException("Failed to define class of name: "+className+ ". "
			+ CLASS_DEFINED_MULTIPLE_TIMES);
		else this.definedClasses.put(className,classDef);
	}
	
	/**
	 * Checks if a class is defined and saved in the DSLGenerationModel
	 * @param className
	 * @return true if its defined and found otherwise false is returned
	 */
	private boolean isClassDefined(String className){
		className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
		if(this.definedClasses.containsKey(className)){
			return true;
		}else
			return false;
	}
	
	/**
	 * Retrieves the attributes and their properties from the class definition 
	 * and creates a ClassAttribute representation in the given modelClass. 
	 * All created Attributes for the ModelClass are internally added its allAttributes list.
	 * @param classDef String with class definition
	 * @param modelClass the corresponding ModelClass 
	 */
	private void retrieveAttributes(String classDef, ModelClass modelClass) {
		Matcher attrDefMatcher = RegexUtil.CLASS_ATTRIBUTES_PATTERN.matcher(classDef);
		while(attrDefMatcher.find()){
			Matcher singleAttrMatcher = RegexUtil.ATTRIBUTE_ALL_PATTERN.matcher(attrDefMatcher.group());
			while(singleAttrMatcher.find()){
				String attrDef = singleAttrMatcher.group();
				String attrName = getNameOfDefinition(attrDef);
				attrName = Character.toLowerCase(attrName.charAt(0)) + attrName.substring(1);
				String attrType = getTypeOfDefinition(attrDef);
				DependencyKind kind = getKind(attrDef);
				// if class is defined make sure capital letter is used
				boolean isRef = false;
				if(isClassDefined(attrType)){
					attrType = Character.toUpperCase(attrType.charAt(0)) + attrType.substring(1);
					isRef = true;
				}
				//check if same name is already in ModelClass
				if(modelClass.getSpefificAttribute(attrName) != null)
					throw new IllegalArgumentException(SAME_ATTRIBUTE_MULTIPLE_TIMES
							+ " Attribute '"+attrName +"' in class " + modelClass.getClassName());
				ClassAttribute currentAttr = new ClassAttribute(attrName,attrType,modelClass);
				currentAttr.setReference(isRef);
				if(kind == DependencyKind.OPTIONAL_ATTRIBUTE){
					currentAttr.setOptional(true);
					if(isRef){
					//optional declared in Regex Model only means optional in the model
						kind = DependencyKind.ATTRIBUTE;
					}
				}
				currentAttr.setDependencyKind(kind);
				if(kind.equals(DependencyKind.LIST_OF_ATTRIBUTES)){
					currentAttr.setList(true);
					if(currentAttr.isPrimitive())
						//TODO If Time Left implement Auto Boxing etc in Template
						throw new IllegalArgumentException(PRIMITVES_NOT_ALLOWED_FOR_LIST
								+ " Attribute name: "+currentAttr.getAttributeName()
								+ " In class: "+currentAttr.getClassName());
					this.genModel.setHasList(true);
					modelClass.setHasList(true);
				}
				if(currentAttr.getDependencyKind() == DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET){
					setOppositeAttribute(currentAttr,attrDef);
				}
			}
		}
	}
	
	/**
	 * Retrieves Opposite definitions and sets the corresponding opposite attribute to the given ClassAttribute.
	 * @param currentAttr the attribute which needs a opposite reference
	 * @param attrDef String with attribute definition which is defined by {@link REGEX_ATTRIBUTE}
	 */
	private void setOppositeAttribute(ClassAttribute currentAttr, String attrDef) {
		Matcher oppositeMatcher = RegexUtil.OPPOSITE_ATTRIBUTE_PATTERN.matcher(attrDef);
		while(oppositeMatcher.find()){
			String opDef = oppositeMatcher.group();
			String name = getNameOfDefinition(opDef);
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
			String opType = getTypeOfDefinition(opDef);
			opType = Character.toUpperCase(opType.charAt(0)) + opType.substring(1);
			String nameOfOpposite = getOppositeNameOfDefinition(opDef);
			nameOfOpposite = Character.toLowerCase(nameOfOpposite.charAt(0)) + nameOfOpposite.substring(1);
			if(isClassDefined(opType)){
				ModelClass oppModelClass = genModel.getModelClass(opType);
				ClassAttribute oppositeAttribute = oppModelClass.getSpefificAttribute(nameOfOpposite);
				if(oppositeAttribute == null)
					throw new IllegalArgumentException(OPPOSITE_ATTRIBUTE_NOT_DEFINED 
							+ "Given OP Attribute: "+opDef);
				else if(oppositeAttribute.getAttributeFullName().equals
						(currentAttr.getAttributeFullName()))
					throw new IllegalArgumentException(OPPOSITE_ATTRIBUTE_THE_SAME 
							+ " Given OP Attribute: "+opDef);
				//set bidirectional relation
				currentAttr.setOpposite(oppositeAttribute);
				oppositeAttribute.setOpposite(currentAttr);
				oppositeAttribute.setCreatorOfOpposite(true);
				checkForMatchingType(currentAttr, oppositeAttribute);
			}
			else 
				throw new IllegalArgumentException(opType + " " +CLASS_NOT_DEFINED  
						+ " but referenced by " + opDef +" . An opposite attributes type has "
								+ "to be defined before the attribute itself.");
		}
	}

	/**
	 * Checks if the attribute's opposite ClassAttribute has the correct type.
	 * @param attribute one side of the opposite relation
	 * @param oppositeAttribute the other side of the relation
	 * @throws IllegalArgumentException if the types do not match.
	 */
	private void checkForMatchingType(ClassAttribute attribute,
			ClassAttribute oppositeAttribute) {
		if(!attribute.getType().equals(oppositeAttribute.getClassName()) ||
				   !oppositeAttribute.getType().equals(attribute.getClassName()))
					throw new IllegalArgumentException(OPPOSITE_DIFFERENT_TYPE + " OP attr '" 
							+ oppositeAttribute.getAttributeName()+":"+oppositeAttribute.getType() 
							+ "' referencing attribute '" +attribute.getAttributeName() +":"+attribute.getType()+"'");
	}

	/**
	 * Retrieves the class name of a modeled class from a definition
	 * @param classDef the class definition as defined by {@link RegexUtil#CLASS_DEFINITION}
	 * @return the class name as a String
	 */
	private String retrieveClassName(String classDef) {
		Matcher classNameMatcher = RegexUtil.CLASS_NAME_PATTERN.matcher(classDef);
		String className = "";
		if(classNameMatcher.find())
			className = getNameOfDefinition(classNameMatcher.group());
		return className;
	}
	
	/**
	 * Returns the matching DependencyKind depending on the Start of the attribute definition
	 * @param attrDef the attribute defined in the language description
	 * @return the matching AttributeKind or null if none is found
	 */
	private DependencyKind getKind(String attrDef){
		if(attrDef.startsWith(RegexUtil.ATTR_START))
			return DependencyKind.ATTRIBUTE;
		else if (attrDef.startsWith(RegexUtil.OPT_START))
			return DependencyKind.OPTIONAL_ATTRIBUTE;
		else if (attrDef.startsWith(RegexUtil.LIST_START))
			return DependencyKind.LIST_OF_ATTRIBUTES;
		else if (attrDef.startsWith(RegexUtil.OPPOSITE_START))
			return DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET;
		else return null;
	}
	
	
	/**
	 * Retrieves the defined import Classes from the language description
	 * and adds them to the import list of the DSGenerationModel {@link #genModel}
	 */
	private void retrieveImports(){
		if(this.imports == null && this.importMatcher.find()){
			imports = new ArrayList<>();
			String importsString = importMatcher.group().substring(RegexUtil.IMPORT_START.length());
			//initialize importParameterMatcher with found imports
			this.importParameterMatcher = RegexUtil.IMPORT_PARAMETER_PATTERN.matcher(importsString);
			while(this.importParameterMatcher.find()){
				String toImport = importParameterMatcher.group();
				if(!toImport.equals("")){
					this.genModel.addImport(toImport);
					for (ModelClass modelClass : this.genModel.getClasses()) {
						modelClass.addImport(toImport);
					}
				}
			}
		}
	}
	
}
