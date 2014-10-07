package de.htwg.javaDSLBuilder.dslmodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DSLGenerationModel {
	
	public DSLGenerationModel(){
		imports = new ArrayList<String>();
		classes = new LinkedHashMap<String,DSLGenerationModel.ModelClass>();
		nestedCalls = new LinkedHashMap<String,String>();
	}
	
	private String modelName;
	private List<String> imports;
	private boolean hasList = false;
	
	public final Map<String,ModelClass> classes;
	public final Map<String,String> nestedCalls;
	
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
	
	public ModelClass getClass(String className){
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
			optionalAttributes = new ArrayList<ClassAttribute>();
			referencedByOpposite = new ArrayList<ClassAttribute>();
		}
		
		private String className;
		private List<ClassAttribute> attributes;
		private List<ClassAttribute> optionalAttributes;
		private List<ClassAttribute> referencedByOpposite;

		public List<ClassAttribute> getAttributes() {
			return attributes;
		}
		
		public List<ClassAttribute> getOptionalAttributes() {
			return optionalAttributes;
		}

		public void addAttribute(ClassAttribute attribute) {
			this.attributes.add(attribute);
		}
		
		public void addOptionalAttribute(ClassAttribute attribute) {
			this.optionalAttributes.add(attribute);
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
		
		public ClassAttribute getSpefificAttributeByFullName(String fullAttributeName) {
			for (ClassAttribute attr : this.attributes) {
				if(attr.attributeFullName.equals(fullAttributeName))
					return attr;
			}
			return null;
		}
		
		public ClassAttribute getSpefificAttribute(String attributeName) {
			for (ClassAttribute attr : this.attributes) {
				if(attr.attributeName.equals(attributeName))
					return attr;
			}
			return null;
		}

		public List<ClassAttribute> getReferencedByOpposite() {
			return referencedByOpposite;
		}

		public void addReferencedByOpposite(ClassAttribute referencedByOpposite) {
			this.referencedByOpposite.add(referencedByOpposite);
		}
		
	}

	public class ClassAttribute{
		private String attributeName;
		private String attributeFullName;
		private String className;
		private String type;
		private boolean optional = false; //TODO used in EMFCreator
		private AttributeKind kind;
		private ClassAttribute nextAttribute; //TODO also List?
		private List<ClassAttribute> nextOptionalAttributes;
		private boolean isReference;
		private List<String> nextClass;
		private List<String> nextOptionalClasses;
		private boolean lastAttribute = false;
		private boolean isList = false;
		private ClassAttribute opposite;
		private boolean isReferencedByAttribute = false;
		private ClassAttribute referencedBy = null;
		
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
			return kind;
		}

		public void setAttributeKind(AttributeKind kind) {
			this.kind = kind;
		}

		public String getAttributeFullName() {
			return attributeFullName;
		}

		public void setAttributeFullName(String attributeFullName) {
			this.attributeFullName = attributeFullName;
		}

		public boolean isLastAttribute() {
			return lastAttribute;
		}

		public void setLastAttribute(boolean lastAttribute) {
			this.lastAttribute = lastAttribute;
		}

		public boolean isReference() {
			return isReference;
		}

		public void setReference(boolean isReference) {
			this.isReference = isReference;
		}

		public void setOpposite(ClassAttribute opp) {
			this.opposite=opp;
		}
		
		public ClassAttribute getOpposite() {
			return this.opposite;
		}

		public ClassAttribute getReferencedBy() {
			return referencedBy;
		}

		public void setReferencedBy(ClassAttribute referencedBy) {
			this.referencedBy = referencedBy;
		}

		public boolean isReferencedByAttribute() {
			return isReferencedByAttribute;
		}

		public void setReferencedByAttribute(boolean isReferencedByAttribute) {
			this.isReferencedByAttribute = isReferencedByAttribute;
		}
		
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}

		public boolean isList() {
			return isList;
		}

		public void setList(boolean isList) {
			this.isList = isList;
		}
		
	}
	
	public String printModel() {
		StringBuffer sb = new StringBuffer();
		sb.append("DSL for: "+this.modelName +"\n");
		sb.append("IMPORTS: ");
		for (String importString : this.imports) {
			sb.append(importString + "; ");
		}
		sb.append("\n"+"CLASSES: ");
		for (Map.Entry<String, ModelClass> entry : this.classes.entrySet()) {
			ModelClass modelClass = (ModelClass) entry.getValue();
			sb.append("\n" + "ModelClass: "+modelClass.className +"\n");
			for (ClassAttribute attr : modelClass.attributes) {
				sb.append("\t" + "Name: " +attr.getAttributeName() + " type: " +attr.getType() 
						+ " kind: " +attr.getAttributeKind()+" " + " reference: " +attr.isReference()
						+ " optional: " +attr.isOptional());
				if(attr.getOpposite()!=null)
					sb.append(" opposite: " +attr.getOpposite().getAttributeFullName());
				sb.append("\n");
			}
			for (ClassAttribute attr : modelClass.optionalAttributes) {
				sb.append("\t" + "Name: " +attr.getAttributeName() + " type: " +attr.getType() 
						+ " kind: " +attr.getAttributeKind()+" " + " reference: " +attr.isReference()
						+ " optional: " +attr.isOptional()+" " +"\n");
			}
			for (ClassAttribute referencedCl : modelClass.referencedByOpposite) {
				sb.append("\t" + referencedCl.getOpposite().getAttributeFullName() +" referenced by nestedClassAttr: "+referencedCl.getAttributeFullName() +"\n");
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
					sb.append(" "+attr.attributeName +":" +attr.getType());
				if(attr.getNextAttribute() != null)
					sb.append("-> "+ attr.getNextAttribute().getAttributeName() +":" +attr.getNextAttribute().getType());
				for (ClassAttribute nextOptional : attr.getNextOptionalAttributes()) {
					sb.append("(->"+nextOptional.getAttributeName()+":" +nextOptional.getType()+")");
				}
				i++;
			}
		}
		return sb.toString();
	}

	public boolean isHasList() {
		return hasList;
	}

	public void setHasList(boolean hasList) {
		this.hasList = hasList;
	}
	
}
