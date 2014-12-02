package de.htwg.javafluentdsl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.htwg.javafluentdsl.parser.emf.using.EMFUsingTest;
import de.htwg.javafluentdsl.parser.regex.using.RegexUsingTest;

/**
 * Suit test class class for starting all DSL using test classes.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ EMFUsingTest.class,
		RegexUsingTest.class})
public class UsingTest {}
