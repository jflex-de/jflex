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

import java.util.List;
import java.util.Locale;

public class TestCase {

  /** Single common input file for all outputs */
  private String commonInputFile;

  private String className;

  /** The test name, usually lower case. */
  private String testName;

  private String description;

  private boolean expectJavacFail, expectJFlexFail;

  /** get- set- methods */
  void setTestName(String s) {
    testName = s;
    // TODO(regisd): The class name should depend on the flex `%class`, not on the test name.
    className = testName.substring(0, 1).toUpperCase(Locale.ENGLISH) + testName.substring(1);
  }

  void setJFlexDiff(List<Integer> d) {}

  void setDescription(String s) {
    description = s;
  }

  void setExpectJavacFail(boolean b) {
    expectJavacFail = b;
  }

  void setExpectJFlexFail(boolean b) {
    expectJFlexFail = b;
  }

  void setJflexCmdln(List<String> v) {}

  void setJavacFiles(List<String> v) {}

  void setInputFileEncoding(String e) {}

  void setOutputFileEncoding(String e) {}

  void setCommonInputFile(String f) {
    commonInputFile = f;
  }

  void setJavaVersion(String v) {}

  void setJavacEncoding(String v) {}

  public String getCommonInputFile() {
    return commonInputFile;
  }

  public String getClassName() {
    return className;
  }

  public String getTestName() {
    return testName;
  }

  public String getDescription() {
    return description;
  }

  public boolean isExpectJavacFail() {
    return expectJavacFail;
  }

  public boolean isExpectJFlexFail() {
    return expectJFlexFail;
  }

  @Override
  public String toString() {
    return testName;
  }
}
