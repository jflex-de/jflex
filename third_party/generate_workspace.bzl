# The following dependencies were calculated from:
#
# generate_workspace --maven_project=jflex/examples/simple --repositories=http://jcenter.bintray.com


def generated_maven_jars():
  # com.google.guava:guava:bundle:22.0-android
  native.maven_jar(
      name = "com_google_code_findbugs_jsr305",
      artifact = "com.google.code.findbugs:jsr305:1.3.9",
      repository = "http://jcenter.bintray.com/",
      sha1 = "40719ea6961c0cb6afaeb6a921eaa1f6afd4cfdf",
  )


  # com.google.guava:guava:bundle:22.0-android
  native.maven_jar(
      name = "org_codehaus_mojo_animal_sniffer_annotations",
      artifact = "org.codehaus.mojo:animal-sniffer-annotations:1.14",
      repository = "http://jcenter.bintray.com/",
      sha1 = "775b7e22fb10026eed3f86e8dc556dfafe35f2d5",
  )


  # pom.xml got requested version
  # de.flex.examples:simple:jar:1.0
  native.maven_jar(
      name = "com_google_truth_truth",
      artifact = "com.google.truth:truth:0.36",
      repository = "http://jcenter.bintray.com/",
      sha1 = "7485219d2c1d341097a19382c02bde07e69ff5d2",
  )


  # com.google.guava:guava:bundle:22.0-android
  # com.google.truth:truth:jar:0.36 wanted version 2.0.19
  native.maven_jar(
      name = "com_google_errorprone_error_prone_annotations",
      artifact = "com.google.errorprone:error_prone_annotations:2.0.18",
      repository = "http://jcenter.bintray.com/",
      sha1 = "5f65affce1684999e2f4024983835efc3504012e",
  )


  # com.google.truth:truth:jar:0.36
  native.maven_jar(
      name = "com_google_guava_guava",
      artifact = "com.google.guava:guava:22.0-android",
      repository = "http://jcenter.bintray.com/",
      sha1 = "7b2fc7ef242f35bc2c9a3b816ed846c9345e3f95",
  )


  # pom.xml got requested version
  # de.flex.examples:simple:jar:1.0
  # com.google.truth:truth:jar:0.36 wanted version 4.12
  native.maven_jar(
      name = "junit_junit",
      artifact = "junit:junit:3.8.2",
      repository = "http://jcenter.bintray.com/",
      sha1 = "07e4cde26b53a9a0e3fe5b00d1dbbc7cc1d46060",
  )


  # com.google.guava:guava:bundle:22.0-android
  native.maven_jar(
      name = "com_google_j2objc_j2objc_annotations",
      artifact = "com.google.j2objc:j2objc-annotations:1.1",
      repository = "http://jcenter.bintray.com/",
      sha1 = "976d8d30bebc251db406f2bdb3eb01962b5685b3",
  )




def generated_java_libraries():
  native.java_library(
      name = "com_google_code_findbugs_jsr305",
      visibility = ["//visibility:public"],
      exports = ["@com_google_code_findbugs_jsr305//jar"],
  )


  native.java_library(
      name = "org_codehaus_mojo_animal_sniffer_annotations",
      visibility = ["//visibility:public"],
      exports = ["@org_codehaus_mojo_animal_sniffer_annotations//jar"],
  )


  native.java_library(
      name = "com_google_truth_truth",
      visibility = ["//visibility:public"],
      exports = ["@com_google_truth_truth//jar"],
      runtime_deps = [
          ":com_google_code_findbugs_jsr305",
          ":com_google_errorprone_error_prone_annotations",
          ":com_google_guava_guava",
          ":com_google_j2objc_j2objc_annotations",
          ":junit_junit",
          ":org_codehaus_mojo_animal_sniffer_annotations",
      ],
  )


  native.java_library(
      name = "com_google_errorprone_error_prone_annotations",
      visibility = ["//visibility:public"],
      exports = ["@com_google_errorprone_error_prone_annotations//jar"],
  )


  native.java_library(
      name = "com_google_guava_guava",
      visibility = ["//visibility:public"],
      exports = ["@com_google_guava_guava//jar"],
      runtime_deps = [
          ":com_google_code_findbugs_jsr305",
          ":com_google_errorprone_error_prone_annotations",
          ":com_google_j2objc_j2objc_annotations",
          ":org_codehaus_mojo_animal_sniffer_annotations",
      ],
  )


  native.java_library(
      name = "junit_junit",
      visibility = ["//visibility:public"],
      exports = ["@junit_junit//jar"],
  )


  native.java_library(
      name = "com_google_j2objc_j2objc_annotations",
      visibility = ["//visibility:public"],
      exports = ["@com_google_j2objc_j2objc_annotations//jar"],
  )


