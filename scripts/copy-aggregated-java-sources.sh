#!/bin/bash
#
# Copyright 2026, Gerwin Klein
# SPDX-License-Identifier: BSD-3-Clause
#
# Usage: copy-aggregated-java-sources.sh <dest>
#
# Creates the directory <dest> with the full Java sources and the resources of
# JFlex, including the generated code, so that it can be compiled with plain
# javac and no annotation processor (see compile-aggregated-java-sources.sh).
# This is the java/ directory of branch aggregated-java-sources.
#
# Expects a Maven build of the jflex module, which generates the code.

set -e

DEST="${1:?Usage: $0 <dest>}"
JFLEX="$(cd "$(dirname "$0")" && pwd -P)"/../jflex
GEN="$JFLEX"/target/generated-sources

# Fails if <dest> exists, which the find below relies on.
mkdir "$DEST"

cp -R "$JFLEX"/src/main/java/. \
      "$GEN"/jflex/. "$GEN"/cup/. "$GEN"/annotations/. "$DEST"
# src/main/java also has build files and READMEs.
find "$DEST" -type f ! -name '*.java' -delete

cp -R "$JFLEX"/src/main/resources/. "$DEST"
