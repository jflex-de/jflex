# Copyright 2021, Régis Décamps
# SPDX-License-Identifier: BSD-3-Clause

load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "NeverTest",
    srcs = [
        "NeverTest.java",
    ],
    data = [
        "expected_sysout.txt",
        "never.flex",
    ],
    deps = [
        "//third_party/com/google/guava",
    ],
)
