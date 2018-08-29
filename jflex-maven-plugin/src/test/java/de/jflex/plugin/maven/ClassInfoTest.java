package de.jflex.plugin.maven;

import static org.junit.Assert.assertEquals;

import java.io.File;
import org.junit.Test;

public class ClassInfoTest {
  @Test
  public void testGetOutputFilename() {
    ClassInfo clazz = new ClassInfo();
    clazz.className = "Bar";
    clazz.packageName = "org.foo";
    assertEquals(new File("org/foo/Bar.java"), new File(clazz.getOutputFilename()));
  }
}
