package de.jflex.pmd.rules;

import de.jflex.pmd.JFlexLanguageModule;
import de.jflex.pmd.ast.CupNode;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRule;

import java.util.List;
import java.util.logging.Logger;

public class UnicodeRule extends AbstractRule {
  public static Logger logger = Logger.getLogger(UnicodeRule.class.getName());
  public UnicodeRule() {
    super.setLanguage(LanguageRegistry.getLanguage(JFlexLanguageModule.NAME));
  }

  public void apply(List<? extends Node> list, RuleContext ruleContext) {
    logger.info("Applying rule");
    for (Node e : list) {
      visit((CupNode)e, ruleContext);
    }
  }

  private void visit(CupNode node, RuleContext context) {
    logger.fine(String.format("Visiting %s", node));
  }

}
