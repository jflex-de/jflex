package jflextest;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

import JFlex.GeneratorException;
import JFlex.Options;
import JFlex.Out;
import JFlex.SilentExit;

public class Exec {

  /**
   * Convert two Vectors with String elements into one
   * array containing all elements.
   *
   * @param a  a Vector with String elements
   * @param b  a Vector with String elements
   *
   * @return an array containing all elements of a then b
   */
  private static String [] toArray(Vector a, Vector b) {
    if (a == null) a = new Vector();
    if (b == null) b = new Vector();

    String [] cmdline = new String[a.size()+b.size()];
    
    int n = 0;
    Enumeration e;
    e = a.elements();
    while (e.hasMoreElements()) cmdline[n++] = (String) e.nextElement();
    e = b.elements();
    while (e.hasMoreElements()) cmdline[n++] = (String) e.nextElement();
    
    return cmdline;
  }

  /**
   * Call javac with input dir. Compile all *.java files below dir.
   */
  public static TestResult execJavac(File dir) {
    Project p = new Project();
    Javac javac = new Javac();
    Path path = new Path(p, dir.toString());
    javac.setProject(p);
    javac.setSrcdir(path);
    javac.setDestdir(dir);

    ByteArrayOutputStream out = new ByteArrayOutputStream();    
    PrintStream outSafe = System.err;
    System.setErr(new PrintStream(out));
    
    try {
      javac.execute();
      return new TestResult(out.toString(), true);
    }
    catch (BuildException e) {
      return new TestResult(out.toString(), false);
    }
    finally {
      System.setErr(outSafe);
    }
  }
  
  /** 
   * Call jflex with command line and input files.
   */
  public static TestResult execJFlex(Vector cmdline, Vector files) {
    String [] cmd = toArray(cmdline, files);

    ByteArrayOutputStream out = new ByteArrayOutputStream();    
    try {
      Options.setDefaults();
      Out.setOutputStream(out);
      JFlex.Main.generate(cmd);      
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
    Vector cmdLine = new Vector();
    cmdLine.add("--version");
    TestResult test = execJFlex(cmdLine, new Vector());
    return test.getOutput();
  }

  /**
   * Call main method of specified class with command line and input files.
   * 
   * @param path  the directory in which to search for the class
   */
  public static TestResult execClass(String theClass, String path, Vector cmdline, Vector files) {
    String [] cmd = toArray(cmdline, files);
    Class c;    
    Method main;
    Class [] sig = { String [].class };

    // System.out.println("exec class "+theClass);
    // System.out.println("cmdline "+cmdline+"\nfiles: "+files);    

    CustomClassLoader l = new CustomClassLoader(path);

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

    return new TestResult(out.toString(), success);
  }


  /** for testing */
  public static void main(String args[]) {
    Vector files = new Vector();
    for (int i = 0; i < args.length; i++) files.addElement(args[i]);
    System.out.println();
    // System.out.println("javac:\n"+execJavac(new Vector(), files));

    System.out.println("jflex:\n"+execJFlex(new Vector(), files));

    System.out.println("class:\n"+execClass("jflextest.Main", ".", new Vector(), files));
  }
}
