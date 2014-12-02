package de.htwg.javafluentdsl.parser.emf.using;

import java.util.Iterator;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.htwg.javafluentdsl.parser.emf.creation.EMFCreationTest;

/**
 * Test class for Using the created DSLs by {@link EMFCreationTest}. Make sure
 * the DSLs where created bevore running this one.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ EMFUsing_SingleBuilderTest.class,
		EMFUsing_MultipleBuilderTest.class })
public class EMFUsingTest {

	/**
	 * Method to validate EObject. Prints out errors and warnings of the
	 * EObject. Uses EMFs validation Framework to check for those errors and
	 * warnings.
	 * 
	 * @param eObject
	 *            EObject instance to validate
	 * @return true if valid
	 */
	public static boolean validateObject(EObject eObject) {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(eObject);
		if (diagnostic.getSeverity() == Diagnostic.ERROR
				|| diagnostic.getSeverity() == Diagnostic.WARNING) {
			System.err.println(diagnostic.getMessage());
			for (Iterator<Diagnostic> i = diagnostic.getChildren().iterator(); i
					.hasNext();) {
				Diagnostic childDiagnostic = (Diagnostic) i.next();
				switch (childDiagnostic.getSeverity()) {
				case Diagnostic.ERROR:
				case Diagnostic.WARNING:
					System.err.println("\t" + childDiagnostic.getMessage());
				default:
					break;
				}
			}
			return false;
		}
		return true;
	}

}
