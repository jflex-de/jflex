/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4                                                               *
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
 * @version JFlex 1.4, $Revision$, $Date$
 */
final public class Emitter {
    
  // bit masks for state attributes
  static final private int FINAL = 1;
  static final private int PUSHBACK = 2;
  static final private int LOOKEND = 4;
  static final private int NOLOOK = 8;

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
  

  /** maps actions to their switch label */
  private Hashtable actionTable = new Hashtable();

  private CharClassInterval [] intervalls;

  private String visibility = "public";

  public Emitter(File inputFile, LexParse parser, DFA dfa) throws IOException {

    String name = parser.scanner.className+".java";

    File outputFile = normalize(name, inputFile);

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
   * Constructs a file in Options.getDir() or in the same directory as
   * another file. Makes a backup if the file already exists.
   *
   * @param name  the name (without path) of the file
   * @param path  the path where to construct the file
   * @param input fallback location if path = <tt>null</tt>
   *              (expected to be a file in the directory to write to)   
   */
  public static File normalize(String name, File input) {
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

  private void emitScanError() {
    print("  private void yyFlexScanError(int errorCode)");
    
    if (scanner.scanErrorException != null) 
      print(" throws "+scanner.scanErrorException);

    println(" {");

    skel.emitNext();

    if (scanner.scanErrorException == null)
      println("    throw new Error(message);");
    else
      println("    throw new "+scanner.scanErrorException+"(message);");    

    skel.emitNext();

    print("  "+visibility+" void yypushback(int number) ");     
    
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
      println("   * This is a standalone scanner, it will print any unmatched");
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
      println("          while ( !scanner.yyFlexAtEOF ) scanner."+scanner.functionName+"();");
    }
    else if (scanner.cupDebug ) {
      println("          while ( !scanner.yyFlexAtEOF ) scanner.debug_"+scanner.functionName+"();");
    }
    else {
      println("          do {");
      println("            System.out.println(scanner."+scanner.functionName+"());");
      println("          } while (!scanner.yyFlexAtEOF);");
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
    println("            yyFlexScanError(YY_NO_MATCH);");
  }
  
  private void emitNextInput() {
    println("          if (yyFlexCurrentPosL < yyFlexEndReadL)");
    println("            yyFlexInput = yyFlexBufferL[yyFlexCurrentPosL++];");
    println("          else if (yyFlexAtEOF) {");
    println("            yyFlexInput = YYEOF;");
    println("            break yy_forAction;");
    println("          }");
    println("          else {");
    println("            // store back cached positions");
    println("            yyFlexCurrentPos  = yyFlexCurrentPosL;");
    println("            yyFlexMarkedPos   = yyFlexMarkedPosL;");
    if ( scanner.lookAheadUsed ) 
      println("            yyFlexPushbackPos = yyFlexPushbackPos_l;");
    println("            boolean eof = yyFlexRefill();");
    println("            // get translated positions and possibly new buffer");
    println("            yyFlexCurrentPosL  = yyFlexCurrentPos;");
    println("            yyFlexMarkedPosL   = yyFlexMarkedPos;");
    println("            yyFlexBufferL      = yyFlexBuffer;");
    println("            yyFlexEndReadL     = yyFlexEndRead;");
    if ( scanner.lookAheadUsed ) 
      println("            yyFlexPushbackPos_l = yyFlexPushbackPos;");
    println("            if (eof) {");
    println("              yyFlexInput = YYEOF;");
    println("              break yy_forAction;");  
    println("            }");
    println("            else {");
    println("              yyFlexInput = yyFlexBufferL[yyFlexCurrentPosL++];");
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
      String path = inputFile.toString();
      // slashify path (avoid backslash u sequence = unicode escape)
      if (File.separatorChar != '/') {
	    path = path.replace(File.separatorChar, '/');
      }
    	
      println("/**");
      println(" * This class is a scanner generated by ");
      println(" * <a href=\"http://www.jflex.de/\">JFlex</a> "+Main.version);
      println(" * on "+date+" from the specification file");    
      println(" * <tt>"+path+"</tt>");      
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
        
    if (!s.endsWith("*/")) return false;
    
    // find beginning of javadoc comment   
    int i = s.lastIndexOf("/**");    
    if (i < 0) return false; 
       
    // javadoc comment shouldn't contain a comment end
    return s.substring(i,s.length()-2).indexOf("*/") < 0;
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

  private void emitDynamicInit() {    
    int count = 0;
    int value = dfa.table[0][0];

    println("  /** ");
    println("   * The transition table of the DFA");
    println("   */");

    CountEmitter e = new CountEmitter("Trans");
    e.setValTranslation(+1); // allow vals in [-1, 0xFFFE]
    e.emitInit();
    
    for (int i = 0; i < dfa.numStates; i++) {
      if ( !rowKilled[i] ) {
        for (int c = 0; c < dfa.numInput; c++) {
          if ( !colKilled[c] ) {
            if (dfa.table[i][c] == value) {
              count++;
            } 
            else {
              e.emit(count, value);

              count = 1;
              value = dfa.table[i][c];              
            }
          }
        }
      }
    }

    e.emit(count, value);
    e.emitUnpack();
    
    println(e.toString());
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
    println("  private static char [] yyFlexUnpackCMap(String packed) {");
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
    println("  private static final int YYTRANS [] = {"); //XXX

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
  
  private void emitCharMapArrayUnPacked() {
   
    CharClasses cl = parser.getCharClasses();
    intervalls = cl.getIntervalls();
    
    println("");
    println("  /** ");
    println("   * Translates characters to character classes");
    println("   */");
    println("  private static final char [] YY_CMAP = {");
  
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
    println("  private static final String YY_CMAP_PACKED = ");
  
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
    println("  private static final char [] YY_CMAP = yyFlexUnpackCMap(YY_CMAP_PACKED);");
    println();
  }


  /**
   * Print number as octal/unicode escaped string character.
   * 
   * @param c   the value to print
   * @prec  0 <= c <= 0xFFFF 
   */
  private void printUC(int c) {
    if (c > 255) {
      out.print("\\u");
      if (c < 0x1000) out.print("0");
      out.print(Integer.toHexString(c));
    }
    else {
      out.print("\\");
      out.print(Integer.toOctalString(c));
    }    
  }


  private void emitRowMapArray() {
    println("");
    println("  /** ");
    println("   * Translates a state to a row index in the transition table");
    println("   */");
    
    HiLowEmitter e = new HiLowEmitter("RowMap");
    e.emitInit();
    for (int i = 0; i < dfa.numStates; i++) {
      e.emit(rowMap[i]*numCols);
    }    
    e.emitUnpack();
    println(e.toString());
  }


  private void emitAttributes() {
    println("  /**");
    println("   * YY_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>");
    println("   */");
    
    CountEmitter e = new CountEmitter("Attribute");    
    e.emitInit();
    
    int count = 1;
    int value = 0; 
    if ( dfa.isFinal[0]    ) value = FINAL;
    if ( dfa.isPushback[0] ) value|= PUSHBACK;
    if ( dfa.isLookEnd[0]  ) value|= LOOKEND;
    if ( !isTransition[0]  ) value|= NOLOOK;
       
    for (int i = 1;  i < dfa.numStates; i++) {      
      int attribute = 0;      
      if ( dfa.isFinal[i]    ) attribute = FINAL;
      if ( dfa.isPushback[i] ) attribute|= PUSHBACK;
      if ( dfa.isLookEnd[i]  ) attribute|= LOOKEND;
      if ( !isTransition[i]  ) attribute|= NOLOOK;

      if (value == attribute) {
        count++;
      }
      else {        
        e.emit(count, value);
        count = 1;
        value = attribute;
      }
    }
    
    e.emit(count, value);    
    e.emitUnpack();
    
    println(e.toString());
  }


  private void emitClassCode() {
    if ( scanner.eofCode != null ) {
      println("  /** denotes if the user-EOF-code has already been executed */");
      println("  private boolean yyFlexEOFDone;");
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

    println("    this.yyFlexReader = in;");

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
    
    print("  private void yyFlexDoEOF()");
    
    if ( scanner.eofThrow != null ) {
      print(" throws ");
      print(scanner.eofThrow);
    }
    
    println(" {");
    
    println("    if (!yyFlexEOFDone) {");
    println("      yyFlexEOFDone = true;");
    println("    "+scanner.eofCode );
    println("    }");
    println("  }");
    println("");
    println("");
  }

  private void emitLexFunctHeader() {
    
    if (scanner.cupCompatible)  {
      // force public, because we have to implement java_cup.runtime.Symbol
      print("  public ");
    }
    else {
      print("  "+visibility+" ");
    }
    
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
      println("    int [] yyFlexTransL = YY_TRANS;");
      println("    int [] yyFlexRowMapL = YY_ROWMAP;");
      println("    int [] yyFlexAttrL = YY_ATTRIBUTE;");

    }

    if ( scanner.lookAheadUsed ) {
      println("    int yyFlexPushbackPos_l = yyFlexPushbackPos = -1;");
      println("    boolean yy_was_pushback;");
    }

    skel.emitNext();    
        
    if ( scanner.charCount ) {
      println("      yychar+= yyFlexMarkedPosL-yyFlexStartRead;");
      println("");
    }
    
    if ( scanner.lineCount || scanner.columnCount ) {
      println("      boolean yyFlexR = false;");
      println("      for (yyFlexCurrentPosL = yyFlexStartRead; yyFlexCurrentPosL < yyFlexMarkedPosL;");
      println("                                                             yyFlexCurrentPosL++) {");
      println("        switch (yyFlexBufferL[yyFlexCurrentPosL]) {");
      println("        case '\\u000B':"); 
      println("        case '\\u000C':"); 
      println("        case '\\u0085':");
      println("        case '\\u2028':"); 
      println("        case '\\u2029':"); 
      if ( scanner.lineCount )
        println("          yyline++;");
      if ( scanner.columnCount )
        println("          yycolumn = 0;");
      println("          yyFlexR = false;");
      println("          break;");      
      println("        case '\\r':");
      if ( scanner.lineCount )
        println("          yyline++;");
      if ( scanner.columnCount )
        println("          yycolumn = 0;");
      println("          yyFlexR = true;");
      println("          break;");
      println("        case '\\n':");
      println("          if (yyFlexR)");
      println("            yyFlexR = false;");
      println("          else {");
      if ( scanner.lineCount )
        println("            yyline++;");
      if ( scanner.columnCount )
        println("            yycolumn = 0;");
      println("          }");
      println("          break;");
      println("        default:");
      println("          yyFlexR = false;");
      if ( scanner.columnCount ) 
        println("          yycolumn++;");
      println("        }");
      println("      }");
      println();

      if ( scanner.lineCount ) {
        println("      if (yyFlexR) {");
        println("        // peek one character ahead if it is \\n (if we have counted one line too much)");
        println("        boolean yyFlexPeek;");
        println("        if (yyFlexMarkedPosL < yyFlexEndReadL)");
        println("          yyFlexPeek = yyFlexBufferL[yyFlexMarkedPosL] == '\\n';");
        println("        else if (yyFlexAtEOF)");
        println("          yyFlexPeek = false;");
        println("        else {");
        println("          boolean eof = yyFlexRefill();");
        println("          yyFlexMarkedPosL = yyFlexMarkedPos;");
        println("          yyFlexBufferL = yyFlexBuffer;");
        println("          if (eof) ");
        println("            yyFlexPeek = false;");
        println("          else ");
        println("            yyFlexPeek = yyFlexBufferL[yyFlexMarkedPosL] == '\\n';");
        println("        }");
        println("        if (yyFlexPeek) yyline--;");
        println("      }");
      }
    }

    if ( scanner.bolUsed ) {
      // yyFlexMarkedPos > yyFlexStartRead <=> last match was not empty
      // if match was empty, last value of yyFlexAtBOL can be used
      // yyFlexStartRead is always >= 0
      println("      if (yyFlexMarkedPosL > yyFlexStartRead) {");
      println("        switch (yyFlexBufferL[yyFlexMarkedPosL-1]) {");
      println("        case '\\n':");
      println("        case '\\u000B':"); 
      println("        case '\\u000C':"); 
      println("        case '\\u0085':");
      println("        case '\\u2028':"); 
      println("        case '\\u2029':"); 
      println("          yyFlexAtBOL = true;");
      println("          break;"); 
      println("        case '\\r': "); 
      println("          if (yyFlexMarkedPosL < yyFlexEndReadL)");
      println("            yyFlexAtBOL = yyFlexBufferL[yyFlexMarkedPosL] != '\\n';");
      println("          else if (yyFlexAtEOF)");
      println("            yyFlexAtBOL = false;");
      println("          else {");
      println("            boolean eof = yyFlexRefill();");
      println("            yyFlexMarkedPosL = yyFlexMarkedPos;");
      println("            yyFlexBufferL = yyFlexBuffer;");
      println("            if (eof) ");
      println("              yyFlexAtBOL = false;");
      println("            else ");
      println("              yyFlexAtBOL = yyFlexBufferL[yyFlexMarkedPosL] != '\\n';");
      println("          }");      
      println("          break;"); 
      println("        default:"); 
      println("          yyFlexAtBOL = false;");
      println("        }"); 
      println("      }"); 
    }

    skel.emitNext();
    
    if (scanner.bolUsed) {
      println("      if (yyFlexAtBOL)");
      println("        yyFlexState = YY_LEXSTATE[yyFlexLexicalState+1];");
      println("      else");    
      println("        yyFlexState = YY_LEXSTATE[yyFlexLexicalState];");
      println();
    }
    else {
      println("      yyFlexState = yyFlexLexicalState;");
      println();
    }

    if (scanner.lookAheadUsed)
      println("      yy_was_pushback = false;");

    skel.emitNext();
  }

  
  private void emitGetRowMapNext() {
    println("          int yyFlexNext = yyFlexTransL[ yyFlexRowMapL[yyFlexState] + yyCMapL[yyFlexInput] ];");
    println("          if (yyFlexNext == "+DFA.NO_TARGET+") break yy_forAction;");
    println("          yyFlexState = yyFlexNext;");
    println();

    println("          int yyFlexAttributes = yyFlexAttrL[yyFlexState];");

    if ( scanner.lookAheadUsed ) {
      println("          if ( (yyFlexAttributes & "+PUSHBACK+") == "+PUSHBACK+" )");
      println("            yyFlexPushbackPos_l = yyFlexCurrentPosL;");
      println();
    }

    println("          if ( (yyFlexAttributes & "+FINAL+") == "+FINAL+" ) {");
    if ( scanner.lookAheadUsed ) 
      println("            yy_was_pushback = (yyFlexAttributes & "+LOOKEND+") == "+LOOKEND+";");

    skel.emitNext();
    
    println("            if ( (yyFlexAttributes & "+NOLOOK+") == "+NOLOOK+" ) break yy_forAction;");

    skel.emitNext();    
  }  

  private void emitTransitionTable() {
    transformTransitionTable();
    
    println("          yyFlexInput = yyCMapL[yyFlexInput];");
    println();

    if ( scanner.lookAheadUsed ) 
      println("          boolean yy_pushback = false;");
      
    println("          boolean yy_isFinal = false;");
    println("          boolean yy_noLookAhead = false;");
    println();
    
    println("          yy_forNext: { switch (yyFlexState) {");

    for (int state = 0; state < dfa.numStates; state++)
      if (isTransition[state]) emitState(state);

    println("            default:");
    println("              // if this is ever reached, there is a serious bug in JFlex");
    println("              yyFlexScanError(YY_UNKNOWN_ERROR);");
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

  public void emitActionTable() {
    int lastAction = 1;    
    int count = 0;
    int value = 0;

    println("  /** ");
    println("   * Translates DFA states to action switch labels.");
    println("   */");
    CountEmitter e = new CountEmitter("Action");    
    e.emitInit();

    for (int i = 0; i < dfa.numStates; i++) {
      int newVal; 
      if ( dfa.isFinal[i] ) {
        Action action = dfa.action[i];
        Integer stored = (Integer) actionTable.get(action);
        if ( stored == null ) { 
          stored = new Integer(lastAction++);
          actionTable.put(action, stored);
        }
        newVal = stored.intValue();
      }
      else {
        newVal = 0;
      }
      
      if (value == newVal) {
        count++;
      }
      else {
        if (count > 0) e.emit(count,value);
        count = 1;
        value = newVal;        
      }
    }
    
    if (count > 0) e.emit(count,value);

    e.emitUnpack();    
    println(e.toString());
  }

  private void emitActions() {
    println("      switch (yyFlexAction < 0 ? yyFlexAction : YY_ACTION[yyFlexAction]) {");

    int i = actionTable.size()+1;  
    Enumeration actions = actionTable.keys();
    while ( actions.hasMoreElements() ) {
      Action action = (Action) actions.nextElement();
      int label = ((Integer) actionTable.get(action)).intValue();

      println("        case "+label+": "); 
      
      if ( scanner.debugOption ) {
        print("          System.out.println(");
        if ( scanner.lineCount )
          print("\"line: \"+(yyline+1)+\" \"+");
        if ( scanner.columnCount )
          print("\"col: \"+(yycolumn+1)+\" \"+");
        println("\"match: --\"+yytext()+\"--\");");        
        print("          System.out.println(\"action ["+action.priority+"] { ");
        print(escapify(action.content));
        println(" }\");");
      }
      
      println("          { "+action.content);
      println("          }");
      println("        case "+(i++)+": break;"); 
    }
  }

  private void emitEOFVal() {
    EOFActions eofActions = parser.getEOFActions();

    if ( scanner.eofCode != null ) 
      println("            yyFlexDoEOF();");
      
    if ( eofActions.numActions() > 0 ) {
      println("            switch (yyFlexLexicalState) {");
      
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
    println("              switch (yyFlexInput) {");
   
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
        print("yyFlexPushbackPos_l = yyFlexCurrentPosL; ");
      
      if ( dfa.isLookEnd[nextState] )
        print("yy_pushback = true; ");

      if ( !isTransition[nextState] )
        print("yy_noLookAhead = true; ");
        
      if ( nextState == state ) 
        println("yyFlexState = "+nextState+"; break yy_forNext;");
      else
        println("yyFlexState = "+nextState+"; break yy_forNext;");
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
        print("yyFlexPushbackPos_l = yyFlexCurrentPosL; ");

      if ( dfa.isLookEnd[nextState] )
        print("yy_pushback = true; ");
          
      if ( !isTransition[nextState] )
        print("yy_noLookAhead = true; ");
        
      if ( nextState == state ) 
        println("yyFlexState = "+nextState+"; break yy_forNext;");
      else
        println("yyFlexState = "+nextState+"; break yy_forNext;");
    }
    else
      println( "break yy_forAction;" );
  }
  
  private void emitPushback() {
    println("      if (yy_was_pushback)");
    println("        yyFlexMarkedPos = yyFlexPushbackPos_l;");
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


  /**
   * Set up EOF code sectioin according to scanner.eofcode 
   */
  private void setupEOFCode() {
    if (scanner.eofclose) {
      scanner.eofCode = LexScan.conc(scanner.eofCode, "  yyclose();");
      scanner.eofThrow = LexScan.concExc(scanner.eofThrow, "java.io.IOException");
    }    
  } 


  /**
   * Main Emitter method.  
   */
  public void emit() {    

    setupEOFCode();

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
    
    emitActionTable();
    
    if (scanner.useRowMap) {
     reduceRows();
    
      emitRowMapArray();

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
