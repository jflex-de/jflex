package de.jflex.pmd.rules;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleChainVisitor;
import net.sourceforge.pmd.lang.rule.RuleChainVisitor;

import java.util.List;

/**
 * A {@link RuleChainVisitor} for JFlex rules.
 */
public class JFlexRuleChainVisitor extends AbstractRuleChainVisitor {
  @Override
  protected void visit(Rule rule, Node node, RuleContext ruleContext) {

  }

  @Override
  protected void indexNodes(List<Node> list, RuleContext ruleContext) {

  }
}
