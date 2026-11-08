<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Testing code

Code that is used by tests, but it is not test code.

Modules that use this code should depend on `jflex-dev-tools` with test scope, like `javatests`:

```xml
<dependency>
  <groupId>de.jflex</groupId>
  <artifactId>jflex-dev-tools</artifactId>
  <version>${project.version}</version>
  <scope>test</scope>
</dependency>
```
