package de.jflex.plugin.cup;

import static com.google.common.truth.Truth.assertThat;

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
  }
}
