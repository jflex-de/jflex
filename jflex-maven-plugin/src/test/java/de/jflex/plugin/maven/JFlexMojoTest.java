/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007       Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

/**
 * @author R�gis D�camps (decamps@users.sf.net)
 */
public class JFlexMojoTest extends AbstractMojoTestCase {

	/**
	 * @see org.apache.maven.plugin.testing.AbstractMojoTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// required for mojo lookups to work
		super.setUp();
	}

	/**
	 * @see org.codehaus.plexus.PlexusTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Configures an instance of the jflex mojo for the specified test case.
	 * 
	 * @param testCase
	 *            The name of the test case (i.e. its directory name).
	 * @return The configured mojo.
	 * @throws Exception
	 */
	protected JFlexMojo newMojo(String testCase) throws Exception {
		File unitBasedir = new File(getBasedir(), "src/test/resources/unit");
		File testPom = new File(unitBasedir, testCase + "/plugin-config.xml");
		JFlexMojo mojo = new JFlexMojo();
		configureMojo(mojo, "jflex-maven-plugin", testPom);
		if (getVariableValueFromObject(mojo, "project") == null) {
			setVariableValueToObject(mojo, "project", new MavenProjectStub());
		}
		return mojo;
	}

	/**
	 * Gets the expected path to the output file from jflex.
	 * 
	 * @param mojo
	 *            The jflex mojo under test.
	 * @return The expected path to the output file from jflex.
	 * @throws Exception
	 */
	protected File getExpectedOutputFile(JFlexMojo mojo) throws Exception {
		File outDir =
				(File) getVariableValueFromObject(mojo, "outputDirectory");
		return new File(outDir, "/org/jamwiki/parser/jflex/"
				+ "JAMWikiPreProcessor" + ".java");
	}

	/**
	 * Tests configuration with a single input file.
	 * 
	 * @throws Exception
	 */
	public void testSingleFile() throws Exception {
		JFlexMojo mojo = newMojo("single-file-test");
		mojo.execute();

		File produced = getExpectedOutputFile(mojo);
		assertTrue("produced file is a file: " + produced, produced.isFile());

		long size = produced.length();
		/*
		 * NOTE: The final file size also depends on the employed line
		 * terminator. For this reason, the generated output will be longer on a
		 * Windows platform ("\r\n") than on a Unix platform ("\n").
		 */
		boolean correctSize = (size > 26624) && (size < 36696);
		assertTrue("size of produced file between 26k and 36k. Actual is "
				+ size, correctSize);
	}

	/**
	 * Tests configuration with a single input directory.
	 * 
	 * @throws Exception
	 */
	public void testSingleDir() throws Exception {
		JFlexMojo mojo = newMojo("single-dir-test");
		mojo.execute();

		File produced = getExpectedOutputFile(mojo);
		assertTrue("produced file is a file: " + produced, produced.isFile());
	}

	/**
	 * Tests configuration with a single input directory containing sub
	 * directories.
	 * 
	 * @throws Exception
	 */
	public void testRecursion() throws Exception {
		JFlexMojo mojo = newMojo("recursion-test");
		mojo.execute();

		File produced = getExpectedOutputFile(mojo);
		assertTrue("produced file is a file: " + produced, produced.isFile());
	}

}
