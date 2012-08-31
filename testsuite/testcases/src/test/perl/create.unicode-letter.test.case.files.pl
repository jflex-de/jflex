#!/usr/bin/perl
#
# create.unicode-letter.test.case.files.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# hex char ranges and the Letter property value, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-letter test case in the JFlex test suite;
# an example line follows:
#
#   0041..005A; Letter
#
# A grammar testing for '[:letter:]' is output, along with a teststuite .test
# file.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $prev_char_num = -2;
my $range_begin = -1;
my $range_category = '';
my $property_value = 'Letter';
my $property_value_regex = qr/^L.$/;
my $property_value_friendly_name = 'Letter';
my $property_value_syntax = '[:letter:]';

my $version = '';
my $input_filename = '';
my @ranges = ();
my @complement_ranges = ();

GetOptions("version=s"=>\$version, "datafile=s"=>\$input_filename);

unless ($version && $input_filename
	&& -f $input_filename && -r $input_filename)
{
    print STDERR "Usage: $0 -v <version> -d <UnicodeData-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

open IN, "<$input_filename"
    || die "ERROR opening '$input_filename' for reading: $!";
while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    if ($version eq '1.1' and /^4E00;<CJK IDEOGRAPH REPRESENTATIVE>;/)
    {   # UnicodeData-1.1.5.txt does not list the end point for the Unified Han
	# range (starting point is listed as U+4E00).  This is U+9FFF according
	# to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
	#
	#    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
	#
	$prev_char_num = 0x9FFF;
    }
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;]+)/)
    {
	my $hex_char = $1;
	my $general_category = $2;
	my $char_num = hex($hex_char);
	if ($prev_char_num + 1 == $char_num
	    && substr($range_category,0,1) eq substr($general_category,0,1))
	{
	    $prev_char_num = $char_num;
	}
	else
	{
	    push @ranges, [ $range_begin, $prev_char_num, $property_value ]
		if ($range_category =~ $property_value_regex);
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
	    && substr($range_category,0,1) eq substr($general_category,0,1))
	{
	    $prev_char_num = $char_num;
	}
	else
	{
	    push @ranges, [ $range_begin, $prev_char_num, $property_value ]
		if ($range_category =~ $property_value_regex);
	    $range_begin = $char_num;
	    $range_category = $general_category;
	    $prev_char_num = $char_num;
	}
    }
}
push @ranges, [ $range_begin, $prev_char_num, $property_value ]
    if ($range_category =~ $property_value_regex);

for my $range_num (0 .. $#ranges)
{
    my $range = $ranges[$range_num];
    if ($range_num == 0 and $range->[0] > 0)
    {
	push @complement_ranges, [ 0, $range->[0] - 1, "Not-$property_value" ];
    }
    else
    {
	my $previous_range = $ranges[$range_num - 1];
	if ($previous_range->[1] < 0xD800 and $range->[0] > 0xDFFF)
	{   # Skip surrogate range
	    push @complement_ranges,
		[ $previous_range->[1] + 1, 0xD7FF, "Not-$property_value" ];
	    push @complement_ranges,
		[ 0xE000, $range->[0] - 1, "Not-$property_value" ];
	}
	else
	{
	    push @complement_ranges, [ $previous_range->[1] + 1,
				       $range->[0] - 1,
				       "Not-$property_value" ];
	}
    }
}
if ($ranges[-1]->[1] < $max_code_point)
{
    push @complement_ranges,
	[ $ranges[-1]->[1] + 1, $max_code_point, "Not-$property_value" ];
}

close IN;

my $base_name = "Unicode${property_value_friendly_name}_${underscore_version}";
my $output_file = "${base_name}.output";
open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
for my $range (sort { $a->[0] <=> $b->[0] } @ranges, @complement_ranges)
{
    my ($start_char_num, $end_char_num, $value) = @$range;
    printf OUTPUT "%04X..%04X; $value\n", $start_char_num, $end_char_num;
}
close OUTPUT;

my $spec_file = "${base_name}.flex";
open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
print SPEC <<"__HEADER__";
%%

%unicode $version
%public
%class $base_name

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

print SPEC qq/${property_value_syntax} { /
         . qq/setCurCharPropertyValue("$property_value"); }\n/;
print SPEC qq/[^${property_value_syntax}] { /
         . qq/setCurCharPropertyValue("Not-$property_value"); }\n/;
close SPEC;

my $jflex_output_file = "${base_name}-flex.output";
open JFLEX_OUTPUT, ">$jflex_output_file"
    || die "ERROR opening '$jflex_output_file': $!";
close JFLEX_OUTPUT;

my $test_file = "${base_name}.test";
open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
print TEST <<"__TEST__";
name: $base_name

description: 
Tests character class syntax of the Unicode $version $property_value_friendly_name property
using the '$property_value_syntax' syntax.

jflex: -q

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;

1;
