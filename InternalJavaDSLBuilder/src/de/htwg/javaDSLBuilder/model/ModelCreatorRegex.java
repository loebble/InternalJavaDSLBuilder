package de.htwg.javaDSLBuilder.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**-
 * Defines Meta Model
 * 
 * Given Language/Model Description has to be in REGEX_PATTERN valid form
 * 
 *
 */
public class ModelCreatorRegex {
	
	public static final String REGEX_PATTERN= "modelName=\\w+"
			+ "((\\.A=\\w+:\\w+)|(\\.A=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.OA=\\w+:\\w+)|(\\.OA=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.LA=\\w+:\\w+)|(\\.LA=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}))*"
			+ "((\\.OLA=\\w+:\\w+)|(\\.OLA=\\w+:\\w+((.A|.OA|.LA|.OLA)=\\{\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})))*"
			+ "(\\.def:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})*"
			+ "(\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\})*"
			+ "(\\.build=\\w+)*";
	
	private static final String NO_MATCH = "Given String does not match BuildPatternCreator Regex Pattern: \n" +REGEX_PATTERN;
	private static final String WRONG_DECLARATION = "Method not declared correctly";
	private static final String SAME_METHOD_MULTIPLE_TIMES = "A Method can only be declared once in the same class!";
	
	public static final String MODEL_NAME = "modelName=\\w+";
	public static final String METHODS = "((\\.m=\\w+:\\w+\\{\\w+(\\s?\\,\\s?\\w+)*\\})|\\.om=\\w+:\\w+)+";
	public static final String PARAMETER_TYPE = ":\\w+";
	public static final String METHOD_NAME = "=\\w+:";
	public static final String NEXT_SCOPES = "\\{\\w+(\\s?\\,\\s?\\w+)*\\}";
	public static final String SINGLE_SCOPE = "\\w+";
	public static final String EP_SCOPE = "\\{\\w+";
	public static final String EP_NEXT = "=\\w+";
	
	public static final String MANDATORY_ATTRIBUTES = "(\\.A=\\w+:\\w+)|(\\.A=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})";
	public static final String OPTIONAL_ATTRIBUTES = "(\\.OA=\\w+:\\w+)|(\\.OA=\\w+:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\})";
//TODO	public static final String LIST_MANDATORY_ATTRIBUTES;
//TODO	public static final String LIST_OPTIONAL_ATTRIBUTES;
	public static final String DEFINED_CLASSES = "\\.def:\\w+\\{(.A|.OA|.LA|.OLA)=\\w+:\\w+(\\,\\s?(.A|.OA|.LA|.OLA)=\\w+:\\w+)*\\}";
	
	public static final String BUILD = "\\.build=\\w+";
	public static final String IMPORT = "\\.imp=\\{(\\w+(\\.)?)+(\\s?\\,\\s? (\\w+(\\.)?)+)*\\}";
	public static final String IMPORT_PARAMETER = "(\\w+(\\.)?)+";
	
	private static final Pattern NAME_PATTERN = Pattern.compile(MODEL_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern EP_SCOPE_PATTERN = Pattern.compile(EP_SCOPE, Pattern.CASE_INSENSITIVE);
	private static final Pattern EP_NEXT_PATTERN = Pattern.compile(EP_NEXT, Pattern.CASE_INSENSITIVE);
	private static final Pattern METHOD_PATTERN= Pattern.compile(METHODS, Pattern.CASE_INSENSITIVE);
	private static final Pattern METHOD_NAME_PATTERN= Pattern.compile(METHOD_NAME, Pattern.CASE_INSENSITIVE);
	private static final Pattern MANDATORY_METHODS_PATTERN= Pattern.compile(MANDATORY_ATTRIBUTES, Pattern.CASE_INSENSITIVE);
	private static final Pattern OPTIONAL_METHODS_PATTERN= Pattern.compile(OPTIONAL_ATTRIBUTES, Pattern.CASE_INSENSITIVE);
	private static final Pattern PARAMETER_TYPE_PATTERN= Pattern.compile(PARAMETER_TYPE, Pattern.CASE_INSENSITIVE);
	private static final Pattern NEXT_SCOPES_PATTERN= Pattern.compile(NEXT_SCOPES, Pattern.CASE_INSENSITIVE);
	private static final Pattern SINGLE_SCOPE_PATTERN= Pattern.compile(SINGLE_SCOPE, Pattern.CASE_INSENSITIVE);
	private static final Pattern BUILD_PATTERN = Pattern.compile(BUILD, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PATTERN = Pattern.compile(IMPORT, Pattern.CASE_INSENSITIVE);
	private static final Pattern IMPORT_PARAMETER_PATTERN = Pattern.compile(IMPORT_PARAMETER, Pattern.CASE_INSENSITIVE);
	
	private String  languageDescr;
	private Matcher nameMatcher;
	private Matcher epMatcher;
	private Matcher methodMatcher;
	private Matcher mandatoryMethodMatcher;
	private Matcher optionalMethodMatcher;
	private Matcher nextScopesMatcher;
	private Matcher nextSingleScopeMatcher;
	private Matcher buildMatcher;
	private Matcher methodNameMatcher;
	private Matcher importMatcher;
	private Matcher importParameterMatcher;
	
	private String dslName;
	private String entryPointMethod;
	public List<String> methodDeclarations;
	private Map<String,String> chainMethods;
	private Map<String,String> optionalMethods;
	private Map<String,String> mandatoryMethods;
	private Map<String,List<String>> nextMethods;
	private Map<String,List<String>> nextOptionalMethods;
	private String buildMethodName;
	private List<String> imports;
	private String firstMethod;
	private Map<String,String> lastMethods;
	
	private ModelCreatorRegex(){}
	
	public static ModelCreatorRegex getInstance(String languageDescr){
		if(!languageDescr.matches(REGEX_PATTERN)){
			throw new IllegalArgumentException(NO_MATCH);
		}else System.out.println("Correct Description");
		ModelCreatorRegex creator = new ModelCreatorRegex();
//		creator.languageDescr = languageDescr;
//		creator.nameMatcher = NAME_PATTERN.matcher(creator.languageDescr);
//		creator.epMatcher = EP_PATTERN.matcher(creator.languageDescr);
//		creator.methodMatcher = METHOD_PATTERN.matcher(creator.languageDescr);
//		creator.buildMatcher = BUILD_PATTERN.matcher(creator.languageDescr);
//		creator.importMatcher = IMPORT_PATTERN.matcher(creator.languageDescr);
//		creator.optionalMethodMatcher = OPTIONAL_METHODS_PATTERN.matcher(creator.languageDescr);
//		creator.mandatoryMethodMatcher = MANDATORY_METHODS_PATTERN.matcher(creator.languageDescr);
//			creator.getEntryPointMethod();
//			creator.getBuildMethodName();
//			creator.getDslName();
//		creator.getMethodDeclarations();
//			creator.getMandatoryMethods();
//			creator.getOptionalMethods();
//			creator.getNextMethods();
//			creator.getNextOptionalMethods();
//			creator.getImports();
		return creator;
	}
	
	public String getDslName(){
		if(this.dslName == null && this.nameMatcher.find()){
			String found = nameMatcher.group();
			this.dslName = found.substring(8);
		}
		return this.dslName;
	}
	
	public Map<String, String> getLastMethods() {
		return lastMethods;
	}
	
	public String getEntryPointMethod(){
//			if(this.entryPointMethod == null && this.epMatcher.find()){
//				String found = epMatcher.group();
//				this.entryPointMethod = found.substring(4);
//			}
		return this.entryPointMethod;
	}
	
	public String getFirstMethod() {
		return firstMethod;
	}
	
	public Map<String,String> getMandatoryMethods() {
		if(this.mandatoryMethods == null){
			this.mandatoryMethods = new LinkedHashMap<String,String>();
			while(this.mandatoryMethodMatcher.find())
				putMethodInMap(mandatoryMethodMatcher.group(), this.mandatoryMethods);
		}
		return this.mandatoryMethods;
	}
	
	public Map<String,String> getOptionalMethods() {
		if(this.optionalMethods == null){
			this.optionalMethods = new LinkedHashMap<>();
			while(this.optionalMethodMatcher.find()){
				String optionalMeth = optionalMethodMatcher.group();
				putMethodInMap(optionalMeth, this.optionalMethods);
			}
		}
		return this.optionalMethods;
	}
	
	/**
	 * 
	 * @return ordered java.util.Map containing methodname parametersType. 
	 */
	public List<String> getMethodDeclarations(){
		if(this.methodDeclarations == null){
			this.methodDeclarations = new ArrayList<String>();
			this.lastMethods = new LinkedHashMap<String,String>();
			if(this.epMatcher.find()){
				String ep = epMatcher.group();
				Matcher nameMatcher = EP_NEXT_PATTERN.matcher(ep);
				if(nameMatcher.find()){
					this.entryPointMethod = nameMatcher.group().substring(1);
				}
				Matcher nextMethod = NEXT_SCOPES_PATTERN.matcher(ep);
				if(nextMethod.find())
					this.firstMethod = nextMethod.group().substring(1,nextMethod.group().length()-1);
			}
			this.mandatoryMethods = new LinkedHashMap<String,String>();
			while(this.mandatoryMethodMatcher.find()){
				String m = mandatoryMethodMatcher.group();
				putMethodInMap(m, this.mandatoryMethods);
				this.methodDeclarations.add(m);
			}
			this.optionalMethods = new LinkedHashMap<>();
			while(this.optionalMethodMatcher.find()){
				String om = optionalMethodMatcher.group();
				putMethodInMap(om, this.optionalMethods);
				this.methodDeclarations.add(om);
			}
		}
		return this.methodDeclarations;
	}
	
	public Map<String, List<String>> getNextMethods() {
		if(this.nextMethods == null){
			setNextMethods(methodDeclarations);
		}
		return this.nextMethods;
	}
	
	public Map<String, List<String>> getNextOptionalMethods() {
		if(this.nextOptionalMethods == null){
			setNextMethods(methodDeclarations);
		}
		return this.nextOptionalMethods;
	}
	
	private void setNextMethods(List<String> methods) {
		this.nextMethods = new LinkedHashMap<>();
		this.nextOptionalMethods = new LinkedHashMap<>();
		for (String methodDecl: methods) {
			this.methodNameMatcher = METHOD_NAME_PATTERN.matcher(methodDecl);
			this.methodNameMatcher.find();
			String methodName = this.methodNameMatcher.group();
			methodName = methodName.substring(1,methodName.length()-1);
			this.nextScopesMatcher = NEXT_SCOPES_PATTERN.matcher(methodDecl);
			if(this.nextScopesMatcher.find()){
				String nextScopes = this.nextScopesMatcher.group();
				this.nextSingleScopeMatcher = SINGLE_SCOPE_PATTERN.matcher(nextScopes);
				List<String> mandatoryScopes = new ArrayList<>();
				List<String> optionalScopes = new ArrayList<>();
				while(this.nextSingleScopeMatcher.find()){
					String nextScopeName = this.nextSingleScopeMatcher.group();
					if(nextScopeName.equals(this.buildMethodName)){
						this.lastMethods.put(methodName,nextScopeName); //TODO Exceptions wenn Methode im Selben Scope mehrfach angegeben wird (auch buildMethod)
						continue;
					}
					if(this.optionalMethods.containsKey(nextScopeName))
						optionalScopes.add(nextScopeName);
					else if(this.mandatoryMethods.containsKey(nextScopeName))
						mandatoryScopes.add(nextScopeName);
				}
				if(optionalScopes.size()>0)
					this.nextOptionalMethods.put(methodName, optionalScopes);
				if(mandatoryScopes.size()>0)
					this.nextMethods.put(methodName, mandatoryScopes);
			}
		}
	}

	public String getBuildMethodName(){
		if(this.buildMethodName == null && this.buildMatcher.find()){
			String found = buildMatcher.group();
			buildMethodName = found.substring(7);
		}
		return this.buildMethodName;
	}
	
	public List<String> getImports(){
		if(this.imports == null && this.importMatcher.find()){
			imports = new ArrayList<>();
			String importString = importMatcher.group().substring(4);
			//initialize importParameterMatcher with found imports
			this.importParameterMatcher = IMPORT_PARAMETER_PATTERN.matcher(importString);
			while(this.importParameterMatcher.find()){
				String toImport = importParameterMatcher.group();
				if(!toImport.equals(""))
					imports.add(toImport);
			}
		}
		return imports;
	}
	
	private void putMethodInMap(String methodDesc, Map<String,String> methodMap){
		String methodName = "";
		String type = "";
		//initialize parameter Matcher with found method description
		Matcher methodNameMatcher = METHOD_NAME_PATTERN.matcher(methodDesc);
		if(methodNameMatcher.find()){
			String tmpName = methodNameMatcher.group();
			methodName = tmpName.substring(1, tmpName.length()-1);
		}
		Matcher parameterTypeMatcher = PARAMETER_TYPE_PATTERN.matcher(methodDesc);
		if(parameterTypeMatcher.find()){
			String tmpType = parameterTypeMatcher.group();
			type = tmpType.substring(1, tmpType.length());
		}
		if(methodName.equals("") || type.equals(""))
			throw new IllegalArgumentException(WRONG_DECLARATION);
		else if (methodMap.containsKey(methodName))
			throw new IllegalArgumentException(SAME_METHOD_MULTIPLE_TIMES + " Multiple occurring method: " +methodName);
		methodMap.put(methodName,type);
	}


}
