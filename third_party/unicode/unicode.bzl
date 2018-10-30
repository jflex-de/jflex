# Workspace macro to import all UCD

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")

BUILD_UCD_ZIP = """# Content of a UCD archive
filegroup(
  name = "files",
  srcs = [
    "Blocks.txt",
    "DerivedCoreProperties.txt",
    "LineBreak.txt",
    "PropList.txt",
    "PropertyAliases.txt",
    "PropertyValueAliases.txt",
    "Scripts.txt",
    "UnicodeData.txt",
    "auxiliary/GraphemeBreakProperty.txt",
    "auxiliary/SentenceBreakProperty.txt",
    "auxiliary/WordBreakProperty.txt",
    {extra_files}
  ],
  visibility = ["//visibility:public"],
)
"""

def unicode_urls(path):
    return [
        # the Unicode.org site is extremely slow. Prefer a mirror.
        "http://ftp.lanet.lv/ftp/mirror/unicode" + path,
        "http://www.unicode.org/Public" + path,
    ]

def ucd_zip_version(name, version, sha256, extra_files = []):
    """Macro to import the UCD for a given unicode version, by individual files.

    Recommended for v4 and later.
    """
    path = "/zipped/" + version + "/UCD.zip"
    http_archive(
        name = name,
        build_file_content = BUILD_UCD_ZIP.format(
            extra_files = "".join(['"{file}",'.format(file = f) for f in extra_files]),
        ),
        sha256 = sha256,
        urls = unicode_urls(path),
    )

def ucd_version(name, version, files):
    """Macro to import the UCD for a given unicode version, by individual files."""
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
        urls = unicode_urls(path),
        sha256 = sha256,
    )

def unicode_deps():
    ucd_version(name = "ucd_1_1_5", version = "1.1-Update", files = {
        "UnicodeData-1.1.5.txt": "b0aa30303db3c13701967320550952e7368470776e304b52270fdb9256e4bd5b",
    })
    ucd_version(name = "ucd_2_0_14", version = "2.0-Update", files = {
        "Blocks-1.txt": "91b0d41a60af658a73e277f108d3f4959a8ec3f3983654b24c00db3a4e146877",
        "PropList-2.0.14.txt": "b23b98764b7e3e6eef67c161cd4b247dc1e5bcea3184598cdfc8d6cf83a83e23",
        "UnicodeData-2.0.14.txt": "fdb15931fefb25d34546c575c6507732a4ac48b4b4148510f46ee0161a84c336",
    })
    ucd_version(name = "ucd_2_1_9", version = "2.1-Update4", files = {
        "Blocks-2.txt": "6a6653752ce1d8bd1f7a7777001d24d5008a58d138e03f11f9399f2de13fc81c",
        "PropList-2.1.9.txt": "c794fe1d60fbdb0ab8c76a151f56c3d4e51c57e0f779914a767f11bc213630f6",
        "UnicodeData-2.1.9.txt": "3dcee8b6b68151956fb799e4445c4e8948c0f4257d241b193ec3881b08c48137",
    })
    ucd_version(name = "ucd_3_0_0", version = "3.0-Update", files = {
        "Blocks-3.txt": "b574340ba81a64c2eee69ef37eacd422258a67e6899c068f98f3165ef60e31ea",
        "LineBreak-5.txt": "a8f29019410364458c30c5a8dad41f9748d07280027eeb808bc4cc7f7c1abc73",
                        })
    ucd_version(name = "ucd_3_0_1", version = "3.0-Update1", files = {
        "PropList-3.0.1.txt": "909eef4adbeddbdddcd9487c856fe8cdbb8912aa8eb315ed7885b6ef65f4dc4c",
        "UnicodeData-3.0.1.txt": "2aea1fc7d7e64d792fcbd56721fef104a153e2783ab28bcaeb171d9742dd5a24",
    })
    ucd_version(name = "ucd_3_1_0", version = "3.1-Update", files = {
        "Blocks-4.txt": "00cdecb26876e7576abb724e39fafb78be24c2f34fe59b94deb70e8262f7bba5",
        "DerivedCoreProperties-3.1.0.txt": "337c9bdd2098fd354c80b9aecb18cedf06b7aecb78aa88a1be74b249dedd2e06",
        "LineBreak-6.txt": "e630bb90aca76ba299f109ee2feaa41ded43c663df9f7a8c4d664e2aabff1017",
        "Scripts-3.1.0.txt": "23423fca7931ba38c5f19ec588c7de76c15af401e2a71b317d9022c2a717665a",
        "UnicodeData-3.1.0.txt": "8e57884da0da3a66782b8a6332a601fccbcae9ed0060e12501aca02fa56ffecd",
    })
    ucd_version(name = "ucd_3_1_1", version = "3.1-Update1", files = {
        "PropList-3.1.1.txt": "d8b483e1b1143be208ee6ef9aeeb424f3c19828834c4d2c1427fc9a8d4c64d0e",
    })
    ucd_version(name = "ucd_3_2_0", version = "3.2-Update", files = {
        "Blocks-3.2.0.txt": "8b367b02089762e753a0b5554182a6a132b9394431c0fe9f1dffb7a3338d86b9",
        "DerivedCoreProperties-3.2.0.txt": "787419dde91701018d7ad4f47432eaa55af14e3fe3fe140a11e4bbf3db18bb4c",
        "LineBreak-3.2.0.txt": "d693ef2a603d07e20b769ef8ba29afca39765588a03e3196294e5be8638ca735",
        "PropList-3.2.0.txt": "e6f4899305fd0a2771529671549dc2d75b502f4346e23b711f7f8b024d89a5f2",
        "PropertyAliases-3.2.0.txt": "8b9b07b0cb9f2d4bd3a27f3284af9dbf00984af53f9b5fd48f7f10f80d29f7c1",
        "PropertyValueAliases-3.2.0.txt": "c239cdc4a71c4d297901f6e6d377eed1c4547d3a12cb7afc3daf29098d94df28",
        "Scripts-3.2.0.txt": "16b3db9e2d1b87600831209df02ecff41aeaf0c2aab0f034684fda14189c1aa5",
        "UnicodeData-3.2.0.txt": "5e444028b6e76d96f9dc509609c5e3222bf609056f35e5fcde7e6fb8a58cd446",
    })
    ucd_zip_version(
        name = "ucd_4_1_0",
        version = "4.1.0",
        sha256 = "1aa4041a36de1ef94b66beeb152ebd967f5f9be62f8b4ef382909258ef99b732",
    )
    ucd_zip_version(
        name = "ucd_5_2_0",
        version = "5.2.0",
        sha256 = "3d7a2467d6ee2533de545d833b3cd1cc2488f198e38d7b8b42adc67023a0c646",
    )
    ucd_zip_version(
        name = "ucd_6",
        version = "6.3.0",
        sha256 = "2d3c6c51b5821e821881b13694eccb78812d493762c41e9c95c31a7686ed3823",
        extra_files = ["ScriptExtensions.txt"],
    )
    ucd_zip_version(
        name = "ucd_7",
        version = "7.0.0",
        sha256 = "9c9d92ec9f011691d6d22d2c2d3e5825f50e4f8d6f85c2c2bc01705f085e2af6",
        extra_files = ["ScriptExtensions.txt"],
    )
    ucd_zip_version(
        name = "ucd_8",
        version = "8.0.0",
        sha256 = "e3959c0b96c5ea7ff118254b55e1a752c2a28170b3404ba6bb5ab2c58536ce2e",
        extra_files = ["ScriptExtensions.txt"],
    )
    ucd_zip_version(
        name = "ucd_9",
        version = "9.0.0",
        sha256 = "df9e028425816fd5117eaea7173704056f88f7cd030681e457c6f3827f9390ec",
        extra_files = ["ScriptExtensions.txt"],
    )
