package jflextest;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

import jflex.GeneratorException;
import jflex.Options;
import jflex.Out;
import jflex.SilentExit;

public class Exec {

    private static String javaVersion = "1.7";

    /**
   * Convert two Lists with String elements into one
   * array containing all elements.
   *
   * @param a  a List with String elements
   * @param b  a List with String elements
   *
   * @return an array containing all elements of a then b
   */
  private static String [] toArray(List<String> a, List<String> b) {
    if (a == null) a = new ArrayList<String>();
    if (b == null) b = new ArrayList<String>();
    
    String [] cmdline = new String[a.size() + b.size()];
    
    int n = 0;
    for (String aElem : a) cmdline[n++] = aElem;
    for (String bElem : b) cmdline[n++] = bElem;
    
    return cmdline;
  }

  /**
   * Call javac on toCompile in input dir. If toCompile is null, 
   * all *.java files below dir will be compiled.
   */
  public static TestResult execJavac(String toCompile, File dir, String jflexTestVersion) {
    Project p = new Project();
    Javac javac = new Javac();
    Path path = new Path(p, dir.toString());
    javac.setProject(p);
    javac.setSrcdir(path);
    javac.setDestdir(dir);
    javac.setTarget(javaVersion);
    javac.setSource(javaVersion);
    javac.setSourcepath(new Path(p, "")); // Only compile explicitly specified source files
    javac.setIncludes(toCompile);
    Path classPath = javac.createClasspath();
    // Locate the jflex jar in the user's Maven local repository
    classPath.setPath(System.getProperty("user.home")
                      + "/.m2/repository/de/jflex/jflex/" + jflexTestVersion
                      + "/jflex-" + jflexTestVersion + ".jar");

    ByteArrayOutputStream out = new ByteArrayOutputStream();    
    PrintStream outSafe = System.err;
    System.setErr(new PrintStream(out));
    
    try {
      javac.execute();
      return new TestResult(out.toString(), true);
    }
    catch (BuildException e) {
      return new TestResult
        (e + System.getProperty("line.separator") + out.toString(), false);
    }
    finally {
      System.setErr(outSafe);
    }
  }
  
  /** 
   * Call jflex with command line and input files.
   */
  public static TestResult execJFlex(List<String> cmdline, List<String> files) {
    String[] cmd = toArray(cmdline, files);
    ByteArrayOutputStream out = new ByteArrayOutputStream();    
    try {
      Options.setDefaults();
      Out.setOutputStream(out);
      jflex.Main.generate(cmd);      
      return new TestResult(out.toString(), true);
    }
    catch (GeneratorException e) {
      return new TestResult(out.toString(), false);
    }
    catch (SilentExit e) {
      return new TestResult(out.toString(), false);
    }       
  }

  /** 
   * Return String with JFlex version number.
   */
  public static String getJFlexVersion() {    
    List<String> cmdLine = new ArrayList<String>();
    cmdLine.add("--version");
    TestResult test = execJFlex(cmdLine, new ArrayList<String>());
    return test.getOutput();
  }

  /**
   * Call main method of specified class with command line and input files.
   * 
   * @param path  the directory in which to search for the class
   */
  public static TestResult execClass
    (String theClass, String path, List<String> cmdline, List<String> files, 
     String jflexTestVersion, String outputFileEncoding)
    throws UnsupportedEncodingException {
    
    String [] cmd = toArray(cmdline, files);
    Class c;    
    Method main;
    Class [] sig = { String [].class };

    // System.out.println("exec class "+theClass);
    // System.out.println("cmdline "+cmdline+"\nfiles: "+files);    

    CustomClassLoader l = new CustomClassLoader(path);
    // Locate the jflex jar in the user's Maven local repository
    l.addPath(System.getProperty("user.home")
              + "/.m2/repository/de/jflex/jflex/" + jflexTestVersion 
              + "/jflex-" + jflexTestVersion + ".jar");

    try {
      c = l.loadClass(theClass, true);
    }
    catch (ClassNotFoundException e) {
      System.out.println("no such class: "+e);
      return null;
    }

    try {
      main = c.getMethod("main", sig);
    }
    catch (NoSuchMethodException e) {
      System.out.println("no main: "+e);
      return null;
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean success = true;

    // System.out.println("loaded class "+theClass);

    PrintStream stdOut = System.out;
    System.setOut(new PrintStream(out,true));
    
    try {
      Object [] params = { cmd };
      main.invoke(null,params);
      System.setOut(stdOut);
    }
    catch (IllegalAccessException e) {
      System.setOut(stdOut);
      System.out.println("main not public :"+e+main);
      return null;
    }
    catch (InvocationTargetException e) { 
      System.setOut(stdOut);
      System.out.println("test subject threw exception :"+e);
      success = false;
    }

    // System.out.println("finished exec class "+theClass);

    return new TestResult(out.toString(outputFileEncoding), success);
  }


  /** for testing */
  public static void main(String args[]) {
    List<String> files = new ArrayList<String>();
    files.addAll(Arrays.asList(args));
    System.out.println();
    // System.out.println("javac:\n"+execJavac(new ArrayList<String>(), files));

    System.out.println("jflex:\n" + execJFlex(new ArrayList<String>(), files));

    try {
      System.out.println
      ("class:\n" 
         + execClass("jflextest.Main", ".", new ArrayList<String>(), files, 
                     "1.7.0-SNAPSHOT", "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      System.out.println("UTF-8 is not a supported encoding.");
      System.exit(1);
    }
  }
}
