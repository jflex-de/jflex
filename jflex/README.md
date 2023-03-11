<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlex

This directory contains JFlex, a fast scanner generator for Java.

To run JFlex, run `bin/jflex` from the command line or double click on the
jflex-full-1.9.1.jar file in the `lib/` directory.

See the manual in `doc/` or the website at <http://jflex.de> for more
information and for how to get started.


## Contents

    ├── BUILD.bazel      build specification for Bazel
    ├── changelog.md     summary of changes between releases
    ├── pom.xml          project object model to build with Maven
    ├── README.md        this file
    ├── bin              command line start scripts
    ├── doc              user manual
    ├── examples         example scanners and parsers
    ├── lib              syntax highlighting files ; also JFlex jar in binary distribution
    └── src              JFlex sources
