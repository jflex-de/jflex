/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
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
 * This Exception could be thrown while scanning the specification 
 * (e.g. unmatched input)
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
public class ScannerException extends RuntimeException implements ErrorMessages {
  
  public int line;
  public int column;
  public int message;
  public File file;

  private ScannerException(File file, String text, int message, int line, int column) {
    super(text);
    this.file    = file;
    this.message = message;
    this.line    = line;
    this.column  = column;
  }


  /**
   * Creates a new ScannerException with a message only.
   *
   * @param message   the code for the error description presented to the user.
   */
  public ScannerException(int message) {
    this( null, messages[message], message, -1, -1 );
  }

  /**
   * Creates a new ScannerException for a file with a message only.
   *
   * @param file      the file in which the error occured
   * @param message   the code for the error description presented to the user.
   */
  public ScannerException(File file, int message) {
    this( file, messages[message], message, -1, -1 );
  }


  /**
   * Creates a new ScannerException with a message and line number.
   *
   * @param message   the code for the error description presented to the user.
   * @param line      the number of the line in the specification that 
   *                  contains the error
   */
  public ScannerException(int message, int line) {
    this( null, messages[message], message, line, -1 );
  }


  /**
   * Creates a new ScannerException for a file with a message and line number.
   *
   * @param message   the code for the error description presented to the user.
   * @param line      the number of the line in the specification that 
   *                  contains the error
   */
  public ScannerException(File file, int message, int line) {
    this( file, messages[message], message, line, -1 );
  }


  /**
   * Creates a new ScannerException with a message, line number and column.
   *
   * @param message   the code for the error description presented to the user.
   * @param line      the number of the line in the specification that 
   *                  contains the error
   * @param column    the column where the error starts
   */
  public ScannerException(File file, int message, int line, int column) {
    this( file, messages[message], message, line, column );
  }

}
