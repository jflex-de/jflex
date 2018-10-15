package jflextest;

import com.google.common.base.Joiner;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import jflex.GeneratorException;
import jflex.Options;
import jflex.Out;
import jflex.SilentExit;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

public class Exec {

  private static final String JAVA_VERSION = "1.7";
  public static final String NL = System.getProperty("line.separator");

  /**
   * Convert two Lists with String elements into one array containing all elements.
   *
   * @param a a List with String elements
   * @param b a List with String elements
   * @return an array containing all elements of a then b
   */
  private static String[] toArray(List<String> a, List<String> b) {
    if (a == null) a = new ArrayList<>();
    if (b == null) b = new ArrayList<>();

    String[] cmdline = new String[a.size() + b.size()];

    int n = 0;
    for (String aElem : a) cmdline[n++] = aElem;
    for (String bElem : b) cmdline[n++] = bElem;

    return cmdline;
  }

  /**
   * Call javac on javaSourceFiles in input dir. If javaSourceFiles is {@code null}, all {@code
   * *.java} files in the directory will be compiled.
   *
   * @param javaSourceFiles A list of files to compile, or {@code null}
   * @param dir Source directory.
   */
  public static TestResult execJavac(
      List<String> javaSourceFiles, File dir, String additionalJars, String encoding)
      throws FileNotFoundException {
    // javac fails if an input file doesn't exist
    checkFilesExist(javaSourceFiles, dir);
    Project p = new Project();
    Path path = new Path(p, dir.toString());
    Javac javac = new Javac();
    javac.setProject(p);
    javac.setSrcdir(path);
    javac.setDestdir(dir);
    javac.setTarget(JAVA_VERSION);
    javac.setSource(JAVA_VERSION);
    javac.setSourcepath(new Path(p, "")); // Only compile explicitly specified source files
    if (javaSourceFiles != null) {
      javac.setIncludes(Joiner.on(' ').join(javaSourceFiles));
    }
    javac.setEncoding(encoding);
    Path classPath = javac.createClasspath();
    // Locate the jflex jar in the user's Maven local repository
    classPath.setPath(additionalJars);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream outSafe = System.err;
    System.setErr(new PrintStream(out));

    try {
      javac.execute();
      return new TestResult(out.toString(), true);
    } catch (BuildException e) {
      return new TestResult(e + NL + out.toString() + NL + "classpath: " + classPath, false);
    } finally {
      System.setErr(outSafe);
    }
  }

  /** Call jflex with command line and input files. */
  public static TestResult execJFlex(List<String> cmdline, List<String> files) {
    String[] cmd = toArray(cmdline, files);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      Options.setDefaults();
      Out.setOutputStream(out);
      jflex.Main.generate(cmd);
      return new TestResult(out.toString(), true);
    } catch (GeneratorException e) {
      return new TestResult(out.toString(), false);
    } catch (SilentExit e) {
      return new TestResult(out.toString(), false);
    }
  }

  /**
   * Call main method of specified class with command line and input files.
   *
   * @param path the directory in which to search for the class
   * @param additionalJars
   */
  public static TestResult execClass(
      String theClass,
      String path,
      List<String> files,
      List<File> additionalJars,
      String outputFileEncoding,
      List<String> cmdline)
      throws UnsupportedEncodingException {

    String[] cmd = toArray(cmdline, files);
    Class c;
    Method main;
    Class[] sig = {String[].class};

    // System.out.println("exec class "+theClass);
    // System.out.println("cmdline "+cmdline+"\nfiles: "+files);

    CustomClassLoader l = new CustomClassLoader(path);
    // Locate the shaded jar in the lib directory
    // TODO(regisd) Alternatively, we could load JFlex and its dependency graph.
    try {
      for (File jar : additionalJars) {
        // We are in $basedir/testsuite/testcases
        l.addPath(jar);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    try {
      c = l.loadClass(theClass, true);
    } catch (ClassNotFoundException e) {
      System.out.println("no such class: " + e);
      return null;
    }

    try {
      main = c.getMethod("main", sig);
    } catch (NoSuchMethodException e) {
      System.out.println("no main: " + e);
      return null;
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean success = true;

    // System.out.println("loaded class "+theClass);

    PrintStream stdOut = System.out;
    System.setOut(new PrintStream(out, true));

    try {
      Object[] params = {cmd};
      main.invoke(null, params);
      System.setOut(stdOut);
    } catch (IllegalAccessException e) {
      System.setOut(stdOut);
      System.out.println("main not public :" + e + main);
      return null;
    } catch (InvocationTargetException e) {
      System.setOut(stdOut);
      System.out.println("test subject threw exception :" + e);
      success = false;
    }

    // System.out.println("finished exec class "+theClass);

    return new TestResult(out.toString(outputFileEncoding), success);
  }

  /**
   * Checks that all files exist in the given directory.
   *
   * @throws FileNotFoundException when a file doesn't exist.
   */
  private static void checkFilesExist(List<String> files, File dir) throws FileNotFoundException {
    if (files != null) {
      for (String src : files) {
        File f = new File(dir, src);
        if (!f.isFile()) {
          throw new FileNotFoundException(f.getPath());
        }
      }
    }
  }
}
