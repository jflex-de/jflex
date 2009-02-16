/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven2 plugin                                                     *
 * Copyright (c) 2007       Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package org.codehaus.mojo.jflex;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

/**
 * @author Régis Décamps (decamps@users.sf.net)
 */
public class JFlexMojoTest extends AbstractMojoTestCase {

	/**
	 * @see org.apache.maven.plugin.testing.AbstractMojoTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// required for mojo lookups to work
		super.setUp();
	}

	/**
	 * @see org.codehaus.plexus.PlexusTestCase#tearDown()
	 */
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
		configureMojo(mojo, "maven-jflex-plugin", testPom);
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
		boolean correctSize = (size > 26624) && (size < 28672);
		assertTrue("size of produced file between 26k and 28k. Actual is "
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
