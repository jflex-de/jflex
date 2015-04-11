package de.jflex.pmd;

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
    // TODO Visitor class
    super(NAME, null, TERSE_NAME, null, "jflex");
    addVersion("", new JFlexHandler(), true);
  }
}
