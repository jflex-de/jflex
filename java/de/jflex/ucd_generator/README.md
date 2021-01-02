# UCD Generator

This program parses the fileset of a Unicode Character Definition,
and generates 
* a `UnicodeProperties_X_Y` java class per version
* one `UnivodeProperties.java` for all version

Note: This is the replacement of `jflex-unicode-maven-plugin` for Bazel.

## Where are the Unicode.org resources defined

Each unicode version is defined as a filegroup in
[`//third_party/unicode`](../../../third_party/unicode).

Bazel is responsible for fetching (using mirrors) and caching the remote resources.

## How to generate the Java files.

Jflex has a convient target:

    bazel build //java/de/jflex/ucd_generator:gen_unicode_properties

### Building for a reduced set of properties

The UnicodeData.txt and DerivedAge.txt must be provided.
The other data files should be provided.
The files must be accessible from the sandbox, e.g. in `/tmp`

    bazel run java/jflex/ucd_generator:Main -- --version=5.0.0 $(ls /tmp/ucd_5_0/*.txt  /tmp/ucd_5_0/auxiliary/*.txt)  --out=/tmp/ucd5_java
    
## Software architecture.

* `Main` is only responsible for parsing the cli arguments
  and generating `UcdVersions`
* The model `UcdVersions` is a map of version → File for each type, and is light in memory.
  * The types are defined in a simple enum `UcdFileType`.
* The different Unicode.org files are parsed by `UcdScanner`.
  * using JFlex (the flex files have been copied with little modifications form the jflex-unicode-maven-plugin)
  * The business logic of the scanners is in an class, so that the class can easily be edited in IDE
* While parsing, the `UnicodeData` model is updated.
  * I've tried to use immutable objects when possible, but that wasn't always possible
* `UcdGenerator` uses velocity
  * `UnicodePropertiesEmitter` emits `UnicodeProperties.java` from the `UcdVersions`.
  * `UnicodeVersionEmitter` emits `Unicode_x_y.java` from the `UnicodeData`.
