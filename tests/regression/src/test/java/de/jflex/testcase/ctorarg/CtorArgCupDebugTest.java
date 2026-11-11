// test: ctorarg-cupdebug
/*
 * Copyright 2026, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests that {@code %cupdebug} together with {@code %ctorarg} generates a scanner that compiles.
 *
 * <p>The debug {@code main} emitted for {@code %cupdebug} constructs the scanner with a reader
 * only, so the emitter must also generate the constructor without the {@code %ctorarg} parameters,
 * as it already did for {@code %standalone} and {@code %debug}.
 */
public class CtorArgCupDebugTest extends AbstractGoldenTest {

  /** scanner generated from {@code ctor-arg-cupdebug.flex}. */
  private final ScannerFactory<CtorargCupdebug> scannerFactory =
      ScannerFactory.of(CtorargCupdebug::new);

  /** Tests that the scanner has the reader-only constructor and can be instantiated with it. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
