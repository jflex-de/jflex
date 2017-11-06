package de.jflex.plugin.cup;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

/** Tests for {@link GenerateMojo}. */
public class GenerateMojoTest {

  private GenerateMojo mojo;

  @Before
  public void setUp() {
    mojo = new GenerateMojo();
  }

  @Test
  public void optionalJavaPackage() {
    assertThat(mojo.optionalJavaPackage("package foo.bar;")).hasValue("foo.bar");
  }

  @Test
  public void optionalJavaPackage_spaces() {
    assertThat(mojo.optionalJavaPackage("package   foo.bar   ;")).hasValue("foo.bar");
  }

  @Test
  public void optionalJavaPackage_noPackage() {
    // assertWithMessage("Only lines starting with `package are valid package declarations")
    assertThat(mojo.optionalJavaPackage("hello world foo.bar;")).isAbsent();
    // assertWithMessage("Only lines ending with `;` are valid package declarations")
    assertThat(mojo.optionalJavaPackage("package foo.bar")).isAbsent();
  }
}
