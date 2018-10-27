# Workspace macro to import all UCD

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

def ucd_version(name, version, files):
    """Macro to import the UCD for a given unicode version."""
    for fn, sha in files.items():
        # TODO All files
        ucd_file(
            name = name,
            version = version,
            file = fn,
            sha256 = sha,
        )

def ucd_file(name, version, file, sha256):
    bzl_name = name + "_" + file.replace(".", "_").replace("-", "_")
    path = "/" + version + "/" + file
    http_file(
        name = bzl_name,
        urls = [
            # the Unicode.org site is extremely slow. Prefer a mirror.
            "http://ftp.lanet.lv/ftp/mirror/unicode" + path,
            "http://www.unicode.org/Public" + path,
        ],
        sha256 = sha256,
    )

def unicode_deps():
    ucd_version(name = "ucd_1", version = "1.1-Update", files = {
        "UnicodeData-1.1.5.txt": "b0aa30303db3c13701967320550952e7368470776e304b52270fdb9256e4bd5b",
    })

