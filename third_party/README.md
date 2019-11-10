# Bazel third-party packages

Contains BUILD aliases for the [Bazel build system][bazel].

To read how to use JFlex on your Bazel project, please read
[jflex/README.md](/README.md)

## Add a new dependency

Contributors who want to add a new dependency need to

1. Add the artifact id in `artifacts =`
2. Create a directory in `third_party` that matches the artifact groupId.
3. Add a BUILD file in that directory
   - The build must have a `license()` declaration.
   - The build target must declare all its dependencies.

[bazel]: https://bazel.build/
