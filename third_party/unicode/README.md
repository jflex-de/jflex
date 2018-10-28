# Unicode Character Definitions

This package provides a Skylark rule to 
import the Unicode character definitions (UCD) from unicode.org.


For further information about data files please see:

Unicode Character Database
	http://www.unicode.org/ucd/

Terms of Use
	http://www.unicode.org/copyright.html


## How to generate Unicode properties?

### Add the source files in the Bazel Workspace

Edit `third_party/unicode/unicode.bzl` and add an `http_archive`.
The `ucd_zip_version` is a convenient way to do this. For instance:

```python
    ucd_zip_version(
        name = "ucd_9",
        version = "9.0.0",
        sha256 = "df9e028425816fd5117eaea7173704056f88f7cd030681e457c6f3827f9390ec",
        extra_files = ["ScriptExtensions.txt"],
    )
```

### Generate the UnicodeProperties.java

Run the generator with all versions

    bazel run //java/ucd_generator:Main 1.1=ucd_1 … 9.0=ucd_9
