# Do not edit. bazel-deps autogenerates this file from jflex-maven-plugin/dependencies.yml.
def _jar_artifact_impl(ctx):
    jar_name = "%s.jar" % ctx.name
    ctx.download(
        output=ctx.path("jar/%s" % jar_name),
        url=ctx.attr.urls,
        sha256=ctx.attr.sha256,
        executable=False
    )
    src_name="%s-sources.jar" % ctx.name
    srcjar_attr=""
    has_sources = len(ctx.attr.src_urls) != 0
    if has_sources:
        ctx.download(
            output=ctx.path("jar/%s" % src_name),
            url=ctx.attr.src_urls,
            sha256=ctx.attr.src_sha256,
            executable=False
        )
        srcjar_attr ='\n    srcjar = ":%s",' % src_name

    build_file_contents = """
package(default_visibility = ['//visibility:public'])
java_import(
    name = 'jar',
    tags = ['maven_coordinates={artifact}'],
    jars = ['{jar_name}'],{srcjar_attr}
)
filegroup(
    name = 'file',
    srcs = [
        '{jar_name}',
        '{src_name}'
    ],
    visibility = ['//visibility:public']
)\n""".format(artifact = ctx.attr.artifact, jar_name = jar_name, src_name = src_name, srcjar_attr = srcjar_attr)
    ctx.file(ctx.path("jar/BUILD"), build_file_contents, False)
    return None

jar_artifact = repository_rule(
    attrs = {
        "artifact": attr.string(mandatory = True),
        "sha256": attr.string(mandatory = True),
        "urls": attr.string_list(mandatory = True),
        "src_sha256": attr.string(mandatory = False, default=""),
        "src_urls": attr.string_list(mandatory = False, default=[]),
    },
    implementation = _jar_artifact_impl
)

def jar_artifact_callback(hash):
    src_urls = []
    src_sha256 = ""
    source=hash.get("source", None)
    if source != None:
        src_urls = [source["url"]]
        src_sha256 = source["sha256"]
    jar_artifact(
        artifact = hash["artifact"],
        name = hash["name"],
        urls = [hash["url"]],
        sha256 = hash["sha256"],
        src_urls = src_urls,
        src_sha256 = src_sha256
    )
    native.bind(name = hash["bind"], actual = hash["actual"])


def list_dependencies():
    return [
    {"artifact": "aopalliance:aopalliance:1.0", "lang": "java", "sha1": "0235ba8b489512805ac13a8f9ea77a1ca5ebe3e8", "sha256": "0addec670fedcd3f113c5c8091d783280d23f75e3acb841b61a9cdb079376a08", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/aopalliance/aopalliance/1.0/aopalliance-1.0.jar", "source": {"sha1": "4a4b6d692e17846a9f3da036438a7ac491d3c814", "sha256": "e6ef91d439ada9045f419c77543ebe0416c3cdfc5b063448343417a3e4a72123", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/aopalliance/aopalliance/1.0/aopalliance-1.0-sources.jar"} , "name": "aopalliance_aopalliance", "actual": "@aopalliance_aopalliance//jar", "bind": "jar/aopalliance/aopalliance"},
    {"artifact": "classworlds:classworlds:1.1-alpha-2", "lang": "java", "sha1": "05adf2e681c57d7f48038b602f3ca2254ee82d47", "sha256": "2bf4e59f3acd106fea6145a9a88fe8956509f8b9c0fdd11eb96fee757269e3f3", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/classworlds/classworlds/1.1-alpha-2/classworlds-1.1-alpha-2.jar", "name": "classworlds_classworlds", "actual": "@classworlds_classworlds//jar", "bind": "jar/classworlds/classworlds"},
# duplicates in com.google.guava:guava promoted to 20.0
# - com.google.inject:guice:jar:no_aop:4.0 wanted version 16.0.1
# - org.apache.maven:maven-core:3.5.2 wanted version 20.0
# - org.apache.maven:maven-model-builder:3.5.2 wanted version 20.0
# - org.apache.maven:maven-resolver-provider:3.5.2 wanted version 20.0
    {"artifact": "com.google.guava:guava:20.0", "lang": "java", "sha1": "89507701249388e1ed5ddcf8c41f4ce1be7831ef", "sha256": "36a666e3b71ae7f0f0dca23654b67e086e6c93d192f60ba5dfd5519db6c288c8", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/com/google/guava/guava/20.0/guava-20.0.jar", "source": {"sha1": "9c8493c7991464839b612d7547d6c263adf08f75", "sha256": "994be5933199a98e98bd09584da2bb69ed722275f6bed61d83459af88ace5cbd", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/com/google/guava/guava/20.0/guava-20.0-sources.jar"} , "name": "com_google_guava_guava", "actual": "@com_google_guava_guava//jar", "bind": "jar/com/google/guava/guava"},
    {"artifact": "com.google.inject:guice:jar:no_aop:4.0", "lang": "java", "sha1": "199b7acaa05b570bbccf31be998f013963e5e752", "sha256": "19393891be59b6feaf7e308bd8a3843b4e552c10cdb687ebffd7695634a250a8", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/com/google/inject/guice/4.0/guice-4.0-no_aop.jar", "source": {"sha1": "961359f8352b791c6443a207c847e25d6d3ca7cd", "sha256": "5ae16a56d478312ecee129b241a3df0fc9016b241bd4a0cbcd6b33f900a1eba6", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/com/google/inject/guice/4.0/guice-4.0-sources.jar"} , "name": "com_google_inject_guice_jar_no_aop", "actual": "@com_google_inject_guice_jar_no_aop//jar", "bind": "jar/com/google/inject/guice_jar_no_aop"},
# duplicates in commons-io:commons-io promoted to 2.5
# - org.apache.maven.plugin-testing:maven-plugin-testing-harness:3.3.0 wanted version 2.2
# - org.apache.maven.shared:maven-shared-utils:3.1.0 wanted version 2.5
    {"artifact": "commons-io:commons-io:2.5", "lang": "java", "sha1": "2852e6e05fbb95076fc091f6d1780f1f8fe35e0f", "sha256": "a10418348d234968600ccb1d988efcbbd08716e1d96936ccc1880e7d22513474", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/commons-io/commons-io/2.5/commons-io-2.5.jar", "source": {"sha1": "0caf033a4a7c37b4a8ff3ea084cba591539b0b69", "sha256": "3b69b518d9a844732e35509b79e499fca63a960ee4301b1c96dc32e87f3f60a1", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/commons-io/commons-io/2.5/commons-io-2.5-sources.jar"} , "name": "commons_io_commons_io", "actual": "@commons_io_commons_io//jar", "bind": "jar/commons_io/commons_io"},
    {"artifact": "javax.annotation:jsr250-api:1.0", "lang": "java", "sha1": "5025422767732a1ab45d93abfea846513d742dcf", "sha256": "a1a922d0d9b6d183ed3800dfac01d1e1eb159f0e8c6f94736931c1def54a941f", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/annotation/jsr250-api/1.0/jsr250-api-1.0.jar", "source": {"sha1": "9b1fba77edd118e13c42bda43d3c993dadd52c25", "sha256": "025c47d76c60199381be07012a0c5f9e74661aac5bd67f5aec847741c5b7f838", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/annotation/jsr250-api/1.0/jsr250-api-1.0-sources.jar"} , "name": "javax_annotation_jsr250_api", "actual": "@javax_annotation_jsr250_api//jar", "bind": "jar/javax/annotation/jsr250_api"},
    {"artifact": "javax.enterprise:cdi-api:1.0", "lang": "java", "sha1": "44c453f60909dfc223552ace63e05c694215156b", "sha256": "1f10b2204cc77c919301f20ff90461c3df1b6e6cb148be1c2d22107f4851d423", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/enterprise/cdi-api/1.0/cdi-api-1.0.jar", "source": {"sha1": "3a3b9c3e5a1ec04c0c8b82e249cee7aeb4a96f9a", "sha256": "0e7c351dfe05759f84dc3eddaac1da4ef72578b494b53338829d34b12271374f", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/enterprise/cdi-api/1.0/cdi-api-1.0-sources.jar"} , "name": "javax_enterprise_cdi_api", "actual": "@javax_enterprise_cdi_api//jar", "bind": "jar/javax/enterprise/cdi_api"},
    {"artifact": "javax.inject:javax.inject:1", "lang": "java", "sha1": "6975da39a7040257bd51d21a231b76c915872d38", "sha256": "91c77044a50c481636c32d916fd89c9118a72195390452c81065080f957de7ff", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/inject/javax.inject/1/javax.inject-1.jar", "source": {"sha1": "a00123f261762a7c5e0ec916a2c7c8298d29c400", "sha256": "c4b87ee2911c139c3daf498a781967f1eb2e75bc1a8529a2e7b328a15d0e433e", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/javax/inject/javax.inject/1/javax.inject-1-sources.jar"} , "name": "javax_inject_javax_inject", "actual": "@javax_inject_javax_inject//jar", "bind": "jar/javax/inject/javax_inject"},
    {"artifact": "junit:junit:3.8.1", "lang": "java", "sha1": "99129f16442844f6a4a11ae22fbbee40b14d774f", "sha256": "b58e459509e190bed737f3592bc1950485322846cf10e78ded1d065153012d70", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/junit/junit/3.8.1/junit-3.8.1.jar", "source": {"sha1": "0525753763e53f6f76da052b316d0f2e3bfa4d73", "sha256": "ce253192093fb24dae717ea5b6863e67087416796ce366d7a53fa467b0d14ead", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/junit/junit/3.8.1/junit-3.8.1-sources.jar"} , "name": "junit_junit", "actual": "@junit_junit//jar", "bind": "jar/junit/junit"},
    {"artifact": "org.apache.commons:commons-lang3:3.5", "lang": "java", "sha1": "6c6c702c89bfff3cd9e80b04d668c5e190d588c6", "sha256": "8ac96fc686512d777fca85e144f196cd7cfe0c0aec23127229497d1a38ff651c", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar", "source": {"sha1": "f7d878153e86a1cdddf6b37850e00a9f8bff726f", "sha256": "1f7adeee4d483a6ca8d479d522cb2b07e39d976b758f3c2b6e1a0fed20dcbd2d", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5-sources.jar"} , "name": "org_apache_commons_commons_lang3", "actual": "@org_apache_commons_commons_lang3//jar", "bind": "jar/org/apache/commons/commons_lang3"},
    {"artifact": "org.apache.maven.plugin-testing:maven-plugin-testing-harness:3.3.0", "lang": "java", "sha1": "8ae849da486819e13c53d20a73cf467a56e1f1d1", "sha256": "4b251e8a8bc345639525427dbe54d8b38650506c1cd2ce7b81fa47e514685ad6", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/plugin-testing/maven-plugin-testing-harness/3.3.0/maven-plugin-testing-harness-3.3.0.jar", "source": {"sha1": "13061ea1b877ba245ec5a57fb28c460408ca7e16", "sha256": "ff382dd2e6502d71230f7bf9f3e849d71c180b53a2753bdbc9364e803e6d7d68", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/plugin-testing/maven-plugin-testing-harness/3.3.0/maven-plugin-testing-harness-3.3.0-sources.jar"} , "name": "org_apache_maven_plugin_testing_maven_plugin_testing_harness", "actual": "@org_apache_maven_plugin_testing_maven_plugin_testing_harness//jar", "bind": "jar/org/apache/maven/plugin_testing/maven_plugin_testing_harness"},
    {"artifact": "org.apache.maven.resolver:maven-resolver-api:1.1.0", "lang": "java", "sha1": "71ab206c1bfb522f9bbb4d9dd5641ec732ffb995", "sha256": "b0942ee5f7c4cd9acc922bc0ce49d4c20849551f404ae39319328481cd288cd9", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-api/1.1.0/maven-resolver-api-1.1.0.jar", "source": {"sha1": "8ed80693bee20884ac4c76acb0136afba592983e", "sha256": "d3ce8bdee0728b97b6a6cae88aec641999ee1f819466ffc83b91634fc003b44b", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-api/1.1.0/maven-resolver-api-1.1.0-sources.jar"} , "name": "org_apache_maven_resolver_maven_resolver_api", "actual": "@org_apache_maven_resolver_maven_resolver_api//jar", "bind": "jar/org/apache/maven/resolver/maven_resolver_api"},
    {"artifact": "org.apache.maven.resolver:maven-resolver-impl:1.1.0", "lang": "java", "sha1": "0c84e6576d50973c8e9e24e05fe2f12c1c9d4393", "sha256": "d1d28bf4dac3cc06fe74ca4fc53a104173b8f82078969f04d03449b47e9fbb5a", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-impl/1.1.0/maven-resolver-impl-1.1.0.jar", "source": {"sha1": "a73038959e24a0463e33c4839293efe08380309a", "sha256": "b6e1f7c0fe4934967c4f6fa2a6bd99c80aac3bd85beca38a697fcee5edc9caed", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-impl/1.1.0/maven-resolver-impl-1.1.0-sources.jar"} , "name": "org_apache_maven_resolver_maven_resolver_impl", "actual": "@org_apache_maven_resolver_maven_resolver_impl//jar", "bind": "jar/org/apache/maven/resolver/maven_resolver_impl"},
    {"artifact": "org.apache.maven.resolver:maven-resolver-spi:1.1.0", "lang": "java", "sha1": "6ba3b964448bd43d87a7452cc67afb3b104091ed", "sha256": "bf37e9a1f505ec9481b2c4f3b821d4ac02d998ccbc00c09e56a354261f598946", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-spi/1.1.0/maven-resolver-spi-1.1.0.jar", "source": {"sha1": "77fb35506091e5396f85ffcf6f96d0742bbc6dea", "sha256": "f1772e2212a4d54f289e555c8b68f7de8c97f6b3ab9e6ffb791a077721604825", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-spi/1.1.0/maven-resolver-spi-1.1.0-sources.jar"} , "name": "org_apache_maven_resolver_maven_resolver_spi", "actual": "@org_apache_maven_resolver_maven_resolver_spi//jar", "bind": "jar/org/apache/maven/resolver/maven_resolver_spi"},
    {"artifact": "org.apache.maven.resolver:maven-resolver-util:1.1.0", "lang": "java", "sha1": "324ea16128d474638340940775ecf5c9c28da2fc", "sha256": "574069e1ed649c11daa7a2ed1f2f53ef605b5afd31a3f78dc6443eec7844b5b9", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-util/1.1.0/maven-resolver-util-1.1.0.jar", "source": {"sha1": "3a1d7b8096387978cc6a96f2cacc8d6ecb68da70", "sha256": "750ff36ed560b11442ba1ac4875d4d2fc6b9f61559f508d32bfb29175aa3cfd3", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/resolver/maven-resolver-util/1.1.0/maven-resolver-util-1.1.0-sources.jar"} , "name": "org_apache_maven_resolver_maven_resolver_util", "actual": "@org_apache_maven_resolver_maven_resolver_util//jar", "bind": "jar/org/apache/maven/resolver/maven_resolver_util"},
    {"artifact": "org.apache.maven.shared:maven-shared-utils:3.1.0", "lang": "java", "sha1": "78d8798fe84d5e095577221d299e9a3c8e696bca", "sha256": "88e5334c4c29a6e81c74a1d814c54a9a3b1e4fc6560a95da196fe16928095471", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/3.1.0/maven-shared-utils-3.1.0.jar", "source": {"sha1": "0a629c40920dd9471841f2cd11e1a146103f9bd9", "sha256": "0ac6c79e41fc198bafbf4b7fc029c88676d29bf139687188782fcab8ced9506c", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/3.1.0/maven-shared-utils-3.1.0-sources.jar"} , "name": "org_apache_maven_shared_maven_shared_utils", "actual": "@org_apache_maven_shared_maven_shared_utils//jar", "bind": "jar/org/apache/maven/shared/maven_shared_utils"},
    {"artifact": "org.apache.maven:maven-artifact:3.5.2", "lang": "java", "sha1": "df674758263619cb16e053fd99ef1d2ae66de4c2", "sha256": "c636e3ee0531abda2e45ff956a1cdc93031b2ac4eacbe311fb3192b586d41e90", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-artifact/3.5.2/maven-artifact-3.5.2.jar", "source": {"sha1": "a3a14b9496052a7c3dd197d5828057b2135caa15", "sha256": "b031b675a6164b8c348fd0e2a1f59271e4186de8efe7b8e49a0c0ef794348cc7", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-artifact/3.5.2/maven-artifact-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_artifact", "actual": "@org_apache_maven_maven_artifact//jar", "bind": "jar/org/apache/maven/maven_artifact"},
    {"artifact": "org.apache.maven:maven-builder-support:3.5.2", "lang": "java", "sha1": "2b4a4fcf267c54f06aa408ac377ec17ac2a3aae9", "sha256": "6afb5a4aa64058086587f54651e5ba0e02f43886e721f32d3606f83dac26a676", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-builder-support/3.5.2/maven-builder-support-3.5.2.jar", "source": {"sha1": "e4ee0e61144d2465960d0e815a137c68623915aa", "sha256": "a1abc618d99f9b5f5d2650f3fa61c60410d2d6c827b0e298239f3e0225821aca", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-builder-support/3.5.2/maven-builder-support-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_builder_support", "actual": "@org_apache_maven_maven_builder_support//jar", "bind": "jar/org/apache/maven/maven_builder_support"},
    {"artifact": "org.apache.maven:maven-core:3.5.2", "lang": "java", "sha1": "b9f5f79d6b9e1635e4395b36a3dda5b93c96e7dd", "sha256": "0a286994fce9a4863d8eee0b36cb8352b46ce72b603bc137dff0aa5e5ac4271b", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-core/3.5.2/maven-core-3.5.2.jar", "source": {"sha1": "f0371486c4e6026e6d3b3100cc0d9f08650c12d1", "sha256": "d3081198ae246245f882cf8f910464658873dba4f1cd5caebd071f4ce5578eeb", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-core/3.5.2/maven-core-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_core", "actual": "@org_apache_maven_maven_core//jar", "bind": "jar/org/apache/maven/maven_core"},
    {"artifact": "org.apache.maven:maven-model-builder:3.5.2", "lang": "java", "sha1": "897371c4b42756f0418986397b0341b7c61dd01a", "sha256": "2582d832f0e6cd15e4d7e13f5489c10da686936e9fa3048b6c44d4145f3e6409", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-model-builder/3.5.2/maven-model-builder-3.5.2.jar", "source": {"sha1": "950463370e500bf8ce62b0499b7dc509e87cf9f3", "sha256": "5bbb61b75ea1844ed7106a473fd100e07af7d1374103e0a605f725ec7477bdca", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-model-builder/3.5.2/maven-model-builder-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_model_builder", "actual": "@org_apache_maven_maven_model_builder//jar", "bind": "jar/org/apache/maven/maven_model_builder"},
    {"artifact": "org.apache.maven:maven-model:3.5.2", "lang": "java", "sha1": "a19b0d3d16656c8152583b21c6d343dce67fb900", "sha256": "e2978746770e3356e8bcf04f7ee1c8cded1fcd94926ab71c6763810a99c1587e", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-model/3.5.2/maven-model-3.5.2.jar", "source": {"sha1": "e18c4435743c5f5037666c2bf575b0c383faaeb3", "sha256": "84ace8621c4382feafe871973dfd9eca98858ae28c784c346c6c90d081bfd581", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-model/3.5.2/maven-model-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_model", "actual": "@org_apache_maven_maven_model//jar", "bind": "jar/org/apache/maven/maven_model"},
    {"artifact": "org.apache.maven:maven-plugin-api:3.5.2", "lang": "java", "sha1": "2f4ada644a22568d57a5bb42d3858d4d222022e1", "sha256": "3a569e42fbdec2de7dd4515e7334fbcef13ff030c5e63aafd6796e5b92337fcf", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-plugin-api/3.5.2/maven-plugin-api-3.5.2.jar", "source": {"sha1": "23bbc39015936f82a8ffb7776082dedfb7a1843d", "sha256": "ec6853fae004dc6a244fdd8d8155fbd21838de2884106a2423b5f66e73128d99", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-plugin-api/3.5.2/maven-plugin-api-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_plugin_api", "actual": "@org_apache_maven_maven_plugin_api//jar", "bind": "jar/org/apache/maven/maven_plugin_api"},
    {"artifact": "org.apache.maven:maven-repository-metadata:3.5.2", "lang": "java", "sha1": "8fc7231cc7c48a3ae598af4c0674d7b229e57031", "sha256": "aa617fc40c260bf8d4933319890ac372cfdd442c8e3819b2e0edd6868d97a80c", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-repository-metadata/3.5.2/maven-repository-metadata-3.5.2.jar", "source": {"sha1": "2c8ec70bee27514d5dc7464fe84b4385aedcc11f", "sha256": "094814f5eed8f8e36cff04101c9a5a2e93a8b98ddfa60dbe0946fea557ac54e8", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-repository-metadata/3.5.2/maven-repository-metadata-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_repository_metadata", "actual": "@org_apache_maven_maven_repository_metadata//jar", "bind": "jar/org/apache/maven/maven_repository_metadata"},
    {"artifact": "org.apache.maven:maven-resolver-provider:3.5.2", "lang": "java", "sha1": "2c18f3c1d082a89caa9d587604f88107f687d2fb", "sha256": "5f2fcbc4c01e810803c2315e3b3ee88385488b78679dd44e75c1e00c125fe339", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-resolver-provider/3.5.2/maven-resolver-provider-3.5.2.jar", "source": {"sha1": "5cdf6423cf56e1c506ee9b3d61f940b8d6dcffd1", "sha256": "00a9bfa884f7a71ff75cdccb12889741f9312e983342f0be1f9fcb22afed19f8", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-resolver-provider/3.5.2/maven-resolver-provider-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_resolver_provider", "actual": "@org_apache_maven_maven_resolver_provider//jar", "bind": "jar/org/apache/maven/maven_resolver_provider"},
    {"artifact": "org.apache.maven:maven-settings-builder:3.5.2", "lang": "java", "sha1": "e7006511e6d7cfda332fe26889334cc7aad8cf5b", "sha256": "70e3f5cabe71f66723dea9b186dec7ec1389e4ba814bb289dfad15c78b878ec9", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-settings-builder/3.5.2/maven-settings-builder-3.5.2.jar", "source": {"sha1": "2959d2004f02cb5f0023b8349c5dce6772df3e26", "sha256": "c4cb7cb88bef58f5f0babccfc347dd090e8b12ecded5091fb74e4d9e7a956a3e", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-settings-builder/3.5.2/maven-settings-builder-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_settings_builder", "actual": "@org_apache_maven_maven_settings_builder//jar", "bind": "jar/org/apache/maven/maven_settings_builder"},
    {"artifact": "org.apache.maven:maven-settings:3.5.2", "lang": "java", "sha1": "fa3c3c96decf4c203f7cc669bd416336f5012a98", "sha256": "1e7f3d9172a90bc7df93928e4a8ff7019930328f883103d284f16665c8216173", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-settings/3.5.2/maven-settings-3.5.2.jar", "source": {"sha1": "7c51954d5b699e15b29a05a2e65e1709094046a2", "sha256": "5203798df0282d39cd3599492a6224f7dca04988a4de4354d515d166749103b4", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/apache/maven/maven-settings/3.5.2/maven-settings-3.5.2-sources.jar"} , "name": "org_apache_maven_maven_settings", "actual": "@org_apache_maven_maven_settings//jar", "bind": "jar/org/apache/maven/maven_settings"},
    {"artifact": "org.codehaus.plexus:plexus-archiver:2.2", "lang": "java", "sha1": "13e55f4c2b7cdbf59a9bbd668d3c058d1a40664b", "sha256": "9154a5e6e1f95a1c74d4254670fec8d7aacd5692115710fe7e1381636c6be38c", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-archiver/2.2/plexus-archiver-2.2.jar", "source": {"sha1": "39939a0345d1d911e2a3cece29e7bbaaaddd8e67", "sha256": "79eee7cfab424ab1400c54c85f3d69c148d13934a6ef3f2c6f61478acbe75581", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-archiver/2.2/plexus-archiver-2.2-sources.jar"} , "name": "org_codehaus_plexus_plexus_archiver", "actual": "@org_codehaus_plexus_plexus_archiver//jar", "bind": "jar/org/codehaus/plexus/plexus_archiver"},
    {"artifact": "org.codehaus.plexus:plexus-classworlds:2.5.2", "lang": "java", "sha1": "4abb111bfdace5b8167db4c0ef74644f3f88f142", "sha256": "b2931d41740490a8d931cbe0cfe9ac20deb66cca606e679f52522f7f534c9fd7", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-classworlds/2.5.2/plexus-classworlds-2.5.2.jar", "source": {"sha1": "8aea177d48dd9bdafe7c3b4116a604e8b3b1b52e", "sha256": "d087c4c0ff02b035111bb72c72603b2851d126c43da39cc3c73ff45139125bec", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-classworlds/2.5.2/plexus-classworlds-2.5.2-sources.jar"} , "name": "org_codehaus_plexus_plexus_classworlds", "actual": "@org_codehaus_plexus_plexus_classworlds//jar", "bind": "jar/org/codehaus/plexus/plexus_classworlds"},
# duplicates in org.codehaus.plexus:plexus-component-annotations promoted to 1.7.1
# - org.apache.maven:maven-core:3.5.2 wanted version 1.7.1
# - org.apache.maven:maven-model-builder:3.5.2 wanted version 1.7.1
# - org.apache.maven:maven-settings-builder:3.5.2 wanted version 1.7.1
# - org.eclipse.sisu:org.eclipse.sisu.plexus:0.3.3 wanted version 1.5.5
    {"artifact": "org.codehaus.plexus:plexus-component-annotations:1.7.1", "lang": "java", "sha1": "862abca6deff0fff241a835a33d22559e9132069", "sha256": "a7fee9435db716bff593e9fb5622bcf9f25e527196485929b0cd4065c43e61df", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-component-annotations/1.7.1/plexus-component-annotations-1.7.1.jar", "source": {"sha1": "91c49e8560cdc3e7bce65ac5fa4e37ee0f08f4d1", "sha256": "18999359e8c1c5eb1f17a06093ceffc21f84b62b4ee0d9ab82f2e10d11049a78", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-component-annotations/1.7.1/plexus-component-annotations-1.7.1-sources.jar"} , "name": "org_codehaus_plexus_plexus_component_annotations", "actual": "@org_codehaus_plexus_plexus_component_annotations//jar", "bind": "jar/org/codehaus/plexus/plexus_component_annotations"},
    {"artifact": "org.codehaus.plexus:plexus-container-default:1.0-alpha-9-stable-1", "lang": "java", "sha1": "94aea3010e250a334d9dab7f591114cd6c767458", "sha256": "7c758612888782ccfe376823aee7cdcc7e0cdafb097f7ef50295a0b0c3a16edf", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-container-default/1.0-alpha-9-stable-1/plexus-container-default-1.0-alpha-9-stable-1.jar", "source": {"sha1": "e9b8cdfbdca8f10c3d86f8c9ef16ab231bc379a7", "sha256": "330e1bf490259c64fd6b27097adb8d41159500f849743b8680f3515044483d62", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-container-default/1.0-alpha-9-stable-1/plexus-container-default-1.0-alpha-9-stable-1-sources.jar"} , "name": "org_codehaus_plexus_plexus_container_default", "actual": "@org_codehaus_plexus_plexus_container_default//jar", "bind": "jar/org/codehaus/plexus/plexus_container_default"},
    {"artifact": "org.codehaus.plexus:plexus-interpolation:1.24", "lang": "java", "sha1": "ff3f217127fbd6846bba831ab156e227db2cf347", "sha256": "8fe2be04b067a75d02fb8a1a9caf6c1c8615f0d5577cced02e90b520763d2f77", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-interpolation/1.24/plexus-interpolation-1.24.jar", "source": {"sha1": "68c7adeee12d3b9985695a46f6b92df280c809a5", "sha256": "0b372b91236c4a2c63dc0d6b2010e10c98b993fc8491f6a02b73052a218b6644", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-interpolation/1.24/plexus-interpolation-1.24-sources.jar"} , "name": "org_codehaus_plexus_plexus_interpolation", "actual": "@org_codehaus_plexus_plexus_interpolation//jar", "bind": "jar/org/codehaus/plexus/plexus_interpolation"},
    {"artifact": "org.codehaus.plexus:plexus-io:2.0.4", "lang": "java", "sha1": "dc773899dfb3f857411ef49db46f17d7a465a634", "sha256": "58f2898b70709f1216fa3afe69e0a7cdb41ad6a3927b2507a4a89941c9e4ab76", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-io/2.0.4/plexus-io-2.0.4.jar", "source": {"sha1": "8c0f15ec81934da7ef02c86785672073a7cab9c8", "sha256": "0f686106cef923951870a5fdeb6590046fc96c191abb0dc554ce0c7fccbc7be0", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-io/2.0.4/plexus-io-2.0.4-sources.jar"} , "name": "org_codehaus_plexus_plexus_io", "actual": "@org_codehaus_plexus_plexus_io//jar", "bind": "jar/org/codehaus/plexus/plexus_io"},
# duplicates in org.codehaus.plexus:plexus-utils promoted to 3.1.0
# - org.apache.maven:maven-artifact:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-core:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-model-builder:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-model:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-plugin-api:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-repository-metadata:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-resolver-provider:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-settings-builder:3.5.2 wanted version 3.1.0
# - org.apache.maven:maven-settings:3.5.2 wanted version 3.1.0
# - org.codehaus.plexus:plexus-archiver:2.2 wanted version 3.0
# - org.codehaus.plexus:plexus-container-default:1.0-alpha-9-stable-1 wanted version 1.0.4
# - org.codehaus.plexus:plexus-io:2.0.4 wanted version 3.0
# - org.eclipse.sisu:org.eclipse.sisu.plexus:0.3.3 wanted version 3.0.17
# - org.sonatype.plexus:plexus-sec-dispatcher:1.4 wanted version 1.5.5
    {"artifact": "org.codehaus.plexus:plexus-utils:3.1.0", "lang": "java", "sha1": "60eecb6f15abdb1c653ad80abaac6fe188b3feaa", "sha256": "0ffa0ad084ebff5712540a7b7ea0abda487c53d3a18f78c98d1a3675dab9bf61", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-utils/3.1.0/plexus-utils-3.1.0.jar", "source": {"sha1": "d4062783613c4c6cf909c939d15fd1f2b584db03", "sha256": "06eb127e188a940ebbcf340c43c95537c3052298acdc943a9b2ec2146c7238d9", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/codehaus/plexus/plexus-utils/3.1.0/plexus-utils-3.1.0-sources.jar"} , "name": "org_codehaus_plexus_plexus_utils", "actual": "@org_codehaus_plexus_plexus_utils//jar", "bind": "jar/org/codehaus/plexus/plexus_utils"},
    {"artifact": "org.eclipse.sisu:org.eclipse.sisu.inject:0.3.3", "lang": "java", "sha1": "b163fc1e714db5f9b389ec11f11950b5913e454c", "sha256": "c6935e0b7d362ed4ca768c9b71d5d4d98788ff0a79c0d2bb954c221a078b166b", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/eclipse/sisu/org.eclipse.sisu.inject/0.3.3/org.eclipse.sisu.inject-0.3.3.jar", "source": {"sha1": "7c0d2c13af6013cbea45f359e72835acf86dddd8", "sha256": "1f4d2575cb004f3fbd8e687c5dfa42d7478e1cf98e0cafa6e22dd1304cdfc6d7", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/eclipse/sisu/org.eclipse.sisu.inject/0.3.3/org.eclipse.sisu.inject-0.3.3-sources.jar"} , "name": "org_eclipse_sisu_org_eclipse_sisu_inject", "actual": "@org_eclipse_sisu_org_eclipse_sisu_inject//jar", "bind": "jar/org/eclipse/sisu/org_eclipse_sisu_inject"},
    {"artifact": "org.eclipse.sisu:org.eclipse.sisu.plexus:0.3.3", "lang": "java", "sha1": "2c892c1fe0cd2dabcc729e1cbff3524b4847b1fe", "sha256": "98045f5ecd802d6a96ba00394f8cb61259f9ac781ec2cb51ca0cb7b2c94ac720", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/eclipse/sisu/org.eclipse.sisu.plexus/0.3.3/org.eclipse.sisu.plexus-0.3.3.jar", "source": {"sha1": "f49542fe54c999baaffedffee925ed3742a8dae6", "sha256": "349dd64dca9d0007d7037862759fd8f74c7a0a5de29cfa1cec1ae2fb25eaa49b", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/eclipse/sisu/org.eclipse.sisu.plexus/0.3.3/org.eclipse.sisu.plexus-0.3.3-sources.jar"} , "name": "org_eclipse_sisu_org_eclipse_sisu_plexus", "actual": "@org_eclipse_sisu_org_eclipse_sisu_plexus//jar", "bind": "jar/org/eclipse/sisu/org_eclipse_sisu_plexus"},
    {"artifact": "org.sonatype.plexus:plexus-cipher:1.4", "lang": "java", "sha1": "50ade46f23bb38cd984b4ec560c46223432aac38", "sha256": "5a15fdba22669e0fdd06e10dcce6320879e1f7398fbc910cd0677b50672a78c4", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar", "source": {"sha1": "fd919edf211954e41e0e60bc8b042bd2e1e5cc1f", "sha256": "35291f96baa7d430cac96e15862e288527e08689b6f30c4f39482f19962ea540", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4-sources.jar"} , "name": "org_sonatype_plexus_plexus_cipher", "actual": "@org_sonatype_plexus_plexus_cipher//jar", "bind": "jar/org/sonatype/plexus/plexus_cipher"},
    {"artifact": "org.sonatype.plexus:plexus-sec-dispatcher:1.4", "lang": "java", "sha1": "43fde524e9b94c883727a9fddb8669181b890ea7", "sha256": "da73e32b58132e64daf12269fd9d011c0b303f234840f179908725a632b6b57c", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.jar", "source": {"sha1": "8c733f36027854485e48fc6a8bbbd7bedb86d4a0", "sha256": "2e5de23342c1f130b0244149dee6aaae7f2d4f5ff668e329265cea08830d39c2", "repository": "http://central.maven.org/maven2/", "url": "http://central.maven.org/maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4-sources.jar"} , "name": "org_sonatype_plexus_plexus_sec_dispatcher", "actual": "@org_sonatype_plexus_plexus_sec_dispatcher//jar", "bind": "jar/org/sonatype/plexus/plexus_sec_dispatcher"},
    ]

def maven_dependencies(callback = jar_artifact_callback):
    for hash in list_dependencies():
        callback(hash)
