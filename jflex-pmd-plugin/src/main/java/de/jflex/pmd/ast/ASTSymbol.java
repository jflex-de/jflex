package de.jflex.pmd.ast;

import java_cup.runtime.Symbol;
import jflex.LexParse;
import net.sourceforge.pmd.lang.ast.AbstractNode;

/**
 * A wrapper around a cup {@link Symbol}.
 */
public class ASTSymbol extends AbstractNode implements CupNode {

  private final LexParse parser;

  public ASTSymbol(LexParse parser, Symbol symbol) {
    super(symbol.sym);
    this.parser = parser;
  }


  @Override
  public String toString() {
    return String.valueOf(id);
  }
}
