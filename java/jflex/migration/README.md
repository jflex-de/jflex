# Migration of testsuite from Maven to Bazel

This is a tool to automate the migration of the test cases
from jflex-testsuite-maven-plugin to bazel.

## gen_test_loader

Generates the TestLoader, reading `.test` files.
This is now only loading the model. All business logic to
parse or compile has been removed from the `.flex` spec.

## model

The data model for a `TestCase`.

## test_case_parser

Parser of a `.test` file that returns a `TestCase`.

