package jflex.dfa;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class DfaTest {

  @Test
  public void copyOf() {
    DFA dfa1 = new DFA(2, 1, 1);
    DFA dfa2 = DeprecatedDfa.copyOf(dfa1);
    assertThat(dfa2).isEqualTo(dfa1);
  }
}
