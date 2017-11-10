package de.jflex.plugin.cup;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.File;
import org.junit.Test;

/** Test for {@link CliCupInvoker}. */
public class CliCupInvokerTest {

  @Test
  public void buildArgv() throws Exception {
    assertThat(
            ImmutableList.copyOf(
                CliCupInvoker.buildArgv(
                    "foo.bar",
                    new File("/outputDirectory"),
                    "Parser",
                    "Symbols", /* interface */
                    true,
                    "/a/b/c/def.cup")))
        .containsExactly(
            "-package",
            "foo.bar",
            "-destdir",
            "/outputDirectory",
            "-parser",
            "Parser",
            "-symbols",
            "Symbols",
            "-interface",
            "/a/b/c/def.cup")
        .inOrder();
  }
}
