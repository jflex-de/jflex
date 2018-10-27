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
    ucd_version(name = "ucd_3", version = "3.2-Update", files = {
        "Blocks-3.2.0.txt": "8b367b02089762e753a0b5554182a6a132b9394431c0fe9f1dffb7a3338d86b9",
        "DerivedCoreProperties-3.2.0.txt": "787419dde91701018d7ad4f47432eaa55af14e3fe3fe140a11e4bbf3db18bb4c",
        "LineBreak-3.2.0.txt": "d693ef2a603d07e20b769ef8ba29afca39765588a03e3196294e5be8638ca735",
        "PropList-3.2.0.txt": "e6f4899305fd0a2771529671549dc2d75b502f4346e23b711f7f8b024d89a5f2",
        "PropertyAliases-3.2.0.txt": "8b9b07b0cb9f2d4bd3a27f3284af9dbf00984af53f9b5fd48f7f10f80d29f7c1",
        "PropertyValueAliases-3.2.0.txt": "c239cdc4a71c4d297901f6e6d377eed1c4547d3a12cb7afc3daf29098d94df28",
        "Scripts-3.2.0.txt": "16b3db9e2d1b87600831209df02ecff41aeaf0c2aab0f034684fda14189c1aa5",
        "UnicodeData-3.2.0.txt": "5e444028b6e76d96f9dc509609c5e3222bf609056f35e5fcde7e6fb8a58cd446",
    })

