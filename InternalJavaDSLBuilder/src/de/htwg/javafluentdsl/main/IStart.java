package de.htwg.javafluentdsl.main;

/**
 * Interface for declaring the start of the DSL generation process.
 *
 */
public interface IStart {

    /**
     * Starts the DSL generation process. With some kind of source 
     * ({@code source}) for dsl model description (e.g. File, description
     * string...) and a defined template option({@code templateOption}). 
     * The classes generated are saved in the given 
     * target packages path ({@code targetPackgage}).
     * 
     * @param source
     *            the source for the dsl model description
     * @param templateOption
     *            the template which should be used for generation
     * @param targetPackage
     *            the relative target package. Preferred with typical package
     *            naming conventions domains seperated by '.' and all in small
     *            letters.
     */
    void startDSLGenerationProcess(String source, String templateOption,
            String targetPackage);

}
