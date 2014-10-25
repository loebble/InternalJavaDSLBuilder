package de.htwg.javafluentdsl.creator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
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

public class CreatorEMF implements ICreator {
	private String modelPackage;
	private EPackage ePackage;
	private Map<String, String> definedEClasses;
	private Map<String, Map<String, String>> eclassWithMethods;

	private String dslName;
	private String buildMethodName;
	private ArrayList<String> imports;
	
	private final DSLGenerationModel genModel;
	
	private final String WRONG_ARG_TYPE = "has wrong Type, only EAttribute or EReference allowed. ";
	private String packageName;
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}

	private CreatorEMF() {
		this.genModel = new DSLGenerationModel();
	}

	public static <T extends EPackage> CreatorEMF getInstance(T ePackage, String fullPackageName,String factoryName) {
		CreatorEMF creator = new CreatorEMF();
		EPackage eP = (EPackage) ePackage;
		creator.ePackage = eP;
		creator.packageName = fullPackageName; 
		creator.genModel.setModelName(eP.getName());
		creator.genModel.setEmfFactoryName(factoryName);
		creator.retrieveAttributes();
		creator.genModel.setAttributeOrder();
		return creator;
	}


	private void retrieveAttributes() {
		if(this.ePackage.getEClassifiers().size() == 0){
			System.err.println("No Classifiers in Package: "+this.ePackage.getClass().getCanonicalName());
			return;
		}
		EFactory eFactory = this.ePackage.getEFactoryInstance();
		if(eFactory == null){
			System.err.println("No EFactoryInstance found for Package: "+this.ePackage.getClass().getCanonicalName());
			return;
		}
		
		for (Iterator<EClassifier> iter = this.ePackage.getEClassifiers().iterator(); iter.hasNext();) {
			
			EClassifier classifier = (EClassifier) iter.next();
			if (classifier instanceof EClass) {
				EClass eClass = (EClass) classifier;
				if(eClass.isAbstract() || eClass.isInterface()) //TODO can be ignored completely?
					continue;
				String eClassName = eClass.getName();
				ModelClass modelClass = this.genModel.addModelClass(eClassName);
				modelClass.addImport(this.packageName +"."+modelClass.getClassName());
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
//							System.out.println(modelAttribute.getAttributeFullName() + " refBy " + reference.getEReferenceType().getName()+ opposite.getName());
							ClassAttribute oppositeAttr = this.genModel.findAttribute(reference.getEReferenceType().getName(), opposite.getName());
							if(oppositeAttr == null){
								//Sets the start of an opposite relation
								// -> if not processed the current attribute is the "creator" and is Referenced by the other one
//								System.out.println("creator: "+ modelAttribute.getAttributeFullName());
								modelAttribute.setCreatorOfOpposite(true);
							}else{
								//Sets the end of an opposite relation
								modelAttribute.setOpposite(oppositeAttr);
//								System.out.println("opposite created for "+modelAttribute.getAttributeFullName() +" ->"+oppositeAttr.getAttributeFullName());
								oppositeAttr.setOpposite(modelAttribute);
//								System.out.println("opposite created for "+oppositeAttr.getAttributeFullName() +" ->"+modelAttribute.getAttributeFullName());
								ClassAttribute creator = null;
								if(modelAttribute.isCreatorOfOpposite()) //TODO do it nicer
									creator = modelAttribute;
								else if (oppositeAttr.isCreatorOfOpposite()) {
									creator = oppositeAttr;
								}
//								System.out.println("Creator is "+creator.getAttributeFullName());
								creator.setReferencedByAttribute(true);
								creator.setReferencedBy(creator.getOpposite());
							}
						}
					}else if (feature instanceof EEnum){ //TODO Enums
						EEnum eEnum = (EEnum)classifier;
						for (Iterator<EEnumLiteral> ei = eEnum.getELiterals().iterator();ei.hasNext();){
							EEnumLiteral literal = (EEnumLiteral) ei.next();
							System.out.println("ENUM: "+literal.getName());
						}
					}
					else if (feature instanceof EDataType) { //TODO Not needed, because EData only defines type
						EDataType eDataType = (EDataType)classifier;
						System.out.println("EDATATYPE: "+eDataType.getName());
					}
				}
			}
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
			modelClass.addImport(this.packageName +"."+modelClass.getModel().getEmfFactoryName());
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
		return attribute;
	}


	public void printEclassWithMethods() {
		if (this.eclassWithMethods.size() <= 0)
			return;
		for (Map.Entry<String, Map<String, String>> eClass : this
				.getEclassWithMethods().entrySet()) {
			System.out.println("EClass: " + eClass.getKey());
			System.out.println("Methods: ");
			if (eClass.getValue().size() <= 0) {
				System.out.println("No setters!");
				continue;
			}
			for (Map.Entry<String, String> method : eClass.getValue()
					.entrySet()) {
				System.out.println(method.getKey() + "(" + method.getValue()
						+ ")");
			}
		}
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public Map<String, String> getDefinedEClasses() {
		return definedEClasses;
	}

	public Map<String, Map<String, String>> getEclassWithMethods() {
		return eclassWithMethods;
	}

	public String getDslName() {
		return dslName;
	}

	public String getBuildMethodName() {
		return buildMethodName;
	}

	public ArrayList<String> getImports() {
		return imports;
	}

}
