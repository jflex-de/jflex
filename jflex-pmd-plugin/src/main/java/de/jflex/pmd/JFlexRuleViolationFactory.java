package de.jflex.pmd;

import de.jflex.pmd.ast.CupNode;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolationFactory;
import net.sourceforge.pmd.lang.rule.ParametricRuleViolation;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

import java.util.logging.Logger;

public class JFlexRuleViolationFactory extends AbstractRuleViolationFactory {
  private static Logger logger = Logger.getLogger(JFlexRuleViolationFactory.class.getName());

  public static final RuleViolationFactory INSTANCE = new JFlexRuleViolationFactory();

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node,
                                              String message) {
    logger.finer(String.format("violation %s on %s", rule, node));
    return new ParametricRuleViolation<CupNode>(rule, ruleContext, (CupNode) node, message);
  }

  @Override
  protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node,
                                              String message, int beginLine, int endLine) {
    //TODO lines
    logger.finer(String.format("violation %s on %s lines %d-%d", rule, node, beginLine, endLine));
    return new ParametricRuleViolation<CupNode>(rule, ruleContext, (CupNode) node, message);
  }

}
