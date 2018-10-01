# Root bazel package

# This pacakge exists because
# `bazel build //...` needs at least one package

# But bazel test //... fails with
# No test targets were found, yet testing was requested
# So here is a test:

sh_test(
  name = "hello_test",
  size = "small",
  srcs = [":hello_test_lib"],
)
sh_library(
  name = "hello_test_lib",
  srcs = [":gen_hello_test_sh"],
)
genrule(
  name = "gen_hello_test_sh",
  srcs = [],
  outs = ["hello_test.sh"],
  cmd = "echo 'echo \"Hello World\"' > $@",
)
