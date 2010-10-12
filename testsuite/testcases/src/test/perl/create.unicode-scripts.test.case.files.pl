#!/usr/bin/perl
#
# create.unicode-scripts.test.case.files.pl
#
# This script is designed to take as input Scripts(-X|-X.X.X).txt, and output
# hex char ranges and corresponding property values, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-scripts test case in the JFlex test suite;
# an example line follows:
#
#    0000..007F; Basic Latin
#
# A teststuite .test file is also output.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $input_filename = '';
my $default_property_value = 'Unknown';
my %property_values = ( $default_property_value => 1 );
my $property_values_to_skip_regex = qr/surrogate/i;
my @ranges = ();

GetOptions("version=s"=>\$version, "s=s"=>\$input_filename);

unless ($version && $input_filename
	&& -f $input_filename && -r $input_filename)
{
    print STDERR "Usage: $0 -v <version> -s <Scripts-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;
my $base_name = "UnicodeScripts_${underscore_version}";

my $prev_char_num = -2;
my $range_begin = -1;
my $range_property_value = '';
my $propname = 'Script';

open IN, "<$input_filename"
    || die "ERROR opening '$input_filename' for reading: $!";

while (<IN>)
{
    chomp;
    s/\s*#.*//;
    s/\s+$//; # Strip trailing space
    next unless (/\S/);

    # 00AA       ; LATIN # L&       FEMININE ORDINAL INDICATOR
    if (/^([A-F0-9a-f]{4})\s*;\s*(.+)/)
    {
	my $start = hex($1);
	my $end = $start;
	my $property_value = $2;
	next if ($start > $max_code_point);
	++$property_values{$property_value};
	push @ranges, [ $start, $end, $property_value ];
    }
    # 0AE6..0AEF ; Gujarati # Nd  [10] GUJARATI DIGIT ZERO..GUJARATI DIGIT NINE
    elsif (/^([A-F0-9a-f]{4})..([A-F0-9a-f]{4,5})\s*;\s*(.+)/)
    {
	my $start = hex($1);
	my $end = hex($2);
	my $property_value = $3;
	next if ($start > $max_code_point);
	$end = $max_code_point if ($end > $max_code_point);
	++$property_values{$property_value};
	push @ranges, [ $start, $end, $property_value ];
    }
}
close IN;

# Merge contiguous ranges
my @merged_ranges = ();
for my $range (sort { $a->[0] <=> $b->[0] } @ranges)
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
		if ($merged_ranges[-1]->[1] + 1 < 0xD800
		    && $range->[0] >= 0xD800)
		{
		    push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
					   0xD7FF,
					   $default_property_value ];
		    push @merged_ranges, [ 0xE000,
					   $range->[0] - 1,
					   $default_property_value ];
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

my $output_file = "${base_name}.output";
open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
for my $range (@merged_ranges)
{
    my ($start, $end, $property_value) = @$range;
    next if ($property_value =~ $property_values_to_skip_regex);
    printf OUTPUT "%04X..%04X; $property_value\n", $start, $end;
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

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

for my $property_value (sort keys %property_values)
{
    next if ($property_value =~ $property_values_to_skip_regex);
    print SPEC qq/\\p{$property_value} { /
	     . qq/setCurCharPropertyValue("$property_value"); }\n/;
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
