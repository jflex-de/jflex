#!/usr/bin/perl
#
# create.general.category.test.output.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# hex char ranges and corresponding general categories, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-general-category test case in the JFlex test
# suite; an example line follows:
#
#   0000..001F; Cc
#

use strict;
use warnings;


my $prev_char_num = -2;
my $range_begin = -1;
my $range_category = '';
while (<>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    if (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;]+)/)
    {
	my $hex_char = $1;
	my $general_category = $2;
	my $char_num = hex($hex_char);
	if ($prev_char_num + 1 == $char_num
	    && $range_category eq $general_category)
	{
	    $prev_char_num = $char_num;
	}
	else
	{
	    printf "%04X..%04X; $range_category\n", $range_begin,
		$prev_char_num
		    unless ($range_category eq 'Cs');
	    if ($prev_char_num + 1 < $char_num)
	    {
		printf "%04X..%04X; Cn\n", ($prev_char_num + 1),
		    ($char_num - 1), "Cn";
	    }
	    $range_begin = $char_num;
	    $range_category = $general_category;
	    $prev_char_num = $char_num;
	}
    }
    # D7A3;<Hangul Syllable, Last>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, Last>;([^;]+)/)
    {
	my $hex_char = $1;
	my $general_category = $2;
	my $char_num = hex($hex_char);
	$prev_char_num = $char_num;
    }
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});[^;]*;([^;]+)/)
    {
	my $hex_char = $1;
	my $general_category = $2;
	my $char_num = hex($hex_char);
	if ($prev_char_num + 1 == $char_num
	    && $range_category eq $general_category)
	{
	    $prev_char_num = $char_num;
	}
	else
	{
	    printf "%04X..%04X; $range_category\n", $range_begin,
		$prev_char_num
		    unless ($range_category eq 'Cs'
			   || $range_begin < 0);
	    if ($prev_char_num + 1 < $char_num)
	    {
		printf "%04X..%04X; Cn\n", ($prev_char_num + 1),
		    ($char_num - 1), "Cn"
			unless ($prev_char_num < 0);
	    }
	    $range_begin = $char_num;
	    $range_category = $general_category;
	    $prev_char_num = $char_num;
	}
    }
}
printf "%04X..%04X; $range_category\n", $range_begin, $prev_char_num,
    $range_category;
