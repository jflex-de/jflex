#!/usr/bin/perl
#
# create.unicode-general-category.test.case.files.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# hex char ranges and corresponding general categories, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-general-category test case in the JFlex test
# suite; an example line follows:
#
#   0000..001F; Cc
#
# Two sets of files will be output, one for two-letter general categories, and
# another for single-letter category groups.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $input_filename = '';
my $default_property_value = 'Cn';
my @ranges = ();

GetOptions("version=s"=>\$version, "d=s"=>\$input_filename);

unless ($version && $input_filename
	&& -f $input_filename && -r $input_filename)
{
    print STDERR "Usage: $0 -v <version> -d <UnicodeData-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

my $base_name = "UnicodeGeneralCategory_${underscore_version}";

open IN, "<$input_filename"
    || die "ERROR opening '$input_filename' for reading: $!";

my %general_categories = ( 'Cn' => 1 );

while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    if ($version eq '1.1' and /^4E00;<CJK IDEOGRAPH REPRESENTATIVE>;([^;]+)/)
    {   # UnicodeData-1.1.5.txt does not list the end point for the Unified Han
	# range (starting point is listed as U+4E00).  This is U+9FFF according
	# to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
	#
	#    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
	#
	my $general_category = $1;
	push @ranges, [ 0x4E00, 0x9FFF, $general_category ];
    }
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;]+)/)
    {
	my $start = hex($1);
	my $general_category = $2;
	++$general_categories{$general_category} unless ($general_category eq 'Cs');
	$_ = <IN>;
	# D7A3;<Hangul Syllable, Last>;Lo;0;L;;;;;N;;;;;
	if (/^([A-F0-9a-f]{4});<[^,]+, Last>;/)
	{
	    my $end = hex($1);
	    push @ranges, [ $start, $end, $general_category ];
	}
    }
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});[^;]*;([^;]+)/)
    {
	my $char_num = hex($1);
	my $general_category = $2;
	++$general_categories{$general_category} unless ($general_category eq 'Cs');
	push @ranges, [ $char_num, $char_num, $general_category ];
    }
}
close IN;

# Merge contiguous ranges
my @merged_ranges = ();
my $end_point;
for my $range (@ranges)
{
    $end_point = $range->[1];
    if (0 == scalar(@merged_ranges))
    {
	if ($range->[0] > 0)
	{   # If the first property range starts after code point 0,
	    # add default property value for the unspecified head range.
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
	    {   # Add default property value for unspecified range
		if ($merged_ranges[-1]->[1] < 0xD800
		    and $range->[0] > 0xDFFF)
		{   # Handle unspecified surrogate range
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
if ($end_point < $max_code_point)
{   # If the last property range ends before the maximum code point,
    # add default property value for the unspecified tail range.
    push @merged_ranges,
	[ $end_point + 1, $max_code_point, $default_property_value ];
}

open OUTPUT, ">${base_name}.output"
    || die "ERROR opening '${base_name}.output' for writing: $!";

for my $range (@merged_ranges)
{
    my ($start, $end, $property_value) = @$range;
    $end = $max_code_point if ($end > $max_code_point);
    printf OUTPUT "%04X..%04X; $property_value\n", $start, $end
	unless ($property_value eq 'Cs'); # Skip surrogate ranges
}
close OUTPUT;

open SPEC, ">${base_name}.flex"
    || die "ERROR opening '${base_name}.flex' for writing: $!";

print SPEC << "__SPEC_HEADER__";
%%

%unicode $version
%public
%class $base_name

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__SPEC_HEADER__

for my $gen_cat (sort keys %general_categories)
{
    print SPEC qq/\\p{$gen_cat} { setCurCharPropertyValue("$gen_cat"); }\n/;
}
close SPEC;

open JFLEX_OUTPUT, ">${base_name}-flex.output"
    || die "ERROR opening '${base_name}-flex.output': $!";
# Nothing is output to this file - it is intended to be empty.
close JFLEX_OUTPUT;

open TEST, ">${base_name}.test" || die "ERROR opening '${base_name}.test': $!";

print TEST <<"__TEST__";
name: $base_name

description: 
Tests character class syntax of the Unicode $version General Category property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;


$base_name = "UnicodeGeneralCategorySingleLetter_${underscore_version}";

open IN, "<$input_filename"
    || die "ERROR opening '$input_filename' for reading: $!";

my %single_letter_general_categories = ();
@ranges = ();
while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    if ($version eq '1.1' and /^4E00;<CJK IDEOGRAPH REPRESENTATIVE>;([^;])/)
    {   # UnicodeData-1.1.5.txt does not list the end point for the Unified Han
	# range (starting point is listed as U+4E00).  This is U+9FFF according
	# to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
	#
	#    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
	#
	my $general_category = $1;
	push @ranges, [ 0x4E00, 0x9FFF, $general_category ];
    }
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;])/)
    {
	my $start = hex($1);
	my $general_category = $2;
	++$single_letter_general_categories{$general_category};
	$_ = <IN>;
	# D7A3;<Hangul Syllable, Last>;Lo;0;L;;;;;N;;;;;
	if (/^([A-F0-9a-f]{4});<[^,]+, Last>;/)
	{
	    my $end = hex($1);
	    push @ranges, [ $start, $end, $general_category ];
	}
    }
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});[^;]*;([^;])/)
    {
	my $char_num = hex($1);
	my $general_category = $2;
	++$single_letter_general_categories{$general_category};
	push @ranges, [ $char_num, $char_num, $general_category ];
    }
}
close IN;

# Merge contiguous ranges
@merged_ranges = ();
$default_property_value = 'C';
$end_point = 0;
for my $range (@ranges)
{
    $end_point = $range->[1];
    if (0 == scalar(@merged_ranges))
    {
	if ($range->[0] > 0)
	{   # If the first property range starts after code point 0,
	    # add default property value for the unspecified head range.
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
	    {   # Add default property value for unspecified range
		if ($range->[2] eq $default_property_value)
		{
		    if ($merged_ranges[-1]->[2] eq $default_property_value)
		    {
			$merged_ranges[-1]->[1] = $range->[0] - 1;
		    }
		    else
		    {
			push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
					       $range->[1],
					       $range->[2] ];
		    }
		}
		else
		{
		    if ($merged_ranges[-1]->[2] eq $default_property_value)
		    {
			$merged_ranges[-1]->[1] = $range->[0] - 1;
		    }
		    else
		    {
			push @merged_ranges, [ $merged_ranges[-1]->[1] + 1,
					       $range->[0] - 1,
					       $default_property_value ];
		    }
		    push @merged_ranges, $range;
		}
	    }
	    else
	    {
		push @merged_ranges, $range;
	    }
	}
    }
}
if ($end_point < $max_code_point)
{   # If the last property range ends before the maximum code point,
    # add default property value for the unspecified tail range.
    push @merged_ranges,
	[ $end_point + 1, $max_code_point, $default_property_value ];
}


open OUTPUT, ">${base_name}.output"
    || die "ERROR opening '${base_name}.output' for writing: $!";

for my $range (@merged_ranges)
{
    my ($start, $end, $property_value) = @$range;
    $end = $max_code_point if ($end > $max_code_point);
    if ($start <= 0xD800 and $end >= 0xDFFF)
    {   # Don't output surrogates
	printf OUTPUT "%04X..%04X; $property_value\n", $start, 0xD7FF
	    if ($start < 0xD800);
	printf OUTPUT "%04X..%04X; $property_value\n", 0xE000, $end
	    if ($end > 0xDFFF);
    }
    else
    {
	printf OUTPUT "%04X..%04X; $property_value\n", $start, $end;
    }
}
close OUTPUT;

open SPEC, ">${base_name}.flex"
    || die "ERROR opening '${base_name}.flex' for writing: $!";

print SPEC << "__SPEC_HEADER__";
%%

%unicode $version
%public
%class $base_name

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__SPEC_HEADER__

for my $gen_cat (sort keys %single_letter_general_categories)
{
    print SPEC qq/\\p{$gen_cat} { setCurCharPropertyValue("$gen_cat"); }\n/;
}
close SPEC;

open JFLEX_OUTPUT, ">${base_name}-flex.output"
    || die "ERROR opening '${base_name}-flex.output': $!";
# Nothing is output to this file - it is intended to be empty.
close JFLEX_OUTPUT;

open TEST, ">${base_name}.test" || die "ERROR opening '${base_name}.test': $!";

print TEST <<"__TEST__";
name: $base_name

description: 
Tests character class syntax of the Unicode $version single-letter General
Category property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;
