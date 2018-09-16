package jflextest;

import java.util.*;
import java.io.*;
import java.util.zip.*;


/**
 * Loads classes and resources from a dynamically 
 * configurable class path (may contain dirs, zip files,
 * and jar files). 
 */
public class CustomClassLoader extends ClassLoader {

  /** the class path */
  private List<String> pathItems;

      
  /**
   * Constructs a CustomClassLoader. It scans the specified 
   * class path (system class path is handled by parent/system 
   * class loader)
   */
  public CustomClassLoader(String classPath) {
    scanPath(classPath);
  }


  /**
   * Break up a class path string into its items and
   * store in <code>pathItems</code>. 
   *
   * Uses system property <code>path.separator</code> as
   * delimiter.
   */
  private void scanPath(String classPath) {
    if (classPath == null) return;
    String separator= System.getProperty("path.separator");
    pathItems= new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(classPath, separator);
    while (st.hasMoreTokens()) {
      pathItems.add(st.nextToken());
    }
  }


  /**
   * Add a new path item (dir, zip, or jar) to the search path.
   */
  public void addPath(String pathItem) {
    pathItems.add(pathItem);
  }
  

  /**
   * Returns a named resource as stream.
   */
  public synchronized InputStream getResourceAsStream(String name) {
    // call super, handles delegation to parent+system class loader
    InputStream s = super.getResourceAsStream(name);
    if (s != null) return s;

    // search in path
    for (String path : pathItems) {
      if (isJar(path)) {
        s = getZipEntryStream(path,name);
        if (s != null) return s;
      } else {
        File file = new File(path,name);
        if (file.canRead()) {
          try {
            return new FileInputStream(file);
          }
          catch (IOException e) {
            // ignore, maybe another path item succeds
          }
        }
      }
    }

    // no success
    return null;
  } 
  

  /**
   * Loads a class by name.
   */
  public synchronized Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException {

    Class c;
          
    // try to delegate to parent/system class loader
    try {
      c = findLoadedClass(name);
      if (c != null) return c;
      c = findSystemClass(name);
      if (c != null) return c;
    }
    catch (ClassNotFoundException e) {
      // ignore, search own class path
    }

    byte [] data = lookupClassData(name);
    if (data == null) throw new ClassNotFoundException();
    c = defineClass(name, data, 0, data.length);    
    if (resolve) resolveClass(c);
    
    return c;
  }
  

  /**
   * Search for a class file, and return class data if found.
   */
  private byte [] lookupClassData(String className) throws ClassNotFoundException {
    byte [] data = null;
    for (String path : pathItems) {
      String fileName= className.replace('.', '/')+".class";
      
      if (isJar(path)) 
        data= loadJarData(path, fileName);
      else 
        data= loadFileData(path, fileName);
      
      if (data != null) return data;
    }
    throw new ClassNotFoundException();
  }
    
  /**
   * Test if string points to a jar file
   */
  boolean isJar(String pathEntry) {
    return pathEntry.endsWith(".jar") || pathEntry.endsWith(".zip");
  }

  
  /**
   * Load class data from a file
   */
  private byte [] loadFileData(String path, String fileName) {
    File file = new File(path, fileName);
    if (file.canRead()) {
      try {
        FileInputStream stream = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
        byte [] b = new byte[1000];
        int n;
        while ((n = stream.read(b)) != -1) 
          out.write(b, 0, n);
        stream.close();
        out.close();
        return out.toByteArray();        
      } catch (IOException e) {
        // ignore, maybe another path item succeds
      }
    }
    return null;
  }


  /**
   * Return InputStream for a jar/zip file entry.
   */
  private InputStream getZipEntryStream(String file, String entryName) {
    try {
      ZipFile zip = new ZipFile(new File(file));
      ZipEntry entry = zip.getEntry(entryName);

      if (entry == null) return null;

      return zip.getInputStream(entry);
    }
    catch (IOException e) {
      return null;
    }
  }


  /**
   * Load class data from jar/zip file
   */
  private byte [] loadJarData(String path, String fileName) {
    ZipFile zipFile;
    ZipEntry entry;
    int size;

    try {
      zipFile = new ZipFile(new File(path));
      entry = zipFile.getEntry(fileName);
      if (entry == null) return null;
      size = (int) entry.getSize();
    } catch(IOException io) {
      return null;
    }
        
    InputStream stream = null;
    try {
      stream = zipFile.getInputStream(entry);
      if (stream == null) return null;
      byte [] data = new byte[size];
      int pos = 0;
      while (pos < size) {
        int n = stream.read(data, pos, data.length - pos);
        pos += n;
      }
      zipFile.close();
      return data;
    } 
    catch (IOException e) {
    } 
    finally {
      try {
        if (stream != null) stream.close();
      }
      catch (IOException e) {
      }
    }
    return null;
  }
}
