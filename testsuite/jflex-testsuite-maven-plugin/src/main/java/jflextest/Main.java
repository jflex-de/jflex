package jflextest;

import java.io.*;
import java.util.*;

public class Main {

  public static boolean verbose;
  public static String jflexTestVersion;

  final public static String version = "1.0alpha";

  public static void showUsage(String error) {
    System.out.println("Usage: [-v] [-testpath path] <test1> <test2> <test3> ...");
		System.out.println();
		System.out.println(error);
  }
  
  /**
   * Scan a directory for files with specific extension
   *
   * @return a list of files
   */
  public static List<File> scan(File dir, final String extension, boolean recursive) {
    List<File> result = new ArrayList<File>();

    FilenameFilter extFilter = new FilenameFilter() {
        public boolean accept(File f, String name) {
          return name.endsWith(extension);
        }
      };
    
    String [] files = dir.list(extFilter);
    if (files != null) {
      for (String file : files) 
        result.add(new File(dir, file));
    }

    if (!recursive) return result;
   
    FilenameFilter dirFilter = new FilenameFilter() {
        public boolean accept(File f, String name) {
          return (new File(f, name)).isDirectory();
        }
      };

    String [] dirs = dir.list(dirFilter);
    if (dirs == null) return result;
    
    for (String childDir : dirs) {
      List<File> t = scan(new File(dir,childDir), extension, true);
      result.addAll(t);
    }
    
    return result;
  }


  /**
   * @param tests  a list of File
   * @return true if all tests succeeded, false otherwise
   */
  public static boolean runTests(List<File> tests) {
    int successCount = 0;
    int totalCount = 0;

    for (File test : tests){      
      totalCount++;
      if (verbose) {
        System.out.println("");
        System.out.println("Processing test [" + test + "]");
      }
      try {
        // set path to test
        File currentDir = new File(test.getParent());
        // trying to load
        TestLoader loader = new TestLoader( new FileReader(test) );
        TestCase currentTest = loader.load();
        currentTest.init(currentDir);

        // success? -> run 
        if (currentTest == null) throw new TestFailException("not loaded");
        
        if (verbose)
          System.out.println("Loaded successfully"); // - Details:\n"+currentTest);

        currentTest.createScanner();
        while (currentTest.hasMoreToDo()) 
          currentTest.runNext();
        
        successCount++;
        System.out.println("Test [" + test + "] finished successfully.");
      }
      catch (TestFailException e) {
        System.out.println("Test ["+test+"] failed!");
      }
      catch (LoadException e) {
        System.out.println("Load Error:" + e.getMessage());
      }
      catch (IOException e) {
        System.out.println("IO Error:" + e.getMessage());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    // Give some Status
    System.out.println();
    System.out.println("All done - "+successCount+" tests completed successfully, "+(totalCount-successCount)+" tests failed.");
    return 0 == totalCount - successCount; 
  }

  public static void main(String[] argv) {
    System.setOut(new PrintStream(System.out,true));
    System.out.println("JFlexTest Version: " + version);
    System.out.println("Testing version: "+Exec.getJFlexVersion());    

    File dir = new File("testcases");   // assume user.dir/testcases
    List<File> files = new ArrayList<File>();

    for (int i=0; i < argv.length; i++) {
      
      if (argv[i].equals("-testpath")) {        
        if (++i >= argv.length) {
          showUsage("missing path argument");
          return;
        }
        dir = new File(argv[i]);
        // Quit if dir is not valid
        if (!dir.isDirectory()) {
          showUsage(dir+" - test path not found");
          return;
        }
        
        continue;
      }
      
      if (argv[i].equals("-v")) {
        verbose = true;
        continue;
      }
    
      List<File> t = scan(new File(dir,argv[i]), ".test", false);
      files.addAll(t);
    }

    // if we still didn't find anything, scan the whole test path
    if (files.isEmpty()) 
      files = scan(dir,".test",true);
    
    runTests(files);    
  }
}
