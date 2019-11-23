package jflex.migration;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;

/** Variables for the velocity templates (both {@code BUILD.vm} and {@code TestCase.java.vm}). */
public class MigrationTemplateVars extends jflex.velocity.TemplateVars {

  /** The name of the test. */
  public String testName;
  /** The description of the test. Used in javadoc. */
  public String testDescription;

  /** Flex specification used by this test. */
  public File flexGrammar;
  /** java package with '.', used by the test and the scanner. */
  public String javaPackage;
  /** java package with '/' (File.sep) */
  public String javaPackageDir;
  /** The name of the test class. */
  public String testClassName;
  /** The name of the scanner class. */
  public String scannerClassName;
  /** Golden input/output files. */
  public ImmutableList<GoldenInOutFilePair> goldens;
}
