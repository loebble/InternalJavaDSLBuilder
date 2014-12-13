package de.htwg.javafluentdsl.dslmodel;

/**
 * Enum for declaring a dependency kind.
 *
 */
public enum DependencyKind {
    /**
     * ClassAttribute is a Attribute to set in the generated DSL.
     * Either a mandatory attribute or a reference attribute 
     * to another modeled Class.
     */
    ATTRIBUTE,
    /**
     * ClassAttribute is no reference attribute 
     * to another modeled Class and is optional to set.
     */
	OPTIONAL_ATTRIBUTE,
	/**
	 * ClassAttribute is a list of attributes.
	 */
	LIST_OF_ATTRIBUTES, 
	/**
	 * ClassAttribute is a opposite attribute
	 * which has to be set by its opposite attribute.
	 */
	OPPOSITE_ATTRIBUTE_TO_SET
}
