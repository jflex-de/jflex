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

import java.io.*;
import java.net.URL;
import java.util.Vector;


/**
 * This class stores the skeleton of generated scanners.
 *
 * The skeleton consists of several parts that can be emitted to
 * a file. Usually there is a portion of generated code
 * (produced in class Emitter) between every two parts of skeleton code.
 *
 * There is a static part (the skeleton code) and state based iterator
 * part to this class. The iterator part is used to emit consecutive skeleton
 * sections to some <code>PrintWriter</code>. 
 *
 * @see JFlex.Emitter
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
public class Skeleton {
  /** expected number of sections in the skeleton file */
  static final private int size = 21;

  /** platform specific newline */
  static final private String NL = System.getProperty("line.separator"); 

  /** The skeleton */  
  public static String line[];
  
  /** initialization */   
  static { readDefault(); }  
  
  // the state based, iterator part of Skeleton:

  /**
   * The current part of the skeleton (an index of nextStop[])
   */
  private int pos;  
  
  /**
   * The writer to write the skeleton-parts to
   */
  private PrintWriter out;


  /**
   * Creates a new skeleton (iterator) instance. 
   *
   * @param   out  the writer to write the skeleton-parts to
   */
  public Skeleton(PrintWriter out) {
    this.out = out;
  }


  /**
   * Emits the next part of the skeleton
   */
  public void emitNext() {
    out.print( line[pos++] );
  }


  /**
   * Make the skeleton private.
   *
   * Replaces all occurences of " public " in the skeleton with " private ". 
   */
  public static void makePrivate() {
    for (int i=0; i < line.length; i++) {
      line[i] = replace(" public ", " private ", line[i]);  
    }
  } 


  /**
   * Reads an external skeleton file for later use with this class.
   * 
   * @param skeletonFile  the file to read (must be != null and readable)
   */
  public static void readSkelFile(File skeletonFile) {
    if (skeletonFile == null)
      throw new IllegalArgumentException("Skeleton file must not be null");

    if (!skeletonFile.isFile() || !skeletonFile.canRead()) {
      Out.error("Error: cannot read skeleton file \"" + skeletonFile + "\".");
      throw new GeneratorException();
    }

    Out.println("Reading skeleton file \""+skeletonFile+"\"");

    try {
      BufferedReader reader = new BufferedReader(new FileReader(skeletonFile));
      readSkel(reader);
    }
    catch (IOException e) {
      Out.error("IO problem reading skeleton file.");
      throw new GeneratorException();
    }
  }


  /**
   * Reads an external skeleton file from a BufferedReader.
   * 
   * @param  reader             the reader to read from (must be != null)
   * @throws IOException        if an IO error occurs
   * @throws GeneratorException if the number of skeleton sections does not match 
   */
  public static void readSkel(BufferedReader reader) throws IOException {
    Vector lines = new Vector();
    StringBuffer section = new StringBuffer();

    String ln;
    while ((ln = reader.readLine()) != null) {
      if (ln.startsWith("---")) {
        lines.addElement(section.toString());
        section.setLength(0);
      } else {
        section.append(ln);
        section.append(NL);
      }
    }

    if (section.length() > 0)
      lines.addElement(section.toString());

    line = new String[lines.size()];
    for (int i = 0; i < lines.size(); i++)
      line[i] = (String) lines.elementAt(i);

    if (line.length != size) {
      Out.error(ErrorMessages.WRONG_SKELETON);
      throw new GeneratorException();
    }
  }
  
  /**
   * Replaces a with b in c.
   * 
   * @param a  the String to be replaced
   * @param b  the replacement
   * @param c  the String in which to replace a by b
   * @return a String object with a replaced by b in c 
   */
  public static String replace(String a, String b, String c) {
    StringBuffer result = new StringBuffer(c.length());
    int i = 0;
    int j = c.indexOf(a);
    
    while (j >= i) {
      result.append(c.substring(i,j));
      result.append(b);
      i = j+a.length();
      j = c.indexOf(a,i);
    }

    result.append(c.substring(i,c.length()));
    
    return result.toString();
  }

  
  /**
   * (Re)load the default skeleton. Looks in the current system class path.   
   */
  public static void readDefault() {
    ClassLoader l = ClassLoader.getSystemClassLoader();
    URL url = l.getResource("skeleton");
    
    if (url == null) {
      Out.error("IO problem reading default skeleton file.");
      throw new GeneratorException();    
    }
    
    try {
      InputStreamReader reader = new InputStreamReader(url.openStream());
      readSkel(new BufferedReader(reader)); 
    } catch (IOException e) {
      Out.error("IO problem reading default skeleton file.");
      throw new GeneratorException();
    }
  }
}
