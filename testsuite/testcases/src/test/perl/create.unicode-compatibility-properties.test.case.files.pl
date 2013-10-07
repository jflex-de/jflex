#!/usr/bin/perl
#
# create.unicode-word.test.case.files.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# hex char ranges for the BMP, excluding surrogates and U+FFFE and U+FFFF, for
# each of the following properties:
#
#    xdigit, alnum, blank, graph, print
#
# in the format expected as output by the tests defined for the
# unicode-compatibility-properties test case in the JFlex test suite;
# an example line follows (for xdigit):
#
#   0040..0045
#
# A teststuite .test file is also output.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;
my $version = '';
my $UnicodeData_filename = '';
my $PropList_filename = '';
my $DerivedCoreProperties_filename = '';

GetOptions(                  "version=s" => \$version,
	             "unicodedatafile=s" => \$UnicodeData_filename,
	                "proplistfile=s" => \$PropList_filename, 
	   "derivedcorepropertiesfile=s" => \$DerivedCoreProperties_filename);

unless ($version && $UnicodeData_filename && -f $UnicodeData_filename
	&& -r $UnicodeData_filename
	&& ($version eq '1.1'
	    || ($PropList_filename
		&& -f $PropList_filename
		&& -r $PropList_filename)
	        && ($version < 3.1
		    || ($DerivedCoreProperties_filename
			&& -f $DerivedCoreProperties_filename
			&& -r $DerivedCoreProperties_filename))))
{
    print STDERR
	"Usage: $0 -v <version> -u <UnicodeData-file> -p <PropList-file>\n",
	"\t\t-d <DerivedCoreProperties-file>\n\n",
	"\t\tPropList-file and DerivedCoreProperties-file are not required",
	" for Unicode 1.1\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

open UNICODE_DATA, "<$UnicodeData_filename"
    || die "ERROR opening '$UnicodeData_filename' for reading: $!";

my $default_unicode_data_property_value = 'Cn';
my @unicode_data_ranges = ();
my %property_ranges = ();

while (<UNICODE_DATA>)
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
	push @unicode_data_ranges, [ 0x4E00, 0x9FFF, $general_category ];
    }
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;]+)/)
    {
	my $start = hex($1);
	my $general_category = $2;
	$_ = <UNICODE_DATA>;
	# D7A3;<Hangul Syllable, Last>;Lo;0;L;;;;;N;;;;;
	if (/^([A-F0-9a-f]{4});<[^,]+, Last>;/)
	{
	    my $end = hex($1);
	    push @unicode_data_ranges, [ $start, $end, $general_category ];
	}
    }
    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});[^;]*;([^;]+)/)
    {
	my $char_num = hex($1);
	my $general_category = $2;
	push @unicode_data_ranges, [ $char_num, $char_num, $general_category ];
    }
}
close UNICODE_DATA;

# Merge contiguous ranges
my @merged_unicode_data_ranges = ();
my $end_point;
for my $range (@unicode_data_ranges)
{
    $end_point = $range->[1];
    if (0 == scalar(@merged_unicode_data_ranges))
    {
	if ($range->[0] > 0)
	{   # If the first property range starts after code point 0,
	    # add default property value for the unspecified head range.
	    push @merged_unicode_data_ranges,
		[ 0, $range->[0] - 1, $default_unicode_data_property_value ];
	}
	push @merged_unicode_data_ranges, $range;
    }
    else
    {
	if ($range->[0] == $merged_unicode_data_ranges[-1]->[1] + 1
	   and $range->[2] eq $merged_unicode_data_ranges[-1]->[2])
	{
	    $merged_unicode_data_ranges[-1]->[1] = $range->[1];
	}
	else
	{
	    if ($range->[0] > $merged_unicode_data_ranges[-1]->[1] + 1)
	    {   # Add default property value for unspecified range
		if ($merged_unicode_data_ranges[-1]->[1] < 0xD800
		    and $range->[0] > 0xDFFF)
		{   # Handle unspecified surrogate range
		    push @merged_unicode_data_ranges,
			[ $merged_unicode_data_ranges[-1]->[1] + 1,
			  0xD7FF,
			  $default_unicode_data_property_value ];
		    push @merged_unicode_data_ranges,
			[ 0xE000,
			  $range->[0] - 1,
			  $default_unicode_data_property_value ];
		}
		else
		{
		    push @merged_unicode_data_ranges,
			[ $merged_unicode_data_ranges[-1]->[1] + 1,
			  $range->[0] - 1,
			  $default_unicode_data_property_value ];
		}
	    }
	    push @merged_unicode_data_ranges, $range;
	}
    }
}
if ($end_point < $max_code_point)
{   # If the last property range ends before the maximum code point,
    # add default property value for the unspecified tail range.
    push @merged_unicode_data_ranges, [ $end_point + 1, $max_code_point,
					$default_unicode_data_property_value ];
}

for my $range (@merged_unicode_data_ranges)
{
    my $general_category = $range->[2];
    push @{$property_ranges{normalize($general_category)}}, $range;
    unless ($general_category eq $default_unicode_data_property_value)
    {
	push @{$property_ranges{'assigned'}},
	    [ $range->[0], $range->[1], 'assigned' ];
    }
}


if ($PropList_filename)
{
    my %proplist_ranges = ();
    open PROPLIST, "<$PropList_filename"
	|| die "ERROR opening '$PropList_filename' for reading: $!";
    if ($version < 3.1)
    {
	my $property;
	while (<PROPLIST>)
	{
	    if (/Property\s+dump\s+for:\s+0x[0-9A-Fa-f]+\s+\((.+)\)/)
	    {
		$property = $1;
	    }
	    elsif (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4})/)
	    {
		my $start = hex($1);
		my $end = hex($2)>$max_code_point ? $max_code_point : hex($2);
		
		next if ($start > $max_code_point);
		
		if (($start < 0xD800 && $end < 0xD800)
		    || ($start > 0xDFFF && $end > 0xDFFF))
		{   # No surrogates involved
		    push @{$proplist_ranges{$property}}, [ $start, $end ];
		}
		else
		{
		    if ($start < 0xD800)
		    {   # Add a range for below the surrogate blocks
			push @{$proplist_ranges{$property}}, [ $start, 0xD7FF ];
		    }
		    if ($end > 0xDFFF)
		    {   # Add a range for above the surrogate blocks
			push @{$proplist_ranges{$property}}, [ 0xE000, $end ];
		    }
		}
	    }
	    elsif (/^([A-Fa-f0-9]{4})[^A-Fa-f0-9]/)
	    {
		my $start_and_end = hex($1);
		if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
		{   # Skip surrogate block definitions
		    push @{$proplist_ranges{$property}},
			[$start_and_end, $start_and_end];
		}
	    }
	}
    }
    else
    {
	while (<PROPLIST>)
	{
	    s/\s*\#.*//;
	    next unless (/\S/);
	    if (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4,6})\s*;\s*(.*)/)
	    {   # 0009..000D    ; White_space # Cc   [5] <control>..<control>
		my $start = hex($1);
		next if ($start > $max_code_point);
		
		my $end = hex($2)>$max_code_point ? $max_code_point : hex($2);
		my $property = $3;
		if (($start < 0xD800 && $end < 0xD800)
		    || ($start > 0xDFFF && $end > 0xDFFF))
		{   # No surrogates involved
		    push @{$proplist_ranges{$property}}, [ $start, $end ];
		}
		else
		{
		    if ($start < 0xD800)
		    {   # Add a range for below the surrogate blocks
			push @{$proplist_ranges{$property}}, [ $start, 0xD7FF ];
		    }
		    if ($end > 0xDFFF)
		    {   # Add a range for above the surrogate blocks
			push @{$proplist_ranges{$property}}, [ 0xE000, $end ];
		    }
		}
	    }
	    elsif (/^([A-Fa-f0-9]{4})\s*;\s*(.*)/)
	    {   # 0020          ; White_space # Zs       SPACE
		my $start_and_end = hex($1);
		my $property = $2;
		if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
		{   # Skip surrogate block definitions
		    push @{$proplist_ranges{$property}},
			[$start_and_end, $start_and_end];
		}
	    }
	}
    }
    close PROPLIST;
    # Merge contiguous ranges
    for my $property (keys %proplist_ranges)
    {
	my $merged_property_ranges = [];
	for my $range (@{$proplist_ranges{$property}})
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
	$property_ranges{normalize($property)} = $merged_property_ranges;
    }
}

if ($DerivedCoreProperties_filename)
{
    my %derived_core_properties_ranges = ();
    open DERIVED_CORE_PROPERTIES, "<$DerivedCoreProperties_filename" || die
	"ERROR opening '$DerivedCoreProperties_filename' for reading: $!";

    while (<DERIVED_CORE_PROPERTIES>)
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
		push @{$derived_core_properties_ranges{$property}}, [ $start, $end ];
	    }
	    else
	    {
		if ($start < 0xD800)
		{   # Add a range for below the surrogate blocks
		    push @{$derived_core_properties_ranges{$property}},
			[ $start, 0xD7FF ];
		}
		if ($end > 0xDFFF)
		{   # Add a range for above the surrogate blocks
		    push @{$derived_core_properties_ranges{$property}},
			[ 0xE000, $end ];
		}
	    }
	}
	elsif (/^([A-Fa-f0-9]{4})\s*;\s*(.*)/)
	{   # 0020          ; White_space # Zs       SPACE
	    my $start_and_end = hex($1);
	    my $property = $2;
	    if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
	    {   # Skip surrogate block definitions
		push @{$derived_core_properties_ranges{$property}},
		    [$start_and_end, $start_and_end];
	    }
	}
    }
    close DERIVED_CORE_PROPERTIES;

    # Merge contiguous ranges
    for my $property (keys %derived_core_properties_ranges)
    {
	my $merged_property_ranges = [];
	for my $range (@{$derived_core_properties_ranges{$property}})
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
	$property_ranges{normalize($property)} = $merged_property_ranges;
    }
}

my %compatibility_property_ranges = ();

# UTR#18: \p{xdigit} = [\p{gc=Decimal_Number}\p{Hex_Digit}]
# \p{gc=Decimal_Number} = \p{Nd} (available in all versions)
if (defined($property_ranges{'hexdigit'}))
{
    $compatibility_property_ranges{'xdigit'}
	= merge_ranges($property_ranges{'nd'}, $property_ranges{'hexdigit'});
}
else
{   # Unicode 1.1
    # In newer versions, Hex_Digit contains 0-9 A-F, fullwidth and halfwidth,
    # upper and lowercase. \p{Nd} contains all required digit forms, so no
    # need to add them here.
    my $hex_digits = [ [ 0x41,   0x46,   'hexdigit' ], # 'A'..'F'
		       [ 0x61,   0x66,   'hexdigit' ], # 'a'..'f'
		       # FF21..FF26;FULLWIDTH LATIN CAPITAL LETTER A..F
		       [ 0xFF21, 0xFF26, 'hexdigit' ],
		       # FF41..FF46;FULLWIDTH LATIN SMALL LETTER A..F
		       [ 0xFF41, 0xFF46, 'hexdigit' ] ];
    # Unicode 1.1 doesn't define HALFWIDTH latin letters (or digits)
    $compatibility_property_ranges{'xdigit'}
	= merge_ranges($property_ranges{'nd'}, $hex_digits);
}

# UTR#18: \p{alnum} = [\p{alpha}\p{digit}]
# \p{alpha} = \p{Alphabetic} (available in all versions except 1.1)
# \p{digit} = \p{gc=Decimal_Digit} = \p{Nd} (available in all versions)
if (defined($property_ranges{'alphabetic'}))
{
    $compatibility_property_ranges{'alnum'}
	= merge_ranges($property_ranges{'alphabetic'},
		       $property_ranges{'nd'});
}
else
{
    # For Unicode 1.1, substitute "Letter" (L) for "Alphabetic".
    # \p{L} = [\p{Ll}\p{Lu}\p{Lt}\p{Lm}\p{Lo}]
    my $letter_ranges
	= merge_ranges($property_ranges{'ll'}, $property_ranges{'lm'});
    $letter_ranges = merge_ranges($letter_ranges, $property_ranges{'lo'});
    $letter_ranges = merge_ranges($letter_ranges, $property_ranges{'lu'});
    # Unicode 1.1 has no characters with the 'Lt' (titlecase letter) property.

    $compatibility_property_ranges{'alnum'}
	= merge_ranges($letter_ranges, $property_ranges{'nd'});
}

# UTR#18: \p{blank} = [\p{Whitespace}
#                      -- [\N{LF} \N{VT} \N{FF} \N{CR} \N{NEL}
#                          \p{gc=Line_Separator} \p{gc=Paragraph_Separator}]]
my $blank_ranges;
if (defined($property_ranges{'whitespace'}))
{
    $blank_ranges = $property_ranges{'whitespace'};
}
else
{   # For Unicode 1.1, subsitute "Space_separator" (Zs) for "Whitespace"
    $blank_ranges = $property_ranges{'zs'};
}
# [\N{LF}\N{VT}\N{FF}\N{CR}]
$blank_ranges = remove_ranges($blank_ranges, [[ 0xA, 0xD ]]);
$blank_ranges = remove_ranges($blank_ranges, [[ 0x85, 0x85 ]]); # \N{NEL}
# \p{gc=Line_Separator} = \p{Zl}
$blank_ranges = remove_ranges($blank_ranges, $property_ranges{'zl'});
# \p{gc=Paragraph_Separator} = \p{Zp}
$blank_ranges = remove_ranges($blank_ranges, $property_ranges{'zp'});
$compatibility_property_ranges{'blank'} = $blank_ranges;


# UTR#18: \p{graph} = [^\p{space}\p{gc=Control}\p{gc=Surrogate}\p{gc=Unassigned}]

my $graph_ranges;
if (defined($property_ranges{'whitespace'}))
{
    $graph_ranges = $property_ranges{'whitespace'};
}
else
{  # For Unicode 1.1, subsitute "Space_separator" (Zs) for "Whitespace"
    $graph_ranges = $property_ranges{'zs'};
}
$graph_ranges = remove_ranges([[ 0x0, $max_code_point ]], $graph_ranges);
# \p{gc=Control}
$graph_ranges = remove_ranges($graph_ranges, $property_ranges{'cc'});
# \p{gc=Surrogate}
$graph_ranges = remove_ranges($graph_ranges, [[0xD800,0xDFFF]]);
# \p{gc=Unassigned}
$graph_ranges = remove_ranges($graph_ranges, $property_ranges{'cn'});
$compatibility_property_ranges{'graph'} = $graph_ranges;


# UTR#18: \p{print} = [\p{graph}\p{blank} -- \p{cntrl}]
my $print_ranges = merge_ranges($graph_ranges, $blank_ranges);
# \p{gc=Control}
$print_ranges = remove_ranges($print_ranges, $property_ranges{'cc'});
$compatibility_property_ranges{'print'} = $print_ranges;


# Create output files.
for my $property (sort keys %compatibility_property_ranges)
{
    my $base_name
	= "UnicodeCompatibilityProperties_${property}_${underscore_version}";
    my $output_file = "${base_name}.output";
    open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
    for my $range (@{$compatibility_property_ranges{$property}})
    {
	printf OUTPUT "%04X..%04X\n", $range->[0], $range->[1];
    }
    close OUTPUT;

    my $test_file = "$base_name.test";
    open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
    print TEST <<"__TEST__";
name: $base_name

description: 
Tests character class syntax of the Unicode $version '$property' compatibility
property, derived from UnicodeData(-X.X.X).txt, PropList(-X|-X.X.X).txt and/or
DerivedCoreProperties(-X.X.X).txt.

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
    # empty file - no expected JFlex output
    close JFLEX_OUTPUT;

    print "\t$property\n";
}



sub normalize
{
    my $name = shift;
    my $normalized = lc $name;
    $normalized =~ s/[()-_ ]//g;
    return $normalized;
}

# Merge two range sets
#
# Assumptions:
#
# - The range sets are ordered
# - The ranges in each range set are disjoint
#
sub merge_ranges
{
    my $output_ranges = [];
    my $range_set_a = shift;
    my $range_set_b = shift;
    my $num_ranges_in_set_a = scalar(@$range_set_a);
    my $num_ranges_in_set_b = scalar(@$range_set_b);
    my $pos_a = 0;
    my $pos_b = 0;
    while ($pos_a < $num_ranges_in_set_a || $pos_b < $num_ranges_in_set_b)
    {
	my $to_add;
	if ($pos_a < $num_ranges_in_set_a
	    && ($pos_b >= $num_ranges_in_set_b
		|| ($range_set_a->[$pos_a]->[0]
		    <= $range_set_b->[$pos_b]->[0])))
	{
	    $to_add = $range_set_a->[$pos_a++];
	}
	else
	{
	    $to_add = $range_set_b->[$pos_b++];
	}
	if (0 == scalar(@$output_ranges)
	    || $to_add->[0] > $output_ranges->[-1]->[1] + 1)
	{
	    push @$output_ranges, [ $to_add->[0], $to_add->[1] ];
	}
	else
	{
	    $output_ranges->[-1]->[1] = $to_add->[1];
	}
    }
    return $output_ranges;
}

# Removes the ranges in the second range set from the ranges in the first
# range set.
#
# Assumptions:
#
# - The range sets are ordered
# - The ranges in each range set are disjoint
#
sub remove_ranges
{
    my $output_ranges = [];
    my $range_set_a = shift;
    my $range_set_b = shift;
 RANGE_A:
    for my $range_a (@$range_set_a)
    {
	$range_a = [ $range_a->[0], $range_a->[1] ]; # preserve the original
	for my $range_b (@$range_set_b)
	{
	    next if ($range_b->[1] < $range_a->[0]);
	    next if ($range_b->[0] > $range_a->[1]);

	    if ($range_b->[0] <= $range_a->[0]
		&& $range_b->[1] >= $range_a->[1])
	    {	# Range B is a superset of Range A
		next RANGE_A;
	    }
	    elsif ($range_b->[0] <= $range_a->[0])
	    {   # Range B intersects with the beginning of Range A
		$range_a->[0] = $range_b->[1] + 1;
	    }
	    elsif ($range_b->[1] >= $range_a->[1])
	    {   # Range B intersects with the end of Range A
		$range_a->[1] = $range_b->[0] - 1
	    }
	    else
	    {   # Range B is a strict subset of Range A
		push @$output_ranges, [ $range_a->[0], $range_b->[0] - 1 ];
		$range_a->[0] = $range_b->[1] + 1;
	    }
	}
	push @$output_ranges, [ $range_a->[0], $range_a->[1] ];
    }
    return $output_ranges;
}

1;
