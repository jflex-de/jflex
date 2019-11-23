package jflex.migration;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.velocity.TemplateVars;

public class TestCaseVars extends TemplateVars {

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
  /** Golden input/output files. */
  public ImmutableList<GoldenInOutFilePair> goldens;
}
