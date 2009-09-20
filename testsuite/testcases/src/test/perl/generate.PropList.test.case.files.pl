#!/usr/bin/perl
#
# generate.PropList.testcase.files.pl
#
# This script is designed to take as input PropList(-X.X.X).txt, and output
# a JFlex test spec to produce output of the following form (one spec file and
# one output file per defined property), when taking as input a file with each
# code point in the BMP, except the surrogates and U+FFFE and U+FFFF:
#
#   0020..0020
#
# Each output file line contains a hex char range matching the property for
# which the spec tests.
#
# A teststuite .test file is also output for each defined property.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $proplist_filename = '';
my %properties = ();

GetOptions("version=s"=>\$version, "p=s"=>\$proplist_filename);

unless ($version && $proplist_filename && -f $proplist_filename
	&& -r $proplist_filename)
{
    print STDERR "Usage: $0 -v <version> -p <unicode-PropList-data-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

open IN, "<$proplist_filename"
    || die "ERROR opening '$proplist_filename' for reading: $!";
if ($version < 3.1)
{
    my $property;
    while (<IN>)
    {
	if (/Property\s+dump\s+for:\s+0x[0-9A-Fa-f]+\s+\((.+)\)/)
	{
	    $property = $1;
	}
        elsif (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4})/)
	{
	    my $start = hex($1);
	    my $end = hex($2) > $max_code_point ? $max_code_point : hex($2);

	    next if ($start > $max_code_point);


	    if (($start < 0xD800 && $end < 0xD800)
		|| ($start > 0xDFFF && $end > 0xDFFF))
	    {   # No surrogates involved
		push @{$properties{$property}}, [ $start, $end ];
	    }
	    else
	    {
		if ($start < 0xD800)
		{   # Add a range for below the surrogate blocks
		    push @{$properties{$property}}, [ $start, 0xD7FF ];
		}
		if ($end > 0xDFFF)
		{   # Add a range for above the surrogate blocks
		    push @{$properties{$property}}, [ 0xE000, $end ];
		}
	    }
	}
	elsif (/^([A-Fa-f0-9]{4})[^A-Fa-f0-9]/)
	{
	    my $start_and_end = hex($1);
	    if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
	    {   # Skip surrogate block definitions
		push @{$properties{$property}}, [$start_and_end, $start_and_end];
	    }
	}
    }
}
else
{
    while (<IN>)
    {
	s/\s*\#.*//;
	next unless (/\S/);
        if (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4,6})\s*;\s*(.*)/)
	{   # 0009..000D    ; White_space # Cc   [5] <control>..<control>
	    my $start = hex($1);
	    next if ($start > $max_code_point);

	    my $end = hex($2) > $max_code_point ? $max_code_point : hex($2);
	    my $property = $3;
	    if (($start < 0xD800 && $end < 0xD800)
		|| ($start > 0xDFFF && $end > 0xDFFF))
	    {   # No surrogates involved
		push @{$properties{$property}}, [ $start, $end ];
	    }
	    else
	    {
		if ($start < 0xD800)
		{   # Add a range for below the surrogate blocks
		    push @{$properties{$property}}, [ $start, 0xD7FF ];
		}
		if ($end > 0xDFFF)
		{   # Add a range for above the surrogate blocks
		    push @{$properties{$property}}, [ 0xE000, $end ];
		}
	    }
	}
	elsif (/^([A-Fa-f0-9]{4})\s*;\s*(.*)/)
	{   # 0020          ; White_space # Zs       SPACE
	    my $start_and_end = hex($1);
	    my $property = $2;
	    if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
	    {   # Skip surrogate block definitions
		push @{$properties{$property}}, [$start_and_end, $start_and_end];
	    }
	}
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
    my $base_name
	= "UnicodePropList_${underscore_property}_${underscore_version}";

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
Tests character class syntax of the Unicode $version '$property' property,
defined in PropList(-X.X.X).txt.

jflex: -q

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

%include src/test/resources/common-unicode-binary-property-java

%%

\\p{$property} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
__SPEC__
    close SPEC;

    my $jflex_output_file = "${base_name}-flex.output";
    open JFLEX_OUTPUT, ">$jflex_output_file"
	|| die "ERROR opening '$jflex_output_file': $!";
    # empty file - no expected JFlex output
    close JFLEX_OUTPUT;

    print "\t" unless ($count % 2);
    print "$property";
    print ' 'x(30-length($property)) unless ($count % 2);
    print "\n" if ($count % 2);
    ++$count;
}
