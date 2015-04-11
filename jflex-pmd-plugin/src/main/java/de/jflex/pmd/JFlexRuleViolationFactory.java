package de.jflex.pmd;


import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolationFactory;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

public class JFlexRuleViolationFactory extends AbstractRuleViolationFactory {
  public static final RuleViolationFactory INSTANCE = new JFlexRuleViolationFactory();

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message) {
    //TODO
    return null;
  }

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message, int beginLine, int endLine) {
    //TODO
    return null;
  }
}
