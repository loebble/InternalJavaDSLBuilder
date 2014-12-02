package de.htwg.javafluentdsl.parser;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.htwg.javafluentdsl.dslmodel.ClassAttribute;
import de.htwg.javafluentdsl.dslmodel.DependencyKind;
import de.htwg.javafluentdsl.dslmodel.ModelClass;
import de.htwg.javafluentdsl.dslmodel.PrimitiveType;

/**
 * Class for Creating a {@link DSLGenerationModel} from EMFs Generator Model or
 * more precisely the {@link EPackage} defined inside the Generator Model.
 * 
 * @see {@link IParser}
 */
public final class ParserEcore extends AbstractIParserBasic {

    /**
     * Holds the {@link EPackage} of this ParserEcore. Which is received through
     * a {@link GenModel}
     */
    private final EPackage ePackage;

    /**
     * The absolute packageName for the created files.
     */
    private final String packageName;

    /*
     * Exception Messages
     */
    /**
     * Message for Wrong EClassifier.
     * 
     * @see #createModelAttribute(EStructuralFeature, ModelClass)
     */
    private static final String WRONG_ARG_TYPE =
            "has wrong Type, only EAttribute or EReference allowed. ";
    /**
     * Warning text that occurs if the EPackage name is different from the first
     * modeled class.
     */
    private static final String PACKAGENAME_NOT_EQUAL_ECLASSNAME = 
            "The ePackage name is not equal to the first modeled "
            + "EClass. This is not recomended."
            + " Only the multiBuilder option is suited for such models.";
    /**
     * Message if the Epackage has not at least one EClass defined (empty
     * EPackage).
     * 
     * @see #checkFirstEClass(EPackage)
     */
    private static final String NO_ECLASS_DEFINED = 
            "The Package has no EClass modeled. "
            + "Therefore no DSL can be created";

    /**
     * Used for lower Bound of an EStructuralFeature.
     * Can be anything but 0,1,-1;
     */
    private static final int LOWERBOUND = -999;
    /**
     * Warning message if the EPackage has has interface or abstract classes
     * defined.
     * 
     * @see #checkFirstEClass(EPackage)
     */
    private static final String ABSTRACT_NOT_SUPPORTED_YET = 
            "Warning: Abstract and Interface declarations "
            + "are not totally suported yet. "
            + "Code is going to be generated but probably with compile erros.";

    /**
     * Private Constructor. This class should be instantiated via its
     * {@link #getInstance(EPackage, String, String)} method.
     * 
     * @param ePackageToInvestigate
     *            the EPackge this instance should investigate
     * @param fullPackageNameToUse
     *            the Package where the EMF Model Code was generated at
     */
    private ParserEcore(final EPackage ePackageToInvestigate
            , final String fullPackageNameToUse) {
        super();
        this.ePackage = ePackageToInvestigate;
        this.packageName = fullPackageNameToUse;
    }

    /**
     * Creates an Instance of this class {@link ParserEcore} . An ParserEcore
     * needs any kind of EPackage to analyze its attributes etc. All the
     * necessary methods are called in this method so that after it the IParsers
     * {@link IParser#genModel} can be used.
     * 
     * @param ePackage
     *            The {@link EPackage} to be analyzed
     * @param fullPackageName
     *            the full base package name of the corresponding
     *            {@link GenPackage}. This is the location where the generated
     *            EMF Models are located
     * @param factoryName
     *            the Factory of the {@link GenPackage} which can be used to
     *            instantiate the EMF Models
     * @return a new ParserEcore instance with the created
     *         {@link DSLGenerationModel}
     */
    public static ParserEcore getInstance(final EPackage ePackage,
            final String fullPackageName, final String factoryName) {
        EPackage eP = (EPackage) ePackage;
        ParserEcore parser = new ParserEcore(ePackage, fullPackageName);
        parser.getGenerationModel().setModelName(eP.getName());
        parser.getGenerationModel().setFactoryName(factoryName);
        parser.retrieveClassesAndAttributes();
        parser.createAttributeOrder();
        return parser;
    }

    /**
     * Analyzes the {@link #ePackage} for its defined classes and attributes to
     * be able to generate a valid {@link DSLGenerationModel}.
     */
    private void retrieveClassesAndAttributes() {
        // check for no Classifier
        if (this.ePackage.getEClassifiers().size() == 0) {
            throw new IllegalArgumentException("No Classifiers in Package: "
                    + this.ePackage.getClass().getCanonicalName());
        }
        EFactory eFactory = this.ePackage.getEFactoryInstance();
        // Without factory there can be no model instantiation
        if (eFactory == null) {
            System.err.println("No EFactoryInstance found for Package: "
                    + this.ePackage.getClass().getCanonicalName());
            return;
        }
        this.checkFirstEClass(ePackage);
        // traverse through the EPackages classifiers to receive attributes and
        // classes
        for (Iterator<EClassifier> iter = this.ePackage.getEClassifiers()
                .iterator(); iter.hasNext();) {
            EClassifier classifier = (EClassifier) iter.next();
            if (classifier instanceof EClass) {
                EClass eClass = (EClass) classifier;
                if (eClass.isAbstract() || eClass.isInterface()) {
                    System.err.println(ABSTRACT_NOT_SUPPORTED_YET);
                    continue;
                }
                String eClassName = eClass.getName();
                ModelClass modelClass = this.getGenerationModel()
                        .addModelClass(eClassName);
                // Set each needed import to the corresponding ModelClass
                modelClass.addImport(this.packageName + "."
                        + modelClass.getModel().getFactoryName());
                modelClass.addImport(this.packageName + "."
                        + modelClass.getClassName());
                // Set all imports to the genModel in case for a single Builder
                this.getGenerationModel().addImport(
                        this.packageName + "." + modelClass.getClassName());
                this.getGenerationModel().addImport(
                        this.packageName + "."
                                + modelClass.getModel().getFactoryName());
                for (Iterator<EStructuralFeature> ai = eClass
                        .getEStructuralFeatures().iterator(); ai.hasNext();) {
                    EStructuralFeature feature = (EStructuralFeature) ai.next();
                    // Not changeable means it has no setter and can therefore
                    // not be set by a dsl
                    if (!feature.isChangeable()) {
                        continue;
                    }
                    createModelAttribute(feature, modelClass);
                }
            }
        }
    }

    /**
     * Checks if the first EClass has the same Name as the {@link EPackage}
     * ePackage And prints a warning if thats not the case.
     * throws IllegalStateException
     *             if ePackage has no EClasses at all defined
     * @param epck
     *            the EPackage to analyze
     * 
     */
    private void checkFirstEClass(final EPackage epck) {
        EClass firstClass = null;
        for (Iterator<EClassifier> iter = 
                epck.getEClassifiers().iterator(); iter
                .hasNext();) {
            EClassifier classifier = (EClassifier) iter.next();
            if (classifier instanceof EClass) {
                firstClass = (EClass) classifier;
                break;
            } else {
                continue;
            }
        }
        if (firstClass != null) {
            if (!epck.getName().equals(firstClass.getName())) {
                System.err.println("Warning: " + epck.getName() + " "
                        + PACKAGENAME_NOT_EQUAL_ECLASSNAME);
            }
        } else {
            throw new IllegalStateException(NO_ECLASS_DEFINED);
        }

    }

    /**
     * Creates ClassAttribute in this classes DSLGenerationModel
     * {@link #genModel} and initializes it with given parameter.
     * Throws IllegalArgumentException
     *             if eClassifier is neither instance of 
     *             {@link EAttribute} or
     *             {@link EReference}
     * @param feature
     *            can be an EAttribute or an EReference
     * @param modelClass
     *            the class in which the attribute is defined
     * throws IllegalArgumentException
     *             if eClassifier is neither instance of {@link EAttribute} or
     *             {@link EReference}
     * @return the created classAttribute within the ModelClass
     */
    private ClassAttribute createModelAttribute(
            final EStructuralFeature feature, final ModelClass modelClass) {
        String featureName = feature.getName();
        String type = "";
        boolean isList = false;
        boolean isRef = false;
        boolean isOpposite = false;
        // Everything lower as -1 is acceptable
        int lowerBound = LOWERBOUND;
        String oppositeClassName = "";
        String oppositeAttributeName = "";
        DependencyKind kind;
        if (feature instanceof EAttribute) {
            EAttribute attribute = (EAttribute) feature;
            if (attribute.getEAttributeType() instanceof EEnum) {
                type = attribute.getEAttributeType().getName();
                this.getGenerationModel().addImport(
                        this.packageName + "." + type);
            } else {
                type = attribute.getEAttributeType().getInstanceClassName();
            }

            lowerBound = attribute.getLowerBound();
        } else if (feature instanceof EReference) {
            EReference ref = (EReference) feature;
            type = ref.getEType().getName();
            modelClass.addImport(this.packageName + "." + type);
            isRef = true;
            if (ref.getEOpposite() != null) {
                isOpposite = true;
                oppositeClassName = ref.getEReferenceType().getName();
                oppositeAttributeName = ref.getEOpposite().getName();
            }
            lowerBound = ref.getLowerBound();
        } else {
            throw new IllegalArgumentException("Parameter eClassifier "
                    + WRONG_ARG_TYPE);
        }

        boolean optional = !feature.isRequired();
        if (optional && !isRef) {
            kind = DependencyKind.OPTIONAL_ATTRIBUTE;
        } else {
            kind = DependencyKind.ATTRIBUTE;
        }

        if (feature.isMany()) {
            kind = DependencyKind.LIST_OF_ATTRIBUTES;
            isList = true;
        }

        ClassAttribute attribute = new ClassAttribute(featureName, type,
                modelClass);
        attribute.setDependencyKind(kind);
        attribute.setReference(isRef);
        attribute.setList(isList);
        // convert primitive list type to corresponding wrapper class type
        if (attribute.isPrimitive() && attribute.isList()) {
            attribute.setType(PrimitiveType.getPrimitiveByKeyword(
                    attribute.getType()).getWrapperClassName());
        }
        if (isOpposite) {
            handleOppositeRelation(attribute, oppositeClassName,
                    oppositeAttributeName);
            // special case, isRequired is true in ecore if its an opposite attr
            // & alist
            // regardless of lowerBound. But for dsl lowerbound is stricter
            if (isList) {
                if (lowerBound == 0) {
                    attribute.setOptional(true);
                }
            }

        }
        if (isList) {
            modelClass.setHasList(isList);
            this.getGenerationModel().setHasList(isList);
        }
        return attribute;
    }

    /**
     * Handles the opposite relation. If this method is called the first time
     * for an opposite relation it means the other side of the relation is not
     * defined in the DSLGenerationModel yet. So the given opposite attribute is
     * marked as the first one (the creator of the other). When this method is
     * called with the other side of the opposite relation it then can complete
     * the opposite relation.
     * 
     * @param currentAttribute
     *            the current opposite attribute
     * @param oppositeClassName
     *            name of the ModelClass the opposite is in
     * @param oppositeAttributeName
     *            attribute name of the opposite
     */
    private void handleOppositeRelation(final ClassAttribute currentAttribute,
            final String oppositeClassName, 
            final String oppositeAttributeName) {
        // check if other side of opposite relation is already in generation
        // model
        ClassAttribute oppositeAttr = this.getGenerationModel().findAttribute(
                oppositeClassName, oppositeAttributeName);
        if (oppositeAttr == null) {
            // Sets the start of an opposite relation
            // the first attribute of an opp relation is the creator (i.e the
            // setter) of the other one
            currentAttribute.setCreatorOfOpposite(true);
        } else {
            // Sets both attributes as their opposite
            currentAttribute.setOpposite(oppositeAttr);
            oppositeAttr.setOpposite(currentAttribute);
            // tells corresponding attribute class its opposite dependency
            oppositeAttr.getOpposite().setDependencyKind(
                    DependencyKind.OPPOSITE_ATTRIBUTE_TO_SET);
        }
    }

}
