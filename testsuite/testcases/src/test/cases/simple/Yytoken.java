public class Yytoken {
  public int m_index;
  public String m_text;
  public int m_line;
  public long m_charBegin;
  public long m_charEnd;
  
  Yytoken (int index, String text, int line, long charBegin, long charEnd) {
     m_index = index;
    m_text = text;
    m_line = line;
    m_charBegin = charBegin;
    m_charEnd = charEnd;
  }

  public String toString() {
    return "Text   : "+m_text+
           "\nindex : "+m_index+
           "\nline  : "+m_line+
           "\ncBeg. : "+m_charBegin+
           "\ncEnd. : "+m_charEnd;
  }
}

