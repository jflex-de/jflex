/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Tests for {@link LexGenerator}. */
public class LexGeneratorTest {

  @Rule public TemporaryFolder tmp = new TemporaryFolder();

  @After
  public void resetOutput() {
    Out.setOutputStream(System.out);
  }

  /** Writes a minimal spec for class {@code name} with an extra option line. */
  private File writeSpec(String name, String directive) throws Exception {
    File spec = tmp.newFile(name + ".flex");
    List<String> lines =
        Arrays.asList("%%", "%class " + name, "%int", directive, "%%", "\"a\" { return 1; }");
    Files.write(spec.toPath(), lines, StandardCharsets.UTF_8);
    return spec;
  }

  private String generate(File spec, Options options) throws Exception {
    Out.setOutputStream(new ByteArrayOutputStream());
    String generated = new LexGenerator(spec, options).generate();
    return new String(Files.readAllBytes(new File(generated).toPath()), StandardCharsets.UTF_8);
  }

  /** {@code %apiprivate} in one spec does not make the API of the next spec private. */
  @Test
  public void generate_apiPrivateDoesNotLeak() throws Exception {
    Options options = new Options();
    options.setDir(tmp.getRoot());

    String privateApi = generate(writeSpec("PrivateApi", "%apiprivate"), options);
    String publicApi = generate(writeSpec("PublicApi", ""), options);

    assertTrue(privateApi, privateApi.contains("private final void yyclose()"));
    assertFalse(privateApi, privateApi.contains("public final void yyclose()"));
    assertTrue(publicApi, publicApi.contains("public final void yyclose()"));
  }

  /** Two generators with different options do not affect each other. */
  @Test
  public void generate_optionsAreSeparate() throws Exception {
    Options privateOptions = new Options();
    privateOptions.setDir(tmp.newFolder("private"));
    privateOptions.setSkeleton(privateOptions.skeleton().makePrivate());
    Options publicOptions = new Options();
    publicOptions.setDir(tmp.newFolder("public"));

    String privateApi = generate(writeSpec("PrivateApi", ""), privateOptions);
    String publicApi = generate(writeSpec("PublicApi", ""), publicOptions);

    assertTrue(privateApi, privateApi.contains("private final void yyclose()"));
    assertTrue(publicApi, publicApi.contains("public final void yyclose()"));
  }
}
