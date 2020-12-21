package de.jflex.ucd_generator.emitter.unicode_version;

import de.jflex.velocity.TemplateVars;

public class UnicodeVersionVars extends TemplateVars {

  public String packageName;
  public String className;
  public int maxCodePoint;
  public String propertyValues;
  public String intervals;
  public String propertyValueAliases;
  public int maxCaselessMatchPartitionSize;
  public String caselessMatchPartitions;
}
