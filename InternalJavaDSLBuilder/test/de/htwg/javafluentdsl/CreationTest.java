package de.htwg.javafluentdsl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.htwg.javafluentdsl.main.MainTest;
import de.htwg.javafluentdsl.parser.emf.creation.EMFCreationTest;
import de.htwg.javafluentdsl.parser.regex.creation.RegexCreationTest;

/**
 * Suit test class for starting the test for the Main class and all DSL creation
 * test classes.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MainTest.class, EMFCreationTest.class, 
    RegexCreationTest.class })
public class CreationTest { }
