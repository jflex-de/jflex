package jflextest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import jflex.GeneratorException;
import jflex.LexGenerator;
import jflex.MacroException;
import jflex.Main;
import jflex.Options;
import jflex.ScannerException;
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
   * Call javac on toCompile in input dir. If toCompile is null, all *.java files below dir will be
   * compiled.
   */
  public static TestResult execJavac(String toCompile, File dir, File additionalJar) {
    Project p = new Project();
    Javac javac = new Javac();
    Path path = new Path(p, dir.toString());
    javac.setProject(p);
    javac.setSrcdir(path);
    javac.setDestdir(dir);
    javac.setTarget(JAVA_VERSION);
    javac.setSource(JAVA_VERSION);
    javac.setSourcepath(new Path(p, "")); // Only compile explicitly specified source files
    javac.setIncludes(toCompile);
    Path classPath = javac.createClasspath();
    classPath.setPath(additionalJar.getAbsolutePath());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream outSafe = System.err;
    System.setErr(new PrintStream(out));

    try {
      javac.execute();
      out.flush();
      return new TestResult(out.toString(), true);
    } catch (BuildException e) {
      return new TestResult(
          "Compilation in " + dir + "with classpath " + classPath + NL + e + NL + out.toString(),
          false);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      System.setErr(outSafe);
    }
  }

  /** Call jflex with command line and input files. */
  public static TestResult execJFlex(List<String> cmdline, List<String> files) {
    Options.Builder opts = Options.builder();
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      try {
        Main.parseOptions(cmdline.toArray(new String[0]), opts);
        opts.setBackup(false); // This is a waste of time when running tests.
        Options options = opts.build();
        LexGenerator lexGenerator = new LexGenerator(options);
        lexGenerator.setLogOut(out);
        for (String fileName : files) {
          lexGenerator.generateFromFile(new File(fileName));
        }
        out.flush();
        return new TestResult(out.toString(), true);
      } catch (GeneratorException | ScannerException | MacroException e) {
        out.flush();
        return new TestResult(out.toString(), false);
      } catch (SilentExit silentExit) {
        silentExit.printStackTrace();
        return new TestResult(silentExit.getMessage(), false);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Call main method of specified class with command line and input files.
   *
   * @param path the directory in which to search for the class
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

    PrintStream stdOut = System.out;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      System.setOut(new PrintStream(out, true));
      Object[] params = {cmd};
      main.invoke(null, params);
      out.flush();
      return new TestResult(out.toString(outputFileEncoding), true);
    } catch (IllegalAccessException e) {
      System.err.println("main not public :" + e + main);
      return null;
    } catch (InvocationTargetException e) {
      System.err.println("test subject threw exception :" + e);
      return new TestResult(e.getMessage(), false);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      System.setOut(stdOut);
    }
  }
}
