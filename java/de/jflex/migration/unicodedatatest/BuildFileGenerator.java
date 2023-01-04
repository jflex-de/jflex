/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.testemoji.EmojiTestGenerator;
import java.nio.file.Path;
import java.nio.file.Paths;

class BuildFileGenerator extends AbstractGenerator<BuildFileTemplateVars> {

  public BuildFileGenerator(UnicodeVersion unicodeVersion) {
    super("BUILD", unicodeVersion);
  }

  @Override
  protected BuildFileTemplateVars createTemplateVars() {
    BuildFileTemplateVars vars = new BuildFileTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.className = "UnicodeAge_" + unicodeVersion.underscoreVersion();
    vars.ages = olderAges(unicodeVersion.version());
    vars.emojiProperties = EmojiTestGenerator.propertiesForVersion(unicodeVersion);
    return vars;
  }

  @Override
  protected String getOuputFileName(BuildFileTemplateVars vars) {
    return "BUILD.bazel";
  }

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path workspaceDir = Paths.get(args[1]);
    new BuildFileGenerator(version).generate(workspaceDir);
  }
}
