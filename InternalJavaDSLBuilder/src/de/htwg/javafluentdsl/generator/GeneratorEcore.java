package de.htwg.javafluentdsl.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;
import de.htwg.javafluentdsl.parser.IParser;
/**
 *Implementation of the {@link ITemplateGenerator} for generating
 *inner DSLs for ECore model instantiation.
 *All templateOption for Regex models should be declared in 
 *static final fields which then can be used in the 
 *{@link #generateDSL(IParser, String, String)} method.
 *
 */
public class GeneratorEcore implements ITemplateGenerator {

    /*
     * Template options
     */
    /**
     * Template option for a single builder. This creates a single builder file
     * for all EClasses in the ECore model.
     */
    public static final String SINGLE_BUILDER_OPTION = "singleBuilder";
    /**
     * Template option for a multiple builder. Creates a own builder class and
     * file for each EClass in the ECore Model.
     */
    public static final String MULTIPLE_BUILDER_OPTION = "multiBuilder";

    /*
     * Template paths
     */
    /**
     * Path to root directory of the ECore Templates.
     */
    private static final String ECORE_TEMPLATES_DIR_PATH = 
            StringTemplateUtil.STRING_TEMPLATES_DIR_PATH
            + "ecore_templates/";

    /*
     * Template fileNames
     */
    /**
     * Name of the StringTemplate file for the {@link #MULTIPLE_BUILDER_OPTION}.
     */
    private static final String MULTI_TEMPLATE_FILE = "MultipleBuilder.stg";
    /**
     * Name of the StringTemplate file for the {@link #SINGLE_BUILDER_OPTION}.
     */
    private static final String SINGLE_TEMPLATE_FILE = "SingleBuilder.stg";
    /**
     * Error message if not a valid template option was given.
     */
    private static final String WRONG_OPTION_ECORE =
            "Not a valid templateOption given. "
            + "For a genmodel source only:"
            + SINGLE_BUILDER_OPTION
            + " OR "
            + MULTIPLE_BUILDER_OPTION + " allowed.";

    /**
     * Starts generation of files with given template.
     * 
     * @param parser
     *            the {@link ParserEcore} which holds the DSLGenerationModel
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
        case SINGLE_BUILDER_OPTION:
            filePath = generateSingleBuilderDSL(dslModel, targetPackage);
            break;
        case MULTIPLE_BUILDER_OPTION:
            generateMultipleBuilderDSL(dslModel, targetPackage, filesCreated);
            break;
        default:
            throw new IllegalArgumentException(WRONG_OPTION_ECORE);
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
     * Generates a DSL with multiple Builders. For each ModelClassin the
     * DSLGenerationModel a Builder in generated in its own java file. Is
     * designed for DSLGenerationModels created by {@link ParserEcore}, but can
     * be used with every DSLGenerationModel if the template is suited for it.
     * In case of ParserEcore generated DSLGenerationModel every EClass gets its
     * own Builder.
     * 
     * @param dslModel
     *            {@link DSLGenerationModel}
     * @param targetPackage
     *            the target package the files are generated at
     * @param filesCreated
     *            a list to which the paths of created files are added
     */
    private void generateMultipleBuilderDSL(final DSLGenerationModel dslModel,
            final String targetPackage, final List<String> filesCreated) {
        for (ModelClass modelClass : dslModel.getClasses()) {
            String filePath = generateMultiBuilderFile(modelClass,
                    targetPackage);
            if (filePath != null) {
                filesCreated.add(filePath);
            }
        }
    }

    /**
     * Method for generating a single Builder file for ecore multi builder
     * option.
     * 
     * @param modelClass
     *            the modelClass of the DSLGenerationModel the builder should be
     *            created for.
     * @param targetPackage
     *            the target package
     * @return path of File which was created
     * @see #generateMultipleBuilderDSL(DSLGenerationModel, String, List)
     */
    private String generateMultiBuilderFile(final ModelClass modelClass,
            final String targetPackage) {
        STGroup group = StringTemplateUtil
                .getStringTemplateFromPath(ECORE_TEMPLATES_DIR_PATH
                        + MULTI_TEMPLATE_FILE);
        ST modelTemplate = group.getInstanceOf("ClassBuilder");
        modelTemplate.add("packageName", targetPackage);
        modelTemplate.add("modelClass", modelClass);
        String modelCode = modelTemplate.render();
        return StringTemplateUtil.writeToJavaFile(targetPackage,
                modelClass.getClassName() + "Builder", modelCode);
    }

    /**
     * Generates a DSL with a Single Builder file. Is designed for
     * DSLGenerationModels created by {@link ParserEcore}, but can be used with
     * every DSLGenerationModel if the template is suited for it.
     * 
     * @param dslModel
     *            {@link DSLGenerationModel}
     * @param targetPackage
     *            the target package the files are generated at
     *@return name of the File which was created or null if exception ocurred
     */
    private String generateSingleBuilderDSL(final DSLGenerationModel dslModel,
            final String targetPackage) {
        STGroup group = StringTemplateUtil
                .getStringTemplateFromPath(ECORE_TEMPLATES_DIR_PATH
                        + SINGLE_TEMPLATE_FILE);
        ST simpleBT = group.getInstanceOf("SingleBuilder");
        simpleBT.add("packageName", targetPackage);
        simpleBT.add("genModel", dslModel);
        return StringTemplateUtil.writeToJavaFile(targetPackage,
                dslModel.getModelName() + "Builder", simpleBT.render());
    }
}
