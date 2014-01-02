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
# If provided, the ScriptExtensions.txt input file is also used to output
# tests for the Script_Extension property,
#
# A teststuite .test file is also output.
#

use strict;
use warnings;
use Getopt::Long;

my $max_code_point = 0xFFFD;

my $version = '';
my $scripts_filename = '';
my $extensions_filename = '';
my $property_value_aliases_filename = '';
my $default_property_value = 'Unknown';
my %property_values = ( $default_property_value => 1 );
my $property_values_to_skip_regex = qr/surrogate/i;
my @ranges = ();

GetOptions("version=s"=>\$version,
           "s=s"=>\$scripts_filename,
           "e:s"=>\$extensions_filename,
           "a:s"=>\$property_value_aliases_filename);

unless (  $version && $scripts_filename
           && -f $scripts_filename && -r $scripts_filename
           && (  ($extensions_filename eq '' && $property_value_aliases_filename eq '')
              || (  -f $extensions_filename && -r $extensions_filename
                 && -f $property_value_aliases_filename && -r $property_value_aliases_filename)))
{
    print STDERR "Usage: $0 -v <version> -s <Scripts-file>",
                 " [ -e <ScriptExtensions-file> -a <PropertyValueAliases-file> ]\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;
my $base_name = "UnicodeScripts_${underscore_version}";

my $prev_char_num = -2;
my $range_begin = -1;
my $range_property_value = '';
my $propname = 'Script';

open IN, "<$scripts_filename"
    || die "ERROR opening '$scripts_filename' for reading: $!";

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

%include ../../resources/common-unicode-enumerated-property-java

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

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST__

close TEST;

exit(0) if ($extensions_filename eq '');

$base_name .= '_extensions_';
$propname = 'Script_Extensions';
my %disjoint_scripts = map { $_ => 1 } keys %property_values;
my %overlapping_scripts = ();
my %alias2canonical_script = ();
my @extension_code_point = ();
my %extension_ranges = ();
my %merged_extension_ranges = ();

# Get the Script property value aliases
open IN, "<$property_value_aliases_filename"
    || die "ERROR opening '$property_value_aliases_filename for reading: $!";
while (<IN>)
{
    chomp;
    s/\s*#.*//;
    s/\s+//;
    next unless (/^\s*sc\s*;\s*(\S+)\s*;\s*(\S+)((?:\s*;\s*\S+)*)/);
    my $alias = $1;
    my $canonical_script = $2;
    my $other_script_aliases = $3;
    $alias2canonical_script{$canonical_script} = $canonical_script;
    $alias2canonical_script{$alias} = $canonical_script;
    if ($other_script_aliases)
    {
        $other_script_aliases =~ s/^\s*;\s*//;
        for my $other_alias (split /\s*;\s*/, $other_script_aliases)
        {
            $alias2canonical_script{$other_alias} = $canonical_script;
        }
    }
}
close IN;

open IN, "<$extensions_filename"
    || die "ERROR opening '$extensions_filename' for reading: $!";

while (<IN>)
{
    chomp;
    s/\s*#.*//;
    s/\s+$//; # Strip trailing space
    next unless (/\S/);

    # 3037 ; Bopo Hang Hani Hira Kana # So  IDEOGRAPHIC TELEGRAPH LINE FEED SEPARATOR SYMBOL
    if (/^([A-F0-9a-f]{4})\s*;\s*([^#]+)/)
    {
        my $start = hex($1);
        my $end = $start;
        ++$extension_code_point[$start];
        my $scripts = $2;
        next if ($start > $max_code_point);
        $scripts =~ s/\s+$//; # Trim trailing whitespace
        my @canonical_scripts = map { $alias2canonical_script{$_} } 
                                split /\s+/, $scripts;
        for my $script (@canonical_scripts) 
        {
            push @{$extension_ranges{$script}}, [ $start, $end, $script ];
            delete $disjoint_scripts{$script};
            $overlapping_scripts{$script} = {}
                unless exists($overlapping_scripts{$script});
            for my $other_script (grep { $_ ne $script } @canonical_scripts)
            { 
                $overlapping_scripts{$script}->{$other_script} = 1;
            } 
        }
    }
    # 1802..1803    ; Mong Phag # Po   [2] MONGOLIAN COMMA..MONGOLIAN FULL STOP
    elsif (/^([A-F0-9a-f]{4})..([A-F0-9a-f]{4,5})\s*;\s*([^#]+)/)
    {
        my $start = hex($1);
        my $end = hex($2);
        my $scripts = $3;
        next if ($start > $max_code_point);
        $end = $max_code_point if ($end > $max_code_point);
        for my $ch ($start..$end)
        {
            ++$extension_code_point[$ch];
        }
        $scripts =~ s/\s+$//; # Trim trailing whitespace
        my @canonical_scripts = map { $alias2canonical_script{$_} } 
                                split /\s+/, $scripts;
        for my $script (@canonical_scripts) 
        {
            push @{$extension_ranges{$script}}, [ $start, $end, $script ];
            delete $disjoint_scripts{$script};
            $overlapping_scripts{$script} = {}
                unless exists($overlapping_scripts{$script});
            for my $other_script (grep { $_ ne $script } @canonical_scripts)
            { 
                $overlapping_scripts{$script}->{$other_script} = 1;
            } 
        }
    }
}
close IN;

# Add Script property values for code points missing from ScriptExtensions.txt
my %mandatory_test_scripts = map { $_ => 1 } ('Inherited', 'Common', 'Unknown');
for my $range (@merged_ranges)
{
    my ($start, $end, $script) = @$range;
    for my $ch ($start..$end)
    {
        unless ($extension_code_point[$ch])
        {
            push @{$extension_ranges{$script}}, [ $ch, $ch, $script ];
        }
    }
 }

# Merge contiguous ranges
for my $script (sort keys %extension_ranges)
{
    if (not exists($merged_extension_ranges{$script}))
    {
        $merged_extension_ranges{$script} = [];
    }
    for my $range (sort { $a->[0] <=> $b->[0] } @{$extension_ranges{$script}}) 
    {
        if (  scalar(@{$merged_extension_ranges{$script}}) > 0
           && (  $range->[0] == $merged_extension_ranges{$script}->[-1]->[1]
              || $range->[0] == $merged_extension_ranges{$script}->[-1]->[1] + 1))
        {
            $merged_extension_ranges{$script}->[-1]->[1] = $range->[1];
        }
        else
        {
            push @{$merged_extension_ranges{$script}}, [ @$range ];
        }
    }
}

## Output disjoint scripts, i.e. those missing from
## ScriptExtensions.txt, as a single test

$output_file = "${base_name}missing.output";
open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
for my $range (sort { $a->[0] <=> $b->[0]}
               map { @{$merged_extension_ranges{$_}} } 
               keys %disjoint_scripts) 
{
    my ($start, $end, $script) = @$range;
    printf OUTPUT "%04X..%04X; ${propname}:${script}\n", $start, $end;
}
close OUTPUT;
for my $script (keys %disjoint_scripts)
{
    delete $merged_extension_ranges{$script};
}

$spec_file = "${base_name}missing.flex";
open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
print SPEC <<"__MISSING_HEADER__";
%%

%unicode $version
%public
%class ${base_name}missing

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
__MISSING_HEADER__

for my $property_value (sort keys %property_values)
{
    next if ($property_value =~ $property_values_to_skip_regex);
    next unless (exists($disjoint_scripts{$property_value}));
    print SPEC qq/\\p{${propname}:${property_value}} { /
             . qq/setCurCharPropertyValue("${propname}:${property_value}"); }\n/;
}
print SPEC "[^] { }\n";
close SPEC;

$jflex_output_file = "${base_name}missing-flex.output";
open JFLEX_OUTPUT, ">$jflex_output_file"
    || die "ERROR opening '$jflex_output_file': $!";
close JFLEX_OUTPUT;

$test_file = "${base_name}missing.test";
open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
print TEST <<"__TEST_MISSING__";
name: ${base_name}missing

description: 
Tests character class syntax of the Unicode $version ${propname}
property, for those scripts missing from ScriptExtensions.txt, which
are defined in Scripts.txt.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST_MISSING__

close TEST;

## Find groups of disjoint scripts and output each group as a test
for (my $test_num = 1                          ; 
     scalar(keys %merged_extension_ranges) > 0 ;
     ++$test_num                               )
{
    my $seed_script = (sort keys %merged_extension_ranges)[0];
    my $seed_script_ranges = $merged_extension_ranges{$seed_script};
    my %script_group = ( $seed_script => 1 );
    OTHER_SCRIPT: for my $other_script (sort keys %merged_extension_ranges) 
    {
       next if (exists($script_group{$other_script}));
       for my $grouped_script (sort keys %script_group)
       {
           next OTHER_SCRIPT
               if (exists($overlapping_scripts{$grouped_script}
                                             ->{$other_script}));
       }
       $script_group{$other_script} = 1;
    }

    $output_file = "${base_name}${test_num}.output";
    open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
    for my $range (sort { $a->[0] <=> $b->[0]}
                   map { @{$merged_extension_ranges{$_}} } 
                   keys %script_group) 
    {
        my ($start, $end, $property_value) = @$range;
        printf OUTPUT "%04X..%04X; ${propname}:${property_value}\n", $start, $end;
    }
    close OUTPUT;
    for my $script (keys %script_group)
    {
        delete $merged_extension_ranges{$script};
    }

    $spec_file = "${base_name}${test_num}.flex";
    open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
    print SPEC <<"__DISJOINT_GROUP_HEADER__";
%%

%unicode $version
%public
%class ${base_name}${test_num}

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
__DISJOINT_GROUP_HEADER__

    for my $script (sort keys %script_group)
    {
        print SPEC qq/\\p{${propname}:${script}} { /
                 . qq/setCurCharPropertyValue("${propname}:${script}"); }\n/;
    }
    print SPEC "[^] { }\n";
    close SPEC;

    $jflex_output_file = "${base_name}${test_num}-flex.output";
    open JFLEX_OUTPUT, ">$jflex_output_file"
        || die "ERROR opening '$jflex_output_file': $!";
    close JFLEX_OUTPUT;

    $test_file = "${base_name}${test_num}.test";
    open TEST, ">$test_file" || die "ERROR opening '$test_file': $!";
    print TEST <<"__TEST_DISJOINT_GROUP__";
name: ${base_name}${test_num}

description: 
Tests character class syntax of the Unicode $version ${propname} property.

jflex: -q --noinputstreamctor

input-file-encoding: UTF-8

common-input-file: ../../resources/All.Unicode.BMP.characters.input

__TEST_DISJOINT_GROUP__

    close TEST;
}

1;
