def _all_targets_but(name):
    pkg = "//" + native.package_name()
    return [pkg + ":" + t for t in native.existing_rules().keys() if t != name]

def check_deps(name, prohibited):
    """Checks for direct dependencies between all targets in the package and the
    provided prohibited list."""
    query_name = "query_" + name
    targets = _all_targets_but(name)
    print(targets)
    query = ("deps(set({targets}), 1)" +
             " intersect {prohibited}").format(
        targets = " ".join(targets),
        prohibited = prohibited,
    )
    print(query)
    native.genquery(
        name = query_name,
        expression = query,
        scope = targets + [prohibited],
        deps = [],
    )
    native.sh_test(
        name = name,
        srcs = ["//scripts:check_file_empty.sh"],
        args = ["$(location :" + query_name + ")"],
        data = [":" + query_name],
    )
