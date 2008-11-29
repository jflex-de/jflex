/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex                                                                   *
 * Copyright (C) 1998-2008  Gerwin Klein <lsf@jflex.de>,                   *
 *                          Regis Decamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

import java.io.File;
import java.io.IOException;

import jflex.anttask.JFlexTask;


import junit.framework.TestCase;

/**
 * Unit tests for the jflex ant task.
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class AntTaskTests extends TestCase {

  private JFlexTask task;
  private final String DIR_RESOURCES="src/test/resources";
  private final String FILE_LEXSCAN="/jflex/LexScan-test.flex";

  /**
   * Constructor for AntTaskTests.
   * 
   * @param name  test case name
   */
  public AntTaskTests(String name) {
    super(name);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    Options.setDefaults();
    task = new JFlexTask();
  }

  public void testPackageAndClass() throws IOException {
    task.setFile(new File(DIR_RESOURCES+FILE_LEXSCAN));
    task.findPackageAndClass();
    assertEquals("jflex", task.getPackage());
    assertEquals("LexScan",task.getClassName());
  }

  public void testPackageAndClassDefaults() throws IOException {
	  //FIXME
    task.setFile(new File(DIR_RESOURCES+"/jflex/simple.flex"));
    task.findPackageAndClass();
    assertEquals(null, task.getPackage());
    assertEquals("Yylex",task.getClassName());
  }

  public void testDestdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES+FILE_LEXSCAN));
    File dir = new File("target/test/src");
    task.setDestdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // not default jflex logic, but javac (uses package name) 
    assertEquals(new File(dir, "jflex"), Options.getDir());
  }

  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES+FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic 
    assertEquals(dir, Options.getDir());
  }

  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES+FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic 
    assertEquals(new File(DIR_RESOURCES+"/jflex"),Options.getDir());
  }

  public void testNomin() {
    assertTrue(!Options.no_minimize);
    task.setNomin(true);
    assertTrue(Options.no_minimize);
  }

  public void testSkipMinimization() {
    assertTrue(!Options.no_minimize);
    task.setSkipMinimization(true);
    assertTrue(Options.no_minimize);
  }

  public void testNobak() {
    assertTrue(!Options.no_backup);
    task.setNobak(true);
    assertTrue(Options.no_backup);
  }

  public void testCodeGen() {
    task.setSwitch(true);
    assertEquals(Options.SWITCH, Options.gen_method);
    task.setTable(true);
    assertEquals(Options.TABLE, Options.gen_method);
    task.setPack(true);
    assertEquals(Options.PACK, Options.gen_method);
  }

  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertTrue(jflex.Skeleton.line[3].indexOf("java.util.Stack") > 0);
  }
  
  public void testVerbose() {
    task.setVerbose(false);
    assertTrue(!Options.verbose);
    task.setVerbose(true);
    assertTrue(Options.verbose);
  }

  public void testTime() {
    assertTrue(!Options.time);
    task.setTimeStatistics(true);
    assertTrue(Options.time);   
    task.setTime(false);
    assertTrue(!Options.time);    
  }
  
  public void testDot() {
    assertTrue(!Options.dot);
    task.setDot(true);
    assertTrue(Options.dot);
    task.setGenerateDot(false);
    assertTrue(!Options.dot);
  }
  
  public void testDump() {
    assertTrue(!Options.dump);
    task.setDump(true);
    assertTrue(Options.dump);
  }
  
  public void testJlex() {
    assertTrue(!Options.jlex);
    task.setJLex(true);
    assertTrue(Options.jlex);
  }
}
