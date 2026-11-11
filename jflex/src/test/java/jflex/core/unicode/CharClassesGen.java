/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import java.util.Collections;
import java.util.Set;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

/**
 * Generator for random {@link CharClasses} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see CharClasses
 */
public class CharClassesGen implements ArbitraryProvider {

  /**
   * Max bound for number of classes to add to the default.
   *
   * <p>Number of classes in the final partition is added classes + 1.
   */
  private static final int MAX_SIZE = 5;

  @Override
  public boolean canProvideFor(TypeUsage targetType) {
    return targetType.isOfType(CharClasses.class);
  }

  @Override
  public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
    return Collections.singleton(
        IntCharSetGen.intCharSets()
            .list()
            .ofMaxSize(MAX_SIZE)
            .map(
                classes -> {
                  // TODO(lsf): make maxChar and UnicodeProperties configurable. Without
                  // UnicodeProperties, there are no caseless classes.
                  CharClasses result = new CharClasses(CharClasses.maxChar, () -> null);
                  classes.forEach(c -> result.makeClass(c, false));
                  return result;
                }));
  }
}
