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
			simpleOptAttr = new ArrayList<ClassAttribute>();
			createdByOpposite = new ArrayList<ClassAttribute>();
			this.imports = new ArrayList<String>();
		}
		/**
		 * Reference to the {@link DSLGenerationModel} this ModelClass is part of
		 */
		private DSLGenerationModel model;
		/**
		 * The unique class name
		 */
		private String className;
		/**
		 * Holds the mandatory, list and reference attribute
		 * of this ModelClass
		 */
		private List<ClassAttribute> attributes;
		/**
		 * Holds the simple optional attributes this ModelClass has
		 */
		private List<ClassAttribute> simpleOptAttr;
		/**
		 * Holds the opposite Attributes which are created
		 * by their opposite (creator)
		 */
		private List<ClassAttribute> createdByOpposite;
		/**
		 * True if this ModelClass only consists of simple optional attributes
		 * @see #simpleOptAttr
		 */
		private boolean simpleOptionalsOnly;
		/**
		 * Holds the imports this ModelClass needs
		 */
		private List<String> imports;
		/**
		 * True if this ModelClass has at least one list attribute
		 * @see ClassAttribute#isList()
		 */
		private boolean hasList = false;

		public boolean isSimpleOptionalsOnly() {
			return simpleOptionalsOnly;
		}

		public List<ClassAttribute> getAttributes() {
			return attributes;
		}
		
		public List<ClassAttribute> getSimpleOptAttr() {
			return simpleOptAttr;
		}

		public void addAttribute(ClassAttribute attribute) {
			attribute.setModelClass(this);
			attribute.setClassName(this.className);
			attribute.setAttributeFullName(this.className + attribute.getAttributeName());
			this.attributes.add(attribute);
		}
		
		public void addOptionalAttribute(ClassAttribute attribute) {
			this.simpleOptAttr.add(attribute);
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

		public List<ClassAttribute> getCreatedByOpposite() {
			return createdByOpposite;
		}

		public void addCreatedByOpposite(ClassAttribute referencedByOpposite) {
			this.createdByOpposite.add(referencedByOpposite);
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
