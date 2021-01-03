# How to add support for a new Unicode version?

## Declare the Unicode.org resources

1. Declare the files fetched from Unicode in 
[//third_party/unicode](/third_party/unicode). See README.
2. Verify and commit the changes.

## Generate the new UnicodeProperties file

Add the new version in
[//java/de/jflex/ucd_generator:gen_unicode_properties](/java/de/jflex/ucd_generator)

1. Add the declared resources in `srcs`.
2. Add the expected generated `Unicode_x_y.java` in `out`.
3. Add the command-line argument in `cmd`.
4. Verify and commit the changes.

## Add the Unicode test case

Add a test case in [//javatests/de/jflex/testcase/unicode](/javatests/de/jflex/testcase/unicode).

1. Extend `KNOWN_VERSIONS` in [unicodedatatest/build_defs.bzl](/java/de/jflex/migration/unicodedatatest/build_defs.bzl).
2. Run the script which runs all the generators:
   ```sh
   java/de/jflex/migration/unicodedatatest/generate.sh`
   ``` 
3. Verify and commit the changes.
