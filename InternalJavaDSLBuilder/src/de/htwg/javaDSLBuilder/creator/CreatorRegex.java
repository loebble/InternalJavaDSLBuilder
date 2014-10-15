package de.htwg.javaDSLBuilder.creator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.htwg.javaDSLBuilder.dslmodel.AttributeKind;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel.ClassAttribute;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel.ModelClass;

/**-
 * Defines DSL Meta Model through regular expression 
 * @author stboeckl
 * 
 *
 */
public class CreatorRegex implements ICreator{
	@Deprecated
	private static final String REGEX_DSLNAME = "(?i)modelName=\\w+";
	@Deprecated
	public static final String MODEL_NAME = "modelName=\\w+";
	@Deprecated
	private static final Pattern MODEL_NAME_PATTERN = Pattern.compile(MODEL_NAME, Pattern.CASE_INSENSITIVE);
	
	/**
	 * Regular expression for the attributes in a class definition
	 */
	private static final String REGEX_CLASS_ATTR_DEFINITION = "\\{(\\.A|\\.OA|\\.LA)=\\w+:\\w+(\\s?\\,\\s?(\\.A|\\.OA|\\.LA)=\\w+:\\w+)*(\\s?\\,\\s?\\.OP=\\w+:\\w+->\\w+)*\\}";
	/**
	 * Regular expression for a single attribute without opposite Attribute
	 */
	private static final String REGEX_ATTRIBUTE = "((.A|.OA|.LA)=\\w+:\\w+)";
	/**
	 * Regular expression for a single attribute with opposite Attribute
	 */
	private static final String FOLLOWING_REGEX_ATTRIBUTE = "(\\s?\\,\\s?(.A|.OA|.LA)=\\w+:\\w+)";
	private static final String REGEX_ALL_ATTRIBUTE = "("+REGEX_ATTRIBUTE+"|(\\.OP=\\w+:\\w+->\\w+))";
	/**
	 * Regular expression for the imports
	 */
	public static final String REGEX_IMPORT = "(\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\})";
	
	/**
	 * Regular Expression for the complete model description 
	 */
	
	/**
	 * Regular expression for a complete Class definition
	 */
	private static final String REGEX_CLASS_DEFINITION = "(\\.class=\\w+\\{"+REGEX_ATTRIBUTE+FOLLOWING_REGEX_ATTRIBUTE+"*(\\s?\\,\\s?\\.OP=\\w+:\\w+->\\w+)*\\})";
//	private static final String MAND_ATTR_FIRTS_REGEX_CLASS_DEFINITION = "(\\.class=\\w+\\{(.A|.LA)=\\w+:\\w+(\\s?\\,\\s?(\\.A|\\.OA|\\.LA|\\.OLA)=\\w+:\\w+)*(\\s?\\,\\s?\\.OP=\\w+:\\w+->\\w+)*\\})"; //Must start with mandatory Attribute
	
	public static final String REGEX_PATTERN= 
			REGEX_CLASS_DEFINITION + "+"
			+ REGEX_IMPORT + "?"
			;
	//Parts of regular expressions
	public static final String CLASS_NAME = ".class=\\w+";
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
	public static final String OPPOSITE_ATTRIBUTE = "(\\.OP=\\w+:\\w+"+OPPOSITE_OPERATOR+"\\w+)";
	public static final String IMPORT_START= ".imp=";
	public static final String IMPORT_PARAMETER = "(\\w+(\\.)?)+";
	//Patterns
	private static final Pattern CLASS_DEFINITION_PATTERN = Pattern.compile(REGEX_CLASS_DEFINITION, Pattern.CASE_INSENSITIVE);
	private static final Pattern CLASS_NAME_PATTERN= Pattern.compile(CLASS_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern CLASS_ATTRIBUTES_PATTERN= Pattern.compile(REGEX_CLASS_ATTR_DEFINITION, Pattern.CASE_INSENSITIVE);
	private static final Pattern ATTRIBUTE_ALL_PATTERN = Pattern.compile(REGEX_ALL_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
	private static final Pattern NAMING_PATTERN = Pattern.compile(NAMING, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PATTERN = Pattern.compile(REGEX_IMPORT, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPPOSITE_ATTRIBUTE_PATTERN = Pattern.compile(OPPOSITE_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
	private static final Pattern TYPING_PATTERN= Pattern.compile(TYPING, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPPOSITE_PATTERN= Pattern.compile(OPPOSITE, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PARAMETER_PATTERN = Pattern.compile(IMPORT_PARAMETER, Pattern.CASE_INSENSITIVE);
	
	//Error Messages
	private static final String NO_MATCH = "Given String does not match BuildPatternCreator Regex Pattern: \n" +REGEX_PATTERN;
	private static final String CLASS_DEFINED_MULTIPLE_TIMES = "The class was defined more than once";
	private static final String WRONG_DECLARATION = "Attribute not declared correctly";
	private static final String SAME_ATTRIBUTE_MULTIPLE_TIMES = "An Attribute can only be declared once in the same class!";
	private static final String CLASS_NOT_DEFINED = "class is not defined!";
	private static final String OPPOSITE_DIFFERENT_TYPE = "The defined opposite attribute and its referencing attribute have different types.";
	private static final String OPPOSITE_ATTRIBUTE_NOT_DEFINED = "Declared opposite attribute is not defined.";
	
	//Matcher
	@Deprecated
	private Matcher modelNameMatcher;
	private Matcher classDefinitionMatcher;
	private Matcher namingMatcher;
	private Matcher typingMatcher;
	private Matcher buildMatcher;
	private Matcher importMatcher;
	private Matcher importParameterMatcher;
	
	private DSLGenerationModel genModel;
	private String languageDescr;
	private String dslName;
	private List<String> imports;
	private String firstAttribute;
	private Map<String,String> lastAttribute;
	private Map<String,String> definedClasses= new LinkedHashMap<>();
	
	private CreatorRegex(){}
	
	public static CreatorRegex getInstance(String languageDescr){
		if(!languageDescr.matches(REGEX_PATTERN)){ //TODO better error-handling and -printing
			Pattern p = Pattern.compile(REGEX_CLASS_DEFINITION);
			Matcher m = p.matcher(languageDescr);
			int endOfLastCorrectClass = 0;
			while(m.find())
				endOfLastCorrectClass = m.end();
			String appendedErrorMsg = "";
			if(endOfLastCorrectClass == 0)
				appendedErrorMsg = "\n First class definition has errors";
			else
				appendedErrorMsg = "\n Error in class definition: " + languageDescr.substring(endOfLastCorrectClass, endOfLastCorrectClass + 15) + "...";
			throw new IllegalArgumentException(NO_MATCH + appendedErrorMsg);
		}
		CreatorRegex creator = new CreatorRegex();
		creator.genModel = new DSLGenerationModel();
		creator.languageDescr = languageDescr;
		creator.classDefinitionMatcher = CLASS_DEFINITION_PATTERN.matcher(languageDescr);
		creator.importMatcher = IMPORT_PATTERN.matcher(languageDescr);
		creator.retrieveDefinedClasses();
		creator.retrieveImports();
		creator.setAttributeOrder();
		return creator;
	}
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}
	
	@Deprecated
	public String retrieveDslName(){
		if(this.dslName == null && this.modelNameMatcher.find()){
			String modelNameDef = modelNameMatcher.group();
			this.genModel.setModelName(getNameOfDefinition(modelNameDef));
		}
		return this.genModel.getModelName();
	}
	
	private String getNameOfDefinition(String def){
		this.namingMatcher = NAMING_PATTERN.matcher(def);
		if(this.namingMatcher.find()){
				String naming = namingMatcher.group();
				return naming.substring(NAMING_OPERATOR.length());
		} 
		else{
			return "";
		}
	}
	
	private String getTypeOfDefinition(String def){
		this.typingMatcher = TYPING_PATTERN.matcher(def);
		if(this.typingMatcher.find()){
				String naming = typingMatcher.group();
				return naming.substring(TYPING_OPERATOR.length());
		} 
		else{
			return "";
		}
	}
	
	private String getOppositeNameOfDefinition(String def){
		Matcher opMatcher = OPPOSITE_PATTERN.matcher(def);
		if(opMatcher.find()){
				String naming = opMatcher.group();
				return naming.substring(OPPOSITE_OPERATOR.length());
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
			if(this.genModel.getModelName() == null)
				this.genModel.setModelName(className);
			addClassDef(className,classDef);
		}
		for (Map.Entry<String,String> classEntry: definedClasses.entrySet()) {
			ModelClass modelClass = this.genModel.addModelClass(classEntry.getKey());
			retrieveAttributes(classEntry.getValue(),modelClass);//TODO exception if no Attribute is defined (cannot happen, because regex?!)
		}
	}
	
	private void addClassDef(String className, String classDef){
		if(isClassDefined(className))
			throw new IllegalArgumentException("Failed to define class of name: "+className+ ". "
			+ CLASS_DEFINED_MULTIPLE_TIMES);
		else this.definedClasses.put(className,classDef);
	}
	
	private boolean isClassDefined(String className){
		className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
		if(this.definedClasses.containsKey(className)){
			return true;
		}else
			return false;
	}
	
	/**
	 * Retives the attributes and their properties from the class definition and adds them to to given modelClass.
	 * @param classDef
	 * @param modelClass
	 * @return
	 */
	private List<ClassAttribute> retrieveAttributes(String classDef, ModelClass modelClass) {
		List<ClassAttribute> attributes = new ArrayList<ClassAttribute>();
		Matcher attrDefMatcher = CLASS_ATTRIBUTES_PATTERN.matcher(classDef);
		while(attrDefMatcher.find()){
			Matcher singleAttrMatcher = ATTRIBUTE_ALL_PATTERN.matcher(attrDefMatcher.group());
			while(singleAttrMatcher.find()){
				String attrDef = singleAttrMatcher.group();
				String attrName = getNameOfDefinition(attrDef);
				attrName = Character.toLowerCase(attrName.charAt(0)) + attrName.substring(1);
				String attrType = getTypeOfDefinition(attrDef);
				AttributeKind kind = getKind(attrDef);
				boolean optional = false;
				if(!isClassDefined(attrType)){
					if(kind == AttributeKind.OPTIONAL_ATTRIBUTE)
						optional = true;
				}else // if class is defined make sure uppcase is used
					attrType = Character.toUpperCase(attrType.charAt(0)) + attrType.substring(1); 
				ClassAttribute currentAttr = genModel.new ClassAttribute(attrName,attrType,modelClass.getClassName());
				currentAttr.setAttributeKind(kind);
				if(kind.equals(AttributeKind.LIST_OF_ATTRIBUTES)){
					currentAttr.setList(true);
					this.genModel.setHasList(true);
				}
				if(modelClass.getSpefificAttribute(attrName) != null)
					throw new IllegalArgumentException(SAME_ATTRIBUTE_MULTIPLE_TIMES
							+ " attribute "+attrName +" in class" + modelClass.getClassName());
				currentAttr.setAttributeName(attrName);
				currentAttr.setAttributeFullName(modelClass.getClassName()+attrName);
				
				attributes.add(currentAttr);
				if(currentAttr.getAttributeKind() == AttributeKind.OPPOSITE_ATTRIBUTE)
					setOppositeAttribute(currentAttr,attrDef);
				modelClass.addAttribute(currentAttr);
			}
		}
		return attributes;
	}
	
	/**
	 * Retrieves Opposite definitions and sets the corresponding opposite attribute to the given one.
	 * @param currentAttr the attribute which needs a opposite reference
	 * @param attrDef String with attribute definitition defined by {@link REGEX_ATTRIBUTE}
	 */
	private void setOppositeAttribute(ClassAttribute currentAttr, String attrDef) {
		Matcher oppositeMatcher = OPPOSITE_ATTRIBUTE_PATTERN.matcher(attrDef);
		while(oppositeMatcher.find()){
			String opDef = oppositeMatcher.group();
			String name = getNameOfDefinition(opDef);
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
			String opType = getTypeOfDefinition(opDef);
			opType = Character.toUpperCase(opType.charAt(0)) + opType.substring(1);
			String nameOfOpposite = getOppositeNameOfDefinition(opDef);
			nameOfOpposite = Character.toLowerCase(nameOfOpposite.charAt(0)) + nameOfOpposite.substring(1);
			if(isClassDefined(opType)){
				ModelClass mc = genModel.getClass(opType);
				ClassAttribute oppositeAttribute = mc.getSpefificAttribute(nameOfOpposite);
				checkForMatchingType(currentAttr, oppositeAttribute);
				mc.addReferencedByOpposite(currentAttr); //adds nested attribute reference to enclosing class
				currentAttr.setReferencedByAttribute(true);
				if(oppositeAttribute != null){
					currentAttr.setOpposite(oppositeAttribute);
				}
				else
					throw new IllegalArgumentException(OPPOSITE_ATTRIBUTE_NOT_DEFINED + "for given "+name+":"+opType+". Class attribute "+ opType +"."+ nameOfOpposite+" not found.");
			}
			else 
				throw new IllegalArgumentException(opType + " " +CLASS_NOT_DEFINED  + " referenced by " + opDef);
		}
	}

	private void checkForMatchingType(ClassAttribute currentAttr,
			ClassAttribute oppositeAttribute) {
		if(!currentAttr.getType().equals(oppositeAttribute.getClassName()) ||
				   !oppositeAttribute.getType().equals(currentAttr.getClassName()))
					throw new IllegalArgumentException(OPPOSITE_DIFFERENT_TYPE + " OP attr '" + currentAttr.getAttributeName()+":"+currentAttr.getType() 
							+ "' referencing attribute '" +oppositeAttribute.getAttributeName() +":"+oppositeAttribute.getType()+"'");
	}

	private void setAttributeOrder() {
		ModelClass firstClass = null;
		for (Map.Entry<String,DSLGenerationModel.ModelClass> classEntry : this.genModel.getClasses().entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			if(firstClass == null) //Map is LinkedHasMap which has Order saved
				firstClass = modelClass; 
			List<ClassAttribute> optionalAttrs = setAttributeOrderInClass(modelClass);
			removeOptionalAttributes(optionalAttrs, modelClass);
		}
		
	}
	
	/**
	 * Sets the attribute order in a ModelClass
	 * @param modelClass The ModelClass object to order attributes in
	 * @return a List<ClassAttribute> with the optionalAttributes
	 */
	private List<ClassAttribute> setAttributeOrderInClass(ModelClass modelClass) { //TODO refactor with attributeKind SIMPLE:OPTIONAL_ATTRIBUTE, to reduce complexity
		List<ClassAttribute> firstOptAttr= new ArrayList<>();
		List<ClassAttribute> simpleOptionalAttrs = new ArrayList<>();
		ClassAttribute previousRequiredAttr = null;
		for (ClassAttribute currentAtt : modelClass.getAttributes()) {
			if(isClassDefined(currentAtt.getType())){ //If current Attribute is a Reference to a defined Class set reference
				currentAtt.setReference(true);
			}
			if(currentAtt.getAttributeKind() == AttributeKind.ATTRIBUTE || // a mandatory attribute
			currentAtt.getAttributeKind() == AttributeKind.LIST_OF_ATTRIBUTES){
				if(previousRequiredAttr==null){
					previousRequiredAttr = currentAtt;
					// optional attributes before first mandatory then add them to the first mandatory attribute(scope)
					currentAtt.setNextOptionalAttributes(firstOptAttr);
				}
				else if(previousRequiredAttr!=null){
					previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
					previousRequiredAttr = currentAtt;
				}
			}else if(currentAtt.getAttributeKind() == AttributeKind.OPTIONAL_ATTRIBUTE){ // an optional attribute
				currentAtt.setOptional(true);
				if(previousRequiredAttr!=null){
					if(currentAtt.isReference()){
						previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
						previousRequiredAttr = currentAtt;
					}else{ // type is not a modeled Class e.g. a simple types
						previousRequiredAttr.addNextOptionalAttribute(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}else{
					if(currentAtt.isReference()){
						previousRequiredAttr = currentAtt;
						currentAtt.setNextOptionalAttributes(firstOptAttr);
					}else{
						firstOptAttr.add(currentAtt);
						simpleOptionalAttrs.add(currentAtt);
					}
				}
				
			}
		}
		//Special Case if no required Attribute in Class
		if(previousRequiredAttr == null){
			boolean simpleOptionalsOnly = true;
			for (ClassAttribute attr : modelClass.getOptionalAttributes()) {
				if(attr.isReference())
					simpleOptionalsOnly = false;
			}
			modelClass.setSimpleOptionalsOnly(simpleOptionalsOnly);
			return simpleOptionalAttrs;
		}
		else{
			previousRequiredAttr.setLastAttribute(true);
			return simpleOptionalAttrs;
		}
	}

	
	private void removeOptionalAttributes(List<ClassAttribute> optionalAttrs, ModelClass modelClass) {
		for (ClassAttribute classAttribute : optionalAttrs) {
			modelClass.getAttributes().remove(classAttribute);
		}
		
	}

	private String retrieveClassName(String classDef) {
		Matcher classNameMatcher = CLASS_NAME_PATTERN.matcher(classDef);
		String className = "";
		if(classNameMatcher.find())
			className = getNameOfDefinition(classNameMatcher.group());
		return className;
	}
	
	private AttributeKind getKind(String attrDef){
		if(attrDef.startsWith(ATTR_START))
			return AttributeKind.ATTRIBUTE;
		else if (attrDef.startsWith(OPT_START))
			return AttributeKind.OPTIONAL_ATTRIBUTE;
		else if (attrDef.startsWith(LIST_START))
			return AttributeKind.LIST_OF_ATTRIBUTES;
		else if (attrDef.startsWith(OPPOSITE_START))
			return AttributeKind.OPPOSITE_ATTRIBUTE;
		else return null;
	}

	public Map<String, String> getLastAttribute() {
		return lastAttribute;
	}
	
	
	public String getFirstAttribute() {
		return firstAttribute;
	}
	
	
	public void retrieveImports(){
		if(this.imports == null && this.importMatcher.find()){
			imports = new ArrayList<>();
			String importsString = importMatcher.group().substring(IMPORT_START.length());
			//initialize importParameterMatcher with found imports
			this.importParameterMatcher = IMPORT_PARAMETER_PATTERN.matcher(importsString);
			while(this.importParameterMatcher.find()){
				String toImport = importParameterMatcher.group();
				if(!toImport.equals(""))
					this.genModel.addImports(toImport);
			}
		}
	}

}
