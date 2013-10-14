package de.jflex.plugin.maven;

import java.io.File;

import junit.framework.TestCase;

public class ClassInfoTest extends TestCase {

	public void testGetOutputFilename() {
		ClassInfo classe=new ClassInfo();
		classe.className="Bar";
		classe.packageName="org.foo";
		assertEquals(new File("org/foo/Bar.java"), new File(classe.getOutputFilename()));
	}

}
