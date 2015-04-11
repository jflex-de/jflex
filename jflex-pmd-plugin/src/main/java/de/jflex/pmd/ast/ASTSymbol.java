package de.jflex.pmd.ast;

import java_cup.runtime.Symbol;
import jflex.LexScan;
import jflex.sym;
import net.sourceforge.pmd.lang.ast.AbstractNode;

import java.util.logging.Logger;

/**
 * A wrapper around a cup {@link Symbol}.
 */
public class AstSymbol extends AbstractNode implements CupNode {
  private static Logger logger = Logger.getLogger(AstSymbol.class.getName());

  private final LexScan scanner;

  public AstSymbol(LexScan scanner, Symbol symbol) {
    super(symbol.sym);
    this.scanner = scanner;

    logger.fine("New Symbol " + symbol + ":" + symbol.value);
  }

  @Override
  public String toString() {
    return String.valueOf(getSymbolName());
  }

  private String getSymbolName() {
    return sym.terminalNames[id];
  }
}
