<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Templates

Minimal text templates for the code generators in `de.jflex.ucd_generator` and
`de.jflex.migration.unicodedatatest`.

- A template is a plain text resource named `<output name>.template`, e.g.
  `UnicodeProperties.java.template`, in which each `${name}` is replaced by a value.
  There are no other directives: loops and conditionals are in the Java code
  that computes the values.
- Extend `TemplateVars` to make all its public fields available to the template,
  using reflection.
- Call `Template.render(...)`. An unknown `${name}` is an error.
