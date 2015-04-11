package de.jflex.pmd.rules;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleChainVisitor;
import net.sourceforge.pmd.lang.rule.RuleChainVisitor;

import java.util.List;

/**
 * A {@link RuleChainVisitor} for JFlex grammar rules.
 */
public class JFlexRuleChainVisitor extends AbstractRuleChainVisitor {
  @Override
  protected void indexNodes(List<Node> nodeList, RuleContext ruleContext) {
    for (Node node : nodeList) {
      indexNode(node);
    }
  }

  @Override
  protected void visit(Rule rule, Node node, RuleContext ruleContext) {
    System.out.println(String.format(
        "Visiting node %s with rule %s", node, rule));
  }
}
