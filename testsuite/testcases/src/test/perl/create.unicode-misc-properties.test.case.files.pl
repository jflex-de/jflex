#!/usr/bin/perl
#
# create.unicode-misc-properties.test.case.files.pl
#
# This script is designed, for each of the following Unicode properties:
#
#    'Any', 'ASCII', and 'Assigned'
#
# to output a JFlex test spec to produce output of the following form (one
# spec file and one output file per property), when taking as input a file
# with each code point in the BMP, except the surrogates and U+FFFE and U+FFFF:
#
#   0020..0020
#
# Each output file line contains a hex char range matching the property for
# which the spec tests.
#
# A teststuite .test file is also output for each of the above-listed
# properties.
#
# The UnicodeData(-X.X.X).txt file must be given as input to this script, in
# order to determine the 'Assigned' property definition.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $unicode_data_filename = '';
my %properties = ();

GetOptions("version=s"=>\$version, "d=s"=>\$unicode_data_filename);

unless ($version && $unicode_data_filename && -f $unicode_data_filename
	&& -r $unicode_data_filename)
{
    print STDERR "Usage: $0 -v <version> -d <UnicodeData-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

push @{$properties{'ASCII'}}, [ 0x0000, 0x007F ];
push @{$properties{'Any'}}, ([ 0x0000, 0xD7FF ], [ 0xE000, $max_code_point ]);

open IN, "<$unicode_data_filename"
    || die "ERROR opening '$unicode_data_filename' for reading: $!";
my $range_begin = undef;
while (<IN>)
{
    s/^\s*#.*//;
    next unless (/\S/);

    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    if (/^([A-F0-9a-f]{4});<[^,]+, First>;/)
    {
	$range_begin = hex($1);
    }
    # D7A3;<Hangul Syllable, Last>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, Last>;/)
    {
	my $range_end = hex($1);
	push @{$properties{'Assigned'}}, [ $range_begin, $range_end ]
	    unless ($range_begin >= 0xD800 && $range_begin <= 0xDFFF
		    && $range_end >= 0xD800 && $range_end <= 0xDFFF);
	$range_begin = undef;
    }
    # 4E00;<CJK IDEOGRAPH REPRESENTATIVE>;Lo;0;L;;;;;N;;;;;
    elsif ($version eq '1.1'
	   and /^4E00;<CJK IDEOGRAPH REPRESENTATIVE>;/)
    {   # UnicodeData-1.1.5.txt does not list the end point for the Unified Han
	# range (starting point is listed as U+4E00).  This is U+9FFF according
	# to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
	#
	#    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
	#
	push @{$properties{'Assigned'}}, [ 0x4E00, 0x9FFF ];
    }
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});/)
    {
	my $char_num = hex($1);
	push @{$properties{'Assigned'}}, [ $char_num, $char_num ];
    }
}
close IN;

# Merge contiguous ranges
for my $property (keys %properties)
{
    my $merged_property_ranges = [];
    for my $range (@{$properties{$property}})
    {
	if (0 == scalar(@$merged_property_ranges))
	{
	    push @$merged_property_ranges, $range;
	}
	else
	{
	    if ($range->[0] == $merged_property_ranges->[-1]->[1] + 1)
	    {
		$merged_property_ranges->[-1]->[1] = $range->[1];
	    }
	    else
	    {
		push @$merged_property_ranges, $range;
	    }
	}
    }
    $properties{$property} = $merged_property_ranges;
}

my $count = 0;
for my $property (sort keys %properties)
{
    next if ($property =~ /surrogate/i);

    my $underscore_property = $property;
    $underscore_property =~ s/[^0-9A-Za-z]+/_/g;
    my $base_name = "UnicodeMisc_${underscore_property}_${underscore_version}";
    my $output_file = "${base_name}.output";
    open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
    for my $range (@{$properties{$property}})
    {
	printf OUTPUT "%04X..%04X\n", $range->[0], $range->[1];
    }
    close OUTPUT;

    my $test_file = "$base_name.test";
    open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
    print TEST <<"__TEST__";
name: $base_name

description: 
Tests character class syntax of the Unicode $version '$property' property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__
    close TEST;

    my $spec_file = "${base_name}.flex";
    open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
    print SPEC <<"__SPEC__";
%%

%unicode $version
%public
%class $base_name

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\\p{$property} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
__SPEC__
    close SPEC;

    my $jflex_output_file = "${base_name}-flex.output";
    open JFLEX_OUTPUT, ">$jflex_output_file"
	|| die "ERROR opening '$jflex_output_file': $!";
    if ($property eq 'Any')
    {
	print JFLEX_OUTPUT << "__JFLEX_OUTPUT__";

Warning in file "src\\test\\cases\\unicode-misc-properties\\UnicodeMisc_Any_${underscore_version}.flex" (line 15): 
Rule can never be matched:
[^] { }
__JFLEX_OUTPUT__
    }
    else
    {
	# empty file - no expected JFlex output
    }
    close JFLEX_OUTPUT;

    print "\t" unless ($count % 2);
    print "$property";
    print ' 'x(30-length($property)) unless ($count % 2);
    print "\n" if ($count % 2);
    ++$count;
}
