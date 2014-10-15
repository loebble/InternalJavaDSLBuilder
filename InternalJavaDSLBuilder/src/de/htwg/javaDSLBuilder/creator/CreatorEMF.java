package de.htwg.javaDSLBuilder.creator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.htwg.javaDSLBuilder.dslmodel.AttributeKind;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel.ClassAttribute;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel.ModelClass;

public class CreatorEMF implements ICreator {
	private String modelPackage;
	private EPackage ePackage;
	private Set<String> primitiveTypes;
	private Map<String, String> definedEClasses;
	private Map<String, String> parameterType;
	private Map<String, String> setterMethods;
	private Map<String, Map<String, String>> eclassWithMethods;

	private String dslName;
	private String entryPointMethod;
	private String buildMethodName;
	private ArrayList<String> imports;
	@Deprecated
	private List<EReference> incompleteOpposites = new ArrayList<>();

	private final DSLGenerationModel genModel;
	
	private final String OPPOSITES_DEFINED_WRONG = "Opposite releation not correct defined. ";
	private final String WRONG_ECORE = "Make sure to have a correct Ecore Model which you can save and generate code from. ";
	private final String WRONG_ARG_TYPE = "has wrong Type, only EAttribute or EReference allowed. ";
	private final String NO_OPPOSITE = "Reference has no opposite";
	
	@Override
	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}

	private CreatorEMF() {
		this.genModel = new DSLGenerationModel();
	}

	public static <T extends EPackage> CreatorEMF getInstance(T ePackage) {
		CreatorEMF creator = new CreatorEMF();
		EPackage eP = (EPackage) ePackage;
		creator.ePackage = eP;
		creator.genModel.setModelName(eP.getName());
		creator.retrieveAttributes();
		// creator.factoryClass = Class.forName(fullyQUalifiedFactoryName);
		// creator.modelPackage = creator.factoryClass.getPackage().getName();
		// creator.builderModel.addImports(creator.modelPackage);
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
		
		for (Iterator iter = this.ePackage.getEClassifiers().iterator(); iter.hasNext();) {
			
			EClassifier classifier = (EClassifier) iter.next();
			if (classifier instanceof EClass) {
				EClass eClass = (EClass) classifier;
				String eClassName = eClass.getName();
				String type = eClass.getInstanceTypeName();
				this.genModel.addImports(type);
				String packageName = eClass.getClass().getPackage().getName();
				ModelClass modelClass = this.genModel.new ModelClass(eClassName);
				this.genModel.getClasses().put(eClassName, modelClass);
				for (Iterator ai = eClass.getEStructuralFeatures().iterator(); ai.hasNext();) {
					EStructuralFeature feature = (EStructuralFeature)ai.next();
					if (feature instanceof EAttribute) {
						EAttribute foundAttribute = (EAttribute) feature;
						if(!foundAttribute.isChangeable()) //TODO should create a corresponding setter Method for the attribute
							continue;
						ClassAttribute modelAttribute = createModelAttribute(foundAttribute, modelClass.getClassName());
						modelClass.addAttribute(modelAttribute);
					}else if(feature instanceof EReference) {
						EReference reference = (EReference) feature;
						ClassAttribute modelAttribute = createModelAttribute(reference, modelClass.getClassName());
						modelAttribute.setReference(true);
						modelClass.addAttribute(modelAttribute);
						EReference opposite = reference.getEOpposite();
						if(opposite != null){
							// Opposite attribute could not have been processed yet
//							System.out.println(modelAttribute.getAttributeFullName() + " refBy " + reference.getEReferenceType().getName()+ opposite.getName());
							ClassAttribute oppositeAttr = findAttribute(reference.getEReferenceType().getName(), opposite.getName());
							if(oppositeAttr == null){
								// if not processed the current attribute is the "creator" and is Referenced by the other one
								System.out.println("creator: "+ modelAttribute.getAttributeFullName());
								modelAttribute.setCreatorOfOpposite(true);
							}else{
								//Sets the end of an opposite relation
								modelAttribute.setOpposite(oppositeAttr);
								System.out.println("opposite created for "+modelAttribute.getAttributeFullName() +" ->"+oppositeAttr.getAttributeFullName());
								oppositeAttr.setOpposite(modelAttribute);
								System.out.println("opposite created for "+oppositeAttr.getAttributeFullName() +" ->"+modelAttribute.getAttributeFullName());
								ClassAttribute creator = null;
								if(modelAttribute.isCreatorOfOpposite()) //TODO do it nicer
									creator = modelAttribute;
								else if (oppositeAttr.isCreatorOfOpposite()) {
									creator = oppositeAttr;
								}
								System.out.println("Creator is "+creator.getAttributeFullName());
								creator.setReferencedByAttribute(true);
								creator.setReferencedBy(creator.getOpposite());
								this.incompleteOpposites.add(reference);
							}
						}
						//TODO multiplicities or not because its a runtime matter
//						reference.getLowerBound();
//						reference.getUpperBound();
					}else if (feature instanceof EEnum){ //Get Enums
						EEnum eEnum = (EEnum)classifier;
						for (Iterator ei = eEnum.getELiterals().iterator();ei.hasNext();){
							EEnumLiteral literal = (EEnumLiteral) ei.next();
							System.out.println("ENUM: "+literal.getName());
						}
					}
					else if (feature instanceof EDataType) { //TODO Enum handling
						EDataType eDataType = (EDataType)classifier;
						System.out.println("EDATATYPE: "+eDataType.getName());
					}
				}
			}
		}
		
	}

	/**
	 * Completes the opposite relations of references.
	 * Only call Method after {@link #retrieveAttributes()}
	 * @param opposites
	 */
	@Deprecated
	private void completeOppositeRelation() {
		for (EReference eReference : this.incompleteOpposites) {
			System.out.println("Completing Opp Relation");
			System.out.println(eReference.getName());
			EReference oppositeRef = eReference.getEOpposite();
			if(oppositeRef == null)
				throw new IllegalArgumentException(NO_OPPOSITE);
			ClassAttribute existingOpAttribute = findAttribute(eReference.getEReferenceType().getName(), eReference.getName());
			ClassAttribute newOpAttribute = findAttribute(oppositeRef.getEReferenceType().getName(), oppositeRef.getName());
			//Is set by retrieve attributes
			if(newOpAttribute.isCreatorOfOpposite()){
				System.out.print("found Creator for this opposite");
				newOpAttribute.setOpposite(existingOpAttribute);
				newOpAttribute.setReferencedBy(existingOpAttribute);
			}
		}
		
	}

	/**
	 * Searches the DSLGenerationModel for the opposite ClassAttribute which is defined by the the references EOpposite object
	 * @param reference the EReference which has an opposite attribute defined
	 * @return the found ClassAttribute in the DSLGenerationModel or null if none has been found
	 */
	
	
	private ClassAttribute findAttribute(String className, String attrName){
		for (Map.Entry<String,ModelClass> classEntry: genModel.getClasses().entrySet()) {
			ModelClass modelClass = classEntry.getValue();
			if(!modelClass.getClassName().equals(className))
				continue;
			ClassAttribute foundAttr = modelClass.getSpefificAttributeByFullName(className+attrName);
			if(foundAttr != null){
				return foundAttr;
			}
		}
		return null;
	}

	/**
	 * * Creates ClassAttribute in this classes DSLGenerationModel {@link #genModel}
	 * and initializes it with given parameter
	 * @param eClassifier can be an EAttribute or an EReference
	 * @param className the class in which the attribute is defined
	 * @return
	 */
	private <T extends EStructuralFeature> ClassAttribute createModelAttribute(T eClassifier, String className) {
		String name = eClassifier.getName();
		String type = "";
		boolean isList = false;
		AttributeKind kind;
		if(eClassifier instanceof EAttribute){
			type = ((EAttribute) eClassifier).getEAttributeType().getInstanceClassName();
		}
		else if(eClassifier instanceof EReference){
			EReference ref = (EReference) eClassifier;
			// Cut the package from the type (packages are imported automatically)
			type = ref.getEType().getName();
		}
		else
			throw new IllegalArgumentException("Parameter eClassifier " +WRONG_ARG_TYPE);
		
		boolean optional = !eClassifier.isRequired();
		if(optional)
			kind = AttributeKind.ATTRIBUTE;
		else
			kind = AttributeKind.OPTIONAL_ATTRIBUTE;
		if(eClassifier.isMany()){
			kind = AttributeKind.LIST_OF_ATTRIBUTES;
			isList = true;
		}
		ClassAttribute attribute = genModel.new ClassAttribute(name,type,className);
		attribute.setAttributeKind(kind);
		attribute.setList(isList);
		return attribute;
	}

//	private void retrieveSetters(String eclassName, String qualifiedClass,
//			ModelClass modelClass) throws ClassNotFoundException {
//		Class eClass = Class.forName(qualifiedClass);
//		Map<String, String> setterMethods = new LinkedHashMap<>();
//		for (Method m : eClass.getDeclaredMethods()) {
//			String methodName = m.getName();
//			Class[] parameterTypes = m.getParameterTypes();
//			if (!methodName.startsWith("set") || parameterTypes.length != 1)
//				continue;
//			for (Class paramType : parameterTypes) {
//				ClassAttribute attr = genModel.new ClassAttribute();
//				String canocicalType = paramType.getCanonicalName();
//				setterMethods.put(methodName, canocicalType); // TODO annotation
//																// for optional
//																// and default
//																// value
//				attr.setType(canocicalType);
//				attr.setAttributeName(methodName);
//				modelClass.addAttribute(attr);
//			}
//			if (setterMethods.size() != 0)
//				this.eclassWithMethods.put(eclassName, setterMethods);
//		}
//	}

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

	public Set<String> getPrimitiveTypes() {
		return primitiveTypes;
	}

	public void setPrimitiveTypes(Set<String> primitiveTypes) {
		this.primitiveTypes = primitiveTypes;
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
