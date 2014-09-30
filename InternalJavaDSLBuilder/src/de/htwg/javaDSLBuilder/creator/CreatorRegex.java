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
	
	/* Old one
	 	REGEX_PATTERN= "modelName=\\w+"
			+ "((\\.A=\\w+:\\w+)|(\\.A=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.OA=\\w+:\\w+)|(\\.OA=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.LA=\\w+:\\w+)|(\\.LA=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.OLA=\\w+:\\w+)|(\\.OLA=\\w+:\\w+((.A|.OA|.LA|.OLA)=\\{\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})))*"
			+ "(\\.class=\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})*"
			+ "(\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\})*"
			+ "(\\.build=\\w+)*";
	 */
	
	private static final String REGEX_DSLNAME = "(?i)modelName=\\w+";
	private static final String REGEX_ATTRIBUTES = "(\\.A=\\w+:\\w+)";
	private static final String REGEX_OPT_ATTRIBUTES = "(\\.OA=\\w+:\\w+)";
	private static final String REGEX_LIST_OF_ATTRIBUTES= "(\\.LA=\\w+:\\w+)";
	private static final String REGEX_OPT_LIST_OF_ATTRIBUTES= "(\\.OLA=\\w+:\\w+)";
	private static final String REGEX_CLASS_DEFINITION = "(\\.class=\\w+\\{(.A|.LA)=\\w+:\\w+(\\s?\\,\\s?(\\.A|\\.OA|\\.LA|\\.OLA)=\\w+:\\w+)*(\\s?\\,\\s?\\.OP=\\w+:\\w+->\\w+)*\\})"; //Must start with mandatory Attribute
	private static final String REGEX_CLASS_ATTR_DEFINITION = "\\{(\\.A|\\.OA|\\.LA|\\.OLA)=\\w+:\\w+(\\s?\\,\\s?(\\.A|\\.OA|\\.LA|\\.OLA)=\\w+:\\w+)*(\\s?\\,\\s?\\.OP=\\w+:\\w+->\\w+)*\\}";
	private static final String REGEX_ATTRIBUTE = "((.A|.OA|.LA|.OLA)=\\w+:\\w+)|(\\.OP=\\w+:\\w+->\\w+)";
	public static final String REGEX_IMPORT = "(\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\})";
	
	public static final String REGEX_PATTERN= 
			REGEX_DSLNAME
//			+ REGEX_ATTRIBUTES +"*"
//			+ REGEX_OPT_ATTRIBUTES +"*"
//			+ REGEX_LIST_OF_ATTRIBUTES +"*"
//			+ REGEX_OPT_LIST_OF_ATTRIBUTES +"*"
			+ REGEX_CLASS_DEFINITION +"+"
			+ REGEX_IMPORT + "?"
			;
	
	public static final String MODEL_NAME = "modelName=\\w+";
	public static final String CLASS_NAME = ".class=\\w+";
	public static final String ATTR_START = ".A";
	public static final String OPT_START = ".OA";
	public static final String LIST_START = ".LA";
	public static final String OPT_LIST_START = ".OLA";
	public static final String OPPOSITE_START = ".OP";
	public static final String NEXT_SCOPES = "\\{\\w+(\\s?\\,\\s?\\w+)*\\}";
	public static final String SINGLE_SCOPE = "\\w+";
	public static final String EP_SCOPE = "\\{\\w+";
	public static final String EP_NEXT = "=\\w+";
	public static final String ATTRIBUTE_NAME = "=\\w+";
	public static final String NAMING = "=\\w+";
	public static final String TYPING = ":\\w+";
	public static final String OPPOSITE = "->\\w+";
	
	public static final String BUILD = "\\.build=\\w+";
	public static final String IMPORT_PARAMETER = "(\\w+(\\.)?)+";
	
	public static final String MANDATORY_ATTRIBUTES = "\\.A=\\w+:\\w+";
	public static final String OPTIONAL_ATTRIBUTES = "\\.OA=\\w+:\\w+";
	public static final String OPPOSITE_ATTRIBUTE = "(\\.OP=\\w+:\\w+->\\w+)";
//TODO	public static final String LIST_MANDATORY_ATTRIBUTES;
//TODO	public static final String LIST_OPTIONAL_ATTRIBUTES;
	
	private static final Pattern MODEL_NAME_PATTERN = Pattern.compile(MODEL_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern CLASS_DEFINITION_PATTERN = Pattern.compile(REGEX_CLASS_DEFINITION, Pattern.CASE_INSENSITIVE);
	private static final Pattern CLASS_NAME_PATTERN= Pattern.compile(CLASS_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern CLASS_ATTRIBUTES_PATTERN= Pattern.compile(REGEX_CLASS_ATTR_DEFINITION, Pattern.CASE_INSENSITIVE);
	private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(REGEX_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
	private static final Pattern NAMING_PATTERN = Pattern.compile(NAMING, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PATTERN = Pattern.compile(REGEX_IMPORT, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPPOSITE_ATTRIBUTE_PATTERN = Pattern.compile(OPPOSITE_ATTRIBUTE, Pattern.CASE_INSENSITIVE);
	//old ones
	private static final Pattern ATTRIBUTE_NAME_PATTERN= Pattern.compile(ATTRIBUTE_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern MANDATORY_ATTRIBUTES_PATTERN= Pattern.compile(MANDATORY_ATTRIBUTES, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPTIONAL_ATTRIBUTES_PATTERN= Pattern.compile(OPTIONAL_ATTRIBUTES, Pattern.CASE_INSENSITIVE);
	private static final Pattern TYPING_PATTERN= Pattern.compile(TYPING, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPPOSITE_PATTERN= Pattern.compile(OPPOSITE, Pattern.CASE_INSENSITIVE);
	private static final Pattern NEXT_SCOPES_PATTERN= Pattern.compile(NEXT_SCOPES, Pattern.CASE_INSENSITIVE);
	private static final Pattern SINGLE_SCOPE_PATTERN= Pattern.compile(SINGLE_SCOPE, Pattern.CASE_INSENSITIVE);
	
	private static final Pattern EP_SCOPE_PATTERN = Pattern.compile(EP_SCOPE, Pattern.CASE_INSENSITIVE);
	private static final Pattern EP_NEXT_PATTERN = Pattern.compile(EP_NEXT, Pattern.CASE_INSENSITIVE);
	private static final Pattern BUILD_PATTERN = Pattern.compile(BUILD, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PARAMETER_PATTERN = Pattern.compile(IMPORT_PARAMETER, Pattern.CASE_INSENSITIVE);
	
	//Error Messages
	private static final String NO_MATCH = "Given String does not match BuildPatternCreator Regex Pattern: \n" +REGEX_PATTERN;
	private static final String CLASS_DEFINED_MULTIPLE_TIMES = "The class was defined more than once";
	private static final String WRONG_DECLARATION = "Attribute not declared correctly";
	private static final String SAME_ATTRIBUTE_MULTIPLE_TIMES = "An Attribute can only be declared once in the same class!";
	private static final String CLASS_NOT_DEFINED = "Class referenced is not defined!";
	private static final String OPPOSITE_ATTRIBUTE_NOT_DEFINED = "Declared opposite attribute is not defined.";
	
	private Matcher classDefinitionMatcher;
	private Matcher modelNameMatcher;
	private Matcher namingMatcher;
	private Matcher typingMatcher;
	private Matcher epMatcher;
	private Matcher nextScopesMatcher;
	private Matcher buildMatcher;
	private Matcher importMatcher;
	private Matcher importParameterMatcher;
	
	private DSLGenerationModel genModel;
	private String languageDescr;
	private String dslName;
	private String entryPointMethod;
	public List<String> attributeDeclarations;
	private String buildMethodName;
	private List<String> imports;
	private String firstAttribute;
	private Map<String,String> lastAttribute;
	private Map<String,String> definedClasses= new LinkedHashMap<>();
	
	private CreatorRegex(){}
	
	public static CreatorRegex getInstance(String languageDescr){
		if(!languageDescr.matches(REGEX_PATTERN)){
			throw new IllegalArgumentException(NO_MATCH);
		}else System.out.println("Correct Description");
		CreatorRegex creator = new CreatorRegex();
		creator.genModel = new DSLGenerationModel();
		creator.languageDescr = languageDescr;
		creator.modelNameMatcher = MODEL_NAME_PATTERN.matcher(languageDescr);
		creator.classDefinitionMatcher = CLASS_DEFINITION_PATTERN.matcher(languageDescr);
		creator.importMatcher = IMPORT_PATTERN.matcher(languageDescr);
		creator.retrieveDslName();
		creator.retrieveDefinedClasses();
		creator.retireveImports();
		creator.setAttributeOrder();
		return creator;
	}
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}
	
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
				return naming.substring(1); //without naming operator "="
		} 
		else{
			return "";
		}
	}
	
	private String getTypeOfDefinition(String def){
		this.typingMatcher = TYPING_PATTERN.matcher(def);
		if(this.typingMatcher.find()){
				String naming = typingMatcher.group();
				return naming.substring(1); //without typing operator ":"
		} 
		else{
			return "";
		}
	}
	
	private String getOppositeNameOfDefinition(String def){
		Matcher opMatcher = OPPOSITE_PATTERN.matcher(def);
		if(opMatcher.find()){
				String naming = opMatcher.group();
				return naming.substring(2); //without poiting operator "->"
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
			Matcher singleAttrMatcher = ATTRIBUTE_PATTERN.matcher(attrDefMatcher.group());
			while(singleAttrMatcher.find()){
				ClassAttribute currentAttr = genModel.new ClassAttribute();
				String attrDef = singleAttrMatcher.group();
				AttributeKind kind = getKind(attrDef);
				currentAttr.setAttributeKind(kind);
				String attrName = getNameOfDefinition(attrDef);
				currentAttr.setAttributeName(attrName);
				currentAttr.setAttributeFullName(modelClass.getClassName()+"."+attrName);
				String attrType = getTypeOfDefinition(attrDef);
				currentAttr.setType(attrType);
				attributes.add(currentAttr);
				if(!isClassDefined(attrType)){
					if(kind == AttributeKind.OPTIONAL_ATTRIBUTE || kind == AttributeKind.OPTIONAL_LIST_OF_ATTRIBUTES)
						modelClass.addOptionalAttribute(currentAttr);
				}
				if(currentAttr.getAttributeKind() == AttributeKind.OPPOSITE_ATTRIBUTE)
					setOppositeAttribute(currentAttr,classDef);
				modelClass.addAttribute(currentAttr);
			}
		}
		return attributes;
	}
	
	/**
	 * Retrieves Opposite definitions and sets the corresponding opposite attribute to the given one.
	 * @param currentAttr
	 * @param classDef
	 */
	private void setOppositeAttribute(ClassAttribute currentAttr, String classDef) {
		Matcher oppositeMatcher = OPPOSITE_ATTRIBUTE_PATTERN.matcher(classDef);
		while(oppositeMatcher.find()){
			String opDef = oppositeMatcher.group();
			String name = getNameOfDefinition(opDef);
			String opType = getTypeOfDefinition(opDef);
			String nameOfOpposite = getOppositeNameOfDefinition(opDef);
			if(isClassDefined(opType)){
				ModelClass mc = genModel.getClass(opType);
				ClassAttribute oppositeAttribute = mc.getSpefificAttribute(nameOfOpposite);
				mc.addReferencedByOpposite(currentAttr); //adds nested attribute reference to enclosing class
				currentAttr.setReferencedByAttribute(true);
				if(oppositeAttribute != null){
					currentAttr.setOpposite(oppositeAttribute);
				}
				else
					throw new IllegalArgumentException(OPPOSITE_ATTRIBUTE_NOT_DEFINED + "Opposite attribute:"+name+":"+opType+". Class attribute "+ opType +"->"+ nameOfOpposite+" not found.");
			}
			else 
				throw new IllegalArgumentException(CLASS_NOT_DEFINED + "opType");
		}
	}

	private void setAttributeOrder() {
		ModelClass firstClass = null;
		for (Map.Entry<String,DSLGenerationModel.ModelClass> classEntry : this.genModel.getClasses().entrySet()) {
			String className = classEntry.getKey();
			ModelClass modelClass = classEntry.getValue();
			if(firstClass == null) //works because Map is LinkedHasMap which has Order saved
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
	private List<ClassAttribute> setAttributeOrderInClass(ModelClass modelClass) {
		List<ClassAttribute> optionalAttrs = new ArrayList<>();
		ClassAttribute previousRequiredAttr = null;
		for (ClassAttribute currentAtt : modelClass.getAttributes()) {
			if(currentAtt.getAttributeKind() == AttributeKind.ATTRIBUTE || // a mandatory attribute
			currentAtt.getAttributeKind() == AttributeKind.LIST_OF_ATTRIBUTES ||
			currentAtt.getAttributeKind() == AttributeKind.OPPOSITE_ATTRIBUTE){
				System.out.println(currentAtt.getAttributeFullName());
				if(previousRequiredAttr==null)
					previousRequiredAttr = currentAtt;
				else if(previousRequiredAttr!=null){
					if(isClassDefined(currentAtt.getType())){ //If current Attribute is a Reference to a defined Class set reference
						currentAtt.setReference(true);
					}
					previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
					previousRequiredAttr = currentAtt;
				}
			}else if(currentAtt.getAttributeKind() == AttributeKind.OPTIONAL_ATTRIBUTE || // an optional attribute
			currentAtt.getAttributeKind() == AttributeKind.OPTIONAL_LIST_OF_ATTRIBUTES){
				System.out.println(currentAtt.getAttributeFullName());
				currentAtt.setOptional(true);
				if(previousRequiredAttr!=null){
					if(isClassDefined(currentAtt.getType())){ //It is a modeled Class
						currentAtt.setReference(true);
						previousRequiredAttr.setNextAttribute(currentAtt); //TODO nextAttribute needed if class is defined? = else
						previousRequiredAttr = currentAtt;
					}else{ // it is not a modeled Class e.g. simple types
						previousRequiredAttr.addNextOptionalAttribute(currentAtt);
						optionalAttrs.add(currentAtt);
					}
				}
			}
		}
		previousRequiredAttr.setLastAttribute(true);
		return optionalAttrs;
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
		else if (attrDef.startsWith(OPT_LIST_START))
			return AttributeKind.OPTIONAL_LIST_OF_ATTRIBUTES;
		else if (attrDef.startsWith(OPPOSITE_START))
			return AttributeKind.OPPOSITE_ATTRIBUTE;
		else return null;
	}

	public Map<String, String> getLastAttribute() {
		return lastAttribute;
	}
	
	public String getEntryPointMethod(){
//			if(this.entryPointMethod == null && this.epMatcher.find()){
//				String found = epMatcher.group();
//				this.entryPointMethod = found.substring(4);
//			}
		return this.entryPointMethod;
	}
	
	public String getFirstAttribute() {
		return firstAttribute;
	}
	
	
	public String getBuildMethodName(){
		if(this.buildMethodName == null && this.buildMatcher.find()){
			String found = buildMatcher.group();
			buildMethodName = found.substring(7); //TODO
		}
		return this.buildMethodName;
	}
	
	public void retireveImports(){
		if(this.imports == null && this.importMatcher.find()){
			imports = new ArrayList<>();
			String importString = importMatcher.group().substring(4);
			//initialize importParameterMatcher with found imports
			this.importParameterMatcher = IMPORT_PARAMETER_PATTERN.matcher(importString);
			while(this.importParameterMatcher.find()){
				String toImport = importParameterMatcher.group();
				if(!toImport.equals(""))
					this.genModel.addImports(toImport);
			}
		}
	}

}
