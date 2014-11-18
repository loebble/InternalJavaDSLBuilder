package de.htwg.javafluentdsl.creator;

import java.util.Iterator;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.htwg.javafluentdsl.dslmodel.AttributeKind;
import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.DSLGenerationModel;
import de.htwg.javafluentdsl.dslmodel.ModelClass;

/**
 * Class for Creating a {@link DSLGenerationModel} from EMFs Generator Model
 * or more precisely the {@link EPackage} defined inside the Generator Model.
 * 
 * @see {@link ICreator}
 */
public final class CreatorEMF implements ICreator {
	/**
	 * Holds the {@link EPackage} of this EMFCreator.
	 * Which is received through a {@link GenModel}
	 */
	private EPackage ePackage;
	
	/**
	 * Holds the created {@link DSLGenerationModel} 
	 * which this Creator creates
	 */
	private final DSLGenerationModel genModel;
	
	/**
	 * The absolute packageName the created files
	 */
	private String packageName;
	
	/*
	 * Exception Messages 
	 */
	/**
	 * Message for Wrong EClassifier
	 * @see #createModelAttribute(EStructuralFeature, ModelClass)
	 */
	private final String WRONG_ARG_TYPE = "has wrong Type, only EAttribute or EReference allowed. ";
	/**
	 * Warning text that occurs if the EPackage 
	 * name is different from the first modeled class
	 */
	private final String PACKAGENAME_NOT_EQUAL_ECLASSNAME = "The ePackage name is not equal to the first modeled "
			+ "EClass. This is not recomended. Only the multiBuilder option is suited for such models.";
	/**
	 * Message if the message has not at least one EClass defined (empty EPackage)
	 * @see #checkFirstEClass(EPackage)
	 */
	private final String NO_ECLASS_DEFINED = "The Package has no EClass modeled. Therefore no DSL can be created";
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}

	/**
	 * Private Constructor. This class should  be instantiated via 
	 * its {@link #getInstance(EPackage, String, String)} method
	 */
	private CreatorEMF() {
		this.genModel = new DSLGenerationModel();
	}

	/**
	 * Creates an Instance of this class {@link CreatorEMF} .
	 * An CreatorEMF needs any kind of EPackage to analyze its attributes etc.
	 * All the necessary methods are called in this method so that after it the
	 * Creators {@link #genModel} can be used.
	 * @param ePackage The {@link EPackage} to be analyzed
	 * @param fullPackageName the full base package name of the corresponding
	 * {@link GenPackage}. This tells the Creator where the generated EMF Models are located
	 * @param factoryName the Factory of the {@link GenPackage} which can be used to
	 * instantiate the EMF Models
	 * @return a new CreatorEMF instance with the created {@link DSLGenerationModel}
	 */
	public static <T extends EPackage> CreatorEMF getInstance(T ePackage, String fullPackageName,String factoryName) {
		CreatorEMF creator = new CreatorEMF();
		EPackage eP = (EPackage) ePackage;
		creator.ePackage = eP;
		creator.packageName = fullPackageName; 
		creator.genModel.setModelName(eP.getName());
		creator.genModel.setFactoryName(factoryName);
		creator.retrieveAttributes();
		creator.genModel.setAttributeOrder();
		return creator;
	}

	/**
	 * Analyzes this {@link #ePackage} for its defined classes and attributes
	 * to be able to generate a valid {@link DSLGenerationModel}
	 */
	private void retrieveAttributes() {
		//check for no Classifier
		if(this.ePackage.getEClassifiers().size() == 0){
			System.err.println("No Classifiers in Package: "
					+this.ePackage.getClass().getCanonicalName());
			return;
		}
		EFactory eFactory = this.ePackage.getEFactoryInstance();
		//Without factory there can be no model instantiation
		if(eFactory == null){
			System.err.println("No EFactoryInstance found for Package: "
					+this.ePackage.getClass().getCanonicalName());
			return;
		}
		
		this.checkFirstEClass(ePackage);
		//traverse through the EPackages classifiers to receive attributes and classes
		for (Iterator<EClassifier> iter = this.ePackage.getEClassifiers().iterator(); iter.hasNext();) {
			
			EClassifier classifier = (EClassifier) iter.next();
			if (classifier instanceof EClass) {
				EClass eClass = (EClass) classifier;
				if(eClass.isAbstract() || eClass.isInterface()) //TODO can be ignored completely?
					continue;
				String eClassName = eClass.getName();
				ModelClass modelClass = this.genModel.addModelClass(eClassName);
				modelClass.addImport(this.packageName +"."+modelClass.getClassName());
				modelClass.addImport(this.packageName +"."+modelClass.getModel().getFactoryName());
				this.genModel.addImport(this.packageName +"."+modelClass.getClassName());
				this.genModel.getClasses().put(eClassName, modelClass);
				for (Iterator<EStructuralFeature> ai = eClass.getEStructuralFeatures().iterator(); ai.hasNext();) {
					EStructuralFeature feature = (EStructuralFeature)ai.next();
					if (feature instanceof EAttribute) {
						EAttribute foundAttribute = (EAttribute) feature;
						if(!foundAttribute.isChangeable()) //TODO should create a corresponding setter Method for the attribute
							continue;
						ClassAttribute modelAttribute = createModelAttribute(foundAttribute, modelClass);
						modelClass.addAttribute(modelAttribute);
					}else if(feature instanceof EReference) {
						EReference reference = (EReference) feature;
						ClassAttribute modelAttribute = createModelAttribute(reference, modelClass);
						modelAttribute.setReference(true);
						modelClass.addAttribute(modelAttribute);
						EReference opposite = reference.getEOpposite();
						if(opposite != null){
							// Opposite attribute could not have been processed yet
							ClassAttribute oppositeAttr = this.genModel.findAttribute(reference.getEReferenceType().getName(), opposite.getName());
							if(oppositeAttr == null){
								//Sets the start of an opposite relation
								// -> if not processed the current attribute is the "creator" and is Referenced by the other one
								modelAttribute.setCreatorOfOpposite(true);
							}else{
								//Sets the end of an opposite relation
								modelAttribute.setOpposite(oppositeAttr);
								oppositeAttr.setOpposite(modelAttribute);
								ClassAttribute creator = null;
								//Decide which opposite is called first in the chain
								//this is equal to the creator of the opposite relation
								if(modelAttribute.isCreatorOfOpposite()){
									creator = modelAttribute;
									creator.setReferencedByAttribute(true);
									creator.setReferencedBy(creator.getOpposite());
								}else if (oppositeAttr.isCreatorOfOpposite()) {
									creator = oppositeAttr;
									creator.setReferencedByAttribute(true);
									creator.setReferencedBy(creator.getOpposite());
								}
							}
						}
					}else if (feature instanceof EEnum){ // TODO Enums
						EEnum eEnum = (EEnum)classifier;
						for (Iterator<EEnumLiteral> ei = eEnum.getELiterals().iterator();ei.hasNext();){
							EEnumLiteral literal = (EEnumLiteral) ei.next();
							System.out.println("ENUM: "+literal.getName());
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Checks if the first EClass has the same Name as the {@link EPackage} ePackage
	 * And gives a prints a warning if thats not the case
	 * @param ePackage the EPackage to analyze
	 * @throws IllegalStateException if ePackage has no EClasses defined
	 */
	private void checkFirstEClass(EPackage ePackage) {
		EClass firstClass = null;
		for (Iterator<EClassifier> iter = ePackage.getEClassifiers().iterator(); iter.hasNext();) {
			EClassifier classifier = (EClassifier) iter.next();
			if (classifier instanceof EClass) {
				firstClass = (EClass) classifier;
				break;
			}else
				continue;
		}
		if(firstClass != null){
			if(!ePackage.getName().equals(firstClass.getName()))
					System.err.println("Warning: "+ePackage.getName()
							+ " "+PACKAGENAME_NOT_EQUAL_ECLASSNAME);
		}else{
			throw new IllegalStateException(NO_ECLASS_DEFINED);
		}
		
	}

	/**
	 * * Creates ClassAttribute in this classes DSLGenerationModel {@link #genModel}
	 * and initializes it with given parameter
	 * @param eClassifier can be an EAttribute or an EReference
	 * @param modelClass the class in which the attribute is defined
	 * @throws IllegalArgumentException if eClassifier is neither instance of {@link EAttribute} or {@link EReference}
	 * @return the created classAttribute within the ModelClass
	 */
	private <T extends EStructuralFeature> ClassAttribute createModelAttribute(T eClassifier, ModelClass modelClass) {
		String clName = eClassifier.getName();
		String type = "";
		boolean isList = false;
		boolean isRef = false;
		AttributeKind kind;
		if(eClassifier instanceof EAttribute){
			type = ((EAttribute) eClassifier).getEAttributeType().getInstanceClassName();
		}
		else if(eClassifier instanceof EReference){
			EReference ref = (EReference) eClassifier;
			type = ref.getEType().getName();
			modelClass.addImport(this.packageName +"."+type);
			this.genModel.addImport(this.packageName +"."+type);
			this.genModel.addImport(this.packageName +"."+modelClass.getModel().getFactoryName());
			isRef = true;
		}
		else
			throw new IllegalArgumentException("Parameter eClassifier " +WRONG_ARG_TYPE);
		
		boolean optional = !eClassifier.isRequired();
		if(optional)
			kind = AttributeKind.OPTIONAL_ATTRIBUTE;
		else
			kind = AttributeKind.ATTRIBUTE;
		if(eClassifier.isMany()){
			kind = AttributeKind.LIST_OF_ATTRIBUTES;
			isList = true;
		}
		ClassAttribute attribute = new ClassAttribute(clName,type,modelClass.getClassName());
		attribute.setAttributeKind(kind);
		attribute.setReference(isRef);
		attribute.setList(isList);
		if(!modelClass.isHasList() && isList)
			modelClass.setHasList(isList);
		if(!this.genModel.isHasList() && isList)
			this.genModel.setHasList(isList);
		return attribute;
	}

}
