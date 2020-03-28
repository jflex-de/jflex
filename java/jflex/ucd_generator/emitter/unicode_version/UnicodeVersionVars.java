package jflex.ucd_generator.emitter.unicode_version;

import jflex.velocity.TemplateVars;

public class UnicodeVersionVars extends TemplateVars {

  public String packageName;
  public String className;
  public int maxCodePoint;
  public String propertyValues;
  public int maxCaselessMatchPartitionSize;
  public String caselessMatchPartitions;
}
