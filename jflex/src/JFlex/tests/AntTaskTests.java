/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4                                                               *
 * Copyright (C) 1998-2003  Gerwin Klein <lsf@jflex.de>                    *
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

package JFlex.tests;

import java.io.File;

import JFlex.Options;
import JFlex.anttask.JFlexTask;

import junit.framework.TestCase;

/**
 * Unit tests for the jflex ant task.
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class AntTaskTests extends TestCase {

	private JFlexTask task;

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
    task = new JFlexTask();
  }
  
	public void testDir() {
		File dir = new File("src");
    task.setDestdir(dir);
		// not default jflex logic, but javac (uses package name) 
    task.configure(new File(dir,"JFlex")); 
    assertEquals(Options.getDir(),new File(dir,"JFlex"));
  }
}
