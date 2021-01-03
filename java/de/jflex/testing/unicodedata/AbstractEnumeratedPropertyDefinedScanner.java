package de.jflex.testing.unicodedata;

import com.google.common.collect.ImmutableList;
import java.io.IOException;

public abstract class AbstractEnumeratedPropertyDefinedScanner {
  private final int maxCodePoint;
  private final String[] propertyValues;

  protected AbstractEnumeratedPropertyDefinedScanner(int maxCodePoint) {
    this.maxCodePoint = maxCodePoint;
    propertyValues = new String[maxCodePoint + 1];
  }

  public ImmutableList<BlockSpec> blocks() {
    ImmutableList.Builder<BlockSpec> blocks = ImmutableList.builder();
    String prevPropertyValue = propertyValues[0];
    int begCodePoint = 0;
    for (int codePoint = 1; codePoint <= maxCodePoint; ++codePoint) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        if (null != prevPropertyValue) {
          blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        begCodePoint = codePoint = 0xE000;
        prevPropertyValue = propertyValues[codePoint];
        continue;
      }
      String propertyValue = propertyValues[codePoint];
      if (null == propertyValue || !propertyValue.equals(prevPropertyValue)) {
        if (null != prevPropertyValue) {
          blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, codePoint - 1));
        }
        prevPropertyValue = propertyValue;
        begCodePoint = codePoint;
      }
    }
    if (null != prevPropertyValue) {
      blocks.add(BlockSpec.create(prevPropertyValue, begCodePoint, maxCodePoint));
    }
    return blocks.build();
  }

  protected void setCurCharPropertyValue(String index, String propertyValue) {
    propertyValues[index.codePointAt(0)] = propertyValue;
  }

  public abstract int yylex() throws IOException;
}
