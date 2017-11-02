package jflextest;

import java.util.*;
import java.io.*;

public class TestCase {
  
  /** command line switches for jflex invocation */
  private List<String> jflexCmdln;
  
  /** files on which to invoke jflex */
  private List<String> jflexFiles;
  
  /** command line switches for javac invocation */
  private List<String> javacExtraFiles;
  
  /** lines with expected differences in jflex output */
  private List<Integer> jflexDiff;
  
  /** Test input file encoding -- defaults to UTF-8 */
  private String inputFileEncoding = "UTF-8";
  
  /** Test output file encoding -- defaults to UTF-8 */
  private String outputFileEncoding = "UTF-8";
  
  /** Single common input file for all outputs */
  private String commonInputFile = null;

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
    className = testName.substring(0,1).toUpperCase(Locale.ENGLISH) + testName.substring(1); 
  }

  public void setJFlexDiff(List<Integer> d) {
    jflexDiff = d;
  }

  public String getTestName() { return testName; }
  
  public void setDescription(String s){ description = s; }
  public void setExpectJavacFail(boolean b){ expectJavacFail = b; }
  public void setExpectJFlexFail(boolean b){ expectJFlexFail = b; }
  public void setJflexCmdln(List<String> v) { jflexCmdln = v; }
  public void setJavacExtraFiles(List<String> v) { javacExtraFiles = v; }
  public void setInputOutput(List<InputOutput> v) { inputOutput = v; }
  public void setInputFileEncoding(String e) { inputFileEncoding = e; }
  public void setOutputFileEncoding(String e) { outputFileEncoding = e; }
  public void setCommonInputFile(String f) { commonInputFile = f; }
  
  public TestCase() {
    setDefaults();
  }

  private void setDefaults() {
    //jflexCmdln = new ArrayList<String>();
    jflexFiles = new ArrayList<String>();
    //javacExtraFiles = new ArrayList<String>();
    //jflexCmdln.add("--dump");
  }

  public void init(File testDir) {
    // get files to process
    List<InputOutput> temp = new ArrayList<InputOutput>();
    String name;
    for (String file : testDir.list()) {
      if (null != commonInputFile) {
        if (file.equals(testName + ".output")) {
          temp.add(new InputOutput((new File(testDir, testName)).toString(), true));
          commonInputFile = (new File(testDir, commonInputFile)).toString();
        }
      } else {
        if (file.endsWith(".input") && file.startsWith(testName + "-")) {
          name = file.substring(0, file.length() - 6);
          temp.add(new InputOutput((new File(testDir, name)).toString(),
                                   new File(testDir, name + ".output").exists()));
        }
      }
    }
    // When a common input file is specified, but no output file exists, add
    // a single InputOutput for the common input file.
    if (null != commonInputFile && 0 == temp.size()) {
      temp.add(new InputOutput(null, false));
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
      StringBuilder builder = new StringBuilder();
      builder.append(new File(testPath, className+".java").getName());
      if (null != javacExtraFiles) {
        for (String extraFile : javacExtraFiles) {
          builder.append(',').append(extraFile);
        }
      }
      String toCompile = builder.toString();
      if (Main.verbose) { 
        System.out.println("File(s) to Compile: " + toCompile);
      }
      javacResult = Exec.execJavac(toCompile, testPath, Main.jflexTestVersion);

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
      } else {
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
      }
    }
  }
  
  public boolean hasMoreToDo() {
    // check if there's more files to run scanner main on
    return !(inputOutput.isEmpty());
  }
  
  public void runNext() throws TestFailException, UnsupportedEncodingException {
    // Get first file and remove it from list
    InputOutput current = inputOutput.remove(0);
    // Create List with only first input in	 
    List<String> inputFiles = new ArrayList<String>();
    inputFiles.add
      (null != commonInputFile ? commonInputFile : current.getName() + ".input");
    // Excute Main on that input
    List<String> cmdLine = new ArrayList<String>();
    cmdLine.add("--encoding");
    cmdLine.add(inputFileEncoding);
    classExecResult = Exec.execClass
      (className, testPath.toString(), cmdLine, inputFiles, 
       Main.jflexTestVersion, outputFileEncoding);
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
                          new InputStreamReader(new FileInputStream(expected), 
                                                outputFileEncoding));
      }
      catch (FileNotFoundException e) {
        System.out.println("Error opening file " + expected);
        throw new TestFailException();
      } catch (UnsupportedEncodingException e) {
        System.out.println("Unsupported encoding '" + outputFileEncoding + "'");
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
        + "JFlex Command line: " + jflexCmdln
        + (null != javacExtraFiles 
            ? " Javac Extra Files: " + Arrays.toString(javacExtraFiles.toArray())
            : "") + "\n"
        + "Files to run Main on " + inputOutput 
        + (null != commonInputFile ? " Common input file: " + commonInputFile : "");
  }
}
