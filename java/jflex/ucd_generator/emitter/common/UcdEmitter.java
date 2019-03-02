package jflex.ucd_generator.emitter.common;

public class UcdEmitter {
  private final String targetPackage;

  public UcdEmitter(String targetPackage) {
    this.targetPackage = targetPackage;
  }

  protected String getTargetPackage() {
    return targetPackage;
  }
}
