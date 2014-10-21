package de.htwg.javaDSLBuilder.dslmodel;

import java.util.ArrayList;
import java.util.List;

public class ClassAttribute{
	private String attributeName;
	private String attributeFullName;
	private String className;
	private ModelClass modelClass;
	private String type;
	private boolean optional = false;
	private boolean lastAttribute = false;
	private boolean isList = false;
	private boolean isReference;
	private boolean isReferencedByAttribute = false;
	private boolean isCreatorOfOpposite = false;
	private AttributeKind kind;
	private ClassAttribute nextAttribute; //TODO also List?
	private List<ClassAttribute> nextOptionalAttributes;
	private List<String> nextClass;
	private List<String> nextOptionalClasses;
	private ClassAttribute opposite;
	private ClassAttribute referencedBy = null;
	
	public ClassAttribute(String name, String type, String className){
		this.attributeName = name;
		this.type = type;
		this.className = className;
		this.attributeFullName = className + name;
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
	
	public String setAttributeFullName(String fullName) {
		return fullName;
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
		this.getModelClass().addReferencedByOpposite(referencedBy);
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

	public ModelClass getModelClass() {
		return modelClass;
	}

	public void setModelClass(ModelClass modelClass) {
		this.modelClass = modelClass;
	}

	public boolean isCreatorOfOpposite() {
		return isCreatorOfOpposite;
	}

	public void setCreatorOfOpposite(boolean isCreatorOfOpposite) {
		this.isCreatorOfOpposite = isCreatorOfOpposite;
	}
	
}