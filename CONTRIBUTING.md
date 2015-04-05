### Contributions are welcome

JFlex is free software and contributions are welcome.

The preferred way to contribute code changes or fixes is via github pull
requests. Before you send a pull request, please make sure that unit tests and
regression test suite pass.

### Setting up your environment

We provide help on how to set up your
[development environment](https://github.com/jflex-de/jflex/wiki/Development-environment).

### Running the test suites

**Short version:**

    ./run-tests

This runs the entire test suites, including unit and regression tests, as
well as examples.

**Longer version:**

Unit tests are run automatically if you locally install the jflex package for
instance via

    mvn3 install

The regression test suite must be run from the directory
`testsuite/testcases`, for instance with

    mvn3 test

The jflex/examples can be run individually either by calling `make` or `mvn
test`.


### Get in contact

If you have larger code changes or new features to contribute it is usually a
good idea to get in touch with the developers first and discuss if your idea
fits with the rest of the JFlex design. You can get in contact either on the
github issue tracking system or the [JFlex users mailing list][1].

[1]: http://jflex.de/mailing.html
