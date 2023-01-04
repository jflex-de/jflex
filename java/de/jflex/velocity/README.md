<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Velocity wrapper

Makes invocation of the [Velocity engine][velocity] easier.
- extend `TemplateVars` to automatically add all fields in the 
  Velocity context, using reflection.
- simply call `Velocity.render(...)`

[velocity]: https://velocity.apache.org/
