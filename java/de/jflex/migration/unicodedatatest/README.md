# Generator of UnicodeAgeTest_x_y

To create the test for Unicode 6.1

    bazel run //java/de/jflex/migration/unicodedatatest:migrator -- 6.1 "$(git rev-parse --show-toplevel)"
