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

  public ASTSymbol(LexScan scanner, Symbol symbol) {
    // TODO: Begin line
    // TODO: Columns
    // Line + 1 because JFlex starts from line 0.
    super(symbol.sym, scanner.currentLine() + 1, scanner.currentLine() + 1, 1, 1 + scanner.yylength());
    this.value = symbol.value;
    this.text = scanner.yytext();

    logger.finer(this.toString());
  }

  @Override
  public String toString() {
    return "[" + getSymbolName() + "] " + value + " \"" + text + "\"";
  }

  private String getSymbolName() {
    return sym.terminalNames[id];
  }
}
