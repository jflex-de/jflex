# Copyright 2021, Régis Décamps
# SPDX-License-Identifier: BSD-3-Clause

load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "NoUnusedTest",
    srcs = [
        "NoUnusedTest.java",
    ],
    data = [
        "expected_nounused_sysout.txt",
        "no-unused.flex",
    ],
    deps = [
    ],
)

jflex_testsuite(
    name = "UnusedTest",
    srcs = [
        "UnusedTest.java",
    ],
    data = [
        "expected_unused_sysout.txt",
        "unused.flex",
    ],
    deps = [
    ],
)
