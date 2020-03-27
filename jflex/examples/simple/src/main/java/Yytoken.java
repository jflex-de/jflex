/** The tokens returned by the scanner. */
class Yytoken {
  public int m_index;
  public String m_text;
  public int m_line;
  public long m_charBegin;
  public long m_charEnd;

  Yytoken(int index, String text, int line, long charBegin, long charEnd) {
    checkArgument("index", index >= 0);
    checkArgument("line", line >= 0);
    checkArgument("charBegin", charBegin >= 0);
    checkArgument("charEnd", charEnd > 0);
    m_index = index;
    m_text = text;
    m_line = line;
    m_charBegin = charBegin;
    m_charEnd = charEnd;
  }

  @Override
  public String toString() {
    return "Text   : "
        + m_text
        + "\nindex : "
        + m_index
        + "\nline  : "
        + m_line
        + "\ncBeg. : "
        + m_charBegin
        + "\ncEnd. : "
        + m_charEnd;
  }

  private static void checkArgument(String argName, boolean expectation) {
    if (!expectation) {
      throw new IllegalArgumentException(argName);
    }
  }
}
