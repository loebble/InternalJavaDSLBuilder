package de.htwg.javafluentdsl.main;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import de.htwg.javafluentdsl.generator.GeneratorEcore;
import de.htwg.javafluentdsl.parser.IParser;
import de.htwg.javafluentdsl.parser.ParserEcore;

/**
 * Class for starting the Application with a EMF {@link GenModel} via the
 * {@link #startDSLGenerationProcess(String, String, String)} method.
 *
 */
public class StartEMF implements IStart {

    /*
     * Exception messages
     */
    /**
     * Exception Message if path leads to no genmodel file.
     */
    private static final String GENMODEL_NO_FILE = 
            "The given path doesnt lead to a valid file!"
            + "For a given GenModel  only "
            + GeneratorEcore.SINGLE_BUILDER_OPTION
            + " or "
            + GeneratorEcore.MULTIPLE_BUILDER_OPTION + "are allowed";
    /**
     * Exception Message if genmodel has no generation model.
     */
    private static final String GENMODEL_HAS_NO_GENERATIONMODEL = 
            "The genmodel File has no generation model declared!"
            + " See EMFs documentation for correct genmodel creation";
    /**
     * Exception Message if genmodel has multiple genpackages defined.     
     */
    private static final String NO_MULTIPLE_GENPACKAGES_SUPPORTED = 
            "The current version does not support "
            + "multiple genPackage declarations in a genmodel";
    /**
     * Exception Message if multiple EPackages are in ECore Model.
     */
    private static final String NO_MULTIPLE_PACKAGES_SUPPORTED = 
            "The current version does not support "
            + "multiple package declarations in a genmodel";
    /**
     * Exception Message if no EPackages was found in the ECore Model.
     */
    private static final String NO_EPACKAGE_FOUND = 
            "There was no defined EPackage found."
            + " Make sure the genmodel and the ecore files are correct."
            + " Try to put both files under the same directory "
            + "and give the absolute path of the "
            + "genfile as source if its still not working";

    /**
     * 
     * @param pathToGenModelFile
     *            the absolute path to the EMF Generator Model file
     * @param templateOption 
     *            the template which should be used for generation
     * @param targetPackage the relative target package. 
     *            Preferred with typical package naming conventions 
     *            domains seperated by '.' and all in small letters.
     */
    @Override
    public final void startDSLGenerationProcess(
            final String pathToGenModelFile, final String templateOption,
            final String targetPackage) {
        if (!new File(pathToGenModelFile).isFile()) {
            throw new IllegalArgumentException(GENMODEL_NO_FILE + " Path: "
                    + pathToGenModelFile);
        }
        org.eclipse.emf.common.util.URI genModelURI = 
                org.eclipse.emf.common.util.URI
                .createFileURI(pathToGenModelFile);
        ResourceSet set = registerResourceSet();
        Resource res = set.getResource(genModelURI, true);
        GenModel genModel = getGenModelFromResourceSet(res);
        GenPackage genPackage = getGenPackageFromGenModel(genModel);
        String packageName = genPackage.getBasePackage();
        EPackage rootEPackage = genPackage.getEcorePackage();
        String factoryName = genPackage.getFactoryName();
        genPackage.initialize(rootEPackage);
        String prefix = genPackage.getPrefix();
        if (packageName != null) {
            packageName += ".";
        } else {
            System.out
                    .println(
                        "The generator model has no base package defined. \n "
                        + "This is not recommended. Please check the import "
                        + "statements of the generated Classes.");
        }
        // If generator model have prefix defined
        if (prefix != null && !prefix.equals("")) {
            packageName = packageName + prefix;
        }
        IParser parser = ParserEcore.getInstance(rootEPackage, packageName,
                factoryName);
        new GeneratorEcore().generateDSL(parser, templateOption, targetPackage);
    }

    /**
     * Retrieves the Generation Package {@link GenPackage} from the generation
     * model {@link GenModel}.
     * Throws an exception if there are multiple or no GenPackages defined.
     * @param genModel
     *            the emf generation model
     * @return the found GenPackage
     */
    private static GenPackage getGenPackageFromGenModel(
            final GenModel genModel) {
        List<GenPackage> genPkgs = genModel.getGenPackages();
        if (genPkgs.size() > 1) {
            throw new IllegalStateException(NO_MULTIPLE_PACKAGES_SUPPORTED);
        }
        if (genPkgs.size() == 0) {
            throw new IllegalStateException(NO_EPACKAGE_FOUND);
        }
        GenPackage genPackage = null;
        for (GenPackage gPck : genPkgs) {
            genPackage = gPck;
        }
        return genPackage;
    }

    /**
     * Retrieves the generation model {@link GenModel} from a Resource.
     * Make sure the resource has a valid Registry
     * for loading genmodel and ecore files from the resource.
     * 
     * @param resource
     *            the Resource object with valid Registry
     * @return the found GenModel
     * @see #registerResourceSet()     */
    private static GenModel getGenModelFromResourceSet(
            final Resource resource) {
        GenModel genModel = null;
        TreeIterator<EObject> list = resource.getAllContents();
        while (list.hasNext()) {
            EObject obj = list.next();
            if (obj instanceof GenModel) {
                if (genModel != null) {
                    throw new IllegalStateException(
                            NO_MULTIPLE_GENPACKAGES_SUPPORTED);
                }
                genModel = (GenModel) obj;
            }
        }
        if (genModel == null) {
            throw new IllegalStateException(GENMODEL_HAS_NO_GENERATIONMODEL);
        }
        genModel.reconcile();
        return genModel;
    }

    /**
     * Registers needed EMF Registry in a {@link ResourceSet} for genmodel and
     * ecore loading.
     * 
     * @return new ResourceSet for loading genmodel and ecore files
     */
    private static ResourceSet registerResourceSet() {
        ResourceSet set = new ResourceSetImpl();
        EcoreResourceFactoryImpl ecoreFactory = new EcoreResourceFactoryImpl();
        Resource.Factory.Registry registry = set.getResourceFactoryRegistry();
        Map<String, Object> map = registry.getExtensionToFactoryMap();
        map.put("ecore", ecoreFactory);
        map.put("genmodel", ecoreFactory);
        GenModelPackage.eINSTANCE.eClass();
        return set;
    }

}
