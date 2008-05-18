package jflextest;

import java.util.*;
import java.io.*;

public class TestCase {
  
  /** command line switches for jflex invocation */
  private Vector /* of String */ jflexCmdln;
  
  /** files on which to invoke jflex */
  private Vector /* of String */ jflexFiles;
  
  /** command line switches for javac invocation */
  private Vector /* of String */ javacCmdln;
  
  /** files on which to invoke javac */
  private Vector /* of String */ javacFiles;

  /** lines with expected differences in jflex output */
  private Vector /* of Integer */ jflexDiff;
  
  /** misc. */
  private String className;
  private String testName;
  private String description;
  private TestResult jflexResult, javacResult, classExecResult;

  /** base directory of this test case */
  private File testPath; 
  private boolean expectJavacFail, expectJFlexFail;

  /** inputOutputFiles to invoke test.main on and compare */
  private Vector inputOutput;
  
  /** get- set- methods */
  public void setTestName(String s) { 
    testName = s; 
    className = testName.substring(0,1).toUpperCase()+testName.substring(1); 
  }

  public void setJFlexDiff(Vector d) {
    jflexDiff = d;
  }

  public String getTestName() { return testName; }
  
  public void setDescription(String s){ description = s; }
  public void setExpectJavacFail(boolean b){ expectJavacFail = b; }
  public void setExpectJFlexFail(boolean b){ expectJFlexFail = b; }
  public void setJflexCmdln(Vector v) { jflexCmdln = v; }
  public void setJavacCmdln(Vector v) { javacCmdln = v; }
  public void setInputOutput(Vector v) { inputOutput = v; }
  
  public TestCase() {
    setDefaults();
  }

  private void setDefaults() {
    //jflexCmdln = new Vector();
    jflexFiles = new Vector();
    //javacCmdln = new Vector();
    javacFiles = new Vector();
    //jflexCmdln.addElement("--dump");
  }

  public void init(File testDir) {
    // get files to process
    Vector temp = new Vector();
    String[] files = testDir.list();
    String name;
    for (int i=0; i<files.length;i++){
      if (files[i].endsWith(".input")) {
        name = files[i].substring(0,files[i].length()-6);
        temp.addElement( new InputOutput( (new File(testDir,name)).toString(), new File(testDir,name+".output").exists() ) );
      }
    }
    setInputOutput(temp);
    testPath=testDir;
  } 
  
  public void createScanner() throws TestFailException {
    jflexFiles.addElement((new File(testPath, testName+".flex")).getPath());
    // invoke JFlex
    jflexResult = Exec.execJFlex(jflexCmdln, jflexFiles);
    // System.out.println(jflexResult);

    if (jflexResult.getSuccess()) {
      // Scanner generation successful
      if (Main.verbose) {
        System.out.println("Scanner generation successful");
      }

      if (expectJFlexFail) {
        System.out.println("JFlex failure expected!");
        throw new TestFailException();
      }

      // check JFlex output conformance
      File expected = new File(testPath, testName+"-flex.output");

      if (expected.exists()) {
        DiffStream check = new DiffStream();        
        String diff;
        try {
          diff = check.diff(jflexDiff, new StringReader(jflexResult.getOutput()), new FileReader(expected));
        }
        catch (FileNotFoundException e) {
          System.out.println("Error opening file "+expected);
          throw new TestFailException();
        }
        if (diff != null) {
          System.out.println("Test failed, unexpected jflex output: "+diff);
          System.out.println("JFlex output: "+jflexResult.getOutput());
          throw new TestFailException();
        }
      }
      else {
        System.out.println("Warning: no file for expected output ["+expected+"]");
      }

      // Compile Scanner
      String toCompile = new File(testPath, className+".java").getPath();
      if (Main.verbose) { 
        System.out.println("File to Compile: "+toCompile);
      }
      javacFiles.addElement(toCompile);      
      javacResult = Exec.execJavac(testPath);

      // System.out.println(javacResult);
      if (Main.verbose) {
        System.out.println("Compilation successful: "+javacResult.getSuccess()+" [expected: "+!expectJavacFail+"]");
      }
      if (javacResult.getSuccess() == expectJavacFail) {
        System.out.println("Compilation failed, messages:");
        System.out.println(javacResult.getOutput());        
        throw new TestFailException();
      }
    }
    else {
      if (!expectJFlexFail) {
        System.out.println("Scanner generation failed!");
        System.out.println("JFlex output was:\n"+jflexResult.getOutput());
        throw new TestFailException();
      }
    }
  }
  
  public boolean hasMoreToDo() {
    // check if there's more files to run scanner main on
    return !(inputOutput.isEmpty());
  }
  
  public void runNext() throws TestFailException {
    // Get first file and remove it from vector
    InputOutput current = (InputOutput)(inputOutput.firstElement());
    inputOutput.removeElement(current);
    // Create Vector with only first input in	 
    Vector param = new Vector();
    param.addElement(current.getName()+".input");

    // Excute Main on that input
    classExecResult = Exec.execClass(className, testPath.toString(), new Vector(), param);
    if (Main.verbose) {
      System.out.println("Running scanner on ["+current.getName()+"]");
    }
    
    // check for output conformance
    File expected = new File(current.getName()+".output");

    if (expected.exists()) {
      DiffStream check = new DiffStream();        
      String diff;
      try {
        diff = check.diff(jflexDiff, new StringReader(classExecResult.getOutput()), new FileReader(expected));
      }
      catch (FileNotFoundException e) {
        System.out.println("Error opening file "+expected);
        throw new TestFailException();
      }
      if (diff != null) {
        System.out.println("Test failed, unexpected output: "+diff);
        System.out.println("Test output: "+classExecResult.getOutput());
        throw new TestFailException();
      }
    }
    else {
      System.out.println("Warning: no file for expected output ["+expected+"]");
    }

    // System.out.println(classExecResult);
  }
  
  public String toString(){
    return "Testname: "+testName+"\nDescription: " + description 
			+ "JFlexFail: " + expectJFlexFail + " JavacFail: " + expectJavacFail + "\n" 
			+ "JFlex Command line: " + jflexCmdln + "Javac Command Line" +  javacCmdln + "\n"
			+ "Files to run Main on" + inputOutput;
  }
}
