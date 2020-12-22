# Migration of testsuite from Maven to Bazel

This is a tool to automate the migration of the test cases
from jflex-testsuite-maven-plugin to bazel.


## How to migrate

Example: To migrate caseless-jflex

### Create a working branch
```sh
git co master
git pull
git co -b bzl-migrate-vaseless-jflex
```

### Run the automatic migration
```
bazel run java/jflex/migration:migrator -- ~/Projects/jflex/testsuite/testcases/src/test/cases/caseless-jflex
cp -r /tmp/caseless_jflex ~/Projects/jflex/javatests/de/jflex/testcase
```

### Verify the migrated test passes
```
bazel test //javatests/de/jflex/testcase/...
```

# Send for review
```
git commit -a
git push
```

## BUILD targets

### migrator

The final binary you care about.

### migration

The migation library.

### gen_test_loader

Generates the TestLoader, reading `.test` files.
This is now only loading the model. All business logic to
parse or compile has been removed from the `.flex` spec.

### velocity_templates

Velocity templates to generate:

- BUILD file
- _Test_ GoldenTest.java

### model

The data model for a `TestCase`.

### test_spec_scanner

Parser of a `.test` file that returns a `TestCase`.

