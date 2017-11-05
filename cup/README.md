# CUP

[CUP][cup]  stands for Construction of Useful Parsers and is an LALR parser generator for Java.

Home directory and parent POM of 2 artefacts:

- **cup** The CUP generator and ant task, extracted from the original jar,
  but where the runtime has been stripped out.
- **cup_runtime** The CUP runtime, compiled from a copy of the source code.

Note that this code is excluded from many rules of our codebase,
for instance google-java-format is not applied.

[cup]: http://www2.cs.tum.edu/projects/cup/
