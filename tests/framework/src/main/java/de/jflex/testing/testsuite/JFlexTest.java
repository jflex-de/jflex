/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.testsuite;

import de.jflex.testing.testsuite.annotations.TestSpec;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * A test class with a {@link TestSpec}. Adds the tests "Generate Lexer" and, unless the spec
 * expects JFlex to fail, "Compile java code" to the class. They report what {@link
 * JFlexTestExtension} did before the tests of the class.
 */
public interface JFlexTest {

  @TestFactory
  default List<DynamicTest> lexer() {
    return JFlexTestExtension.report(getClass());
  }
}
