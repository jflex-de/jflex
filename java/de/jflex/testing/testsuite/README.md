<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlexTestRunner

The JFlexTestRunner allows to test a situation where the lexer generation
is expected to fail, or the generated code should not compile.

Supported:
- generating the lexer
- passing options such as `--jlex` or `-q` to jflex
- verifying that the generated lexer compiles
- verifying that the generated lexer compiles with extra source files
- checking that the generation fails
- comparing the output logs with a golden file
