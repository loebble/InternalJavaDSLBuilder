package de.htwg.javafluentdsl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Runs all Test Classes created.
 */
import de.htwg.javafluentdsl.parser.regex.creation.RegexCreationTest;
@RunWith(Suite.class)
@SuiteClasses({CreationTest.class, UsingTest.class,
		RegexCreationTest.class})
public class TestAll {
	

	
}
