# The following dependencies were calculated from:
#
# generate_workspace --maven_project=/usr/local/google/home/regisd/Projects/jflex/jflex-maven-plugin/effective-pom.xml --repositories=https://jcenter.bintray.com


def generated_maven_jars():
  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.apache.maven:maven-core:jar:3.5.2
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_model_builder",
      artifact = "org.apache.maven:maven-model-builder:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "897371c4b42756f0418986397b0341b7c61dd01a",
  )


  # org.apache.maven:maven-core:jar:3.5.2
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_settings_builder",
      artifact = "org.apache.maven:maven-settings-builder:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "e7006511e6d7cfda332fe26889334cc7aad8cf5b",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-settings-builder:jar:3.5.2
  native.maven_jar(
      name = "org_apache_maven_maven_builder_support",
      artifact = "org.apache.maven:maven-builder-support:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "2b4a4fcf267c54f06aa408ac377ec17ac2a3aae9",
  )


  # com.google.inject:guice:jar:4.0
  native.maven_jar(
      name = "aopalliance_aopalliance",
      artifact = "aopalliance:aopalliance:1.0",
      repository = "http://repository.jboss.org/maven2/",
      sha1 = "0235ba8b489512805ac13a8f9ea77a1ca5ebe3e8",
  )


  # org.codehaus.plexus:plexus-archiver:jar:2.2
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_container_default",
      artifact = "org.codehaus.plexus:plexus-container-default:1.0-alpha-9-stable-1",
      repository = "http://repository.jboss.org/maven2/",
      sha1 = "94aea3010e250a334d9dab7f591114cd6c767458",
  )


  # javax.enterprise:cdi-api:jar:1.0
  native.maven_jar(
      name = "javax_annotation_jsr250_api",
      artifact = "javax.annotation:jsr250-api:1.0",
      repository = "http://repository.jboss.org/maven2/",
      sha1 = "5025422767732a1ab45d93abfea846513d742dcf",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  # org.apache.maven.shared:maven-shared-utils:jar:3.1.0 wanted version 2.5
  # org.apache.maven.plugin-testing:maven-plugin-testing-harness:jar:3.3.0 wanted version 2.2
  native.maven_jar(
      name = "commons_io_commons_io",
      artifact = "commons-io:commons-io:2.6",
      repository = "https://jcenter.bintray.com/",
      sha1 = "815893df5f31da2ece4040fe0a12fd44b577afaf",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-resolver-provider:jar:3.5.2
  # org.apache.maven.resolver:maven-resolver-impl:jar:1.1.0 got requested version
  # org.apache.maven.resolver:maven-resolver-util:jar:1.1.0 got requested version
  # org.apache.maven.resolver:maven-resolver-spi:jar:1.1.0 got requested version
  native.maven_jar(
      name = "org_apache_maven_resolver_maven_resolver_api",
      artifact = "org.apache.maven.resolver:maven-resolver-api:1.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "71ab206c1bfb522f9bbb4d9dd5641ec732ffb995",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_plugin_api",
      artifact = "org.apache.maven:maven-plugin-api:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "2f4ada644a22568d57a5bb42d3858d4d222022e1",
  )


  # org.apache.maven:maven-core:jar:3.5.2
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_resolver_provider",
      artifact = "org.apache.maven:maven-resolver-provider:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "2c18f3c1d082a89caa9d587604f88107f687d2fb",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.eclipse.sisu:org.eclipse.sisu.plexus:eclipse-plugin:0.3.3
  native.maven_jar(
      name = "org_eclipse_sisu_org_eclipse_sisu_inject",
      artifact = "org.eclipse.sisu:org.eclipse.sisu.inject:0.3.3",
      repository = "https://jcenter.bintray.com/",
      sha1 = "b163fc1e714db5f9b389ec11f11950b5913e454c",
  )


  # org.sonatype.plexus:plexus-sec-dispatcher:jar:1.4
  native.maven_jar(
      name = "org_sonatype_plexus_plexus_cipher",
      artifact = "org.sonatype.plexus:plexus-cipher:1.4",
      repository = "https://jcenter.bintray.com/",
      sha1 = "50ade46f23bb38cd984b4ec560c46223432aac38",
  )


  # org.apache.maven:maven-settings-builder:jar:3.5.2
  native.maven_jar(
      name = "org_sonatype_plexus_plexus_sec_dispatcher",
      artifact = "org.sonatype.plexus:plexus-sec-dispatcher:1.4",
      repository = "https://jcenter.bintray.com/",
      sha1 = "43fde524e9b94c883727a9fddb8669181b890ea7",
  )


  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.apache.maven:maven-core:jar:3.5.2
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_repository_metadata",
      artifact = "org.apache.maven:maven-repository-metadata:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "8fc7231cc7c48a3ae598af4c0674d7b229e57031",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-resolver-provider:jar:3.5.2
  # org.apache.maven.resolver:maven-resolver-impl:jar:1.1.0 got requested version
  native.maven_jar(
      name = "org_apache_maven_resolver_maven_resolver_util",
      artifact = "org.apache.maven.resolver:maven-resolver-util:1.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "324ea16128d474638340940775ecf5c9c28da2fc",
  )


  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-plugin-api:jar:3.5.2
  native.maven_jar(
      name = "org_apache_maven_maven_model",
      artifact = "org.apache.maven:maven-model:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "a19b0d3d16656c8152583b21c6d343dce67fb900",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-plugin-api:jar:3.5.2
  # org.apache.maven.plugin-tools:maven-plugin-annotations:jar:3.5 wanted version 3.0
  native.maven_jar(
      name = "org_apache_maven_maven_artifact",
      artifact = "org.apache.maven:maven-artifact:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "df674758263619cb16e053fd99ef1d2ae66de4c2",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-resolver-provider:jar:3.5.2
  # org.apache.maven.resolver:maven-resolver-impl:jar:1.1.0 got requested version
  native.maven_jar(
      name = "org_apache_maven_resolver_maven_resolver_spi",
      artifact = "org.apache.maven.resolver:maven-resolver-spi:1.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "6ba3b964448bd43d87a7452cc67afb3b104091ed",
  )


  # org.apache.maven:maven-core:jar:3.5.2
  native.maven_jar(
      name = "com_google_inject_guice",
      artifact = "com.google.inject:guice:4.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "0f990a43d3725781b6db7cd0acf0a8b62dfd1649",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  native.maven_jar(
      name = "org_apache_maven_plugin_tools_maven_plugin_annotations",
      artifact = "org.apache.maven.plugin-tools:maven-plugin-annotations:3.5",
      repository = "https://jcenter.bintray.com/",
      sha1 = "403ab542cc88975c9fb41c0fba893d5b0d028c97",
  )


  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-settings-builder:jar:3.5.2
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_interpolation",
      artifact = "org.codehaus.plexus:plexus-interpolation:1.24",
      repository = "https://jcenter.bintray.com/",
      sha1 = "ff3f217127fbd6846bba831ab156e227db2cf347",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_core",
      artifact = "org.apache.maven:maven-core:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "b9f5f79d6b9e1635e4395b36a3dda5b93c96e7dd",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  native.maven_jar(
      name = "org_apache_maven_maven_compat",
      artifact = "org.apache.maven:maven-compat:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "31bd327fe4eb6c841f4ea1af235e4fb2fbc6cf5d",
  )


  # de.jflex:jflex:jar:1.6.1
  native.maven_jar(
      name = "org_apache_ant_ant",
      artifact = "org.apache.ant:ant:1.7.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "9746af1a485e50cf18dcb232489032a847067066",
  )


  # org.codehaus.plexus:plexus-container-default:jar:1.0-alpha-9-stable-1
  native.maven_jar(
      name = "junit_junit",
      artifact = "junit:junit:3.8.1",
      repository = "http://repository.jboss.org/maven2/",
      sha1 = "99129f16442844f6a4a11ae22fbbee40b14d774f",
  )


  # org.apache.maven:maven-compat:jar:3.5.2 wanted version 1.7.1
  # org.eclipse.sisu:org.eclipse.sisu.plexus:eclipse-plugin:0.3.3
  # org.apache.maven:maven-settings-builder:jar:3.5.2 wanted version 1.7.1
  # org.apache.maven:maven-model-builder:jar:3.5.2 wanted version 1.7.1
  # org.apache.maven:maven-core:jar:3.5.2 wanted version 1.7.1
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_component_annotations",
      artifact = "org.codehaus.plexus:plexus-component-annotations:1.5.5",
      repository = "https://jcenter.bintray.com/",
      sha1 = "c72f2660d0cbed24246ddb55d7fdc4f7374d2078",
  )


  # org.codehaus.plexus:plexus-container-default:jar:1.0-alpha-9-stable-1 wanted version 1.0.4
  # org.apache.maven:maven-settings-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-plugin-api:jar:3.5.2 got requested version
  # org.apache.maven:maven-model:jar:3.5.2
  # org.eclipse.sisu:org.eclipse.sisu.plexus:eclipse-plugin:0.3.3 wanted version 3.0.17
  # org.apache.maven:maven-settings:jar:3.5.2 got requested version
  # org.sonatype.plexus:plexus-sec-dispatcher:jar:1.4 wanted version 1.5.5
  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.codehaus.plexus:plexus-io:jar:2.0.4 wanted version 3.0
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-artifact:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-repository-metadata:jar:3.5.2 got requested version
  # org.apache.maven.wagon:wagon-provider-api:jar:2.12 wanted version 3.0.24
  # org.codehaus.plexus:plexus-archiver:jar:2.2 wanted version 3.0.7
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_utils",
      artifact = "org.codehaus.plexus:plexus-utils:3.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "60eecb6f15abdb1c653ad80abaac6fe188b3feaa",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-plugin-api:jar:3.5.2
  native.maven_jar(
      name = "org_eclipse_sisu_org_eclipse_sisu_plexus",
      artifact = "org.eclipse.sisu:org.eclipse.sisu.plexus:0.3.3",
      repository = "https://jcenter.bintray.com/",
      sha1 = "2c892c1fe0cd2dabcc729e1cbff3524b4847b1fe",
  )


  # org.apache.ant:ant:jar:1.7.0
  native.maven_jar(
      name = "org_apache_ant_ant_launcher",
      artifact = "org.apache.ant:ant-launcher:1.7.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "e7e30789211e074aa70ef3eaea59bd5b22a7fa7a",
  )


  # org.apache.maven:maven-core:jar:3.5.2
  # org.apache.maven:maven-settings-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  native.maven_jar(
      name = "org_apache_maven_maven_settings",
      artifact = "org.apache.maven:maven-settings:3.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "fa3c3c96decf4c203f7cc669bd416336f5012a98",
  )


  # org.apache.maven:maven-compat:jar:3.5.2
  native.maven_jar(
      name = "org_apache_maven_wagon_wagon_provider_api",
      artifact = "org.apache.maven.wagon:wagon-provider-api:2.12",
      repository = "https://jcenter.bintray.com/",
      sha1 = "a19b458a40eac754198b2b3bf99a601feee1a37c",
  )


  # org.apache.maven:maven-core:jar:3.5.2
  native.maven_jar(
      name = "org_apache_maven_shared_maven_shared_utils",
      artifact = "org.apache.maven.shared:maven-shared-utils:3.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "78d8798fe84d5e095577221d299e9a3c8e696bca",
  )


  # org.apache.maven.plugin-testing:maven-plugin-testing-harness:jar:3.3.0
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_archiver",
      artifact = "org.codehaus.plexus:plexus-archiver:2.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "13e55f4c2b7cdbf59a9bbd668d3c058d1a40664b",
  )


  # org.codehaus.plexus:plexus-archiver:jar:2.2
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_io",
      artifact = "org.codehaus.plexus:plexus-io:2.0.4",
      repository = "https://jcenter.bintray.com/",
      sha1 = "dc773899dfb3f857411ef49db46f17d7a465a634",
  )


  # org.eclipse.sisu:org.eclipse.sisu.plexus:eclipse-plugin:0.3.3
  native.maven_jar(
      name = "javax_enterprise_cdi_api",
      artifact = "javax.enterprise:cdi-api:1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "44c453f60909dfc223552ace63e05c694215156b",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  native.maven_jar(
      name = "de_jflex_jflex_1_6_1",
      artifact = "de.jflex:jflex:1.6.1",
      repository = "https://jcenter.bintray.com/",
      sha1 = "eb4d51e1a8ea7ee96068905ddeceb9b28737c7eb",
  )


  # pom.xml got requested version
  # de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
  native.maven_jar(
      name = "org_apache_maven_plugin_testing_maven_plugin_testing_harness",
      artifact = "org.apache.maven.plugin-testing:maven-plugin-testing-harness:3.3.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "8ae849da486819e13c53d20a73cf467a56e1f1d1",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-compat:jar:3.5.2 got requested version
  # org.apache.maven:maven-resolver-provider:jar:3.5.2
  native.maven_jar(
      name = "org_apache_maven_resolver_maven_resolver_impl",
      artifact = "org.apache.maven.resolver:maven-resolver-impl:1.1.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "0c84e6576d50973c8e9e24e05fe2f12c1c9d4393",
  )


  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.apache.maven:maven-settings-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-builder-support:jar:3.5.2 got requested version
  # org.apache.maven:maven-artifact:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2 got requested version
  # org.apache.maven:maven-model:jar:3.5.2
  native.maven_jar(
      name = "org_apache_commons_commons_lang3",
      artifact = "org.apache.commons:commons-lang3:3.5",
      repository = "https://jcenter.bintray.com/",
      sha1 = "6c6c702c89bfff3cd9e80b04d668c5e190d588c6",
  )


  # org.apache.maven:maven-resolver-provider:jar:3.5.2 got requested version
  # org.apache.maven:maven-model-builder:jar:3.5.2
  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # com.google.inject:guice:jar:4.0 wanted version 16.0.1
  native.maven_jar(
      name = "com_google_guava_guava",
      artifact = "com.google.guava:guava:20.0",
      repository = "https://jcenter.bintray.com/",
      sha1 = "89507701249388e1ed5ddcf8c41f4ce1be7831ef",
  )


  # org.apache.maven:maven-core:jar:3.5.2 got requested version
  # org.apache.maven:maven-plugin-api:jar:3.5.2 got requested version
  # org.eclipse.sisu:org.eclipse.sisu.plexus:eclipse-plugin:0.3.3
  native.maven_jar(
      name = "org_codehaus_plexus_plexus_classworlds",
      artifact = "org.codehaus.plexus:plexus-classworlds:2.5.2",
      repository = "https://jcenter.bintray.com/",
      sha1 = "4abb111bfdace5b8167db4c0ef74644f3f88f142",
  )


  # org.codehaus.plexus:plexus-container-default:jar:1.0-alpha-9-stable-1
  native.maven_jar(
      name = "classworlds_classworlds",
      artifact = "classworlds:classworlds:1.1-alpha-2",
      repository = "http://repository.jboss.org/maven2/",
      sha1 = "05adf2e681c57d7f48038b602f3ca2254ee82d47",
  )




def generated_java_libraries():
  native.java_library(
      name = "org_apache_maven_maven_model_builder",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_model_builder//jar"],
      runtime_deps = [
          ":com_google_guava_guava",
          ":org_apache_commons_commons_lang3",
          ":org_apache_maven_maven_artifact",
          ":org_apache_maven_maven_builder_support",
          ":org_apache_maven_maven_model",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_interpolation",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_settings_builder",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_settings_builder//jar"],
      runtime_deps = [
          ":org_apache_commons_commons_lang3",
          ":org_apache_maven_maven_builder_support",
          ":org_apache_maven_maven_settings",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_interpolation",
          ":org_codehaus_plexus_plexus_utils",
          ":org_sonatype_plexus_plexus_cipher",
          ":org_sonatype_plexus_plexus_sec_dispatcher",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_builder_support",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_builder_support//jar"],
      runtime_deps = [
          ":org_apache_commons_commons_lang3",
      ],
  )


  native.java_library(
      name = "aopalliance_aopalliance",
      visibility = ["//visibility:public"],
      exports = ["@aopalliance_aopalliance//jar"],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_container_default",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_container_default//jar"],
      runtime_deps = [
          ":classworlds_classworlds",
          ":junit_junit",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "javax_annotation_jsr250_api",
      visibility = ["//visibility:public"],
      exports = ["@javax_annotation_jsr250_api//jar"],
  )


  native.java_library(
      name = "commons_io_commons_io",
      visibility = ["//visibility:public"],
      exports = ["@commons_io_commons_io//jar"],
  )


  native.java_library(
      name = "org_apache_maven_resolver_maven_resolver_api",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_resolver_maven_resolver_api//jar"],
  )


  native.java_library(
      name = "org_apache_maven_maven_plugin_api",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_plugin_api//jar"],
      runtime_deps = [
          ":javax_annotation_jsr250_api",
          ":javax_enterprise_cdi_api",
          ":org_apache_commons_commons_lang3",
          ":org_apache_maven_maven_artifact",
          ":org_apache_maven_maven_model",
          ":org_codehaus_plexus_plexus_classworlds",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_utils",
          ":org_eclipse_sisu_org_eclipse_sisu_inject",
          ":org_eclipse_sisu_org_eclipse_sisu_plexus",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_resolver_provider",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_resolver_provider//jar"],
      runtime_deps = [
          ":com_google_guava_guava",
          ":org_apache_commons_commons_lang3",
          ":org_apache_maven_maven_model",
          ":org_apache_maven_maven_model_builder",
          ":org_apache_maven_maven_repository_metadata",
          ":org_apache_maven_resolver_maven_resolver_api",
          ":org_apache_maven_resolver_maven_resolver_impl",
          ":org_apache_maven_resolver_maven_resolver_spi",
          ":org_apache_maven_resolver_maven_resolver_util",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_eclipse_sisu_org_eclipse_sisu_inject",
      visibility = ["//visibility:public"],
      exports = ["@org_eclipse_sisu_org_eclipse_sisu_inject//jar"],
  )


  native.java_library(
      name = "org_sonatype_plexus_plexus_cipher",
      visibility = ["//visibility:public"],
      exports = ["@org_sonatype_plexus_plexus_cipher//jar"],
  )


  native.java_library(
      name = "org_sonatype_plexus_plexus_sec_dispatcher",
      visibility = ["//visibility:public"],
      exports = ["@org_sonatype_plexus_plexus_sec_dispatcher//jar"],
      runtime_deps = [
          ":org_codehaus_plexus_plexus_utils",
          ":org_sonatype_plexus_plexus_cipher",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_repository_metadata",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_repository_metadata//jar"],
      runtime_deps = [
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_resolver_maven_resolver_util",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_resolver_maven_resolver_util//jar"],
      runtime_deps = [
          ":org_apache_maven_resolver_maven_resolver_api",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_model",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_model//jar"],
      runtime_deps = [
          ":org_apache_commons_commons_lang3",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_artifact",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_artifact//jar"],
      runtime_deps = [
          ":org_apache_commons_commons_lang3",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_resolver_maven_resolver_spi",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_resolver_maven_resolver_spi//jar"],
      runtime_deps = [
          ":org_apache_maven_resolver_maven_resolver_api",
      ],
  )


  native.java_library(
      name = "com_google_inject_guice",
      visibility = ["//visibility:public"],
      exports = ["@com_google_inject_guice//jar"],
      runtime_deps = [
          ":aopalliance_aopalliance",
          ":com_google_guava_guava",
      ],
  )


  native.java_library(
      name = "org_apache_maven_plugin_tools_maven_plugin_annotations",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_plugin_tools_maven_plugin_annotations//jar"],
      runtime_deps = [
          ":org_apache_maven_maven_artifact",
      ],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_interpolation",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_interpolation//jar"],
  )


  native.java_library(
      name = "org_apache_maven_maven_core",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_core//jar"],
      runtime_deps = [
          ":aopalliance_aopalliance",
          ":com_google_guava_guava",
          ":com_google_inject_guice",
          ":commons_io_commons_io",
          ":org_apache_commons_commons_lang3",
          ":org_apache_maven_maven_artifact",
          ":org_apache_maven_maven_builder_support",
          ":org_apache_maven_maven_model",
          ":org_apache_maven_maven_model_builder",
          ":org_apache_maven_maven_plugin_api",
          ":org_apache_maven_maven_repository_metadata",
          ":org_apache_maven_maven_resolver_provider",
          ":org_apache_maven_maven_settings",
          ":org_apache_maven_maven_settings_builder",
          ":org_apache_maven_resolver_maven_resolver_api",
          ":org_apache_maven_resolver_maven_resolver_impl",
          ":org_apache_maven_resolver_maven_resolver_spi",
          ":org_apache_maven_resolver_maven_resolver_util",
          ":org_apache_maven_shared_maven_shared_utils",
          ":org_codehaus_plexus_plexus_classworlds",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_interpolation",
          ":org_codehaus_plexus_plexus_utils",
          ":org_eclipse_sisu_org_eclipse_sisu_inject",
          ":org_eclipse_sisu_org_eclipse_sisu_plexus",
          ":org_sonatype_plexus_plexus_cipher",
          ":org_sonatype_plexus_plexus_sec_dispatcher",
      ],
  )


  native.java_library(
      name = "org_apache_maven_maven_compat",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_compat//jar"],
      runtime_deps = [
          ":org_apache_maven_maven_artifact",
          ":org_apache_maven_maven_core",
          ":org_apache_maven_maven_model",
          ":org_apache_maven_maven_model_builder",
          ":org_apache_maven_maven_repository_metadata",
          ":org_apache_maven_maven_resolver_provider",
          ":org_apache_maven_maven_settings",
          ":org_apache_maven_maven_settings_builder",
          ":org_apache_maven_resolver_maven_resolver_api",
          ":org_apache_maven_resolver_maven_resolver_impl",
          ":org_apache_maven_resolver_maven_resolver_util",
          ":org_apache_maven_wagon_wagon_provider_api",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_interpolation",
          ":org_codehaus_plexus_plexus_utils",
          ":org_eclipse_sisu_org_eclipse_sisu_plexus",
      ],
  )


  native.java_library(
      name = "org_apache_ant_ant",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_ant_ant//jar"],
      runtime_deps = [
          ":org_apache_ant_ant_launcher",
      ],
  )


  native.java_library(
      name = "junit_junit",
      visibility = ["//visibility:public"],
      exports = ["@junit_junit//jar"],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_component_annotations",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_component_annotations//jar"],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_utils",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_utils//jar"],
  )


  native.java_library(
      name = "org_eclipse_sisu_org_eclipse_sisu_plexus",
      visibility = ["//visibility:public"],
      exports = ["@org_eclipse_sisu_org_eclipse_sisu_plexus//jar"],
      runtime_deps = [
          ":javax_annotation_jsr250_api",
          ":javax_enterprise_cdi_api",
          ":org_codehaus_plexus_plexus_classworlds",
          ":org_codehaus_plexus_plexus_component_annotations",
          ":org_codehaus_plexus_plexus_utils",
          ":org_eclipse_sisu_org_eclipse_sisu_inject",
      ],
  )


  native.java_library(
      name = "org_apache_ant_ant_launcher",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_ant_ant_launcher//jar"],
  )


  native.java_library(
      name = "org_apache_maven_maven_settings",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_maven_settings//jar"],
      runtime_deps = [
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_wagon_wagon_provider_api",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_wagon_wagon_provider_api//jar"],
      runtime_deps = [
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_shared_maven_shared_utils",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_shared_maven_shared_utils//jar"],
      runtime_deps = [
          ":commons_io_commons_io",
      ],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_archiver",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_archiver//jar"],
      runtime_deps = [
          ":classworlds_classworlds",
          ":junit_junit",
          ":org_codehaus_plexus_plexus_container_default",
          ":org_codehaus_plexus_plexus_io",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_io",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_io//jar"],
      runtime_deps = [
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "javax_enterprise_cdi_api",
      visibility = ["//visibility:public"],
      exports = ["@javax_enterprise_cdi_api//jar"],
      runtime_deps = [
          ":javax_annotation_jsr250_api",
      ],
  )


  native.java_library(
      name = "de_jflex_jflex",
      visibility = ["//visibility:public"],
      exports = ["@de_jflex_jflex_1_6_1//jar"],
      runtime_deps = [
          ":org_apache_ant_ant",
          ":org_apache_ant_ant_launcher",
      ],
  )


  native.java_library(
      name = "org_apache_maven_plugin_testing_maven_plugin_testing_harness",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_plugin_testing_maven_plugin_testing_harness//jar"],
      runtime_deps = [
          ":classworlds_classworlds",
          ":commons_io_commons_io",
          ":junit_junit",
          ":org_codehaus_plexus_plexus_archiver",
          ":org_codehaus_plexus_plexus_container_default",
          ":org_codehaus_plexus_plexus_io",
          ":org_codehaus_plexus_plexus_utils",
      ],
  )


  native.java_library(
      name = "org_apache_maven_resolver_maven_resolver_impl",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_maven_resolver_maven_resolver_impl//jar"],
      runtime_deps = [
          ":org_apache_maven_resolver_maven_resolver_api",
          ":org_apache_maven_resolver_maven_resolver_spi",
          ":org_apache_maven_resolver_maven_resolver_util",
      ],
  )


  native.java_library(
      name = "org_apache_commons_commons_lang3",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_commons_commons_lang3//jar"],
  )


  native.java_library(
      name = "com_google_guava_guava",
      visibility = ["//visibility:public"],
      exports = ["@com_google_guava_guava//jar"],
  )


  native.java_library(
      name = "org_codehaus_plexus_plexus_classworlds",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_plexus_plexus_classworlds//jar"],
  )


  native.java_library(
      name = "classworlds_classworlds",
      visibility = ["//visibility:public"],
      exports = ["@classworlds_classworlds//jar"],
  )


