package de.htwg.javafluentdsl.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.parser.IParser;

/**
 *Implementation of the {@link ITemplateGenerator} for generating
 *inner DSLs for Regex model instantiation. 
 *
 */
public class GeneratorRegex implements ITemplateGenerator {

    /*
     * Template options
     */
    /**
     * Template option for a intern builder. This option creates the builder
     * as an inner class inside the model.
     */
    public static final String INTERN_BUILDER_OPTION = "internBuilder";
    /**
    * Template option for a separated builder. This option creates the builder
    * in a separated file from the model.
    */
    public static final String SEPARATED_BUILDER_OPTION = "separatedBuilder";

    /*
     * Template paths
     */
    /**
     * Path to the directory of the StringTemplate Files for regex models.
     */
    private static final String REGEX_TEMPLATES_DIR_PATH = 
            StringTemplateUtil.STRING_TEMPLATES_DIR_PATH
            + "regex_templates/";

    /*
     * Template fileNames
     */
    /**
     * File name of the Template for the templateOption.
     * {@link #INTERN_BUILDER_OPTION}.
     */
    private static final String INTERN_BUILDER_FILE = "InternBuilder.stg";
    /**
     * File name of the Template for the templateOption.
     * {@link #SEPARATED_BUILDER_OPTION}.
     */
    private static final String SEPARATED_BUILDER_FILE = "SeparatedBuilder.stg";
    /**
     * Error Message if a wrong templateOption was given.
     */
    private static final String WRONG_OPTION_REGEX = 
            "Not a valid templateOption given. "
            + "For a string model description only:"
            + INTERN_BUILDER_OPTION
            + " OR " + SEPARATED_BUILDER_OPTION + " allowed.";

    /**
     * Starts generation of files with given template.
     * 
     * @param parser
     *            the {@link ParserRegex} which holds the DSLGenerationModel
     * @param templateOption
     *            which template to use
     * @param targetPackage
     *            the target package the files are generated at
     */
    public final void generateDSL(final IParser parser,
            final String templateOption, final String targetPackage) {
        DSLGenerationModel dslModel = parser.getGenerationModel();
        List<String> filesCreated = new ArrayList<>();
        String filePath = null;
        switch (templateOption) {
        case INTERN_BUILDER_OPTION:
            filePath = generateBuilderInternDSL(dslModel, targetPackage);
            break;
        case SEPARATED_BUILDER_OPTION:
            generateBuilderSeparateDSL(dslModel, targetPackage, filesCreated);
            break;
        default:
            throw new IllegalArgumentException(WRONG_OPTION_REGEX);
        }
        if (filePath != null) {
            filesCreated.add(filePath);
        }
        if (filesCreated.size() > 0) {
            for (String path : filesCreated) {
                System.out.println("File created under path: "
                        + path.replace('/', File.separatorChar));
            }
            System.out.println();
        }

    }

    /**
     * Creates a DSL in one File. This means the Builder is inside the Model and
     * thus the model can only be instantiated by the Builder.
     * 
     * @param dslModel
     *            {@link DSLGenerationModel}
     * @param targetPackage
     *            the target package the files are generated at
     * @return path of DSL File which was created
     */
    private String generateBuilderInternDSL(final DSLGenerationModel dslModel,
            final String targetPackage) {
        STGroup group = StringTemplateUtil
                .getStringTemplateFromPath(REGEX_TEMPLATES_DIR_PATH
                        + INTERN_BUILDER_FILE);
        ST simpleBT = group.getInstanceOf("ModelTemplate");
        simpleBT.add("packageName", targetPackage);
        simpleBT.add("genModel", dslModel);
        return StringTemplateUtil.writeToJavaFile(targetPackage,
                dslModel.getModelName(), simpleBT.render());
    }

    /**
     * Generates a DSL with a separated Model. This means the Builder is
     * seperated from the Model (or vice versa) which can be desired in terms of
     * separation of concerns. This method is created to be used with
     * DSLGenerationModels created by a {@link ParserRegex} instance, but can be
     * used with every DSLGenerationModel while it still generates a correct
     * model for the template.
     * 
     * @param dslModel
     *            {@link DSLGenerationModel}
     * @param targetPackage
     *            the target package the files are generated at
     * @param filesCreated
     *            List to add the files that are created
     * @return path of DSL File which was created
     */
    private String generateBuilderSeparateDSL(
            final DSLGenerationModel dslModel, final String targetPackage,
            final List<String> filesCreated) {
        String modelFilePath = regexGenerateSeperatedModel(dslModel,
                targetPackage);
        if (modelFilePath != null) {
            filesCreated.add(modelFilePath);
        }

        String builderFilePath = regexGenerateBuilderSeperated(dslModel,
                targetPackage);
        if (builderFilePath != null) {
            filesCreated.add(builderFilePath);
        }
        return null;
    }

    /**
     * Generates a single model file for separated builder.
     * 
     * @param dslModel
     *            the generation model
     * @param targetPackage
     *            the target package the file is be written to
     * @return @return path of DSL File which was created
     * @see {@link #generateBuilderSeparateDSL(DSLGenerationModel,
     *  String, List)}
     */
    private String regexGenerateSeperatedModel(
            final DSLGenerationModel dslModel, final String targetPackage) {
        STGroup group = StringTemplateUtil
                .getStringTemplateFromPath(REGEX_TEMPLATES_DIR_PATH
                        + SEPARATED_BUILDER_FILE);
        ST modelTemplate = group.getInstanceOf("Model");
        modelTemplate.add("packageName", targetPackage);
        modelTemplate.add("genModel", dslModel);
        String modelCode = modelTemplate.render();
        return StringTemplateUtil.writeToJavaFile(targetPackage,
                dslModel.getModelName(), modelCode);
    }

    /**
     * Generates a single builder file for separated builder.
     * 
     * @param dslModel
     *            the generation model
     * @param targetPackage
     *            the target package the file is be written to
     * @return @return path of DSL File which was created
     * @see {@link #generateBuilderSeparateDSL(DSLGenerationModel,
     *  String, List)}
     */
    private String regexGenerateBuilderSeperated(
            final DSLGenerationModel dslModel, final String targetPackage) {
        STGroup group = StringTemplateUtil
                .getStringTemplateFromPath(REGEX_TEMPLATES_DIR_PATH
                        + SEPARATED_BUILDER_FILE);
        ST modelTemplate = group.getInstanceOf("Builder");
        modelTemplate.add("packageName", targetPackage);
        modelTemplate.add("genModel", dslModel);
        String modelCode = modelTemplate.render();
        return StringTemplateUtil.writeToJavaFile(targetPackage,
                dslModel.getModelName() + "Builder", modelCode);
    }

}
