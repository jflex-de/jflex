package de.jflex.pmd;

import de.jflex.pmd.rules.JFlexRuleChainVisitor;
import net.sourceforge.pmd.lang.BaseLanguageModule;

/**
 * The module for PMD that defines JFlex.
 */
public class JFlexLanguageModule extends BaseLanguageModule {
  /** The user-facing display name. */
  public static final String NAME = "JFlex.de";
  /** The internal name. */
  public static final String TERSE_NAME = "jflex";

  public JFlexLanguageModule() {
    super(NAME, null, TERSE_NAME, JFlexRuleChainVisitor.class, "jflex");
    addVersion("", new JFlexHandler(), true);
  }
}
