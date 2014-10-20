package de.htwg.javaDSLBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;
import de.htwg.javaDSLBuilder.generator.GeneratorEMF;

public class DSLBuilder {
	
	private static final String GENMODEL_NO_FILE = "The given path doesnt lead to a valid file!";
	private static final String WRONG_BUILDER_KIND= "The given BuilderKind argument was not correct";
	private static final String GENMODEL_HAS_NO_GENERATIONMODEL = "The genmodel File has no generation model declared!"
			+ " See EMFs documentation for correct genmodel creation";
	private static final String NO_MULTIPLE_GENPACKAGES_SUPPORTED = "The current version does not support multiple genPackage declarations in a genmodel";
	private static final String NO_MULTIPLE_PACKAGES_SUPPORTED = "The current version does not support multiple package declarations in a genmodel";
	
	public static void main(String[] args) {
		if(args.length < 3){
			System.err.println("Not enough arguments given");
			return;
		}
		createDSLForEMF(args[0],args[1],args[2]);
	}

	public static void createDSLForEMF(String genModelFilePath, String builderKind, String targetPackage) {
		if(!new File(genModelFilePath).isFile())
			throw new IllegalArgumentException(GENMODEL_NO_FILE + " Path: "+genModelFilePath);
		if(builderKind.equals(GeneratorEMF.MULTIPLE_BUILDER_TEMPLATE) 
				&& builderKind.equals(GeneratorEMF.SINGLE_BUILDER_TEMPLATE))
			throw new IllegalArgumentException(WRONG_BUILDER_KIND);
		org.eclipse.emf.common.util.URI genModelURI = org.eclipse.emf.common.util.URI
				.createFileURI(genModelFilePath);
		ResourceSet set = registerResourceSet();
		Resource res = set.getResource(genModelURI, true);
		GenModel genModel = null;
		TreeIterator<EObject> list = res.getAllContents();
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
		List<GenPackage> genPkgs = genModel.getGenPackages();
		if(genPkgs.size() > 1)
			throw new IllegalStateException(NO_MULTIPLE_PACKAGES_SUPPORTED);
		EPackage rootPackage = null;
		String packagePath = "";
		String factoryName = "";
		for (GenPackage genPackage : genPkgs) {
			String basePackage = genPackage.getBasePackage();
			rootPackage = genPackage.getEcorePackage();
			factoryName = genPackage.getFactoryName();
			genPackage.initialize(rootPackage);
			String prefix = genPackage.getPrefix();
			packagePath = basePackage;
			if (prefix != null && !prefix.equals(""))
				packagePath = packagePath + "." + prefix;
		}
		
		CreatorEMF creator = CreatorEMF.getInstance(rootPackage, packagePath,
				factoryName);
		GeneratorEMF.buildDSL(creator, builderKind,
				targetPackage);

	}

	/**
	 * Registers needed EMF Registry for genmodel and ecore loading
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
