Benchmarking JFlex
==================

This directory is work in progress on a small performance benchmarking
suite for JFlex.

Main ideas
----------

 * use [JMH][1] as the benchmarking framework. There are good [technical articles][2] on the subtleties, and accessible [short][3] and slightly [longer tutorials][4].

 * main goal is to gather performance numbers on the scanning engine. There are multiple options for this:

   * micro benchmark on just the generated JFlex code + skeleton, as tightly as possible, without action code

   * macro end-to-end benchmark for a full scanner on realistic input (somehow eliminating file reading overhead etc, although it might be interesting to see if anything we do makes any difference once IO is present)

   * anything in between these two

 * run on current development snapshot

 * add generated scanners from previous versions of JFlex to track development over time

 * use something like java.util.regex and maybe JLex as baseline. Unclear if we can get a theoretical maximum performance.

 * at some point automate and auto-publish results

We could also benchmark various aspects of the generator itself, but so far
that is lower priority.

The plan is to start with a small micro benchmark and incrementally add from
there. This should eventually include profiling to at least be informed about
what we're actually measuring.

Open to ideas on any of this. Please comment on github issue [#689][github-issue] if you have opinions or would like to contribute.


Building and Running
---------------------

    mvn package

will build the benchmark and

    java -jar target/benchmark-full-1.8.0-SNAPSHOT.jar

will run it.



[1]: https://openjdk.java.net/projects/code-tools/jmh/
[2]: https://www.oracle.com/technical-resources/articles/java/architect-benchmarking.html
[3]: https://www.mkyong.com/java/java-jmh-benchmark-tutorial/
[4]: http://tutorials.jenkov.com/java-performance/jmh.html

[github-issue]: https://github.com/jflex-de/jflex/issues/698
