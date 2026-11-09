<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Testing code

Code that is used by tests, but it is not test code.

Test modules that use this code depend on `jflex-test-framework` with test scope, like `tests/regression`:

```xml
<dependency>
  <groupId>de.jflex</groupId>
  <artifactId>jflex-test-framework</artifactId>
  <version>${project.version}</version>
  <scope>test</scope>
</dependency>
```
