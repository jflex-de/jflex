#!/bin/sh
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#

file=$1
lines=$(wc -l < $file)
lines=$((0 + lines))
if [[ "${lines}" -ne 0 ]]; then
  echo "There are unexpected dependencies (from to):"
  cat $file
fi
exit $lines
