# Contribution
## Contributions are welcome

JFlex is free software and contributions are welcome.

The preferred way to contribute code changes or fixes is via github pull
requests. Before you send a pull request, please make sure that unit tests and
regression test suite pass.

## Code style

JFlex follows the [Google-style][google-style] for Java.

This is enforced by the [fmt-maven-plugin][fmt-maven-plugin].

You can also use a configuration style

* [Google style for Eclipse][google-style-eclipse]
* [Google style for IntelliJ][google-style-intellij]

## Setting up your environment

We provide help on how to set up your
[development environment](https://github.com/jflex-de/jflex/wiki/Development-environment).

## Running the test suites

### Short version

    ./run-tests

This runs the entire test suites, including unit and regression tests, as
well as examples.

### Longer version

Unit tests are run automatically if you locally install the jflex package for
instance via

    mvn -N install
    mvn install

The regression test suite must be run from the directory
`testsuite/testcases`, for instance with

    mvn test

The jflex/examples can be run individually either by calling `make` or `mvn
test`.


## Get in touch

If you have larger code changes or new features to contribute it is usually a
good idea to get in touch with the developers first and discuss if your idea
fits with the rest of the JFlex design. You can get in contact either on the
github issue tracking system or the [JFlex users mailing list][ml].

[ml]: http://jflex.de/mailing.html
[fmt-maven-plugin]: https://github.com/coveo/fmt-maven-plugin
[google-style]: https://github.com/google/styleguide/
[google-style-eclipse]: https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml
[google-style-intellij]: https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml

