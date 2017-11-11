package de.jflex.plugin.cup;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;

/** Integration test for {@link GenerateMojo} on {@code /jflex/cup/sample-project/pom.xml}. */
public class GenerateMojoIntegrationTest {

  private MavenProject mavenTestProject;

  @Rule
  public MojoRule mojoRule =
      new MojoRule() {
        @Override
        protected void before() throws Throwable {
          super.before();
          mavenTestProject = readMavenProject(new File("../cup/sample-project"));
          assertThat(mavenTestProject.getGroupId()).isEqualTo("de.jflex.testing");
          assertThat(mavenTestProject.getArtifactId()).isEqualTo("sample-project");
        }
      };

  @Test
  public void testExecution() throws Exception {
    mojoRule.executeMojo(mavenTestProject, "generate");
    File outputDir = new File(mavenTestProject.getBasedir(), "/target/generated-sources/cup");
    assertWithMessage("The target `cup` directory exists in the target directory")
        .that(outputDir.exists())
        .isTrue();
    File generatedJavaPath = new File(outputDir, "/de/jflex/testing");
    assertWithMessage(
            "The target `cup` directory contains the path inferred from the package"
                + " declaration in the cup file")
        .that(generatedJavaPath.exists())
        .isTrue();
    File generatedParseFile = new File(generatedJavaPath, "MyParser.java");
    assertWithMessage(
            "The java path in the target `cup` directory contains the generated parser, "
                + "using the name defined in the sample-project pom.xml")
        .that(generatedParseFile.exists())
        .isTrue();
    File generatedSymbolsFile = new File(generatedJavaPath, "MySymbol.java");
    assertWithMessage(
            "The java path in the target `cup` directory contains the generated sym, "
                + "using the name defined in the sample-project pom.xml")
        .that(generatedParseFile.exists())
        .isTrue();
  }
}
