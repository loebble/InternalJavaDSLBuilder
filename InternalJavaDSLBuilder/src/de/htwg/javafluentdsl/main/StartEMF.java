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
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import de.htwg.javafluentdsl.creator.CreatorEMF;
import de.htwg.javafluentdsl.generator.Generator;

/**
 * Class for starting the Application with a EMF {@link GenModel} 
 * via the {@link #startDSLGenerationProcess(String, String, String)} method.
 *
 */
public class StartEMF implements IStart {
	
	/*
	 * Exception messages
	 */
	private static final String GENMODEL_NO_FILE = "The given path doesnt lead to a valid file!"
			+ "For a given GenModel  only " + Generator.SINGLE_BUILDER_OPTION 
			+" or "+ Generator.MULTIPLE_BUILDER_OPTION +"are allowed";
	private static final String GENMODEL_HAS_NO_GENERATIONMODEL = "The genmodel File has no generation model declared!"
			+ " See EMFs documentation for correct genmodel creation";
	private static final String NO_MULTIPLE_GENPACKAGES_SUPPORTED = 
			"The current version does not support multiple genPackage declarations in a genmodel";
	private static final String NO_MULTIPLE_PACKAGES_SUPPORTED = 
			"The current version does not support multiple package declarations in a genmodel";
	private static final String NO_EPACKAGE_FOUND = 
			"There was no defined EPackage found. Make sure the genmodel and the ecore files are correct."
			+ " Try to put both files under the same directory "
			+ "and give the absolute path of the genfile as source if its still not working";
	
	
	/**
	 * 
	 * @param source the absolute path to the EMF Generator Model file
	 * 
	 */
	@Override
	public void startDSLGenerationProcess(String source, String templateOption, String targetPackage){
		if(!new File(source).isFile())
			throw new IllegalArgumentException(GENMODEL_NO_FILE + " Path: "+source);
		org.eclipse.emf.common.util.URI genModelURI = org.eclipse.emf.common.util.URI
				.createFileURI(source);
		
		ResourceSet set = registerResourceSet();
		Resource res = set.getResource(genModelURI, true);
		GenModel genModel = getGenModelFromResourceSet(res);
		GenPackage genPackage = getGenPackageFromGenModel(genModel);
		
		EPackage rootEPackage = null;
		String packageName = "";
		String factoryName = "";
		String basePackage = genPackage.getBasePackage();
		rootEPackage = genPackage.getEcorePackage();
		factoryName = genPackage.getFactoryName();
		genPackage.initialize(rootEPackage);
		String prefix = genPackage.getPrefix();
		if(basePackage != null)
			packageName = basePackage + ".";
		else
			System.out.println("The EMF generation model has no base package defined. \n "
					+ "This is not recommended. Please check the import statements of the generated Classes.");
		if (prefix != null && !prefix.equals(""))
			packageName = packageName  + prefix;
		CreatorEMF creator = CreatorEMF.getInstance(rootEPackage, packageName,
				factoryName);
		Generator.buildDSL(creator, templateOption,
				targetPackage);

	}

	/**
	 * Retrieves the Generation Package {@link GenPackage} from the generation model {@link GenModel}
	 * @param genModel the emf generation model
	 * @return
	 */
	private static GenPackage getGenPackageFromGenModel(GenModel genModel) {
		List<GenPackage> genPkgs = genModel.getGenPackages();
		if(genPkgs.size() > 1)
			throw new IllegalStateException(NO_MULTIPLE_PACKAGES_SUPPORTED);
		if(genPkgs.size() == 0)
			throw new IllegalStateException(NO_EPACKAGE_FOUND);
		GenPackage genPackage = null;
		for (GenPackage gPck : genPkgs) {
			genPackage = gPck;
		}
		return genPackage;
	}

	/**
	 * Retrieves the generation model {@link GenModel} from a Resource {@link Resource}.
	 * Make sure the resource has a valid Registry {@link Registry} for loading genmodel and ecore files from the resource.
	 * @param set the Resource object with valid Registry
	 * @return the found GenModel
	 * @see for valid registry {@link #registerResourceSet()
	 */
	private static GenModel getGenModelFromResourceSet(Resource resource) {
		GenModel genModel = null;
		TreeIterator<EObject> list = resource.getAllContents();
		while (list.hasNext()) {
			EObject obj = list.next();
			if (obj instanceof GenModel) {
				if(genModel != null)
					throw new IllegalStateException(NO_MULTIPLE_GENPACKAGES_SUPPORTED);
				genModel = (GenModel) obj;
			}
		}
		if(genModel == null)
			throw new IllegalStateException(GENMODEL_HAS_NO_GENERATIONMODEL);
		genModel.reconcile();
		return genModel;
	}

	/**
	 * Registers needed EMF Registry in a {@link ResourceSet} for genmodel and ecore loading
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
