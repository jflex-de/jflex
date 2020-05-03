/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import java.util.Arrays;

/** Immutable second-level blocks for constructing the two-level character map table. */
public class CMapBlock {
  /** How many bits the second-level char map tables translate */
  public static final int BLOCK_BITS = 8;
  /** Size of the second-level char map arrays */
  public static final int BLOCK_SIZE = 1 << BLOCK_BITS;

  /** array of BLOCK_SIZE; reference immutable; contents intended to be as well */
  public final int[] block;
  /** pre-computed hash, since we will compare often */
  private final int hash;

  /**
   * Constructs new CMapBlock and pre-computes its hash
   *
   * @param block an int array of size @{link BLOCK_SIZE}.
   */
  public CMapBlock(int[] block) {
    assert block.length == BLOCK_SIZE : block;
    this.block = block;
    this.hash = Arrays.hashCode(block);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public boolean equals(Object other) {
    return this == other
        || (other instanceof CMapBlock
            && hash == ((CMapBlock) other).hash
            && Arrays.equals(block, ((CMapBlock) other).block));
  }
}
