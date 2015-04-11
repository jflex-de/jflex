package de.jflex.pmd.rules;

import de.jflex.pmd.JFlexLanguageModule;
import de.jflex.pmd.ast.CupNode;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRule;

import java.util.List;

public class UnicodeRule extends AbstractRule {
  public UnicodeRule() {
    super.setLanguage(LanguageRegistry.getLanguage(JFlexLanguageModule.NAME));
  }

  public void apply(List<? extends Node> list, RuleContext ruleContext) {
    for (Node e : list) {
      visit((CupNode)e, ruleContext);
    }
  }

  private void visit(CupNode node, RuleContext context) {
    System.out.println("Visiting "+  node);
  }

}
