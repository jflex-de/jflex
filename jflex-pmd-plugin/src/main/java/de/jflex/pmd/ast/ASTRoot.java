package de.jflex.pmd.ast;

import net.sourceforge.pmd.lang.ast.AbstractNode;
import net.sourceforge.pmd.lang.ast.Node;

import java.util.List;

public class AstRoot extends AbstractNode implements CupNode {
  private final List<AstSymbol> nodes;

  public AstRoot(List<AstSymbol> nodes) {
    super(-1);
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    boolean sep = false;
    for (AstSymbol node : nodes) {
      if (sep) {
        sb.append(", ");
      } else {
        sep = true;
      }
      sb.append(node.toString());
    }
    sb.append(']');
    return sb.toString();
  }
}
