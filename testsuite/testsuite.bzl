BASE_DEPS = [
    "//jflex",
    "//jflex:testing",
    "//java/de/jflex/testing/testsuite",
    "//third_party/com/google/truth",
]

def jflex_testsuite(**kwargs):
    args = update_args(kwargs)
    native.java_test(**args)

def update_args(kwargs):
    if ("deps" in kwargs):
        kwargs["deps"] = kwargs["deps"] + BASE_DEPS
    else:
        kwargs["deps"] = BASE_DEPS
    return kwargs
