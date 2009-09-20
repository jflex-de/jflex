#!/usr/bin/perl
#
# create.general.category.test.spec.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# a JFlex test spec to produce output of the following form, when taking as
# input a file with each code point in the BMP, except the surrogates and
# U+FFFE and U+FFFF:
#
#   0000..001F; Cc
#

use strict;
use warnings;

print <<'HEADER';
%%

%unicode 5.1
%public
%class UnicodeGeneralCategory_

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
HEADER

my %general_categories = ();
while (<>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    if (/^[A-F0-9a-f]{4};[^;]*;([^;]+)/)
    {
	my $general_category = $1;
	++$general_categories{$general_category}
	    unless ($general_category eq 'Cs');
    }
}

# \p{Yi} { setCurCharBlock("Yi"); }
for my $gen_cat (sort keys %general_categories)
{
    print qq/\\p{$gen_cat} { setCurCharBlock("$gen_cat"); }\n/;
}
