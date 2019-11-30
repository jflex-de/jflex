package jflex.testcase.ccl_esc;

class Yytoken {
  Yytoken(int index, String text) {
    m_index = index;
    m_text = text;
  }

  int m_index;
  String m_text;

  public String toString() {
    return "Token #" + m_index + ": " + m_text;
  }
}
