package jflextest;

import java.util.*;
import java.io.*;

public class TestCase {
  
  /** command line switches for jflex invocation */
  private List<String> jflexCmdln;
  
  /** files on which to invoke jflex */
  private List<String> jflexFiles;
  
  /** command line switches for javac invocation */
  private List<String> javacCmdln;
  
  /** files on which to invoke javac */
  private List<String> javacFiles;

  /** lines with expected differences in jflex output */
  private List<Integer> jflexDiff;
  
  /** misc. */
  private String className;
  private String testName;
  private String description;
  private TestResult jflexResult, javacResult, classExecResult;

  /** base directory of this test case */
  private File testPath; 
  private boolean expectJavacFail, expectJFlexFail;

  /** inputOutputFiles to invoke test.main on and compare */
  private List<InputOutput> inputOutput;
  
  /** get- set- methods */
  public void setTestName(String s) { 
    testName = s; 
    className = testName.substring(0,1).toUpperCase() + testName.substring(1); 
  }

  public void setJFlexDiff(List<Integer> d) {
    jflexDiff = d;
  }

  public String getTestName() { return testName; }
  
  public void setDescription(String s){ description = s; }
  public void setExpectJavacFail(boolean b){ expectJavacFail = b; }
  public void setExpectJFlexFail(boolean b){ expectJFlexFail = b; }
  public void setJflexCmdln(List<String> v) { jflexCmdln = v; }
  public void setJavacCmdln(List<String> v) { javacCmdln = v; }
  public void setInputOutput(List<InputOutput> v) { inputOutput = v; }
  
  public TestCase() {
    setDefaults();
  }

  private void setDefaults() {
    //jflexCmdln = new ArrayList<String>();
    jflexFiles = new ArrayList<String>();
    //javacCmdln = new ArrayList<String>();
    javacFiles = new ArrayList<String>();
    //jflexCmdln.add("--dump");
  }

  public void init(File testDir) {
    // get files to process
    List<InputOutput> temp = new ArrayList<InputOutput>();
    String name;
    for (String file : testDir.list()) {
      if (file.endsWith(".input") && file.startsWith(testName + "-")) {
        name = file.substring(0, file.length() - 6);
        temp.add(new InputOutput((new File(testDir, name)).toString(),
                                 new File(testDir, name + ".output").exists()));
      }
    }
    setInputOutput(temp);
    testPath = testDir;
  } 
  
  public void createScanner() throws TestFailException {
    jflexFiles.add((new File(testPath, testName+".flex")).getPath());
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
      javacFiles.add(toCompile);      
      javacResult = Exec.execJavac(testPath, Main.jflexTestVersion);

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
    InputOutput current = inputOutput.remove(0);
    // Create List with only first input in	 
    List<String> param = new ArrayList<String>();
    param.add(current.getName() + ".input");

    // Excute Main on that input
    classExecResult = Exec.execClass
      (className, testPath.toString(), new ArrayList<String>(), param, 
       Main.jflexTestVersion);
    if (Main.verbose) {
      System.out.println("Running scanner on [" + current.getName() + "]");
    }
    
    // check for output conformance
    File expected = new File(current.getName() + ".output");

    if (expected.exists()) {
      DiffStream check = new DiffStream();        
      String diff;
      try {
        diff = check.diff(jflexDiff, new StringReader(classExecResult.getOutput()), 
                          new FileReader(expected));
      }
      catch (FileNotFoundException e) {
        System.out.println("Error opening file " + expected);
        throw new TestFailException();
      }
      if (diff != null) {
        System.out.println("Test failed, unexpected output: " + diff);
        System.out.println("Test output: " + classExecResult.getOutput());
        throw new TestFailException();
      }
    }
    else {
      System.out.println("Warning: no file for expected output [" + expected + "]");
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
