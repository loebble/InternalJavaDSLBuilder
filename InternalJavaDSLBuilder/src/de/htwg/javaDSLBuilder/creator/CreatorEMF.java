package de.htwg.javaDSLBuilder.creator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import de.htwg.javaDSLBuilder.model.DSLGenerationModel;
import de.htwg.javaDSLBuilder.model.DSLGenerationModel.ClassAttribute;
import de.htwg.javaDSLBuilder.model.DSLGenerationModel.ModelClass;

public class CreatorEMF implements ICreator{
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
	
	private DSLGenerationModel genModel;

	public DSLGenerationModel getGenerationModel() {
		return this.genModel;
	}

	private CreatorEMF() {}

	public static <T extends EPackage> CreatorEMF getInstance(T ePackage) {
		CreatorEMF creator = new CreatorEMF();
		EPackage eP = (EPackage) ePackage;
		creator.ePackage = eP; 
		creator.genModel = new DSLGenerationModel();
		creator.genModel.setModelName(eP.getName());
		creator.retrieveAttributes();
//		creator.factoryClass = Class.forName(fullyQUalifiedFactoryName);
//		creator.modelPackage = creator.factoryClass.getPackage().getName();
//		creator.builderModel.addImports(creator.modelPackage);
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
				String className = classifier.getName();
				String type = classifier.getInstanceTypeName();
				this.genModel.addImports(type);
				ModelClass modelClass = this.genModel.new ModelClass(className);
				this.genModel.getClasses().put(className, modelClass);
				Map<String, String> attributes = new LinkedHashMap<>();
				ClassAttribute firstRequiredAttr = null;
				ClassAttribute previousRequiredAttr = null;
				List<ClassAttribute> firstOptionalAttribiutes = new ArrayList<ClassAttribute>();
				ClassAttribute lastRequiredClassAttr = null;
				for (Iterator ai = eClass.getEAttributes().iterator(); ai.hasNext();) { //Get Attributes
					EAttribute foundAttribute = (EAttribute) ai.next();
					if(!foundAttribute.isChangeable()) //TODO should create a corresponding setter Method for the attribute
						continue;
					boolean optional = !foundAttribute.isRequired();
					ClassAttribute builderAttribute = genModel.new ClassAttribute();
					builderAttribute.setAttributeName(foundAttribute.getName());
					builderAttribute.setType(foundAttribute.getEAttributeType().getInstanceClassName());
					builderAttribute.setOptional(optional);
					modelClass.addAttribute(builderAttribute);
					if(!optional){//TODO check for correction
						if(firstRequiredAttr==null)
							firstRequiredAttr = builderAttribute;
						if(previousRequiredAttr==null)
							previousRequiredAttr = builderAttribute;
						else if(previousRequiredAttr!=null){
							previousRequiredAttr.setNextAttribute(builderAttribute);
							previousRequiredAttr = builderAttribute;
						}
					}
					if(optional){
						if(firstRequiredAttr==null)
							firstOptionalAttribiutes.add(builderAttribute);
						if(previousRequiredAttr!=null)
							previousRequiredAttr.addNextOptionalAttribute(builderAttribute);
					}
				}
				lastRequiredClassAttr = previousRequiredAttr; // TODO check for Last Attribute and buildMethod
				for (Iterator ri = eClass.getEReferences().iterator();ri.hasNext();){ //Get References
					EReference reference = (EReference) ri.next();
					boolean optionalClass = !reference.isRequired();
					if(optionalClass)
						lastRequiredClassAttr.addNextOptionalClass(reference.getEReferenceType().getName());
					else
						lastRequiredClassAttr.addNextClass(reference.getEReferenceType().getName());
//					System.out.println("REFERENCE: "+reference.getName()); //TODO multiplicities 
//					reference.getLowerBound();
//					reference.getUpperBound();
				}
			}
			else if (classifier instanceof EEnum){ //Get Enums
				EEnum eEnum = (EEnum)classifier;
				for (Iterator ei = eEnum.getELiterals().iterator();ei.hasNext();){
					EEnumLiteral literal = (EEnumLiteral) ei.next();
					System.out.println("ENUM: "+literal.getName());
				}
			}
			else if (classifier instanceof EDataType) { //TODO ???
				EDataType eDataType = (EDataType)classifier;
				System.out.println("EDATATYPE: "+eDataType.getName());
			}
		}
		
	}

	
	private void retrieveSetters(String eclassName, String qualifiedClass, ModelClass modelClass) throws ClassNotFoundException{
		Class eClass = Class.forName(qualifiedClass);
		Map<String, String> setterMethods = new LinkedHashMap<>();
		for (Method m : eClass.getDeclaredMethods()) {
			String methodName = m.getName();
			Class[] parameterTypes = m.getParameterTypes();
			if (!methodName.startsWith("set") || parameterTypes.length != 1)
				continue;
			for (Class paramType : parameterTypes) {
				ClassAttribute attr = genModel.new ClassAttribute();
				String canocicalType = paramType.getCanonicalName();
				setterMethods.put(methodName, canocicalType); //TODO annotation for optional and default value
				attr.setType(canocicalType);
				attr.setAttributeName(methodName);
				modelClass.addAttribute(attr);
			}
			if(setterMethods.size() != 0)
				this.eclassWithMethods.put(eclassName, setterMethods);
		}
	}
	
	
	public void printEclassWithMethods(){
		if(this.eclassWithMethods.size() <= 0)
			return;
		for (Map.Entry<String,Map<String,String>> eClass : this.getEclassWithMethods().entrySet()) {
			System.out.println("EClass: " + eClass.getKey());
			System.out.println("Methods: ");
			if(eClass.getValue().size() <= 0){
				System.out.println("No setters!");
				continue;
			}
			for (Map.Entry<String,String> method : eClass.getValue().entrySet()) {
				System.out.println(method.getKey()+"("+method.getValue()+")");
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
