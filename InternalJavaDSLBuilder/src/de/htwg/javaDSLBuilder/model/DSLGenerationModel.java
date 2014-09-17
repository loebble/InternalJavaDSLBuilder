package de.htwg.javaDSLBuilder.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DSLGenerationModel {
	
	public DSLGenerationModel(){
		imports = new ArrayList<String>();
		classes = new LinkedHashMap<String,DSLGenerationModel.ModelClass>();
	}
	
	private String modelName;
	private List<String> imports;
	
	private final Map<String,ModelClass> classes;
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public List<String> getImports() {
		return imports;
	}

	public void addImports(String importString) {
		if(!imports.contains(importString))
			imports.add(importString);
	}
	
	public ModelClass addModelClass(String className) {
		if(!classes.containsKey(className))
			classes.put(className,new ModelClass(className));
		return classes.get(className);
	}

	/**
	 * returns a Map (Classname as Key and the {@code ModelClass} Object as Value) for all the Classes defined in the Model.
	 * classes can be added via the put method of the Map interface
	 */
	public Map<String,ModelClass> getClasses() {
		return classes;
	}
	
	public class ModelClass{
		
		public ModelClass(String name){
			className = name;
			attributes = new ArrayList<ClassAttribute>();
		}
		
		private String className;
		private List<ClassAttribute> attributes;

		public List<ClassAttribute> getAttributes() {
			return attributes;
		}

		public void addAttribute(ClassAttribute attribute) {
			this.attributes.add(attribute);
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
	}

	public class ClassAttribute{
		private String attributeName;
		private String attributeFullName;
		private String type;
		private boolean optional; //TODO used in EMFCreator
		private AttributeKind attributeType;
		private ClassAttribute nextAttribute; //TODO also List?
		private List<ClassAttribute> nextOptionalAttributes;
		private List<String> nextClass;
		private List<String> nextOptionalClasses;
		
		public ClassAttribute(){
			this.nextOptionalAttributes = new ArrayList<ClassAttribute>();
			this.nextClass = new ArrayList<String>();
			this.nextOptionalClasses = new ArrayList<String>();
		}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public boolean isOptional() {
			return optional;
		}
		public void setOptional(boolean optional) {
			this.optional = optional;
		}
		public ClassAttribute getNextAttribute() {
			return nextAttribute;
		}
		public void setNextAttribute(ClassAttribute nextAttribute) {
			this.nextAttribute = nextAttribute;
		}
		public List<ClassAttribute> getNextOptionalAttributes() {
			return nextOptionalAttributes;
		}
		public void addNextOptionalAttribute(ClassAttribute nextOptionalAttribute) {
			this.nextOptionalAttributes.add(nextOptionalAttribute);
		}
		public void setNextOptionalAttributes(
				List<ClassAttribute> nextOptionalAttributes) {
			this.nextOptionalAttributes = nextOptionalAttributes;
		}
		public String getAttributeName() {
			return attributeName;
		}
		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}
		public List<String> getNextClass() {
			return nextClass;
		}
		public void setNextClass(List<String> nextClasses) {
			this.nextClass = nextClasses;
		}
		public void addNextClass(String nextClasse) {
			this.nextClass.add(nextClasse);
		}
		public List<String> getNextOptionalClass() {
			return nextOptionalClasses;
		}
		public void setNextOptionalClass(List<String> nextOptionalClass) {
			this.nextOptionalClasses = nextOptionalClass;
		}
		public void addNextOptionalClass(String nextOptionalAttribute) {
			this.nextOptionalClasses.add(nextOptionalAttribute);
		}

		public AttributeKind getAttributeKind() {
			return attributeType;
		}

		public void setAttributeKind(AttributeKind kind) {
			this.attributeType = kind;
		}

		public String getAttributeFullName() {
			return attributeFullName;
		}

		public void setAttributeFullName(String attributeFullName) {
			this.attributeFullName = attributeFullName;
		}

	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DSL for: "+this.modelName +"\n");
		sb.append("imports: ");
		for (String importString : this.imports) {
			sb.append(importString + "; ");
		}
		sb.append("\n"+"Attributes: ");
		for (Map.Entry<String, ModelClass> entry : this.classes.entrySet()) {
			ModelClass modelClass = (ModelClass) entry.getValue();
			sb.append("\n" + "ModelClass: "+modelClass.className +"\n");
			for (ClassAttribute attr : modelClass.attributes) {
				sb.append("AttributeName: " +attr.attributeName + " type: " +attr.type + " optional: " +attr.optional+" ");
			} 
		}
		return sb.toString();
	}
	
	public String printOrder(){
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, ModelClass> entry : this.classes.entrySet()) {
			ModelClass modelClass = (ModelClass) entry.getValue();
			sb.append("\n" + "ModelClass: "+modelClass.className +"\n");
			int i = 0;
			for (ClassAttribute attr : modelClass.attributes) {
				if(i == 0)
					sb.append(" "+attr.attributeName);
				if(attr.getNextAttribute() != null)
					sb.append("-> "+ attr.getNextAttribute().getAttributeName());
				for (ClassAttribute nextOptional : attr.getNextOptionalAttributes()) {
					sb.append("(->"+nextOptional.getAttributeName()+")");
				}
				for (String nextClass : attr.getNextClass()) {
					sb.append("->"+nextClass);
				}
				for (String nextClass : attr.getNextOptionalClass()) {
					sb.append("(->"+nextClass+")");
				}
				i++;
			}
		}
		return sb.toString();
	}
	
}
