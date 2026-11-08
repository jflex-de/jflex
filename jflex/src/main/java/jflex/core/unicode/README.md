<!--
  Copyright 2021, Régis Décamps
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Unicode properties

These files have been generated automatically.
Eventually, they should be removed from SCM.

## Generate

```sh
scripts/gen-unicode-tables.sh
```

This needs JDK 21+, for google-java-format. The script overwrites the files
here, so a clean checkout has no diff afterwards. Do not edit these by hand:
changes and fixes should go into the templates of
[ucd_generator](/java/de/jflex/ucd_generator).

See also

* [/third_party/unicode](/third_party/unicode) for how to add a new version from Unicode.org
* [ucd_generator](/java/de/jflex/ucd_generator) for the implementation details.
