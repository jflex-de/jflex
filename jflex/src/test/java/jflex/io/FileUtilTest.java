package jflex.io;

import java.io.File;
import junit.framework.TestCase;
import org.junit.Test;

public class FileUtilTest extends TestCase {

  @Test
  public void test_getRelativePath_fileInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c/d/foo.bar");
    assertEquals("d/foo.bar", FileUtil.getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_fileNotInDir() {
    File dir = new File("/a/b/c");
    File f = new File("/d/e/f/foo.bar");
    assertEquals("/d/e/f/foo.bar", FileUtil.getRelativePath(dir, f));
  }

  @Test
  public void test_getRelativePath_sameStart() {
    File dir = new File("/a/b/c");
    File f = new File("/a/b/c.txt");
    assertEquals("/a/b/c.txt", FileUtil.getRelativePath(dir, f));
  }

  @Test
  public void test_slashify() {
    assertEquals("C:/u0022.txt", FileUtil.slashify("C:\\u0022.txt"));
  }
}
