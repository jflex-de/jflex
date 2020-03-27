#
# make test (or just: make)
#    generates lexer & parser, compiles all *.java files, and runs test
#

# main class
MAIN = Main

# test data
TEST_IN  = src/test/data/example.as
OUT_GOOD = src/test/data/output.good

# jflex input and output
LEXER_IN = src/main/jflex/scanner.flex
LEXER_CLASS = Lexer

# cup file
PARSER_IN = src/main/cup/parser.cup

# this parser wants its input on stdin:
PIPE_INPUT = <

include ../common/Makefile.inc
