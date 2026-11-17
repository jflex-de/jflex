/*
 * Copyright 2020, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.cup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.apache.maven.api.plugin.testing.Basedir;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

/** Runs the mojo with the configuration in the {@code pom.xml} of {@code sample-project}. */
@MojoTest
public class GenerateMojoIntegrationTest {

  @Test
  @Basedir("sample-project")
  @InjectMojo(goal = "generate")
  public void testExecution(GenerateMojo mojo) throws Exception {
    mojo.execute();
    MavenProject mavenTestProject = mojo.mavenProject;
    File outputDir = new File(mavenTestProject.getBasedir(), "/target/generated-sources/cup");
    assertTrue(outputDir.exists(), "The target `cup` directory exists in the target directory");
    File generatedJavaPath = new File(outputDir, "/de/jflex/testing");
    assertTrue(
        generatedJavaPath.exists(),
        "The target `cup` directory contains the path inferred from the package"
            + " declaration in the cup file");
    File generatedParseFile = new File(generatedJavaPath, "MyParser.java");
    assertTrue(
        generatedParseFile.exists(),
        "The java path in the target `cup` directory contains the generated parser, "
            + "using the name defined in the sample-project pom.xml");
    File generatedSymbolsFile = new File(generatedJavaPath, "MySymbols.java");
    assertTrue(
        generatedSymbolsFile.exists(),
        "The java path in the target `cup` directory contains the generated sym, "
            + "using the name defined in the sample-project pom.xml");
  }
}
