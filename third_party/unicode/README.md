# Unicode Character Definitions

This package provides a Skylark rule to 
import the Unicode character definitions (UCD) from unicode.org.

For further information about data files please see:

Unicode Character Database
	http://www.unicode.org/ucd/

Terms of Use
	http://www.unicode.org/copyright.html

## How to add Unicode data (UCD)?

### Add the source files in the Bazel Workspace

Edit `third_party/unicode/unicode.bzl` and add an `http_archive`.
The `ucd_zip_version` is a convenient way to do this. For instance:

```python
    ucd_zip_version(
        name = "ucd_9_0",
        version = "9.0.0",
        sha256 = "df9e028425816fd5117eaea7173704056f88f7cd030681e457c6f3827f9390ec",
        extra_files = ["ScriptExtensions.txt"],
    )
```

* The sha256 is the sha of the zip file. 
  Tip: Use a fake value like "1111111111111111111111111111111111111111111111111111111111111111" and see bazel complain.
  It will then provide the actual value.
* Note that "ScriptExtensions.txt" is added since Unicode 6.0
* Note that Unicode.org offers a zip since Unicode 4.0, for which the macro
  `ucd_zip_version` can be used.

### Add a filegroup target in BUILD.bazel

This is a convenience group to group the UCD with its corresponding emoji data.

Note: for old versions, it's also a convenience group for the various files,
because Unicode.org didn't provide a zip archive.

```python
filegroup(
    name = "ucd_9_0",
    srcs = [
        "@emoji_4_emoji_data_txt//file",
        "@ucd_9_0//:files",
    ],
)
```

## Why this declaration?

Because Bazel build are reproducible, the comprehensive list of resources
(with their sha256) must be declared.

## Where to see downloaded content?

```
bazel build //third_party/unicode/...
ls $(bazel info output_base)/external/ucd*
```
