/*
 * Copyright (C) 2019-2020 Google, LLC.
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
package de.jflex.migration;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import java.io.File;

/** Variables for the velocity templates (both {@code BUILD.vm} and {@code TestCase.java.vm}). */
public class MigrationTemplateVars extends de.jflex.velocity.TemplateVars {

  /** The name of the test. */
  public String testName;
  /** The description of the test. Used in javadoc. */
  public String testDescription;

  /** Flex specification used by this test. */
  public File flexGrammar;
  /** java package with '.', used by the test and the scanner. */
  public String javaPackage;
  /** java package with '/' (File.sep) */
  public String javaPackageDir;
  /** The name of the test class. */
  public String testClassName;
  /** The name of the scanner class. */
  public String scannerClassName;
  /** Golden input/output files. */
  public ImmutableList<GoldenInOutFilePair> goldens;
}
