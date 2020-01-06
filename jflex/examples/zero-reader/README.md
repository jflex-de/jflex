Readers returning 0 characters
==============================

This example shows what to do about input Readers that sometimes return 0
characters.

**Short Version:**
Wrap your Reader in `ZeroReader` to get the behaviour of JFlex pre 1.6.1.


Background
----------

The specification for the method `int read(char[] cbuf, int off, int len)` in
`java.io.Reader` says:

    * Reads characters into a portion of an array.  This method will block
    * until some input is available, an I/O error occurs, or the end of the
    * stream is reached.

This means, the `read` method should never return 0. It should either block and
return more than 0 characters, should throw an exception, or should return -1
for end-of-stream. Unfortunately, not all implementations of this class follow
that protocol. Up to version 1.6.1, JFlex was trying to work around those
problems. This worked in most circumstances, but not in all, depending on how
exactly the Reader implementation violates the protocol. See also the
discussion on https://github.com/jflex-de/jflex/issues/131

From version 1.6.1, JFlex will not try to work around this issue any more, but
instead throw an IOException if 0 characters are returned. This makes the
problem explicit and solutions more robust.


Solutions
---------

The usual solution if you encounter such an IOException is to wrap the
offending Reader in your own Reader class that knows under which conditions the
original Reader returns 0.

For instance, for FilterReaders, it is usually enough to just request input
again when 0 was returned, until new input arrives. For other Readers, you may
have to request a specific number of characters to read to get any input. For
yet other situations, you may have to request a single character.


Example
-------

The example wrapper `ZeroReader` in this directory implements the workaround
that JFlex used previously: it first requests characters as usual, using the
`int read(char[], int, int)` method. If the result is 0, it requests a single
character using the `int read()` method. Because this method is supposed to
always return either a character or -1, most Readers implement it correctly by
blocking if no input is available yet (if `read()` returns 0, it means the
character 0 has been read, not that 0 characters have been read). This method
is usually too inefficient to use always, but 0-character returns tend to be
rare, so the impact of using it only in those cases is small.

Build
-----

### Build with maven

     mvn package
     java -jar target/zero-reader-1.0.jar <inputfiles>

e.g.

     java -jar target/zero-reader-1.0.jar src/test/data/test-input.txt

and expect `src/test/data/lexer-output.good`.

To test automatically:

    mvn test



### Build with ant

    ant test

To build and run the test.


### Build with make

    make test

To build and run the test.

