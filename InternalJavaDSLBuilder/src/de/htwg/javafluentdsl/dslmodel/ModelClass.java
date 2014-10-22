package de.htwg.javafluentdsl.dslmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Class the model defines.
 * Also contains all the Attributes the model defined for this class {@link ClassAttribute}
 *
 */
public class ModelClass {

	/**
	 * Constructor for a ModelClass.
	 * The ModelClass needs a name which is unique in the model
	 * and a {@link DSLGenerationModel} instance it belongs to 
	 * @param name The unique name of this ModelClass
	 * @param model The correponding DSLGenerationModel
	 */
		public ModelClass(String name, DSLGenerationModel model){
			this.model = model;
			className = name;
			attributes = new ArrayList<ClassAttribute>();
			optionalAttributes = new ArrayList<ClassAttribute>();
			referencedByOpposite = new ArrayList<ClassAttribute>();
			this.imports = new ArrayList<String>();
		}
		
		private DSLGenerationModel model;
		private String className;
		private List<ClassAttribute> attributes;
		private List<ClassAttribute> optionalAttributes;
		private List<ClassAttribute> referencedByOpposite;
		private boolean simpleOptionalsOnly;
		private List<String> imports;
		private boolean hasList = false;

		public boolean isSimpleOptionalsOnly() {
			return simpleOptionalsOnly;
		}

		public List<ClassAttribute> getAttributes() {
			return attributes;
		}
		
		public List<ClassAttribute> getOptionalAttributes() {
			return optionalAttributes;
		}

		public void addAttribute(ClassAttribute attribute) {
			attribute.setModelClass(this);
			attribute.setClassName(this.className);
			attribute.setAttributeFullName(this.className + attribute.getAttributeName());
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
				if(attr.getAttributeFullName().equals(fullAttributeName))
					return attr;
			}
			return null;
		}
		
		public ClassAttribute getSpefificAttribute(String attributeName) {
			for (ClassAttribute attr : this.attributes) {
				if(attr.getAttributeName().equals(attributeName))
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

		public void setSimpleOptionalsOnly(boolean b) {
			this.simpleOptionalsOnly = b;
		}

		public List<String> getImports() {
			return imports;
		}

		public void addImport(String importString) {
			if(!imports.contains(importString))
				imports.add(importString);
		}

		public DSLGenerationModel getModel() {
			return model;
		}

		public boolean isHasList() {
			return hasList;
		}

		public void setHasList(boolean hasList) {
			this.hasList = hasList;
		}
		
}
