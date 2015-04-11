package de.jflex.pmd.ast;

import net.sourceforge.pmd.lang.ast.AbstractNode;

import java.util.List;

public class ASTRoot extends AbstractNode implements CupNode {
  private final List<ASTSymbol> nodes;

  public ASTRoot(List<ASTSymbol> nodes) {
    super(-1);
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    return String.format("ASTRoot of %d nodes", + nodes.size());
  }

  public List<ASTSymbol> getTokens() {
    return nodes;
  }
}
