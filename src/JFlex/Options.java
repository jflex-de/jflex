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

package JFlex;

import java.io.File;

/**
 * Global JFlex options.
 * 
 * @author Gerwin Klein
 * @version JFlex 1.4, $Revision$, $Date$
 */
public class Options {
	
	static private File directory;

  /**
   * @return the outpur directory
   */
  public static File getDir() {    
    return directory;
  }

  /**
   * Set output directory
   * 
   * @param dirName the name of the directory to write output files to
   */
  public static void setDir(String dirName) {
  	setDir(new File(dirName)); 
  }
  

	/**
	 * Set output directory
	 * 
	 * @param d  the directory to write output files to
	 */
  public static void setDir(File d) {
    if ( d.isFile() ) {
      Out.error("Error: \""+d+"\" is not a directory.");
      throw new GeneratorException();
    }
    
    if ( !d.isDirectory() && !d.mkdirs() ) {
      Out.error("Error: couldn't create directory \""+d+"\"");
      throw new GeneratorException();
    }
  
    directory = d;
  }   
}
