# Maven reports

This module aggregates reports from other modules.

The reason for this module is that:

- the Maven parent is executed first
- the reports are created in every module
- Maven doesn't run the reporting again on the parent.

In particular, this is used to aggregated code coverage of java tests.

