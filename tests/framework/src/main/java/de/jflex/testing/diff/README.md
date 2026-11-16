<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# File diff

`DiffOutputStream` is an `OutputStream` that compares output line by line with
the expected content of a golden file. The output is not saved or echoed.

The comparison is strict. The output must have exactly the lines of the golden
file, including empty lines at the end and whether the last line ends in a
newline. Only `\n` separates lines, `\r` is content like any other character.

Closing the stream compares a last line without newline and checks that no
expected content is missing, so always close the stream, for instance with
try-with-resources.
