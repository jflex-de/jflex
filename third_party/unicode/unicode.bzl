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
    ucd_version(name = "ucd_2", version = "2.1-Update4", files = {
        "Blocks-2.txt": "6a6653752ce1d8bd1f7a7777001d24d5008a58d138e03f11f9399f2de13fc81c",
        "PropList-2.1.9.txt": "c794fe1d60fbdb0ab8c76a151f56c3d4e51c57e0f779914a767f11bc213630f6",
        "UnicodeData-2.1.9.txt": "3dcee8b6b68151956fb799e4445c4e8948c0f4257d241b193ec3881b08c48137",
    })


