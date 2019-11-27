package jflex.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import org.junit.Test;

public class FileUtilsTest {

  @Test
  public void test_getRelativePath_fileInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c/d/foo.bar");
    assertEquals("d/foo.bar", FileUtils.getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_fileNotInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/d/e/f/foo.bar");
    assertEquals("/d/e/f/foo.bar", FileUtils.getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_sameStart() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c.txt");
    assertEquals("/a/b/c.txt", FileUtils.getRelativePath(dir, f));
  }

  @Test
  public void test_slashify() {
    assertEquals("C:/u0022.txt", FileUtils.slashify("C:\\u0022.txt"));
  }
}
