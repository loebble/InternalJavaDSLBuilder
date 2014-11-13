package de.htwg.javafluentdsl.dslmodel;
/**
 * Enum for all primitive types in the java Language
 *
 */
public enum PrimitiveType {
	BYTE("byte"), SHORT("short"), INT("int"), LONG("long"), CHAR("char"),
	FLOAT("float"), DOUBLE("double"),
	BOOLEAN("boolean");
	
	/**
	 * holds the case sensitive String represenation 
	 * of the keyword.
	 */
	private String keyword;
	
	/**
	 * Constructor to initialize Primitive type with its keyword
	 * @param keyword
	 */
	PrimitiveType(String keyword) {
        this.keyword = keyword;
    }
	
	/**
	 * Retrieves the keyword.
	 * @return {@link #keyword}
	 */
	public String getKeyword(){
		return keyword;
	}
	
	@Override
    public String toString() {
        return this.getKeyword();
    }
}
