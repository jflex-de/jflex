A few words on performance {#performance}
==========================

This section gives some tips on how to make your specification produce a
faster scanner.

Although JFlex generated scanners show good performance without special
optimisations, there are some heuristics that can make a lexical
specification produce an even faster scanner. Those are (roughly in
order of performance gain):

-   Avoid rules that require backtracking

    From the C/C++ flex [@flex] man page: <span><span>**</span>“Getting
    rid of backtracking is messy and often may be an enormous amount of
    work for a complicated scanner.”</span> Backtracking is introduced
    by the longest match rule and occurs for instance on this set of
    expressions:

    `  averylongkeyword`\
    `  .`

    With input `averylongjoke` the scanner has to read all characters up
    to `’j’` to decide that rule `.` should be matched. All characters
    of `verylong` have to be read again for the next matching process.
    Backtracking can be avoided in general by adding error rules that
    match those error conditions

    ` "av"|"ave"|"avery"|"averyl"|..`

    While this is impractical in most scanners, there is still the
    possibility to add a “catch all” rule for a lengthy list of keywords

        "keyword1"  { return symbol(KEYWORD1); } 
        .. 
        "keywordn"  { return symbol(KEYWORDn); }
        [a-z]+      { error("not a keyword"); }

    Most programming language scanners already have a rule like this for
    some kind of variable length identifiers.

-   Avoid line and column counting

    It costs multiple additional comparisons per input character and the
    matched text has to be re-scanned for counting. In most scanners it
    is possible to do the line counting in the specification by
    incrementing `yyline` each time a line terminator has been matched.
    Column counting could also be included in actions. This will be
    faster, but can in some cases become quite messy.

-   Avoid look-ahead expressions and the end of line operator ’\$’

    In the best case, the trailing context will first have to be read
    and then (because it is not to be consumed) re-read again. The cases
    of fixed-length look-ahead and fixed-length base expressions are
    handled efficiently by matching the concatenation and then pushing
    back the required amount of characters. This extends to the case of
    a disjunction of fixed-length look-ahead expressions such as
    `r1 / \r|\n|\r\n`. All other cases `r1 / r2` are handled by first
    scanning the concatenation of `r1` and `r2`, and then finding the
    correct end of `r1`. The end of `r1` is found by scanning forwards
    in the match again, marking all possible `r1` terminations, and then
    scanning the reverse of `r2` backwards from the end until a start of
    `r2` intersects with an end of `r1`. This algorithm is linear in the
    size of the input (not quadratic or worse as backtracking is), but
    about a factor of 2 slower than normal scanning. It also consumes
    memory proportional to the size of the matched input for `r1 r2`.

-   Avoid the beginning of line operator ’`^`’

    It costs multiple additional comparisons per match. In some cases
    one extra look-ahead character is needed (when the last character
    read is `\r`, the scanner has to read one character ahead to check
    if the next one is an `\n` or not).

-   Match as much text as possible in a rule.

    One rule is matched in the innermost loop of the scanner. After each
    action some overhead for setting up the internal state of the
    scanner is necessary.

Note that writing more rules in a specification does not make the
generated scanner slower.

The two main rules of optimisation apply also for lexical
specifications:

1.  **don’t do it**

2.  **(for experts only) don’t do it yet**

Some of the performance tips above contradict a readable and compact
specification style. When in doubt or when requirements are not or not
yet fixed: don’t use them — the specification can always be optimised in
a later state of the development process.

