package jflex.testing.javac;

@SuppressWarnings("WeakerAccess")
public final class PackageUtil {
  public static String getPathForClass(Class clazz) {
    return getPathForPackage(clazz.getPackage());
  }

  public static String getPathForPackage(Package targetPackage) {
    return targetPackage.getName().replace('.', '/');
  }

  private PackageUtil() {}
}
