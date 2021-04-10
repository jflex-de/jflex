# Copyright 2021 Google LLC

def java_library_with_nullness_check(name = None, **kwargs):
    if "plugins" not in kwargs:
        kwargs["plugins"] = []
    kwargs["plugins"].append("//third_party/org/checkerframework:plugin")

    if "deps" not in kwargs:
        kwargs["deps"] = []
    kwargs["deps"].append("@maven//:org_checkerframework_checker")

    return native.java_library(name = name, **kwargs)
