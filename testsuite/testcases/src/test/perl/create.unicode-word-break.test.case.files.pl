#!/usr/bin/perl
#
# create.unicode-word-break.test.output.pl
#
# This script is designed to take as input WordBreakProperty.txt, and output
# hex char ranges and corresponding properties, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-word-break test case in the JFlex test suite;
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
my $input_filename = '';
my $default_property_value = 'Other';
my $property_name = 'WordBreak';
my %property_values = ( $default_property_value => 1 );
my %property_values_to_skip = ( );
my @ranges = ();

GetOptions("version=s"=>\$version, "datafile=s"=>\$input_filename);

unless ($version && $input_filename
	&& -f $input_filename && -r $input_filename)
{
    print STDERR "Usage: $0 -v <version> -d <data-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;
my $base_name = "Unicode${property_name}_${underscore_version}";

my $prev_char_num = -2;
my $range_begin = -1;
my $range_property_value = '';

open IN, "<$input_filename"
    || die "ERROR opening '$input_filename' for reading: $!";

while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    # 0950          ; ALetter # Lo       DEVANAGARI OM
    if (/^([A-F0-9a-f]{4})\s*;\s*([^;#\s]+)/)
    {
	my $char_num = hex($1);
	my $property_value = $2;
	++$property_values{$property_value};
	push @ranges, [ $char_num, $char_num, $property_value ];
    }
    # 0958..0961    ; ALetter # Lo  [10] DEVANAGARI LETTER QA..DEVANAGARI...
    elsif (/^([A-F0-9a-f]{4})..([A-F0-9a-f]{4,5})\s*;\s*([^;#\s]+)/)
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
for my $range (sort { $a->[0] <=> $b->[0] } @ranges)
{
    if (0 == scalar(@merged_ranges))
    {
	if ($range->[0] > 0)
	{
	    push @merged_ranges,
		[ 0, $range->[0] - 1, $default_property_value ];
	}
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
		if ($merged_ranges[-1]->[1] + 1 <= 0xD800
		    && $range->[0] - 1 >= 0xD800)
		{
		    push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
					   0xD7FF,
					   $default_property_value ];
		    if ($range->[0] - 1 > 0xDFFF)
		    {
			push @merged_ranges, [ 0xE000,
					       $range->[0] - 1,
					       $default_property_value ];
		    }
		}
		else
		{
		    push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
					   $range->[0] - 1,
					   $default_property_value ];
		}
	    }
	    push @merged_ranges, $range;
	}
    }
}
if ($merged_ranges[-1]->[1] < $max_code_point)
{
    push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
			   $max_code_point,
			   $default_property_value ];
}

my $output_file = "${base_name}.output";
open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
for my $range (@merged_ranges)
{
    my ($start_char_num, $end_char_num, $property_value) = @$range;
    next if (defined($property_values_to_skip{$property_value}));
    printf OUTPUT "%04X..%04X; $property_name:$property_value\n",
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

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

for my $property_value (sort keys %property_values)
{
    next if (defined($property_values_to_skip{$property_value}));
    print SPEC qq/\\p{$property_name:$property_value} { /
	     . qq/setCurCharPropertyValue/
	     . qq/("$property_name:$property_value"); }\n/;
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
Tests character class syntax of the Unicode $version $property_name property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;

1;
