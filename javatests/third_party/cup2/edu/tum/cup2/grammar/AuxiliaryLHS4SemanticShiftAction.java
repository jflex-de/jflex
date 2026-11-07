package edu.tum.cup2.grammar;


/**
 * Non-terminals used as left-hand-size of auxiliary productions created by the
 * parser generator if semantic shift-actions are being used.
 * 
 * WARNING: DO NEVER USE OR EXEND OR ANYTHING THIS EXTREMLY SPECIAL NON-TERMINALS
 *          UNLESS YOUR REALLY KNOW WHAT YOU ARE DOING!
 *          ALL PRODUCTIONS ASSOCIATED WITH THIS TYPE OF ENUM WILL BE TREATED IN
 *          A POSSIBLY VERY SURPRISING WAY!
 *          USAGE MIGHT RESULT IN CRASH OF PARSER GENERATOR OR PARSER DRIVER OR
 *          APOCALYPSE!!!
 **/

public enum AuxiliaryLHS4SemanticShiftAction implements NonTerminal {
  // leave empty: auxiliary enumerations will be created at runtime if needed.
  ;
  public int numPrecedingSymbols = 0;
  public int numPrecedingSymbolsNotEpsilon = 0;
  public String originatedFrom = "";
  public Class<?> symbolValueType = null;
}
