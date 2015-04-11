package de.jflex.pmd;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolationFactory;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

import java.util.logging.Logger;

public class JFlexRuleViolationFactory extends AbstractRuleViolationFactory {
  private static Logger logger = Logger.getLogger(JFlexRuleViolationFactory.class.getName());

  public static final RuleViolationFactory INSTANCE = new JFlexRuleViolationFactory();

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message) {
    logger.fine(String.format("Create RuleViolation %s on %s", rule, node));
    //TODO
    return null;
  }

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message, int beginLine, int endLine) {
    //TODO
    logger.fine(String.format("Create RuleViolation %s on %s lines %d-%d", rule, node, beginLine, endLine));
    return null;
  }
}
