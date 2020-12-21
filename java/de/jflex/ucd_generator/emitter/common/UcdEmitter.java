package de.jflex.ucd_generator.emitter.common;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class UcdEmitter {
  private final String targetPackage;

  public UcdEmitter(String targetPackage) {
    this.targetPackage = targetPackage;
  }

  protected String getTargetPackage() {
    return targetPackage;
  }

  protected InputStreamReader readResource(String resourceName) {
    return new InputStreamReader(
        getClass().getClassLoader().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
  }
}
