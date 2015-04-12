package de.jflex.pmd.rules;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleChainVisitor;
import net.sourceforge.pmd.lang.rule.RuleChainVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * A {@link RuleChainVisitor} for JFlex grammar rules.
 */
public class JFlexRuleChainVisitor extends AbstractRuleChainVisitor {
  private static Logger logger = Logger.getLogger(JFlexRuleChainVisitor.class.getName());

  @Override
  protected void indexNodes(List<Node> nodeList, RuleContext ruleContext) {
    logger.info(String.format("Index nodes: %s", Arrays.toString(nodeList.toArray())));
    for (Node node : nodeList) {
      indexNode(node);
    }
  }

  @Override
  protected void visit(Rule rule, Node node, RuleContext ruleContext) {
    logger.fine(String.format(
        "Visiting node %s with rule %s", node, rule));
  }
}
