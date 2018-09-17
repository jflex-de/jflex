Encodings, Platforms, and Unicode {#sec:encodings}
=================================

This section discusses Unicode and encodings, cross platform scanning, and
how to deal with binary data.

The Problem
-----------

Java aims to be implementation platform independent, yet different platforms
use different ways to encode characters. Moreover, a file written on one
platform, say Windows, may later be read by a scanner on another platform,
for instance Linux.
 
If a program reads a file from disk, what it really reads is a stream of
bytes. These bytes can be mapped to characters in different ways. For
instance, in standard ASCII, the byte value 65 stands for the character `A`,
and in the encoding `iso-latin-1`, the byte value 213 stands for the umlaut
character `ä`, but in the encoding `iso-latin-2` <!-- FIXME: check --> the
value 213 is `é` instead. As long as one encoding is used consistently, this
is no problem. Some characters may not be available in the encoding you are
using, but at least the interpretation of the mapping between bytes and
characters agrees between different programs.

When your program runs on more than one platform, however, as is often the
case with Java, things become more complex. Java’s solution to this is to use
Unicode internally. Unicode aims to be able to represent all known character
sets and is therefore a perfect base for encoding things that might get used
all over the world and on different platforms. To make things work correctly,
you still have to know where you are and how to map byte values to Unicode
characters and vice versa, but the important thing is, that this mapping is
at least possible (you can map Kanji characters to Unicode, but you cannot
map them to ASCII or `iso-latin-1`).


Scanning text files
-------------------

Scanning text files is the standard application for scanners like JFlex.
Therefore it should also be the most convenient one. Most times it is.

The following scenario works fine: You work on a platform X, write your lexer
specification there, can use any obscure Unicode character in it as you like,
and compile the program. Your users work on any platform Y (possibly but not
necessarily something different from X), they write their input files on Y
and they run your program on Y. No problems.

Java does this as follows: If you want to read anything in Java that is
supposed to contain text, you use a `FileReader`, which converts the bytes of
the file into Unicode characters with the platform’s default encoding. If a
text file is produced on the same platform, the platform’s default encoding
should do the mapping correctly. Since JFlex also uses readers and Unicode
internally, this mechanism also works for the scanner specifications. If you
write an `A` in your text editor and the editor uses the platform’s encoding
(say `A` is 65), then Java translates this into the logical Unicode `A`
internally. If a user writes an `A` on a completely different platform (say
`A` is 237 there), then Java also translates this into the logical Unicode
`A` internally. Scanning is performed after that translation and both match.

Note that because of this mapping from bytes to characters, you should always
use the `%unicode` switch in you lexer specification if you want to scan text
files. `%8bit` may not be enough, even if you know that your platform only
uses one byte per character. The encoding `Cp1252` used on many Windows
machines for instance knows 256 characters, but the character `'` with
`Cp1252` code `\x92` has the Unicode value `\u2019`, which is larger than 255
and which would make your scanner throw an `ArrayIndexOutOfBoundsException`
if it is encountered.

So for the usual case you don’t have to do anything but use the `%unicode`
switch in your lexer specification.

Things may break when you produce a text file on platform X and consume it on
a different platform Y. Let’s say you have a file written on a Windows PC
using the encoding `Cp1252`. Then you move this file to a Linux PC with
encoding `ISO 8859-1` and there you run your scanner on it. Java now thinks
the file is encoded in `ISO 8859-1` (the platform’s default encoding) while
it really is encoded in `Cp1252`. For most characters `Cp1252` and `ISO
8859-1` are the same, but for the byte values `\x80` to `\x9f` they disagree:
`ISO 8859-1` is undefined there. You can fix the problem by telling Java
explicitly which encoding to use. When constructing the `InputStreamReader`,
you can give the encoding as argument. The line

    Reader r = new InputStreamReader(input, Cp1252);

will do the trick.

Of course the encoding to use can also come from the data itself: for
instance, when you scan an HTML page, it may have embedded information
about its character encoding in the headers.

More information about encodings, which ones are supported, how they are
called, and how to set them may be found in the official Java
documentation in the chapter about internationalisation. The link
<http://docs.oracle.com/javase/7/docs/technotes/guides/intl/>
leads to an online version of this for Oracle’s JDK 1.7.


Scanning binaries
-----------------

Scanning binaries is both easier and more difficult than scanning text files.
It’s easier because you want the raw bytes and not their meaning, i.e. you
don’t want any translation. It’s more difficult because it’s not so easy to
get "no translation" when you use Java readers.

The problem (for binaries) is that JFlex scanners are designed to work on
text. Therefore the interface is the `Reader` class. You can still get a
binary scanner when you write your own custom `InputStreamReader` class that
explicitly does no translation, but just copies byte values to character
codes instead. It sounds quite easy, and actually it is no big deal, but
there are a few pitfalls on the way. In the scanner specification you can
only enter positive character codes (for bytes that is `\x00` to `\xFF`).
Java’s `byte` type on the other hand is a signed 8 bit integer (-128 to 127),
so you have to convert them accordingly in your custom `Reader`. Also, you
should take care when you write your lexer spec: if you use text in there, it
gets interpreted by an encoding first, and what scanner you get as result
might depend on which platform you run JFlex on when you generate the scanner
(this is what you want for text, but for binaries it gets in the way). If you
are not sure, or if the development platform might change, it’s probably best
to use character code escapes in all places, since they don’t change their
meaning.


Conformance with Unicode Regular Expressions UTS\#18 {#unicoderegexconformance}
====================================================

This section gives details about JFlex $VERSION’s
conformance with the requirements for Basic Unicode Support Level 1
given in UTS\#18 [@unicode_rep].

## RL1.1 Hex Notation

> *To meet this requirement, an implementation shall supply a mechanism
> for specifying any Unicode code point (from U+0000 to U+10FFFF), using
> the hexadecimal code point representation.*

JFlex conforms. Syntax is provided to express values across the whole
range, via `\uXXXX`, where `XXXX` is a 4-digit hex value; `\Uyyyyyy`,
where `yyyyyy` is a 6-digit hex value; and `\u{X+( X+)*}`, where `X+` is
a 1-6 digit hex value.

## RL1.2 Properties

> *To meet this requirement, an implementation shall provide at least a
> minimal list of properties, consisting of the following:
> General_Category, Script and Script_Extensions, Alphabetic,
> Uppercase, Lowercase, White_Space, Noncharacter_Code_Point,
> Default_Ignorable_Code_Point, ANY, ASCII, ASSIGNED.*
>
> *The values for these properties must follow the Unicode definitions,
> and include the property and property value aliases from the UCD.
> Matching of Binary, Enumerated, Catalog, and Name values, must follow
> the Matching Rules from [UAX44].*

JFlex conforms. The minimal set of properties is supported, as well as a
few others. To see the full list of supported properties, use the JFlex
command line option `--uniprops <ver>`, where `<ver>` is the Unicode
version. Loose matching is performed: case distinctions, whitespace,
underscores and hyphens in property names and values are ignored.


## RL1.2a Compatibility Properties

> *To meet this requirement, an implementation shall provide the
> properties listed in Annex C: Compatibility Properties, with the
> property values as listed there. Such an implementation shall document
> whether it is using the Standard Recommendation or POSIX-compatible
> properties.*

JFlex does not fully conform. The Standard Recommendation version of the
Annex C Compatibility Properties are provided, with two exceptions: `\X`
Extended Grapheme Clusters; and `\b` Default Word Boundaries.


## RL1.3 Subtraction and Intersection

> *To meet this requirement, an implementation shall supply mechanisms
> for union, intersection and set-difference of Unicode sets.*

JFlex conforms by providing these mechanisms, as well as symmetric
difference.


## RL1.4 Simple Word Boundaries

> *To meet this requirement, an implementation shall extend the word
> boundary mechanism so that:*
>
> 1.  *The class of `<word_character>` includes all the Alphabetic
>     values from the Unicode character database, from UnicodeData.txt
>     [UData], plus the decimals (General_Category = Decimal_Number, or
>     equivalently Numeric_Type = Decimal), and the U+200C ZERO WIDTH
>     NON-JOINER and U+200D ZERO WIDTH JOINER (Join_Control=True). See
>     also Annex C: Compatibility Properties.*
>
> 2.  *Nonspacing marks are never divided from their base characters,
>     and otherwise ignored in locating boundaries.*

JFlex does not conform: `\b` does not match simple word boundaries.


## RL1.5 Simple Loose Matches

> *To meet this requirement, if an implementation provides for
> case-insensitive matching, then it shall provide at least the simple,
> default Unicode case-insensitive matching, and specify which
> properties are closed and which are not.*
>
> *To meet this requirement, if an implementation provides for case
> conversions, then it shall provide at least the simple, default
> Unicode case folding.*

JFlex conforms. All supported Unicode Properties are closed.


## RL1.6 Line Boundaries

> *To meet this requirement, if an implementation provides for
> line-boundary testing, it shall recognize not only CRLF, LF, CR, but
> also NEL (U+0085), PARAGRAPH SEPARATOR (U+2029) and LINE SEPARATOR
> (U+2028).*

JFlex conforms.


## RL1.7 Supplementary Code Points

> *To meet this requirement, an implementation shall handle the full
> range of Unicode code points, including values from U+FFFF to
> U+10FFFF. In particular, where UTF-16 is used, a sequence consisting
> of a leading surrogate followed by a trailing surrogate shall be
> handled as a single code point in matching.*

JFlex conforms.

