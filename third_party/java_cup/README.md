# CUP

The cup generator and and task for CUP.

This is build by

1. installing in the `//third_party/java_cup` a copy of the original `java-cup-{version}.jar`
   (we never use it directly, though)
2. unpacking the jar classes, **`java_cup/runtime/**`**.

This artifact is used as a dependency of the `maven-ant-plugin`.
