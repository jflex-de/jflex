package org.codehaus.mojo.jflex;

import org.codehaus.mojo.jflex.ClassInfo;

import junit.framework.TestCase;

public class ClassInfoTest extends TestCase {

	public void testGetOutputFilename() {
		ClassInfo classe=new ClassInfo();
		classe.className="Bar";
		classe.packageName="org.foo";
		assertEquals("/org/foo/Bar.java", classe.getOutputFilename());
	}

}
