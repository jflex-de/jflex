""" Skylark rules for maven. """

def maven_plugin_test(name, srcs, deps = [], runtime_deps = [], data = []):
  native.java_test(
      name = name,
      srcs = srcs,
      deps = deps + [
          "//third_party/org/apache/maven/testing:maven_plugin_testing_harness",
      ],
      runtime_deps = runtime_deps + [
          "//third_party:org_apache_maven_maven_compat",
      ],
      data = data,
    )