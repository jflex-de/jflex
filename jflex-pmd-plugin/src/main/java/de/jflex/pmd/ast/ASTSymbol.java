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

    int beginLine = scanner.getCurrentLine() + 1;
    // +1 because JFlex counts lines from 0.
    // TODO endline
    int endLine = scanner.getCurrentLine() + 1;
    int beginColumn = scanner.getCurrentColumn() + 1;
    // TODO end column, incorrect if multiline text
    int endColumn = scanner.getCurrentColumn() + 1 + scanner.yylength();
    return new ASTSymbol(symbol.sym, symbol.value, scanner.yytext(),
        beginLine, endLine, beginColumn, endColumn);
  }
}
