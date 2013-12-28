#!/usr/bin/perl
#
# create.unicode-word-break.test.output.pl
#
# This script is designed to take as input DerivedAge.txt, and output
# hex char ranges and corresponding properties, for the BMP, excluding
# surrogates and U+FFFE and U+FFFF, in the format expected as output by the
# tests defined for the unicode-age test case in the JFlex test suite;
# an example line follows:
#
#   0000..0008; 1.1
#   0009..1000; unassigned
#
# A testsuite .test file is also output.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $input_filename = '';
my $default_property_value = 'Unassigned';
my $property_name = 'Age';
my %property_values = ();
my %property_values_to_skip = ( );
my %ranges = ();

GetOptions("version=s"=>\$version, "datafile=s"=>\$input_filename);

unless ($version && $input_filename
    && -f $input_filename && -r $input_filename)
{
    print STDERR "Usage: $0 -v <version> -d <data-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/; # there should only be one period: X.Y
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

    # 05C4          ; 2.0 #       HEBREW MARK UPPER DOT
    if (/^([A-F0-9a-f]{4})\s*;\s*([^;#\s]+)/)
    {
        my $char_num = hex($1);
        my $property_value = $2;
        ++$property_values{$property_value};
        if ($char_num < 0xD800 || $char_num >= 0xE000)
        {
            push @{$ranges{$property_value}}, [ $char_num, $char_num, $property_value ];
        }
    }
    # 0000..001F    ; 1.1 #  [32] <control-0000>..<control-001F>
    elsif (/^([A-F0-9a-f]{4})..([A-F0-9a-f]{4,5})\s*;\s*([^;#\s]+)/)
    {
        my $start_char_num = hex($1);
        my $end_char_num = hex($2);
        $end_char_num = $max_code_point if ($end_char_num > $max_code_point);
        if ($start_char_num <= $end_char_num)
        {
            my $property_value = $3;
            ++$property_values{$property_value};
            if (  ($start_char_num < 0xD800 && $end_char_num < 0xD800)
               || ($start_char_num >= 0xE000 && $end_char_num >= 0xE000))
            {
                push @{$ranges{$property_value}}, [ $start_char_num, $end_char_num, $property_value ];
            }
            else
            {   # Exclude surrogates
                if ($start_char_num < 0xD800)
                {
                    push @{$ranges{$property_value}}, [ $start_char_num, 0xD7FF, $property_value ];
                }
                if ($end_char_num >= 0xE000)
                {
                    push @{$ranges{$property_value}}, [ 0xE000, $end_char_num, $property_value ];
                }
            }
        }
    }
}
close IN;

sub versioncmp($$)
{
    return 0 if ($_[0] eq $_[1]);
    my ($amajor, $aminor) = ($_[0] =~ /^(\d+)\.(\d+)/);
    my ($bmajor, $bminor) = ($_[1] =~ /^(\d+)\.(\d+)/);
    return $amajor == $bmajor ? $aminor <=> $bminor : $amajor <=> $bmajor;
}

my %prev_age_unioned_ranges = ();
# Copy ranges and merge contiguous (same-age) ranges
for my $age (sort versioncmp keys %property_values)
{
    next if (versioncmp($age, $version) > 0);
    my $previous_range = [ -1, -1, ''];
    for my $range (sort { $a->[0] <=> $b->[0] } @{$ranges{$age}})
    {
        if (  $previous_range->[0] > -1
           && $range->[0] == $previous_range->[1] + 1)
        {
        
            $previous_range->[1] = $range->[1];
        }
        else
        {
            my $new_range = [ @$range ];
            push @{$prev_age_unioned_ranges{$age}}, $new_range;
            $previous_range = $new_range;
        }
    }
}
my $prev_age = '1.1';
for my $age (sort versioncmp keys %property_values) 
{
    next if ($age eq '1.1');
    last if (versioncmp($age, $version) > 0);
    # Add older versions' ranges to newer versions' ranges
    push @{$prev_age_unioned_ranges{$age}}, @{$prev_age_unioned_ranges{$prev_age}};
    $prev_age = $age;
}

my %all_merged_ranges = ();
for my $age (sort versioncmp keys %property_values)
{
    last if (versioncmp($age, $version) > 0);
    my $merged_ranges = [];
    $all_merged_ranges{$age} = $merged_ranges;
    # Merge contiguous ranges
    for my $range (sort { $a->[0] <=> $b->[0] } @{$prev_age_unioned_ranges{$age}})
    {
        if (0 == scalar(@$merged_ranges))
        {
            push @$merged_ranges, [ @$range ];
        }
        else
        {
            if ($range->[0] == $merged_ranges->[-1]->[1] + 1)
            {
                $merged_ranges->[-1]->[1] = $range->[1];
            }
            else
            {
                push @$merged_ranges, [ @$range ];
            }
        }
    }
}

# Create a test for each Age value 
for my $age (sort versioncmp keys %property_values)
{
    last if (versioncmp($age, $version) > 0);
    my $underscore_age = $age;
    $underscore_age =~ s/\./_/;
    my $output_file = "${base_name}_age_${underscore_age}.output";
    open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
    for my $range (@{$all_merged_ranges{$age}})
    {
        my ($start_char_num, $end_char_num) = @$range;
        printf OUTPUT "%04X..%04X; $property_name:$age\n",
                      $start_char_num, $end_char_num;
    }
    close OUTPUT;

    my $spec_file = "${base_name}_age_${underscore_age}.flex";
    open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
    print SPEC <<"__HEADER__";
%%

%unicode $version
%public
%class ${base_name}_age_${underscore_age}

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
__HEADER__

    print SPEC qq/\\p{$property_name:$age} { /
             . qq/setCurCharPropertyValue/
             . qq/("$property_name:$age"); }\n/;
    print SPEC "[^] { }\n";
    close SPEC;

    my $jflex_output_file = "${base_name}_age_${underscore_age}-flex.output";
    open JFLEX_OUTPUT, ">$jflex_output_file"
        || die "ERROR opening '$jflex_output_file': $!";
    close JFLEX_OUTPUT;

    my $test_file = "${base_name}_age_${underscore_age}.test";
    open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
    print TEST <<"__TEST__";
name: ${base_name}_age_${underscore_age}

description: 
Tests character class syntax of the Unicode ${version} ${property_name}=${age} property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

    close TEST;
}

# Create test files for Age:Unassigned
my $unassigned_output_file = "${base_name}_age_unassigned.output";
open UNASSIGNED_OUTPUT, ">$unassigned_output_file" 
    || die "ERROR opening '$unassigned_output_file': $!";
my $last_end_char_num = -1;
for my $range (sort { $a->[0] <=> $b->[0] } @{$all_merged_ranges{$version}})
{
    if ($last_end_char_num < 0xD800 && $range->[0] >= 0xE000)
    {
        # Don't output anything if the gap is exactly 0xD800..0xDFFF
        if ($last_end_char_num < 0xD7FF)
        {
            my ($start_char_num, $end_char_num) = ($last_end_char_num + 1, 0xD7FF);
            printf UNASSIGNED_OUTPUT "%04X..%04X; $property_name:$default_property_value\n",
                                     $start_char_num, $end_char_num;
        }
        if ($range->[0] > 0xE000)
        {
            my ($start_char_num, $end_char_num) = (0xE000, $range->[0] - 1);
            printf UNASSIGNED_OUTPUT "%04X..%04X; $property_name:$default_property_value\n",
                                     $start_char_num, $end_char_num;
        }
    }
    elsif ($range->[0] > 0)
    {
        my ($start_char_num, $end_char_num) = ($last_end_char_num + 1, $range->[0] - 1);
        printf UNASSIGNED_OUTPUT "%04X..%04X; $property_name:$default_property_value\n",
                                 $start_char_num, $end_char_num;
    }
    $last_end_char_num = $range->[1]; 
}
close UNASSIGNED_OUTPUT;

my $unassigned_spec_file = "${base_name}_age_unassigned.flex";
open UNASSIGNED_SPEC, ">$unassigned_spec_file"
    || die "ERROR opening '$unassigned_spec_file': $!";
print UNASSIGNED_SPEC <<"__UNASSIGNED_HEADER__";
%%

%unicode $version
%public
%class ${base_name}_age_unassigned

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
__UNASSIGNED_HEADER__

print UNASSIGNED_SPEC qq/\\p{$property_name:$default_property_value} { /
                    . qq/setCurCharPropertyValue/
                    . qq/("$property_name:$default_property_value"); }\n/;
print UNASSIGNED_SPEC "[^] { }\n";
close UNASSIGNED_SPEC;

my $unassigned_jflex_output_file = "${base_name}_age_unassigned-flex.output";
open UNASSIGNED_JFLEX_OUTPUT, ">$unassigned_jflex_output_file"
    || die "ERROR opening '$unassigned_jflex_output_file': $!";
close UNASSIGNED_JFLEX_OUTPUT;

my $unassigned_test_file = "${base_name}_age_unassigned.test";
open UNASSIGNED_TEST, ">$unassigned_test_file" 
    || die "ERROR opening '$unassigned_test_file': $!";
print UNASSIGNED_TEST <<"__UNASSIGNED_TEST__";
name: ${base_name}_age_unassigned

description: 
Tests character class syntax of the Unicode $version ${property_name}=${default_property_value} property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__UNASSIGNED_TEST__

close UNASSIGNED_TEST;


# Create a test for [\p{Age:X}--\p{Age:X-1}], where 1.1 < X <= $version 
my %age_to_prev_age = ();
$prev_age = '1.1';
for my $age (sort versioncmp keys %property_values)
{
    next if ($age eq '1.1');
    $age_to_prev_age{$age} = $prev_age;
    $prev_age = $age;
}
my $subtraction_output_file = "${base_name}_age_subtraction.output";
open SUBTRACTION_OUTPUT, ">$subtraction_output_file" 
    || die "ERROR opening '$subtraction_output_file': $!";
for my $range (sort { $a->[0] <=> $b->[0] } @{$prev_age_unioned_ranges{$version}})
{
    my ($start_char_num, $end_char_num, $age) = @$range;
    next if ($age eq '1.1');
    my $prev_age = $age_to_prev_age{$age};
    printf SUBTRACTION_OUTPUT "%04X..%04X; [\\p{$property_name:$age}--\\p{$property_name:$prev_age}]\n",
                              $start_char_num, $end_char_num;
}
close SUBTRACTION_OUTPUT;

my $subtraction_spec_file = "${base_name}_age_subtraction.flex";
open SUBTRACTION_SPEC, ">$subtraction_spec_file"
    || die "ERROR opening '$subtraction_spec_file': $!";
print SUBTRACTION_SPEC <<"__SUBTRACTION_HEADER__";
%%

%unicode $version
%public
%class ${base_name}_age_subtraction

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
__SUBTRACTION_HEADER__

$prev_age = '1.1';
for my $age (sort versioncmp keys %property_values)
{
    next if ($age eq '1.1');
    last if (versioncmp($age, $version) > 0);

    print SUBTRACTION_SPEC qq/[\\p{$property_name:$age}--\\p{$property_name:$prev_age}] { /
                         . qq/setCurCharPropertyValue/
                         . qq/("[\\\\p{$property_name:$age}--\\\\p{$property_name:$prev_age}]"); }\n/;
    $prev_age = $age;
}
print SUBTRACTION_SPEC "[^] { }\n";
close SUBTRACTION_SPEC;

my $subtraction_jflex_output_file = "${base_name}_age_subtraction-flex.output";
open SUBTRACTION_JFLEX_OUTPUT, ">$subtraction_jflex_output_file"
    || die "ERROR opening '$subtraction_jflex_output_file': $!";
close SUBTRACTION_JFLEX_OUTPUT;

my $subtraction_test_file = "${base_name}_age_subtraction.test";
open SUBTRACTION_TEST, ">$subtraction_test_file" 
    || die "ERROR opening '$subtraction_test_file': $!";
print SUBTRACTION_TEST <<"__SUBTRACTION_TEST__";
name: ${base_name}_age_subtraction

description: 
Tests subtracting ${property_name} Unicode property values in character sets 
for Unicode ${version}, e.g. [\\p{${property_name}:2.0}--\\p{${property_name}:1.1}].

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__SUBTRACTION_TEST__

close SUBTRACTION_TEST;

1;
