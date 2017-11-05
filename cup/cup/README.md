# CUP

The generator and and task for CUP.

This is build by

1. installing in the local repository a copy of the original `java-cup-{version}.jar`
   (we never use it directly, though)
2. unpacking the jar classes, **excluding `java_cup/runtime/**`**.
   This allows the ant task to reference `de.jflex:cup` as a dependency
   (whose classpath can be resolved as `cup/cup/target/classes` depending on
   the compilation phase)

This artefact is used as a dependency of the `maven-ant-plugin`.
