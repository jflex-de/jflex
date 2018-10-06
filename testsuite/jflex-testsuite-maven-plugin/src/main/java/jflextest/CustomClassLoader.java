package jflextest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Loads classes and resources from a dynamically configurable class path (may contain dirs, zip
 * files, and jar files).
 */
public class CustomClassLoader extends ClassLoader {

  /** the class path */
  private List<String> pathItems = new ArrayList<>();

  /**
   * Constructs a CustomClassLoader. It scans the specified class path (system class path is handled
   * by parent/system class loader)
   */
  public CustomClassLoader(String classPath) {
    scanPath(classPath);
  }

  public CustomClassLoader(File... files) {
    for (File file : files) {
      pathItems.add(file.getAbsolutePath());
    }
  }

  /**
   * Break up a class path string into its items and store in {@code pathItems}.
   *
   * <p>Uses system property {@code path.separator} as delimiter.
   */
  private void scanPath(String classPath) {
    if (classPath == null) return;
    String separator = System.getProperty("path.separator");
    StringTokenizer st = new StringTokenizer(classPath, separator);
    while (st.hasMoreTokens()) {
      pathItems.add(st.nextToken());
    }
  }

  /** Add a new path item (dir, zip, or jar) to the search path. */
  public void addPath(File pathItem) throws FileNotFoundException {
    if (!pathItem.exists()) {
      throw new FileNotFoundException("Couldn't load classpath item " + pathItem.getAbsolutePath());
    }
    pathItems.add(pathItem.getAbsolutePath());
  }

  /** Returns a named resource as stream. */
  public synchronized InputStream getResourceAsStream(String name) {
    // call super, handles delegation to parent+system class loader
    InputStream s = super.getResourceAsStream(name);
    if (s != null) {
      return s;
    }

    // search in path
    for (String path : pathItems) {
      if (isJar(path)) {
        try {
          s = getZipEntryStream(path, name);
        } catch (FileNotFoundException e) {
          // we might find the entry in another path item.
          s = null;
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        s = getFileEntry(name, path);
      }
      if (s != null) {
        return s;
      }
    }

    // no success
    return null;
  }

  /** Loads a class by name. */
  public synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

    Class c;

    // try to delegate to parent/system class loader
    try {
      c = findLoadedClass(name);
      if (c != null) return c;
      c = findSystemClass(name);
      if (c != null) return c;
    } catch (ClassNotFoundException e) {
      // ignore, search own class path
    }

    byte[] data = lookupClassData(name);
    if (data == null) throw new ClassNotFoundException();
    c = defineClass(name, data, 0, data.length);
    if (resolve) resolveClass(c);

    return c;
  }

  /** Search for a class file, and return class data if found. */
  private byte[] lookupClassData(String className) throws ClassNotFoundException {
    byte[] data = null;
    for (String path : pathItems) {
      String fileName = className.replace('.', '/') + ".class";

      if (isJar(path)) data = loadJarData(path, fileName);
      else data = loadFileData(path, fileName);

      if (data != null) return data;
    }
    throw new ClassNotFoundException();
  }

  /** Test if string points to a jar file */
  boolean isJar(String pathEntry) {
    return pathEntry.endsWith(".jar") || pathEntry.endsWith(".zip");
  }

  /** Load class data from a file */
  private byte[] loadFileData(String path, String fileName) {
    File file = new File(path, fileName);
    if (file.canRead()) {
      try (InputStream stream = Files.newInputStream(Paths.get(file.toString()))) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = stream.read(b)) != -1) out.write(b, 0, n);
        stream.close();
        out.close();
        return out.toByteArray();
      } catch (IOException e) {
        // ignore, maybe another path item succeeds
      }
    }
    return null;
  }

  /** Returns {@link InputStream} for a jar/zip file entry. */
  private InputStream getZipEntryStream(String file, String entryName) throws IOException {
    ZipFile zip = new ZipFile(new File(file));
    ZipEntry entry = zip.getEntry(entryName);
    if (entry != null) {
      // Don't close the zip now, otherwise the content is unreadable.
      return zip.getInputStream(entry);
    }
    zip.close();
    return null;
  }

  private InputStream getFileEntry(String name, String path) {
    File file = new File(path, name);
    if (file.canRead()) {
      try {
        return Files.newInputStream(Paths.get(file.toString()));
      } catch (IOException e) {
        return null;
      }
    }
    return null;
  }

  /** Load class data from jar/zip file */
  private byte[] loadJarData(String path, String fileName) {
    ZipFile zipFile;
    ZipEntry entry;
    int size;

    try {
      zipFile = new ZipFile(new File(path));
      entry = zipFile.getEntry(fileName);
      if (entry == null) return null;
      size = (int) entry.getSize();
    } catch (IOException io) {
      return null;
    }

    InputStream stream = null;
    try {
      stream = zipFile.getInputStream(entry);
      if (stream == null) return null;
      byte[] data = new byte[size];
      int pos = 0;
      while (pos < size) {
        int n = stream.read(data, pos, data.length - pos);
        pos += n;
      }
      zipFile.close();
      return data;
    } catch (IOException e) {
    } finally {
      try {
        if (stream != null) stream.close();
      } catch (IOException e) {
      }
    }
    return null;
  }
}
