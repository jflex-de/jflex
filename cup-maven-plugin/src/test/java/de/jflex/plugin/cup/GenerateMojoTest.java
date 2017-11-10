package de.jflex.plugin.cup;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.google.common.io.Files;
import java.io.File;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/** Tests for {@link GenerateMojo}. */
public class GenerateMojoTest {

  private GenerateMojo mojo;

  @Mock CliCupInvoker mockCupInvoker;
  @Mock Log mockLogger;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  private File tempTargetGeneratedCupDirectory;

  @Before
  public void setUp() {
    mojo = new GenerateMojo(mockCupInvoker, mockLogger);
    Model pom = new Model();
    pom.setGroupId("de.jflex.testing.groupId");
    pom.setArtifactId("testing-artifactid");
    tempTargetGeneratedCupDirectory = Files.createTempDir();
    mojo.mavenProject = new MavenProjectStub(pom);
    mojo.generatedSourcesDirectory = tempTargetGeneratedCupDirectory;
    mojo.parserName = GenerateMojo.DEFAULT_PARSER_NAME;
    mojo.symbolsName = GenerateMojo.DEFAULT_SYMBOLS_NAME;
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

  /** Test the correct invocation of the CUP command-line interface. */
  @Test
  public void generateParser_cliInvocation() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("test.cup").getFile());

    mojo.generateParser(file);
    File expectedDestDir = new File(tempTargetGeneratedCupDirectory, "/foo/bar");
    verify(mockCupInvoker)
        .invoke(
            eq("foo.bar"),
            eq(expectedDestDir),
            eq("parser"),
            eq("sym"),
            eq(false),
            endsWith("test.cup"));
    verify(mockLogger)
        .debug("Parser file for test.cup is not actual: " + expectedDestDir + "/parser.java");
  }

  /** Test the correct invocation of the CUP command-line interface. */
  @Test
  public void generateParser_cliInvocation_interface() throws Exception {
    mojo.symbolInterface = true;
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("test.cup").getFile());

    mojo.generateParser(file);
    File expectedDestDir = new File(tempTargetGeneratedCupDirectory, "/foo/bar");
    verify(mockCupInvoker)
        .invoke(
            eq("foo.bar"),
            eq(expectedDestDir),
            eq("parser"),
            eq("sym"),
            eq(true),
            endsWith("test.cup"));
    verify(mockLogger)
        .debug("Parser file for test.cup is not actual: " + expectedDestDir + "/parser.java");
  }
}
