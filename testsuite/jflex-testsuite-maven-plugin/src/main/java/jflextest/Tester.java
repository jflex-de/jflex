package jflextest;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tester {

  public static boolean verbose;
  public static String jflexTestVersion;

  public static final String version = "1.0alpha";

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
    List<File> result = new ArrayList<>();

    FilenameFilter extFilter =
        new FilenameFilter() {
          public boolean accept(File f, String name) {
            return name.endsWith(extension);
          }
        };

    String[] files = dir.list(extFilter);
    if (files != null) {
      for (String file : files) result.add(new File(dir, file));
    }

    if (!recursive) return result;

    FilenameFilter dirFilter =
        new FilenameFilter() {
          public boolean accept(File f, String name) {
            return (new File(f, name)).isDirectory();
          }
        };

    String[] dirs = dir.list(dirFilter);
    if (dirs == null) return result;

    for (String childDir : dirs) {
      List<File> t = scan(new File(dir, childDir), extension, true);
      result.addAll(t);
    }

    return result;
  }

  /**
   * @param tests a list of File
   * @param jflexUberJar The JFlex shaded jar
   * @return true if all tests succeeded, false otherwise
   */
  public static boolean runTests(List<File> tests, File jflexUberJar) {
    int successCount = 0;
    int totalCount = 0;

    for (File test : tests) {
      totalCount++;
      if (verbose) {
        System.out.println("");
        System.out.println("Processing test [" + test + "]");
      }
      try {
        // set path to test
        File currentDir = new File(test.getParent());
        // trying to load
        TestLoader loader = new TestLoader(new FileReader(test));
        TestCase currentTest = loader.load();
        currentTest.init(currentDir);

        // success? -> run
        if (currentTest == null) throw new TestFailException("not loaded");
        if (verbose) System.out.println("Loaded successfully"); // - Details:\n"+currentTest);

        if (currentTest.checkJavaVersion()) {
          currentTest.createScanner(jflexUberJar);
          while (currentTest.hasMoreToDo()) currentTest.runNext(jflexUberJar);

          successCount++;
          System.out.println("Test [" + test + "] finished successfully.");
        } else {
          successCount++;
          System.out.println("Test [" + test + "] skipped (JDK version mismatch).");
        }
      } catch (TestFailException e) {
        System.out.println("Test [" + test + "] failed!");
      } catch (LoadException e) {
        System.out.println("Load Error:" + e.getMessage());
      } catch (IOException e) {
        System.out.println("IO Error:" + e.getMessage());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // Give some Status
    System.out.println();
    System.out.println(
        "All done - "
            + successCount
            + " tests completed successfully, "
            + (totalCount - successCount)
            + " tests failed.");
    return 0 == totalCount - successCount;
  }
}
