/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.jflex.migration.unicodedatatest.testcaseless;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator.Equivalences;

public class UnicodeCaselessTestGenerator
    extends AbstractGenerator<UnicodeCaselessTestTemplateVars> {

  private final Equivalences<Integer> equivalences;

  protected UnicodeCaselessTestGenerator(
      UnicodeVersion unicodeVersion, Equivalences<Integer> equivalences) {
    super("UnicodeCaselessTest.java", unicodeVersion);
    this.equivalences = equivalences;
  }

  @Override
  protected UnicodeCaselessTestTemplateVars createTemplateVars() {
    UnicodeCaselessTestTemplateVars vars = new UnicodeCaselessTestTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.className = "UnicodeCaselessTest_" + unicodeVersion.underscoreVersion();
    vars.equivalences =
        ImmutableList.sortedCopyOf(equivalences.getKeys()).stream()
            .collect(toImmutableMap(identity(), equivalences::getEquivalentValue));
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCaselessTestTemplateVars vars) {
    return vars.className + ".java";
  }
}
