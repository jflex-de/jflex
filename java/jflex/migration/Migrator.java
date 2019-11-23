package jflex.migration;

import com.google.common.base.Preconditions;

public class Migrator {

  public static void main(String[] args) {
    Preconditions.checkArgument(args.length > 0, "Syntax error: migrator TESTCASE_DIRS_ABS_PATH");
    for (String testCase : args) {
      migrate(testCase);
    }
  }

  private static void migrate(String testCase) {}
}
