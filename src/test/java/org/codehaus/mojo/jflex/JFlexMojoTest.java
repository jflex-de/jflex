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
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.jflex.JFlexMojo;
import org.codehaus.plexus.PlexusTestCase;

import junit.framework.TestCase;

public class JFlexMojoTest extends PlexusTestCase {
	protected static final String SRC_TEST_RESOURCES_FLEX = "src/test/resources/preprocessor.jflex";
	protected static final String EXPECTED_PACKAGE_DIR = "/org/jamwiki/parser/jflex/";
	protected static final String EXPECTED_CLASS_NAME = "JAMWikiPreProcessor";
	protected static final String OUTPUT_DIRECTORY = "target/test/generated";
	
	JFlexMojo mojo;
	File preprocessorLex;

	@Override
	protected void setUp() throws Exception {
		/* this is done by Maven */
		mojo = new JFlexMojo();
		mojo.setProject(new MavenProject(new Model()));
		mojo.setOutputDirectory(new File(OUTPUT_DIRECTORY));
		// parameter that I want to use frequently
		preprocessorLex=new File(SRC_TEST_RESOURCES_FLEX);
	}

	public void testInit() {
		assertEquals(OUTPUT_DIRECTORY, mojo.getOutputDirectory().getPath());
	}

	@Override
	protected void tearDown() throws Exception {
		mojo=null;
	}
	
	/**
	 * Test the full parser generation.
	 * 
	 * This is an integration rather than a unit-test, and is extremely useful.
	 */
	public void testGenerate() throws MojoExecutionException,
			MojoFailureException {
		File[] lexFiles = { preprocessorLex };
		mojo.setLexFiles(lexFiles);
		mojo.execute();

		// approximative checking... Can't use md5 as the generated file
		// contains the date.
		File produced = new File(OUTPUT_DIRECTORY
				+ EXPECTED_PACKAGE_DIR + EXPECTED_CLASS_NAME + ".java");
	
		assertTrue("produced file is a file: " + produced.getAbsolutePath(),
				produced.isFile());
		long size = produced.length();
		boolean correctSize = (size > 26000) && (size < 28000);
		assertTrue("size of produced file between 26k and 28k. Actual is "
				+ size, correctSize);
		produced.delete();
	}
	
}
