package jflex.testcase.ccl_esc;

public class Yytoken {
  Yytoken(int index, String text) {
    m_index = index;
    m_text = text;
  }

  public int m_index;
  public String m_text;

  public String toString() {
    return "Token #" + m_index + ": " + m_text;
  }
}
