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
import java.util.*;
import java.text.*;

/**
 * This class manages the actual code generation, putting
 * the scanner together, filling in skeleton sections etc.
 *
 * Table compression, String packing etc. is also done here.
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
final public class Emitter {
    
  // bit masks for state attributes
  static final private int FINAL = 1;
  static final private int PUSHBACK = 2;
  static final private int LOOKEND = 4;
  static final private int NOLOOK = 8;

  // maximum size of the compressed transition table
  // String constants are stored as UTF8 with 2 bytes length
  // field in class files. One Unicode char can be up to 3 
  // UTF8 bytes.
  // 64K max and some safety 
  static final int maxSize = 0xFFFF-6;

  static final private String date = (new SimpleDateFormat()).format(new Date());

  private File inputFile;

  private PrintWriter out;
  private Skeleton skel;
  private LexScan scanner;
  private LexParse parser;
  private DFA dfa;

  // for switch statement:
  // table[i][j] is the set of input characters that leads from state i to state j
  private CharSet table[][];

  private boolean isTransition[];
  
  // noTarget[i] is the set of input characters that have no target state in state i
  private CharSet noTarget[];
      
  // for row killing:
  private int numRows;
  private int [] rowMap;
  private boolean [] rowKilled;
  
  // for col killing:
  private int numCols;
  private int [] colMap;
  private boolean [] colKilled;
  
  private int numTableChunks;

  private CharClassIntervall [] intervalls;
  private int currentIntervall;

  private String visibility = "public";

  public Emitter(File inputFile, LexParse parser, DFA dfa) throws IOException {

    String name = parser.scanner.className+".java";

    File outputFile = normalize(name, Options.getDir(), inputFile);

    Out.println("Writing code to \""+outputFile+"\"");
    
    this.out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
    this.parser = parser;
    this.scanner = parser.scanner;
    this.visibility = scanner.visibility;
    this.inputFile = inputFile;
    this.dfa = dfa;
    this.skel = new Skeleton(out);
  }


  /**
   * Constructs a file in a path or in the same directory as
   * another file. Makes a backup if the file already exists.
   *
   * @param name  the name (without path) of the file
   * @param path  the path where to construct the file
   * @param input fallback location if path = <tt>null</tt>
   *              (expected to be a file in the directory to write to)   
   */
  public static File normalize(String name, File path, File input) {
    File outputFile;

    if ( Options.getDir() == null ) 
      if ( input == null || input.getParent() == null )
        outputFile = new File(name);
      else
        outputFile = new File(input.getParent(), name);
    else 
      outputFile = new File(Options.getDir(), name);
        
    if ( outputFile.exists() && !Options.no_backup ) {      
      File backup = new File( outputFile.toString()+"~" );
      
      if ( backup.exists() ) backup.delete();
      
      if ( outputFile.renameTo( backup ) )
        Out.println("Old file \""+outputFile+"\" saved as \""+backup+"\"");
      else
        Out.println("Couldn't save old file \""+outputFile+"\", overwriting!");
    }

    return outputFile;
  }
  
  private void println() {
    out.println();
  }

  private void println(String line) {
    out.println(line);
  }

  private void println(int i) {
    out.println(i);
  }

  private void print(String line) {
    out.print(line);
  }

  private void print(int i) {
    out.print(i);
  }

  private void print(int i, int tab) {
    int exp;

    if (i < 0) 
      exp = 1;
    else
      exp = 10;

    while (tab-- > 1) {
      if (Math.abs(i) < exp) print(" ");
      exp*= 10;
    }

    print(i);
  }

  private void printUC(int i) {
    if (i > 255) {
      print("\\u");
      if (i < 0x1000) print("0");
      print(Integer.toHexString(i));
    }
    else {
      print("\\");
      print(Integer.toOctalString(i));
    }
  } 

  private void emitScanError() {
    print("  private void yy_ScanError(int errorCode)");
    
    if (scanner.scanErrorException != null) 
      print(" throws "+scanner.scanErrorException);

    println(" {");

    skel.emitNext();

    if (scanner.scanErrorException == null)
      println("    throw new Error(message);");
    else
      println("    throw new "+scanner.scanErrorException+"(message);");    

    skel.emitNext();

    print("  private void yypushback(int number) ");     
    
    if (scanner.scanErrorException == null)
      println(" {");
    else       
      println(" throws "+scanner.scanErrorException+" {");
  }

  private void emitMain() {
    if ( !(scanner.standalone || scanner.debugOption || scanner.cupDebug) ) return;

    if ( scanner.cupDebug ) {
      println("  /**");
      println("   * Converts an int token code into the name of the");
      println("   * token by reflection on the cup symbol class/interface "+scanner.cupSymbol);
      println("   *");
      println("   * This code was contributed by Karl Meissner <meissnersd@yahoo.com>"); 
      println("   * <a href=\"http://meissner.v0.net/msd.htm\">meissner.v0.net/msd.htm</a>");
      println("   */");
      println("  private String getTokenName(int token) {");
      println("    try {");
      println("      java.lang.reflect.Field [] class_fields = " + scanner.cupSymbol + ".class.getFields();");
      println("      for (int i = 0; i < class_fields.length; i++) {");
      println("        if (class_fields[i].getInt(null) == token) {");
      println("          return class_fields[i].getName();");
      println("        }");
      println("      }");
      println("    } catch (Exception e) {");
      println("      e.printStackTrace(System.err);");
      println("    }");
      println("");
      println("    return \"UNKNOWN TOKEN\";");
      println("  }");
      println("");
      println("  /**");
      println("   * Same as "+scanner.functionName+" but also prints the token to standard out");
      println("   * for debugging.");
      println("   *");
      println("   * This code was contributed by Karl Meissner <meissnersd@yahoo.com>"); 
      println("   * <a href=\"http://meissner.v0.net/msd.htm\">meissner.v0.net/msd.htm</a>");
      println("   */");

      print("  "+visibility+" ");
      if ( scanner.tokenType == null ) {
        if ( scanner.isInteger )
          print( "int" );
        else 
          if ( scanner.isIntWrap )
            print( "Integer" );
          else
            print( "Yytoken" );
      }
      else
        print( scanner.tokenType );
      
      print(" debug_");
      
      print(scanner.functionName);
      
      print("() throws java.io.IOException");
    
      if ( scanner.lexThrow != null ) {
        print(", ");
        print(scanner.lexThrow);
      }

      if ( scanner.scanErrorException != null ) {
        print(", ");
        print(scanner.scanErrorException);
      }
      
      println(" {");

      println("    java_cup.runtime.Symbol s = "+scanner.functionName+"();");
      print("    System.out.println( ");
      if (scanner.lineCount) print("\"line:\" + (yyline+1) + ");
      if (scanner.columnCount) print("\" col:\" + (yycolumn+1) + ");
      println("\" --\"+ yytext() + \"--\" + getTokenName(s.sym) + \"--\");");
      println("    return s;");
      println("  }");
      println("");
    }

    if ( scanner.standalone ) {
      println("  /**");
      println("   * Runs the scanner on input files.");
      println("   *");
      println("   * This is a standalone scanner, i.e. it will print any unmatched");
      println("   * text to System.out unchanged.");      
      println("   *");
      println("   * @param argv   the command line, contains the filenames to run");
      println("   *               the scanner on.");
      println("   */");
    }
    else {
      println("  /**");
      println("   * Runs the scanner on input files.");
      println("   *");
      println("   * This main method is the debugging routine for the scanner.");
      println("   * It prints debugging information about each returned token to");
      println("   * System.out until the end of file is reached, or an error occured.");
      println("   *"); 
      println("   * @param argv   the command line, contains the filenames to run");
      println("   *               the scanner on."); 
      println("   */"); 
    }      
    
    println("  public static void main(String argv[]) {");
    println("    if (argv.length == 0) {");
    println("      System.out.println(\"Usage : java "+scanner.className+" <inputfile>\");");
    println("    }");
    println("    else {");
    println("      for (int i = 0; i < argv.length; i++) {");
    println("        "+scanner.className+" scanner = null;");
    println("        try {");
    println("          scanner = new "+scanner.className+"( new java.io.FileReader(argv[i]) );");

    if ( scanner.standalone ) {      
      println("          while ( !scanner.yy_atEOF ) scanner."+scanner.functionName+"();");      
    }
    else if (scanner.cupDebug ) {
      println("          while ( !scanner.yy_atEOF ) scanner.debug_"+scanner.functionName+"();");            
    }
    else {
      println("          do {");
      println("            System.out.println(scanner."+scanner.functionName+"());");
      println("          } while (!scanner.yy_atEOF);");
      println("");
    }
 
    println("        }");
    println("        catch (java.io.FileNotFoundException e) {");
    println("          System.out.println(\"File not found : \\\"\"+argv[i]+\"\\\"\");");
    println("        }");
    println("        catch (java.io.IOException e) {");
    println("          System.out.println(\"IO error scanning file \\\"\"+argv[i]+\"\\\"\");");
    println("          System.out.println(e);");
    println("        }"); 
    println("        catch (Exception e) {");
    println("          System.out.println(\"Unexpected exception:\");");
    println("          e.printStackTrace();");
    println("        }"); 
    println("      }");
    println("    }");
    println("  }");
    println("");    
  }
  
  private void emitNoMatch() {
    println("            yy_ScanError(YY_NO_MATCH);");
  }
  
  private void emitNextInput() {
    println("          if (yy_currentPos_l < yy_endRead_l)");
    println("            yy_input = yy_buffer_l[yy_currentPos_l++];");
    println("          else if (yy_atEOF) {");
    println("            yy_input = YYEOF;");
    println("            break yy_forAction;");
    println("          }");
    println("          else {");
    println("            // store back cached positions");
    println("            yy_currentPos  = yy_currentPos_l;");
    println("            yy_markedPos   = yy_markedPos_l;");
    if ( scanner.lookAheadUsed ) 
      println("            yy_pushbackPos = yy_pushbackPos_l;");
    println("            boolean eof = yy_refill();");
    println("            // get translated positions and possibly new buffer");
    println("            yy_currentPos_l  = yy_currentPos;");
    println("            yy_markedPos_l   = yy_markedPos;");
    println("            yy_buffer_l      = yy_buffer;");
    println("            yy_endRead_l     = yy_endRead;");
    if ( scanner.lookAheadUsed ) 
      println("            yy_pushbackPos_l = yy_pushbackPos;");
    println("            if (eof) {");
    println("              yy_input = YYEOF;");
    println("              break yy_forAction;");  
    println("            }");
    println("            else {");
    println("              yy_input = yy_buffer_l[yy_currentPos_l++];");
    println("            }");
    println("          }"); 
  }

  private void emitHeader() {
    println("/* The following code was generated by JFlex "+Main.version+" on "+date+" */");   
    println(""); 
  } 

  private void emitUserCode() {
    if ( scanner.userCode.length() > 0 )
      println(scanner.userCode.toString());
  }

  private void emitClassName() {    
    if (!endsWithJavadoc(scanner.userCode)) {
      println("/**");
      println(" * This class is a scanner generated by ");
      println(" * <a href=\"http://www.jflex.de/\">JFlex</a> "+Main.version);
      println(" * on "+date+" from the specification file");    
      println(" * <tt>"+inputFile+"</tt>");      
      println(" */");
    }   

    if ( scanner.isPublic ) print("public ");

    if ( scanner.isAbstract) print("abstract ");
   
    if ( scanner.isFinal ) print("final ");
    
    print("class ");
    print(scanner.className);
    
    if ( scanner.isExtending != null ) {
      print(" extends ");
      print(scanner.isExtending);
    }

    if ( scanner.isImplementing != null ) {
      print(" implements ");
      print(scanner.isImplementing);
    }   
    
    println(" {");
  }  

  /**
   * Try to find out if user code ends with a javadoc comment 
   * 
   * @param buffer   the user code
   * @return true    if it ends with a javadoc comment
   */
  public static boolean endsWithJavadoc(StringBuffer usercode) {
    String s = usercode.toString().trim();
    int len = s.length();
    
    if (!s.endsWith("*/")) return false;
       
    // look for "/**" backwards, but do not allow "/*" 
    for (int i = len-2; i >= 0; i--) {
       if (s.charAt(i) == '*') {
         i--;
         if (i < 0) return false;
         if (s.charAt(i) == '/') return false;
         if (s.charAt(i) != '*') continue;         
         do i--; while (i >= 0 && s.charAt(i) == '*');
         if (i < 0) return false;         
         if (s.charAt(i) == '/') return true;
         // otherwise continue         
       }
    }
        
    return false;
  }


  private void emitLexicalStates() {
    Enumeration stateNames = scanner.states.names();
    
    while ( stateNames.hasMoreElements() ) {
      String name = (String) stateNames.nextElement();
      
      int num = scanner.states.getNumber(name).intValue();

      if (scanner.bolUsed)      
        println("  "+visibility+" static final int "+name+" = "+2*num+";");
      else
        println("  "+visibility+" static final int "+name+" = "+dfa.lexState[2*num]+";");
    }
    
    if (scanner.bolUsed) {
      println("");
      println("  /**");
      println("   * YY_LEXSTATE[l] is the state in the DFA for the lexical state l");
      println("   * YY_LEXSTATE[l+1] is the state in the DFA for the lexical state l");
      println("   *                  at the beginning of a line");
      println("   * l is of the form l = 2*k, k a non negative integer");
      println("   */");
      println("  private static final int YY_LEXSTATE[] = { ");
  
      int i, j = 0;
      print("    ");

      for (i = 0; i < dfa.lexState.length-1; i++) {
        print( dfa.lexState[i], 2 );

        print(", ");

        if (++j >= 16) {
          println();
          print("    ");
          j = 0;
        }
      }
            
      println( dfa.lexState[i] );
      println("  };");

    }
  }

  private void emitDynInitHead(int chunk) {
    println("  /** ");
    println("   * The packed transition table of the DFA (part "+chunk+")");
    println("   */");
    println("  private static final String yy_packed"+chunk+" = ");    
  }

  /**
   * Calculates the number of bytes a Unicode character
   * would have in UTF8 representation in a class file.
   *
   * @param value  the char code of the Unicode character
   *               (expected to satisfy 0 <= value <= 0xFFFF)
   *
   * @return length of UTF8 representation.
   */
  private int UTF8Length(int value) {
    if (value < 0 || value > 0xFFFF) throw new Error("not a char value ("+value+")");

    // see JVM spec §4.4.7, p 111
    if (value == 0) return 2;
    if (value <= 0x7F) return 1;

    // workaround for javac bug (up to jdk 1.3):
    if (value <  0x0400) return 2;
    if (value <= 0x07FF) return 3;

    // correct would be:
    // if (value <= 0x7FF) return 2;
    return 3;
  }

  private void emitDynamicInit() {    
    emitDynInitHead(numTableChunks++);

    int i,c;
    int n = 0;
    print("    \"");
    
    int count = 0;
    int value = dfa.table[0][0];

    // the current length of the resulting UTF8 String constant
    // in the class file. Must be smaller than 64K
    int UTF8Length = 0;    
    
    for (i = 0; i < dfa.numStates; i++) {
      if ( !rowKilled[i] ) {
        for (c = 0; c < dfa.numInput; c++) {
          if ( !colKilled[c] ) {
            if (dfa.table[i][c] == value) count++;
            else {
              printUC( count );
              printUC( value+1 );

              // calculate resulting UTF8 size
              UTF8Length += UTF8Length(count)+UTF8Length(value+1);

              n+= 2;
              if (n >= 16) {
                print("\"+");
                println();
                print("    \"");
                n = 0;
              }
              count = 1;
              value = dfa.table[i][c];
              
              if (UTF8Length >= maxSize) {
                // System.out.println("UTF8 size chunk "+(numTableChunks-1)+": "+Integer.toHexString(UTF8Length));
                UTF8Length = 0;
                println("\";");
                println();
                emitDynInitHead(numTableChunks++);
                print("    \"");
                n = 0;
              }
            }
          }
        }
      }
    }

    printUC( count );
    printUC( value+1 );

    UTF8Length += UTF8Length(count)+UTF8Length(value+1);

    // System.out.println("UTF8 size chunk "+(numTableChunks-1)+": "+Integer.toHexString(UTF8Length));

    println("\";");

    println();
    println("  /** ");
    println("   * The transition table of the DFA");
    println("   */");
    println("  private static final int yytrans [] = yy_unpack();");
    println();
  }

  private void emitDynamicInitFunction() {
    println();
    println("  /** ");
    println("   * Unpacks the split, compressed DFA transition table.");
    println("   *");
    println("   * @return the unpacked transition table");
    println("   */");
    println("  private static int [] yy_unpack() {");
    println("    int [] trans = new int["+(numRows*numCols)+"];");
    println("    int offset = 0;");

    for (int i = 0; i < numTableChunks; i++) {
      println("    offset = yy_unpack(yy_packed"+i+", offset, trans);");
    }

    println("    return trans;");
    println("  }");

    println();
    println("  /** ");
    println("   * Unpacks the compressed DFA transition table.");
    println("   *");
    println("   * @param packed   the packed transition table");
    println("   * @return         the index of the last entry");
    println("   */");
    println("  private static int yy_unpack(String packed, int offset, int [] trans) {");
    println("    int i = 0;       /* index in packed string  */");
    println("    int j = offset;  /* index in unpacked array */");
    println("    int l = packed.length();");
    println("    while (i < l) {");
    println("      int count = packed.charAt(i++);");
    println("      int value = packed.charAt(i++);");
    println("      value--;");
    println("      do trans[j++] = value; while (--count > 0);");
    println("    }");
    println("    return j;");
    println("  }");
  }

  private void emitCharMapInitFunction() {

    CharClasses cl = parser.getCharClasses();
    
    if ( cl.getMaxCharCode() < 256 ) return;

    println("");
    println("  /** ");
    println("   * Unpacks the compressed character translation table.");
    println("   *");
    println("   * @param packed   the packed character translation table");
    println("   * @return         the unpacked character translation table");
    println("   */");
    println("  private static char [] yy_unpack_cmap(String packed) {");
    println("    char [] map = new char[0x10000];");
    println("    int i = 0;  /* index in packed string  */");
    println("    int j = 0;  /* index in unpacked array */");
    println("    while (i < "+2*intervalls.length+") {");
    println("      int  count = packed.charAt(i++);");
    println("      char value = packed.charAt(i++);");
    println("      do map[j++] = value; while (--count > 0);");
    println("    }");
    println("    return map;");
    println("  }");
  }

  private void emitYYTrans() {    

    int i,c;
    int n = 0;
    
    println("  /** ");
    println("   * The transition table of the DFA");
    println("   */");
    println("  private static final int yytrans [] = {");

    boolean isFirstRow = true;
    
    print("    ");
    for (i = 0; i < dfa.numStates; i++) {
      
      if ( !rowKilled[i] ) {        
        for (c = 0; c < dfa.numInput; c++) {          
          if ( !colKilled[c] ) {            
            if (n >= 10) {
              println();
              print("    ");
              n = 0;
            }
            print( dfa.table[i][c] );
            if (i != dfa.numStates-1 || c != dfa.numInput-1)
              print( ", ");
            n++;
          }
        }
      }
    }

    println();
    println("  };");
  }
  
  private void emitRowMap() {
    println("  /** ");
    println("   * Translates a state to a row index in the transition table");    
    println("   */");
    println("  private static final int yy_rowMap [] = { ");

    int i;
    int n = 0;
    print("    ");
    
    for (i = 0; i < dfa.numStates-1; i++) {      
      print( rowMap[i]*numCols, 5 );
      print( ", " );
      
      if (++n >= 10) {
        println();
        print("    ");
        n = 0;
      }
    }
    
    print( rowMap[i]*numCols, 5 );
    println();
    println("  };");
    println();  
  }

  private void emitCharMapArrayUnPacked() {
   
    CharClasses cl = parser.getCharClasses();
    intervalls = cl.getIntervalls();
    
    println("");
    println("  /** ");
    println("   * Translates characters to character classes");
    println("   */");
    println("  private static final char [] yycmap = {");
  
    int n = 0;  // numbers of entries in current line    
    print("    ");
    
    int max =  cl.getMaxCharCode();
    int i = 0;     
    while ( i < intervalls.length && intervalls[i].start <= max ) {

      int end = Math.min(intervalls[i].end, max);
      for (int c = intervalls[i].start; c <= end; c++) {

        print(colMap[intervalls[i].charClass], 2);

        if (c < max) {
          print(", ");        
          if ( ++n >= 16 ) { 
            println();
            print("    ");
            n = 0; 
          }
        }
      }

      i++;
    }

    println();
    println("  };");
    println();
  }

  private void emitCharMapArray() {       
    CharClasses cl = parser.getCharClasses();

    if ( cl.getMaxCharCode() < 256 ) {
      emitCharMapArrayUnPacked();
      return;
    }

    // ignores cl.getMaxCharCode(), emits all intervalls instead

    intervalls = cl.getIntervalls();
    
    println("");
    println("  /** ");
    println("   * Translates characters to character classes");
    println("   */");
    println("  private static final String yycmap_packed = ");
  
    int n = 0;  // numbers of entries in current line    
    print("    \"");
    
    int i = 0; 
    while ( i < intervalls.length-1 ) {
      int count = intervalls[i].end-intervalls[i].start+1;
      int value = colMap[intervalls[i].charClass];
      
      printUC(count);
      printUC(value);

      if ( ++n >= 10 ) { 
        println("\"+");
        print("    \"");
        n = 0; 
      }

      i++;
    }

    printUC(intervalls[i].end-intervalls[i].start+1);
    printUC(colMap[intervalls[i].charClass]);

    println("\";");
    println();

    println("  /** ");
    println("   * Translates characters to character classes");
    println("   */");
    println("  private static final char [] yycmap = yy_unpack_cmap(yycmap_packed);");
    println();
  }

  private void emitRowMapArray() {
    println("");
    println("  /** ");
    println("   * Translates a state to a row index in the transition table (packed version)");
    println("   */");
    println("  final private static String yy_rowMap_packed = ");
  
    int i, value;
    int n = 0;
    print("    \"");
    
    for (i = 0; i < dfa.numStates-1; i++) {
      value = rowMap[i]*numCols;
      printUC(value >> 16);      
      printUC(value & 0xFFFF);

      if (++n >= 10) {
        println("\"+");
        print("    \"");
        n = 0;
      }
    }
    
    value = rowMap[i]*numCols;
    printUC(value >> 16);      
    printUC(value & 0xFFFF);
    println("\";");
    println();
    
    println("  /** ");
    println("   * Translates a state to a row index in the transition table");
    println("   */");
    println("  final private static int [] yy_rowMap = yy_unpack_rowMap(yy_rowMap_packed);");
    println();
  }


  private void emitRowMapInitFunction() {
    println("");
    println("  /** ");
    println("   * Unpacks the compressed row translation table.");
    println("   *");
    println("   * @param packed   the packed row translation table");
    println("   * @return         the unpacked row translation table");
    println("   */");
    println("  private static int [] yy_unpack_rowMap(String packed) {");
    println("    int [] map = new int["+dfa.numStates*2+"];");
    println("    int i = 0;  /* index in packed string  */");
    println("    int j = 0;  /* index in unpacked array */");
    println("    while (i < "+2*dfa.numStates+") {");
    println("      int high = ((int) packed.charAt(i++)) << 16;");
    println("      map[j++] = high | packed.charAt(i++);");
    println("    }");
    println("    return map;");
    println("  }");
  }


  private void emitAttributes() {
    
    if (dfa.numStates <= 0) return;
    
    println("  /**");
    println("   * YY_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>");
    println("   */");
    println("  private static final byte YY_ATTRIBUTE[] = {");

    int i,j, attribute;
    print("    ");
    
    for (i = 0, j = 0;  i < dfa.numStates-1; i++, j++) {
      
      if (j >= 16) {
        j = 0;
        println();
        print("    ");
      }
      
      attribute = 0;      
      if ( dfa.isFinal[i]    ) attribute = FINAL;
      if ( dfa.isPushback[i] ) attribute|= PUSHBACK;
      if ( dfa.isLookEnd[i]  ) attribute|= LOOKEND;
      if ( !isTransition[i]  ) attribute|= NOLOOK;

      print( attribute, 2 );
      print( ", " );      
    }
    
    attribute = 0;      
    if ( dfa.isFinal[dfa.numStates-1]    ) attribute = FINAL;
    if ( dfa.isPushback[dfa.numStates-1] ) attribute|= PUSHBACK;
    if ( dfa.isLookEnd[dfa.numStates-1]  ) attribute|= LOOKEND;
    if ( !isTransition[dfa.numStates-1]  ) attribute|= NOLOOK;
    
    print( attribute, 2 );
    println();
    println("  };");
    println();
  }


  private void emitClassCode() {
    if ( scanner.eofCode != null ) {
      println("  /** denotes if the user-EOF-code has already been executed */");
      println("  private boolean yy_eof_done;");
      println("");
    }
    
    if ( scanner.classCode != null ) {
      println("  /* user code: */");
      println(scanner.classCode);
    }
  }

  private void emitConstructorDecl() {
    
    print("  ");

    if ( scanner.isPublic ) print("public ");   
    print( scanner.className );      
    print("(java.io.Reader in)");
    
    if ( scanner.initThrow != null ) {
      print(" throws ");
      print( scanner.initThrow );
    }
    
    println(" {");

    if ( scanner.initCode != null ) {
      print("  ");
      print( scanner.initCode );
    }

    println("    this.yy_reader = in;");

    println("  }");
    println();

    
    println("  /**");
    println("   * Creates a new scanner.");
    println("   * There is also java.io.Reader version of this constructor.");
    println("   *");
    println("   * @param   in  the java.io.Inputstream to read input from.");
    println("   */");

    print("  ");
    if ( scanner.isPublic ) print("public ");    
    print( scanner.className );      
    print("(java.io.InputStream in)");
    
    if ( scanner.initThrow != null ) {
      print(" throws ");
      print( scanner.initThrow );
    }
    
    println(" {");    
    println("    this(new java.io.InputStreamReader(in));");
    println("  }");
  }


  private void emitDoEOF() {
    if ( scanner.eofCode == null ) return;
    
    println("  /**");
    println("   * Contains user EOF-code, which will be executed exactly once,");
    println("   * when the end of file is reached");
    println("   */");
    
    print("  private void yy_do_eof()");
    
    if ( scanner.eofThrow != null ) {
      print(" throws ");
      print(scanner.eofThrow);
    }
    
    println(" {");
    
    println("    if (!yy_eof_done) {");
    println("      yy_eof_done = true;");
    println("    "+scanner.eofCode );
    println("    }");
    println("  }");
    println("");
    println("");
  }

  private void emitLexFunctHeader() {
    
    print("  "+visibility+" ");
    
    if ( scanner.tokenType == null ) {
      if ( scanner.isInteger )
        print( "int" );
      else 
      if ( scanner.isIntWrap )
        print( "Integer" );
      else
        print( "Yytoken" );
    }
    else
      print( scanner.tokenType );
      
    print(" ");
    
    print(scanner.functionName);
      
    print("() throws java.io.IOException");
    
    if ( scanner.lexThrow != null ) {
      print(", ");
      print(scanner.lexThrow);
    }

    if ( scanner.scanErrorException != null ) {
      print(", ");
      print(scanner.scanErrorException);
    }
    
    println(" {");
    
    skel.emitNext();

    if ( scanner.useRowMap ) {
      println("    int [] yytrans_l = yytrans;");
      println("    int [] yy_rowMap_l = yy_rowMap;");
      println("    byte [] yy_attr_l = YY_ATTRIBUTE;");

    }

    if ( scanner.lookAheadUsed ) {
      println("    int yy_pushbackPos_l = yy_pushbackPos = -1;");
      println("    boolean yy_was_pushback;");
    }

    skel.emitNext();    
        
    if ( scanner.charCount ) {
      println("      yychar+= yy_markedPos_l-yy_startRead;");
      println("");
    }
    
    if ( scanner.lineCount || scanner.columnCount ) {
      println("      boolean yy_r = false;");
      println("      for (yy_currentPos_l = yy_startRead; yy_currentPos_l < yy_markedPos_l;");
      println("                                                             yy_currentPos_l++) {");
      println("        switch (yy_buffer_l[yy_currentPos_l]) {");      
      println("        case '\\u000B':"); 
      println("        case '\\u000C':"); 
      println("        case '\\u0085':");
      println("        case '\\u2028':"); 
      println("        case '\\u2029':"); 
      if ( scanner.lineCount )
        println("          yyline++;");
      if ( scanner.columnCount )
        println("          yycolumn = 0;");
      println("          yy_r = false;");
      println("          break;");      
      println("        case '\\r':");
      if ( scanner.lineCount )
        println("          yyline++;");
      if ( scanner.columnCount )
        println("          yycolumn = 0;");
      println("          yy_r = true;");
      println("          break;");
      println("        case '\\n':");
      println("          if (yy_r)");
      println("            yy_r = false;");
      println("          else {");
      if ( scanner.lineCount )
        println("            yyline++;");
      if ( scanner.columnCount )
        println("            yycolumn = 0;");
      println("          }");
      println("          break;");
      println("        default:");
      println("          yy_r = false;");
      if ( scanner.columnCount ) 
        println("          yycolumn++;");
      println("        }");
      println("      }");
      println();

      if ( scanner.lineCount ) {
        println("      if (yy_r) {");
        println("        // peek one character ahead if it is \\n (if we have counted one line too much)");
        println("        boolean yy_peek;");
        println("        if (yy_markedPos_l < yy_endRead_l)");
        println("          yy_peek = yy_buffer_l[yy_markedPos_l] == '\\n';");
        println("        else if (yy_atEOF)");
        println("          yy_peek = false;");
        println("        else {");
        println("          boolean eof = yy_refill();");
        println("          yy_markedPos_l = yy_markedPos;");
        println("          yy_buffer_l = yy_buffer;");
        println("          if (eof) ");
        println("            yy_peek = false;");
        println("          else ");
        println("            yy_peek = yy_buffer_l[yy_markedPos_l] == '\\n';");
        println("        }");
        println("        if (yy_peek) yyline--;");
        println("      }");
      }
    }

    if ( scanner.bolUsed ) {
      // yy_markedPos > yy_startRead <=> last match was not empty
      // if match was empty, last value of yy_atBOL can be used
      // yy_startRead is always >= 0
      println("      if (yy_markedPos_l > yy_startRead) {");
      println("        switch (yy_buffer_l[yy_markedPos_l-1]) {");
      println("        case '\\n':");
      println("        case '\\u000B':"); 
      println("        case '\\u000C':"); 
      println("        case '\\u0085':");
      println("        case '\\u2028':"); 
      println("        case '\\u2029':"); 
      println("          yy_atBOL = true;"); 
      println("          break;"); 
      println("        case '\\r': "); 
      println("          if (yy_markedPos_l < yy_endRead_l)");
      println("            yy_atBOL = yy_buffer_l[yy_markedPos_l] != '\\n';");
      println("          else if (yy_atEOF)");
      println("            yy_atBOL = false;");
      println("          else {");
      println("            boolean eof = yy_refill();");
      println("            yy_markedPos_l = yy_markedPos;");
      println("            yy_buffer_l = yy_buffer;");
      println("            if (eof) ");
      println("              yy_atBOL = false;");
      println("            else ");
      println("              yy_atBOL = yy_buffer_l[yy_markedPos_l] != '\\n';");
      println("          }");      
      println("          break;"); 
      println("        default:"); 
      println("          yy_atBOL = false;"); 
      println("        }"); 
      println("      }"); 
    }

    skel.emitNext();
    
    if (scanner.bolUsed) {
      println("      if (yy_atBOL)");
      println("        yy_state = YY_LEXSTATE[yy_lexical_state+1];");
      println("      else");    
      println("        yy_state = YY_LEXSTATE[yy_lexical_state];");
      println();
    }
    else {
      println("      yy_state = yy_lexical_state;");
      println();
    }

    if (scanner.lookAheadUsed)
      println("      yy_was_pushback = false;");

    skel.emitNext();
  }

  
  private void emitGetRowMapNext() {
    println("          int yy_next = yytrans_l[ yy_rowMap_l[yy_state] + yycmap_l[yy_input] ];");

    println("          if (yy_next == "+DFA.NO_TARGET+") break yy_forAction;");
    println("          yy_state = yy_next;");
    println();

    println("          int yy_attributes = yy_attr_l[yy_state];");

    if ( scanner.lookAheadUsed ) {
      println("          if ( (yy_attributes & "+PUSHBACK+") == "+PUSHBACK+" )");
      println("            yy_pushbackPos_l = yy_currentPos_l;");
      println();
    }

    println("          if ( (yy_attributes & "+FINAL+") == "+FINAL+" ) {");
    if ( scanner.lookAheadUsed ) 
      println("            yy_was_pushback = (yy_attributes & "+LOOKEND+") == "+LOOKEND+";");

    skel.emitNext();
    
    println("            if ( (yy_attributes & "+NOLOOK+") == "+NOLOOK+" ) break yy_forAction;");

    skel.emitNext();    
  }  

  private void emitTransitionTable() {
    transformTransitionTable();
    
    println("          yy_input = yycmap_l[yy_input];");
    println();

    if ( scanner.lookAheadUsed ) 
      println("          boolean yy_pushback = false;");
      
    println("          boolean yy_isFinal = false;");
    println("          boolean yy_noLookAhead = false;");
    println();
    
    println("          yy_forNext: { switch (yy_state) {");

    for (int state = 0; state < dfa.numStates; state++)
      if (isTransition[state]) emitState(state);

    println("            default:");
    println("              yy_ScanError(YY_ILLEGAL_STATE);");
    println("              break;");
    println("          } }");
    println();
    
    println("          if ( yy_isFinal ) {");
    
    if ( scanner.lookAheadUsed ) 
      println("            yy_was_pushback = yy_pushback;");
    
    skel.emitNext();
    
    println("            if ( yy_noLookAhead ) break yy_forAction;");

    skel.emitNext();    
  }


  /**
   * Escapes all " ' \ tabs and newlines
   */
  private String escapify(String s) {
    StringBuffer result = new StringBuffer(s.length()*2);
    
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
      case '\'': result.append("\\\'"); break;
      case '\"': result.append("\\\""); break;
      case '\\': result.append("\\\\"); break;
      case '\t': result.append("\\t"); break;
      case '\r': if (i+1 == s.length() || s.charAt(i+1) != '\n') result.append("\"+yy_NL+\""); 
                 break;
      case '\n': result.append("\"+yy_NL+\""); break;
      default: result.append(c);
      }
    }

    return result.toString();
  }

  private void emitActions() {
    Hashtable actionTable = new Hashtable();

    for (int i = 0; i < dfa.numStates; i++) 
      if ( dfa.isFinal[i] ) {
        Action action = dfa.action[i];
        if ( actionTable.get(action) == null ) 
          actionTable.put(action, new StateSet(dfa.numStates,i));
        else
          ((StateSet) actionTable.get(action)).addState(i);
      }

    int i = dfa.numStates+1;  
    Enumeration actions = actionTable.keys();
    while ( actions.hasMoreElements() ) {
      Action action   = (Action)   actions.nextElement();
      StateSet states = (StateSet) actionTable.get(action);

      StateSetEnumerator s = states.states();
      while ( s.hasMoreElements() ) 
        println("        case "+s.nextElement()+": "); 
      
      if ( scanner.debugOption ) {
        print("          System.out.println(");
        if ( scanner.lineCount )
          print("\"line: \"+(yyline+1)+\" \"+");
        if ( scanner.columnCount )
          print("\"col: \"+(yycolumn+1)+\" \"+");
        println("\"match: --\"+yytext()+\"--\");");        
        print("          System.out.println(\"action ["+action.priority+"] {");
        print(escapify(action.content));
        println("}\");");
      }
      
      println("          { "+action.content+" }");
      println("        case "+(i++)+": break;"); 
    }
  }

  private void emitEOFVal() {
    EOFActions eofActions = parser.getEOFActions();

    if ( scanner.eofCode != null ) 
      println("            yy_do_eof();");
      
    if ( eofActions.numActions() > 0 ) {
      println("            switch (yy_lexical_state) {");
      
      Enumeration stateNames = scanner.states.names();

      // record lex states already emitted:
      Hashtable used = new Hashtable();

      // pick a start value for break case labels. 
      // must be larger than any value of a lex state:
      int last = dfa.numStates;
      
      while ( stateNames.hasMoreElements() ) {
        String name = (String) stateNames.nextElement();
        int num = scanner.states.getNumber(name).intValue();
        Action action = eofActions.getAction(num);

        // only emit code if the lex state is not redundant, so
        // that case labels don't overlap
        // (redundant = points to the same dfa state as another one).
        // applies only to scanners that don't use BOL, because
        // in BOL scanners lex states get mapped at runtime, so
        // case labels will always be unique.
        boolean unused = true;                
        if (!scanner.bolUsed) {
          Integer key = new Integer(dfa.lexState[2*num]);
          unused = used.get(key) == null;
          
          if (!unused) 
            Out.warning("Lexical states <"+name+"> and <"+used.get(key)+"> are equivalent.");
          else
            used.put(key,name);
        }

        if (action != null && unused) {
          println("            case "+name+":");
          println("              { "+action.content+" }");
          println("            case "+(++last)+": break;");
        }
      }
      
      println("            default:");
    }

    if (eofActions.getDefault() != null) 
      println("              { " + eofActions.getDefault().content + " }");
    else if ( scanner.eofVal != null ) 
      println("              { " + scanner.eofVal + " }");
    else if ( scanner.isInteger ) 
      println("            return YYEOF;");
    else
      println("            return null;");

    if (eofActions.numActions() > 0)
      println("            }");
  }
  
  private void emitState(int state) {
    
    println("            case "+state+":");
    println("              switch (yy_input) {");
   
    int defaultTransition = getDefaultTransition(state);
    
    for (int next = 0; next < dfa.numStates; next++) {
            
      if ( next != defaultTransition && table[state][next] != null ) {
        emitTransition(state, next);
      }
    }
    
    if ( defaultTransition != DFA.NO_TARGET && noTarget[state] != null ) {
      emitTransition(state, DFA.NO_TARGET);
    }
    
    emitDefaultTransition(state, defaultTransition);
    
    println("              }");
    println("");
  }
  
  private void emitTransition(int state, int nextState) {

    CharSetEnumerator chars;
    int num;
    
    if (nextState != DFA.NO_TARGET) 
      chars = table[state][nextState].characters();
    else 
      chars = noTarget[state].characters();
  
    print("                case ");
    print((int)chars.nextElement());
    print(": ");
    
    while ( chars.hasMoreElements() ) {
      println();
      print("                case ");
      print((int)chars.nextElement());
      print(": ");
    } 
    
    if ( nextState != DFA.NO_TARGET ) {
      if ( dfa.isFinal[nextState] )
        print("yy_isFinal = true; ");
        
      if ( dfa.isPushback[nextState] ) 
        print("yy_pushbackPos_l = yy_currentPos_l; ");
      
      if ( dfa.isLookEnd[nextState] )
        print("yy_pushback = true; ");

      if ( !isTransition[nextState] )
        print("yy_noLookAhead = true; ");
        
      if ( nextState == state ) 
        println("yy_state = "+nextState+"; break yy_forNext;");
      else
        println("yy_state = "+nextState+"; break yy_forNext;");
    }
    else
      println("break yy_forAction;");
  }
  
  private void emitDefaultTransition(int state, int nextState) {
    print("                default: ");
    
    if ( nextState != DFA.NO_TARGET ) {
      if ( dfa.isFinal[nextState] )
        print("yy_isFinal = true; ");
        
      if ( dfa.isPushback[nextState] ) 
        print("yy_pushbackPos_l = yy_currentPos_l; ");

      if ( dfa.isLookEnd[nextState] )
        print("yy_pushback = true; ");
          
      if ( !isTransition[nextState] )
        print("yy_noLookAhead = true; ");
        
      if ( nextState == state ) 
        println("yy_state = "+nextState+"; break yy_forNext;");
      else
        println("yy_state = "+nextState+"; break yy_forNext;");
    }
    else
      println( "break yy_forAction;" );
  }
  
  private void emitPushback() {
    println("      if (yy_was_pushback)");
    println("        yy_markedPos = yy_pushbackPos_l;");
  }
  
  private int getDefaultTransition(int state) {
    int max = 0;
    
    for (int i = 0; i < dfa.numStates; i++) {
      if ( table[state][max] == null )
        max = i;
      else
      if ( table[state][i] != null && table[state][max].size() < table[state][i].size() )
        max = i;
    }
    
    if ( table[state][max] == null ) return DFA.NO_TARGET;
    if ( noTarget[state] == null ) return max;
    
    if ( table[state][max].size() < noTarget[state].size() ) 
      max = DFA.NO_TARGET;
    
    return max;
  }

  // for switch statement:
  private void transformTransitionTable() {
    
    int numInput = parser.getCharClasses().getNumClasses()+1;

    int i;    
    char j;
    
    table = new CharSet[dfa.numStates][dfa.numStates];
    noTarget = new CharSet[dfa.numStates];
    
    for (i = 0; i < dfa.numStates;  i++) 
      for (j = 0; j < dfa.numInput; j++) {

        int nextState = dfa.table[i][j];
        
        if ( nextState == DFA.NO_TARGET ) {
          if ( noTarget[i] == null ) 
            noTarget[i] = new CharSet(numInput, colMap[j]);
          else
            noTarget[i].add(colMap[j]);
        }
        else {
          if ( table[i][nextState] == null ) 
            table[i][nextState] = new CharSet(numInput, colMap[j]);
          else
            table[i][nextState].add(colMap[j]);
        }
      }
  }

  private void findActionStates() {
    isTransition = new boolean [dfa.numStates];
    
    for (int i = 0; i < dfa.numStates;  i++) {
      char j = 0;
      while ( !isTransition[i] && j < dfa.numInput )
        isTransition[i] = dfa.table[i][j++] != DFA.NO_TARGET;
    }
  }

  
  private void reduceColumns() {
    colMap = new int [dfa.numInput];
    colKilled = new boolean [dfa.numInput];

    int i,j,k;
    int translate = 0;
    boolean equal;

    numCols = dfa.numInput;

    for (i = 0; i < dfa.numInput; i++) {
      
      colMap[i] = i-translate;
      
      for (j = 0; j < i; j++) {
        
        // test for equality:
        k = -1;
        equal = true;        
        while (equal && ++k < dfa.numStates) 
          equal = dfa.table[k][i] == dfa.table[k][j];
        
        if (equal) {
          translate++;
          colMap[i] = colMap[j];
          colKilled[i] = true;
          numCols--;
          break;
        } // if
      } // for j
    } // for i
  }
  
  private void reduceRows() {
    rowMap = new int [dfa.numStates];
    rowKilled = new boolean [dfa.numStates];
    
    int i,j,k;
    int translate = 0;
    boolean equal;

    numRows = dfa.numStates;

    // i is the state to add to the new table
    for (i = 0; i < dfa.numStates; i++) {
      
      rowMap[i] = i-translate;
      
      // check if state i can be removed (i.e. already
      // exists in entries 0..i-1)
      for (j = 0; j < i; j++) {
        
        // test for equality:
        k = -1;
        equal = true;
        while (equal && ++k < dfa.numInput) 
          equal = dfa.table[i][k] == dfa.table[j][k];
        
        if (equal) {
          translate++;
          rowMap[i] = rowMap[j];
          rowKilled[i] = true;
          numRows--;
          break;
        } // if
      } // for j
    } // for i
    
  } 

  public void emit() {    

    if (scanner.functionName == null) 
      scanner.functionName = "yylex";

    reduceColumns();
    findActionStates();

    emitHeader();
    emitUserCode();
    emitClassName();
    
    skel.emitNext();
    
    println("  private static final int YY_BUFFERSIZE = "+scanner.bufferSize+";");

    if (scanner.debugOption) {
      println("  private static final String yy_NL = System.getProperty(\"line.separator\");");
    }

    skel.emitNext();

    emitLexicalStates();
   
    emitCharMapArray();
    
    if (scanner.useRowMap) {
     reduceRows();
    
     // emitRowMap();
      emitRowMapArray();
      emitRowMapInitFunction();

      if (scanner.packed)
        emitDynamicInit();
      else
        emitYYTrans();
    }
    
    skel.emitNext();
    
    if (scanner.useRowMap) 
      emitAttributes();
    
    skel.emitNext();
    
    emitClassCode();
    
    skel.emitNext();
    
    emitConstructorDecl();
    
    if (scanner.packed)
      emitDynamicInitFunction();
    
    emitCharMapInitFunction();

    skel.emitNext();
    
    emitScanError();

    skel.emitNext();        

    emitDoEOF();
    
    skel.emitNext();
    
    emitLexFunctHeader();
    
    emitNextInput();

    if (scanner.useRowMap)
      emitGetRowMapNext();
    else
      emitTransitionTable();
        
    if (scanner.lookAheadUsed) 
      emitPushback();
    
    skel.emitNext();
    
    emitActions();
    
    skel.emitNext();
    
    emitEOFVal();
    
    skel.emitNext();
    
    emitNoMatch();

    skel.emitNext();
    
    emitMain();
    
    skel.emitNext();

    out.close();
  } 
}
