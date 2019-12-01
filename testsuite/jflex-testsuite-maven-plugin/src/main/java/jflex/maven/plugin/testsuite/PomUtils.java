package jflex.maven.plugin.testsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PomUtils {

  private PomUtils() {}

  static String getPomVersion(String groupId, String artifactId, File jar) throws IOException {
    if (!jar.isFile() || !jar.canRead()) {
      throw new FileNotFoundException("Couldn't open jar file " + jar);
    }
    return getPomProperty("version", artifactId, jar, groupId);
  }

  private static String getPomProperty(
      String propertyName, String artifactId, File jar, String groupId) throws IOException {
    try {
      Properties pomProperties = getPomProperties(groupId, artifactId, new CustomClassLoader(jar));
      return pomProperties.getProperty(propertyName);
    } catch (IOException e) {
      throw new IOException("Couldn't extract POM version of " + groupId + ":" + artifactId, e);
    }
  }

  private static Properties getPomProperties(
      String groupId, String artifactId, CustomClassLoader classLoader) throws IOException {
    Properties pomProperties = new Properties();
    String propertiesName = "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";
    try (InputStream pomPropertiesContent = classLoader.getResourceAsStream(propertiesName)) {
      if (pomPropertiesContent == null) {
        throw new IOException("Missing POM property: " + propertiesName);
      }
      pomProperties.load(pomPropertiesContent);
    }
    return pomProperties;
  }
}
