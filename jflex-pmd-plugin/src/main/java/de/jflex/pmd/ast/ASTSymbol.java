package de.jflex.pmd.ast;

import java_cup.runtime.Symbol;
import jflex.LexScan;
import jflex.sym;
import net.sourceforge.pmd.lang.ast.AbstractNode;

import java.util.logging.Logger;

/**
 * A wrapper around a cup {@link Symbol}.
 */
public class ASTSymbol extends AbstractNode implements CupNode {
  private static Logger logger = Logger.getLogger(ASTSymbol.class.getName());

  public final Object value;
  public final String text;

  public ASTSymbol(int id, Object value, String originText,
                   int theBeginLine, int theEndLine, int theBeginColumn, int theEndColumn) {
    super(id, theBeginLine, theEndLine, theBeginColumn, theEndColumn);
    this.value = value;
    this.text = originText;

    logger.finer(this.toString());
  }

  @Override
  public String toString() {
    return "[" + getSymbolName() + "] " + value + " \"" + text + "\"";
  }

  private String getSymbolName() {
    return sym.terminalNames[id];
  }

  public static ASTSymbol create(LexScan scanner, Symbol symbol) {
    // Line + 1 because JFlex starts from line 0.

    // TODO: Begin line
    int beginLine = scanner.currentLine() + 1;
    // +1 because JFlex counts lines from 0.
    int endLine = scanner.currentLine() + 1;
    // TODO: Columns
    int beginColumn = 0;
    int endColumn = 0;
    return new ASTSymbol(symbol.sym, symbol.value, scanner.yytext(),
        beginLine, endLine, beginColumn, endColumn);
  }
}
