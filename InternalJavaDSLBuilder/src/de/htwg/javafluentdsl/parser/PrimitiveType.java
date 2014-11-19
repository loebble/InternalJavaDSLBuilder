package de.htwg.javafluentdsl.parser;

/**
 * Enum for all primitive types in the java Language
 *
 */
public enum PrimitiveType {
	BYTE("byte"), SHORT("short"), INT("int"), LONG("long"), CHAR("char"), FLOAT(
			"float"), DOUBLE("double"), BOOLEAN("boolean");

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
	 * Constructor to initialize Primitive type with its keyword
	 * 
	 * @param keyword
	 */
	PrimitiveType(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * Retrieves the PrimitiveType which belongs to the given keyword
	 * @param keyword the keyword of the primitive type
	 * @return the PrimitiveType literal
	 */
	public static PrimitiveType getPrimitiveByKeyword(String keyword) {
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
		return keyword;
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
