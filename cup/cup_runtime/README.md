# CUP runtime

The CUP runtime, compiled from a copy of the java source.
Keeping the source allows us to track modifications – whereas the original code
changed locations a couple of times.

This artefact is used for:

1. the ant task that generates source code for JFlex;
2. many projects which use `%cup` integration.
