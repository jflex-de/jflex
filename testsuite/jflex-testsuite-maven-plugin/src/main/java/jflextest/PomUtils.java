package jflextest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PomUtils {

  static String getPomVersion(String groupId, String artifactId, File jar)
      throws IllegalArgumentException {
    return getPomProperty("version", artifactId, jar, groupId);
  }

  private static String getPomProperty(
      String propertyName, String artifactId, File jar, String groupId) {
    try {
      Properties pomProperties = getPomProperties(groupId, artifactId, new CustomClassLoader(jar));
      return pomProperties.getProperty(propertyName);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Couldn't extract POM version of " + groupId + ":" + artifactId, e);
    }
  }

  private static Properties getPomProperties(
      String groupId, String artifactId, CustomClassLoader classLoader) throws IOException {
    Properties pomProperties = new Properties();
    String propertiesName = "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";
    try (InputStream pomPropertiesContent = classLoader.getResourceAsStream(propertiesName)) {
      if (pomPropertiesContent == null) {
        throw new FileNotFoundException("Missing POM properties: " + propertiesName);
      }
      pomProperties.load(pomPropertiesContent);
    }
    return pomProperties;
  }
}
