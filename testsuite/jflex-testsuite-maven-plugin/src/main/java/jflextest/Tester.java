package jflextest;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Tester {

  public static final String VERSION = "1.1";

  final boolean verbose;
  final boolean stopOnFailure;

  public Tester(boolean verbose, boolean stopOnFailure) {
    this.verbose = verbose;
    this.stopOnFailure = stopOnFailure;
  }

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
  public boolean runTests(List<File> tests, File jflexUberJar) {
    int successCount = 0;
    int skipCount = 0;
    int failCount = 0;
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
        Status status = runTest(currentTest, jflexUberJar);
        switch (status) {
          case SUCCESS:
            successCount++;
            System.out.println("Test [" + test + "] finished successfully.");
            break;
          case JDK_MISMATCH:
            skipCount++;
            System.out.println("Test [" + test + "] skipped (JDK version mismatch).");
            break;
        }
      } catch (TestFailException e) {
        failCount++;
        System.err.println("Test [" + test + "] failed! " + e.getMessage());
        if (stopOnFailure) {
          break;
        }
      } catch (LoadException e) {
        failCount++;
        System.err.println("Load Error:" + e.getMessage());
      } catch (Exception e) {
        failCount++;
        System.err.println("Exception running test");
        e.printStackTrace();
      }
    }

    // Give some Status
    System.out.println();
    System.out.println(
        String.format(
            "All done: %s tests completed successfully, %s tests failed. %s in total",
            successCount, failCount, totalCount));
    if (totalCount != failCount + successCount + skipCount) {
      throw new IllegalStateException("Incorrect count of tests");
    }
    return 0 == failCount;
  }

  private Status runTest(TestCase currentTest, File jflexUberJar)
      throws TestFailException, UnsupportedEncodingException {
    if (currentTest == null) {
      throw new TestFailException("Test not loaded");
    }
    if (verbose) {
      System.out.println("Loaded successfully");
    }
    if (!currentTest.checkJavaVersion()) {
      return Status.JDK_MISMATCH;
    }
    currentTest.createScanner(jflexUberJar);
    while (currentTest.hasMoreToDo()) {
      currentTest.runNext(jflexUberJar);
    }
    return Status.SUCCESS;
  }

  private enum Status {
    SUCCESS,
    JDK_MISMATCH
  }
}
