%%

%unicode
%public
%class UnicodeNotBlock

%type int
%standalone

%{
  private static final int maxCodePoint = 0xFFFD;
  private final String[] charBlock = new String[maxCodePoint + 1];
  
  private void printOutput() {
    String prevBlockName = charBlock[0];
    int begCodePoint = 0;
    for (int codePoint = 1 ; codePoint <= maxCodePoint ; ++codePoint ) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        printBlock(begCodePoint, codePoint - 1, prevBlockName);
        begCodePoint = codePoint = 0xE000;
        prevBlockName = charBlock[codePoint];
        continue;
      }
      String blockName = charBlock[codePoint];
      if (null == blockName || ! blockName.equals(prevBlockName)) {
        printBlock(begCodePoint, codePoint - 1, prevBlockName);
        prevBlockName = blockName;
        begCodePoint = codePoint;
      }
    }
    printBlock(begCodePoint, maxCodePoint, prevBlockName);
  }
  
  private void printBlock(int begCodePoint, int endCodePoint, String blockName) {
    System.out.println
      (toHex(begCodePoint) + ".." + toHex(endCodePoint) + "; " + blockName);    
  }
  
  private String toHex(int codePoint) {
    StringBuilder builder = new StringBuilder();
    String codePointStr = Integer.toString(codePoint, 16).toUpperCase();
    for (int padPos = 0 ; padPos < 4 - codePointStr.length() ; ++padPos) {
      builder.append('0');
    }
    builder.append(codePointStr);
    return builder.toString();
  }
%} 

%% 

\P{Block:Latin Extended Additional} { charBlock[ (int)yytext().charAt(0) ] = "Not Latin Extended Additional"; }
\p{Block:Latin Extended Additional} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended Additional"; }

<<EOF>> { printOutput(); return 1; }
