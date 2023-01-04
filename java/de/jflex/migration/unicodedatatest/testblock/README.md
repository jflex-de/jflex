<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Unicode blocks tests

```sh
bazel run java/de/jflex/migration/unicodedatatest/testblock:generator -- 6.1 $(git rev-parse --show-toplevel) /path/to/Blocks.txt
```

Or more simply

```shell script
bazel build //java/de/jflex/migration/unicodedatatest/testblock:generate
```
