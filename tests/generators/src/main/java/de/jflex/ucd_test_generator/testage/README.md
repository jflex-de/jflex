<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Generator of UnicodeAgeTest_x_y

Run by [AllTestsGenerator](../AllTestsGenerator.java) for all versions, see [README](../README.md).

For each Unicode version, it generates one scanner per age, one for `Age:Unassigned`
and one for the subtraction of consecutive ages, the test class, and the golden file
of each scanner. [UnicodeAgeGoldens](UnicodeAgeGoldens.java) computes the golden
files from `DerivedAge.txt`.
