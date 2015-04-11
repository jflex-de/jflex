package de.jflex.pmd.ast;

import java_cup.runtime.Symbol;
import jflex.LexParse;
import net.sourceforge.pmd.lang.ast.AbstractNode;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import org.jaxen.JaxenException;
import org.w3c.dom.Document;

import java.util.List;

/**
 * A wrapper around a cup {@link Symbol}.
 */
public class ASTSymbol extends AbstractNode {

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
