package de.jflex.testing.unicodedata;

import com.google.common.collect.ImmutableList;
import java.io.IOException;

public abstract class AbstractEnumeratedPropertyDefinedScanner {
  private static final int maxCodePoint = 0xFFFD;
  private final String[] propertyValues = new String[maxCodePoint + 1];

  public ImmutableList<String> blocks() {
    ImmutableList.Builder<String> blocks = ImmutableList.builder();
    String prevPropertyValue = propertyValues[0];
    int begCodePoint = 0;
    for (int codePoint = 1; codePoint <= maxCodePoint; ++codePoint) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        if (null != prevPropertyValue) {
          blocks.add(block(begCodePoint, codePoint - 1, prevPropertyValue));
        }
        begCodePoint = codePoint = 0xE000;
        prevPropertyValue = propertyValues[codePoint];
        continue;
      }
      String propertyValue = propertyValues[codePoint];
      if (null == propertyValue || !propertyValue.equals(prevPropertyValue)) {
        if (null != prevPropertyValue) {
          blocks.add(block(begCodePoint, codePoint - 1, prevPropertyValue));
        }
        prevPropertyValue = propertyValue;
        begCodePoint = codePoint;
      }
    }
    if (null != prevPropertyValue) {
      blocks.add(block(begCodePoint, maxCodePoint, prevPropertyValue));
    }
    return blocks.build();
  }

  protected String block(int begCodePoint, int endCodePoint, String propertyValue) {
    return String.format("%04X..%04X; %s", begCodePoint, endCodePoint, propertyValue);
  }

  protected void setCurCharPropertyValue(String index, String propertyValue) {
    propertyValues[(int) index.charAt(0)] = propertyValue;
  }

  public abstract int yylex() throws IOException;
}
