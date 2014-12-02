package de.htwg.javafluentdsl.dslmodel;

/**
 * Enum for all primitive types in the java Language.
 *
 */
public enum PrimitiveType {
    /**
     * Represents the primitive data type byte.
     */
	BYTE("byte"),
	/**
     * Represents the primitive data type short.
     */
	SHORT("short"),
	/**
     * Represents the primitive data type int.
     */
	INT("int"),
	/**
     * Represents the primitive data type long.
     */
	LONG("long"),
	/**
     * Represents the primitive data type char.
     */
	CHAR("char"),
	/**
     * Represents the primitive data type float.
     */
	FLOAT("float"),
	/**
     * Represents the primitive data type double.
     */
	DOUBLE("double"),
	/**
     * Represents the primitive data type boolean.
     */
	BOOLEAN("boolean");

	/**
	 * holds the case sensitive String representation of the keyword.
	 */
	private String keyword;

	/**
	 * Private Constructor. Only instantiate with keyword.
	 */
	private PrimitiveType() {
	}

	/**
	 * Constructor to initialize Primitive type with its keyword.
	 * @param keywordInJava the keyword as used in the java language.
	 */
	PrimitiveType(final String keywordInJava) {
		this.keyword = keywordInJava;
	}
	/**
	 * Retrieves the PrimitiveType which belongs to the given keyword.
	 * @param keyword the keyword of the primitive type
	 * @return the PrimitiveType literal
	 */
	public static PrimitiveType getPrimitiveByKeyword(final String keyword) {
		switch (keyword) {
		case "byte":
			return BYTE;
		case "short":
			return SHORT;
		case "int":
			return INT;
		case "long":
			return LONG;
		case "char":
			return CHAR;
		case "float":
			return FLOAT;
		case "double":
			return DOUBLE;
		case "boolean":
			return BOOLEAN;
		default:
			return null;
		}
	}

	/**
	 * Retrieves the keyword.
	 * 
	 * @return {@link #keyword}
	 */
	public String getKeyword() {
		return this.keyword;
	}

	@Override
	public String toString() {
		return this.getKeyword();
	}

	/**
	 * Gets the wrapper classes name for boxing
	 * and unboxing of this literal.
	 * @return the name of the wrapper class
	 */
	public String getWrapperClassName() {
		switch (this.keyword) {
		case "byte":
			return "Byte";
		case "short":
			return "Short";
		case "int":
			return "Integer";
		case "long":
			return "Long";
		case "char":
			return "Character";
		case "float":
			return "Float";
		case "double":
			return "Double";
		case "boolean":
			return "Boolean";
		default:
			return "";
		}
	}

}
