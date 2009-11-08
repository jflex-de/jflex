#!/usr/bin/perl
#
# create.unicode-word.test.case.files.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# hex char ranges and the gc:Nd property value, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-word test case in the JFlex test suite;
# an example line follows:
#
#   0030..0039; Word
#
# A teststuite .test file is also output for each syntax to be tested:
#
#    \w
#
#    \W  (negative)
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $prev_char_num = -2;
my $range_begin = -1;
my $range_category = '';
my $property_value = 'Word';
my $PropList_property_value_regex = qr/^Alphabetic$/i;
my $DerivedCoreProperties_property_value_regex = qr/^Alphabetic$/i;
my $property_value_friendly_name = 'Word';
my %property_value_polarity = ( '\w' => 'positive', '\W' => 'negative');
my @property_value_syntaxes = sort keys %property_value_polarity;

my $version = '';
my $UnicodeData_filename = '';
my $PropList_filename = '';
my $DerivedCoreProperties_filename = '';
my @ranges = ();

GetOptions(                  "version=s" => \$version,
	             "unicodedatafile=s" => \$UnicodeData_filename,
	                "proplistfile=s" => \$PropList_filename, 
	   "derivedcorepropertiesfile=s" => \$DerivedCoreProperties_filename);

unless ($version && $UnicodeData_filename && -f $UnicodeData_filename
	&& -r $UnicodeData_filename
	&& ($version eq '1.1'
	    || ($version < 3.1
		&& $PropList_filename
		&& -f $PropList_filename
		&& -r $PropList_filename)
	    || ($version > 3.0
		&& $DerivedCoreProperties_filename
		&& -f $DerivedCoreProperties_filename
		&& -r $DerivedCoreProperties_filename)))
{
    print STDERR
	"Usage: $0 -v <version> -u <UnicodeData-file> -p <PropList-file>\n",
	"\t\t-d <DerivedCoreProperties-file>\n\n",
	"\t<PropList-file> is required when 1.1 < version < 3.1\n",
	"\t<DerivedCoreProperties-file> is required when version > 3.0\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;


# UTS #18: \w = [\p{Alphabetic}\p{M}\p{Nd}\p{Pc}]
# Unicode 1.1, \p{Alphabetic} = \p{L} => \w = [\p{L}\p{M}\p{Nd}\p{Pc}]
my $UnicodeData_property_value_regex
    = $version eq '1.1' ? qr/^(?:L.|M.|Nd|Pc)$/ : qr/^(?:M.|Nd|Pc)$/;

open UNICODE_DATA, "<$UnicodeData_filename"
    || die "ERROR opening '$UnicodeData_filename' for reading: $!";

# Parse UnicodeData(-X.X.X).txt
while (<UNICODE_DATA>)
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
	push @ranges, [ 0x4E00, 0x9FFF, $property_value ];
    }
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    elsif (/^([A-F0-9a-f]{4});<[^,]+, First>;([^;]+)/)
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
	    push @ranges, [ $range_begin, $prev_char_num, $property_value ]
		if ($range_category =~ $UnicodeData_property_value_regex);

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
	    push @ranges, [ $range_begin, $prev_char_num, $property_value ]
		if ($range_category =~ $UnicodeData_property_value_regex);

	    $range_begin = $char_num;
	    $range_category = $general_category;
	    $prev_char_num = $char_num;
	}
    }
}
push @ranges, [ $range_begin, $prev_char_num, $property_value ]
    if ($range_category =~ $UnicodeData_property_value_regex);

close UNICODE_DATA;

# Unicode 1.1 doesn't have any Connector_Punctuation defined - substituting
# underscore for this here:
# 005F;LOW LINE;So;0;ON;0020 0332;;;;N;SPACING UNDERSCORE;;;;
push @ranges, [ 0x005F, 0x005F, $property_value ]
    if ($version eq '1.1');

if ($version ne '1.1')
{   # Parse archaic PropList-X.X.X.txt format
    if ($version < 3.1)
    {
	open PROP_LIST, "<$PropList_filename"
	    || die "ERROR opening '$PropList_filename' for reading: $!";
	my $property;
	while (<PROP_LIST>)
	{
	    if (/Property\s+dump\s+for:\s+0x[0-9A-Fa-f]+\s+\((.+)\)/)
	    {
		$property = $1;
	    }
	    elsif (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4})/)
	    {
		my $start = hex($1);
		my $end
		    = hex($2) > $max_code_point ? $max_code_point : hex($2);
		
		next unless ($property =~ $PropList_property_value_regex);
		
		next if ($start > $max_code_point);
		
		if (($start < 0xD800 && $end < 0xD800)
		    || ($start > 0xDFFF && $end > 0xDFFF))
		{   # No surrogates involved
		    push @ranges, [ $start, $end, $property_value ];
		}
		else
		{
		    if ($start < 0xD800)
		    {   # Add a range for below the surrogate blocks
			push @ranges, [ $start, 0xD7FF, $property_value ];
		    }
		    if ($end > 0xDFFF)
		    {   # Add a range for above the surrogate blocks
			push @ranges, [ 0xE000, $end, $property_value ];
		    }
		}
	    }
	    elsif (/^([A-Fa-f0-9]{4})[^A-Fa-f0-9]/)
	    {
		my $start_and_end = hex($1);
		
		next unless ($property =~ $PropList_property_value_regex);
		
		if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
		{   # Skip surrogate block definitions
		    push @ranges,
			[ $start_and_end, $start_and_end, $property_value ];
		}
	    }
	}
	close PROP_LIST;
    }
    else
    {   # $version > 3.0: Parse DerivedCoreProperties(-X.X.X).txt
	open DERIVED_CORE_PROPERTIES, "<$DerivedCoreProperties_filename"
	    || die "ERROR opening '$DerivedCoreProperties_filename'"
		 . " for reading: $!";
	while (<DERIVED_CORE_PROPERTIES>)
	{
	    s/\s*\#.*//;
	    next unless (/\S/);
	    if (/^([A-Fa-f0-9]{4})\.\.([A-Fa-f0-9]{4,6})\s*;\s*(.*)/)
	    {   # 0009..000D    ; White_space # Cc   [5] <control>..<control>
		my $start = hex($1);
		next if ($start > $max_code_point);
		
		my $end
		    = hex($2) > $max_code_point ? $max_code_point : hex($2);
		my $property = $3;

		next unless ($property
			     =~ $DerivedCoreProperties_property_value_regex);

		if (($start < 0xD800 && $end < 0xD800)
		    || ($start > 0xDFFF && $end > 0xDFFF))
		{   # No surrogates involved
		    push @ranges, [ $start, $end, $property_value ];
		}
		else
		{
		    if ($start < 0xD800)
		    {   # Add a range for below the surrogate blocks
			push @ranges, [ $start, 0xD7FF, $property_value ];
		    }
		    if ($end > 0xDFFF)
		    {   # Add a range for above the surrogate blocks
			push @ranges, [ 0xE000, $end, $property_value ];
		    }
		}
	    }
	    elsif (/^([A-Fa-f0-9]{4})\s*;\s*(.*)/)
	    {   # 0020          ; White_space # Zs       SPACE
		my $start_and_end = hex($1);
		my $property = $2;

		next unless ($property
			     =~ $DerivedCoreProperties_property_value_regex);

		if ($start_and_end < 0xD800 || $start_and_end > 0xDFFF)
		{   # Skip surrogate block definitions
		    push @ranges,
			[$start_and_end, $start_and_end, $property_value];
		}
	    }
	}
	close DERIVED_CORE_PROPERTIES;
    }
}

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
	if ($range->[0] <= $merged_ranges[-1]->[1] + 1)
	{
	    $merged_ranges[-1]->[1] = $range->[1]
		if ($range->[1] > $merged_ranges[-1]->[1]);
	}
	else
	{
	    push @merged_ranges, $range;
	}
    }
}
@ranges = @merged_ranges;

my @complement_ranges = ();
for my $range_num (0 .. $#ranges)
{
    my $range = $ranges[$range_num];
    if ($range_num == 0)
    {
	if ($range->[0] > 0)
	{
	    push @complement_ranges,
		[ 0, $range->[0] - 1, "Not-$property_value" ];
	}
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
    if ($ranges[-1]->[1] < 0xD7FF)
    {   # Skip surrogate range
	push @complement_ranges,
	    [ $ranges[-1]->[1] + 1, 0xD7FF, "Not-$property_value" ];
	push @complement_ranges,
	    [ 0xE000, $max_code_point, "Not-$property_value" ];
    }
    else
    {
	push @complement_ranges,
	    [ $ranges[-1]->[1] + 1, $max_code_point, "Not-$property_value" ];
    }
}


for my $syntax_num (0 .. $#property_value_syntaxes)
{
    my $property_value_syntax = $property_value_syntaxes[$syntax_num];
    my $syntax_polarity = $property_value_polarity{$property_value_syntax};
    my $base_name = "Unicode${property_value_friendly_name}"
	          . ($syntax_num + 1) . "_${underscore_version}";
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

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

    print SPEC qq/${property_value_syntax} { /
	     . qq/setCurCharPropertyValue("/
	     . ($syntax_polarity eq 'negative' ? "Not-" : "")
             . qq/$property_value"); }\n/;
    print SPEC qq/[^${property_value_syntax}] { /
	     . qq/setCurCharPropertyValue("/
	     . ($syntax_polarity eq 'negative' ? "" : "Not-")
             . qq/$property_value"); }\n/;
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
}

1;
