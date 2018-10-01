# Use JFlex in Bazel

This directory contains build rules to use JFlex with the
[Bazel build system][bazel].

## Preparation
### Update your workspace

Add the [`maven_jar`][be_maven_jar] rule in your [`WORKSPACE` file][be_workspace].

    # TODO(#390) Use maven 1.7.0 when cup_runtime is published
    # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
    native.maven_jar(
        name = "de_jflex_jflex_1_6_1",
        artifact = "de.jflex:jflex:1.6.1",
        # TODO() Use jcenter.bintray.com when synced with central
        repository = "https://jcenter.bintray.com/",
        sha1 = "eb4d51e1a8ea7ee96068905ddeceb9b28737c7eb",
    )

### Add a BUILD alias for JFlex

Create `third_party/de/jflex/BUILD` with that defines the
`//third_party/de/jflex:jflex_bin` target.
It should be sufficient to take out `third_party/de/jflex/BUILD`.

**N.B.** In our case, JFlex is obviously not a third-party package.
However, we use `//third_party/de/jflex` in the examples
to make it consistent with the recommended
[directory structure for third-party dependencies][be_3p].

## Usage

    load("//third_party/de/jflex:build_rules.bzl", "jflex")
    
    jflex(
        name = "",           # Choose a rule name
        srcs = [],           # Add input lex specifications
        outputs = [],        # List expected generated files
    )

Then, this rule can be used as one of the `srcs` of another rules, such as a `java_library`.

## Example

See `examples/WORKSPACE` and `examples/simple/BUILD`.



[bazel]: https://bazel.build/
[be_maven_jar]: https://docs.bazel.build/versions/master/be/workspace.html#maven_jar
[be_workspace]: https://docs.bazel.build/versions/master/tutorial/java.html#set-up-the-workspace 
[be_3p]: https://docs.bazel.build/versions/master/best-practices.html#third-party-dependencies
