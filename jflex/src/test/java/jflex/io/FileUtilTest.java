package jflex.io;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.annotation.Target;
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
    assertThat(FileUtil.slashify("C:\\u1234.txt")).isEqualTo("C:/u1234.txt");
  }
}
