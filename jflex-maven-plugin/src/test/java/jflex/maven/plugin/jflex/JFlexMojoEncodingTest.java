/*
 * JFlex Maven3 plugin
 * Copyright (c) 2026  Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.maven.plugin.jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoExtension;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.descriptor.Parameter;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptorBuilder;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests the encoding parameter of {@link JFlexMojo} in a project with a source encoding. */
@MojoTest
public class JFlexMojoEncodingTest {

  private static final String CONFIG = "src/test/projects/single-file-test/plugin-config.xml";

  /** The project of the mojo under test. */
  @Inject private MavenProject project;

  /** Runs before the mojo is configured. */
  @BeforeEach
  public void setSourceEncoding() {
    project.getProperties().setProperty("project.build.sourceEncoding", "ISO-8859-1");
  }

  /** The encoding defaults to the source encoding of the project. */
  @Test
  @InjectMojo(goal = "generate", pom = CONFIG)
  public void defaultsToSourceEncoding(JFlexMojo mojo) throws Exception {
    assertEquals("ISO-8859-1", MojoExtension.getVariableValueFromObject(mojo, "encodingName"));
  }

  /** A configured encoding has precedence over the source encoding of the project. */
  @Test
  @InjectMojo(goal = "generate", pom = CONFIG)
  @MojoParameter(name = "encodingName", value = "UTF-16")
  public void configuredEncodingHasPrecedence(JFlexMojo mojo) throws Exception {
    assertEquals("UTF-16", MojoExtension.getVariableValueFromObject(mojo, "encodingName"));
  }

  /**
   * The parameter can be configured as {@code encoding}. Checks the plugin descriptor, because the
   * test harness does not map parameter aliases the way Maven does.
   */
  @Test
  public void encodingAlias() throws Exception {
    PluginDescriptor plugin;
    try (InputStream in = JFlexMojo.class.getResourceAsStream("/META-INF/maven/plugin.xml")) {
      assertNotNull(in, "plugin descriptor");
      Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
      plugin = new PluginDescriptorBuilder().build(reader);
    }
    Parameter parameter = plugin.getMojo("generate").getParameterMap().get("encodingName");
    assertEquals("encoding", parameter.getAlias());
  }
}
