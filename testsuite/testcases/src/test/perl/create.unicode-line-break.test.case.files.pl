#!/usr/bin/perl
#
# create.unicode-line-break.test.output.pl
#
# This script is designed to take as input LineBreak(-X|-X.X.X).txt, and output
# hex char ranges and corresponding property values, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-line-break test case in the JFlex test suite;
# an example line follows:
#
#   0000..0008; CM
#
# A teststuite .test file is also output.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $line_break_filename = '';
my $default_property_value = 'XX';
my %property_values = ( $default_property_value => 1 );
my %property_values_to_skip = ( 'SG' => 1 );
my @ranges = ();

GetOptions("version=s"=>\$version, "l=s"=>\$line_break_filename);

unless ($version && $line_break_filename && -f $line_break_filename
	&& -r $line_break_filename)
{
    print STDERR "Usage: $0 -v <version> -l <LineBreak-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;
my $base_name = "UnicodeLineBreak_${underscore_version}";

my $prev_char_num = -2;
my $range_begin = -1;
my $range_property_value = '';
my $propname = 'LineBreak';

open IN, "<$line_break_filename"
    || die "ERROR opening '$line_break_filename' for reading: $!";

while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    # 4E00;ID;<CJK Ideograph, First>
    if (/^([A-F0-9a-f]{4});([^;#\s]+);<[^,]+, First>/)
    {
	my $start_char_num = hex($1);
	my $property_value = $2;
	++$property_values{$property_value};
	$_ = <IN>;
	# 4DB5;ID;<CJK Ideograph Extension A, Last>
	if (/^([A-F0-9a-f]{4});[^;#\s]+;<[^,]+, Last>/)
	{
	    my $end_char_num = hex($1);
	    push @ranges, [ $start_char_num, $end_char_num, $property_value ];
	}
    }
    # 0000;CM;<control>
    elsif (/^([A-F0-9a-f]{4});([^;#\s]+)/)
    {
	my $char_num = hex($1);
	my $property_value = $2;
	++$property_values{$property_value};
	push @ranges, [ $char_num, $char_num, $property_value ];
    }
    # AC01..AC1B;H3 # HANGUL SYLLABLE GAG..HANGUL SYLLABLE GAH
    elsif (/^([A-F0-9a-f]{4})..([A-F0-9a-f]{4,5});([^;#\s]+)/)
    {
	my $start_char_num = hex($1);
	my $end_char_num = hex($2);
	$end_char_num = $max_code_point if ($end_char_num > $max_code_point);
	my $property_value = $3;
	++$property_values{$property_value};
	push @ranges, [ $start_char_num, $end_char_num, $property_value ];
    }
}
close IN;

# Merge contiguous ranges
my @merged_ranges = ();
for my $range (@ranges)
{
    if (0 == scalar(@merged_ranges))
    {
	push @merged_ranges, $range;
    }
    else
    {
	if ($range->[0] == $merged_ranges[-1]->[1] + 1
	   and $range->[2] eq $merged_ranges[-1]->[2])
	{
	    $merged_ranges[-1]->[1] = $range->[1];
	}
	else
	{
	    if ($range->[0] > $merged_ranges[-1]->[1] + 1)
	    {
		push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
				       $range->[0] - 1,
				       $default_property_value ];
	    }
	    push @merged_ranges, $range;
	}
    }
}

my $output_file = "${base_name}.output";
open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
for my $range (@merged_ranges)
{
    my ($start_char_num, $end_char_num, $property_value) = @$range;
    next if (defined($property_values_to_skip{$property_value}));
    printf OUTPUT "%04X..%04X; $propname:$property_value\n",
	$start_char_num, $end_char_num;

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

%include src/test/resources/unicode-common-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

for my $property_value (sort keys %property_values)
{
    next if (defined($property_values_to_skip{$property_value}));
    print SPEC qq/\\p{$propname:$property_value} { /
	     . qq/setCurCharPropertyValue("$propname:$property_value"); }\n/;
}

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
Tests character class syntax of the Unicode $version $propname property.

jflex: -q

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;

1;
