#!/usr/bin/perl
#
# create.caseless.test.files.pl
#
# This script is designed to take as input UnicodeData(-X.X.X).txt, and output
# a JFlex test spec to produce output of the following form, when taking as
# input a file with each code point in the BMP, except the surrogates and
# U+FFFE and U+FFFF:
#
#   input char 0060 matches 0040 case-insensitively
#
# The expected output in the above form is also output, as a separate file.
#

use strict;
use warnings;
use Getopt::Long;


my $version = '';
my $data = '';

GetOptions("version=s"=>\$version, "data=s"=>\$data);

unless ($version && $data && -f $data && -r $data)
{
    print STDERR "Usage: $0 -v <version> -d <unicode-data-file>\n";
    exit(1);
}

my $underscore_version = $version;
$underscore_version =~ s/\./_/g;

my $spec_file = "UnicodeCaseless_$underscore_version.flex";
my $output_file = "UnicodeCaseless_$underscore_version.output";

open IN, "<$data" || die "ERROR opening '$data' for reading: $!";

open OUTPUT, ">$output_file" || die "ERROR opening '$output_file': $!";
print OUTPUT "\n";

open SPEC, ">$spec_file" || die "ERROR opening '$spec_file': $!";
print SPEC <<"SPEC_HEADER";
%%

%unicode $version
%public
%class UnicodeCaseless_$underscore_version

%type int
%caseless
%standalone

%{
  void print(int codePoint) {
    System.out.format("input char %04X matches %04X case-insensitively\%n",
                      (int)yycharat(0), codePoint);
  }
%}

%%

<<EOF>> { return 1; }
SPEC_HEADER

my %equivalents = ();
my %mapped = ();
while (<IN>)
{
    chomp;
    s/^\s*#.*//;
    next unless (/\S/);

    # 0000;<control>;Cc;0;ON;;;;;N;;;;;
    # AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;
    if (/^([A-F0-9a-f]{4});(?:[^;]*;){11}([^;]*);([^;]*);([^;]*)/)
    {
	my $code_point = $1;
	my $uppercase_mapping = $2;
	my $lowercase_mapping = $3;
	my $titlecase_mapping = $4;
	next unless
	    ($uppercase_mapping or $lowercase_mapping or $titlecase_mapping);

	my %equiv = ($code_point => 1);
	if ($mapped{$code_point})
	{
	    $equiv{$mapped{$code_point}} = 1;
	}
	if ($uppercase_mapping)
	{
	    $equiv{$uppercase_mapping} = 1;
	    if ($mapped{$uppercase_mapping})
	    {
		$equiv{$mapped{$uppercase_mapping}} = 1;
	    }
	}
	if ($lowercase_mapping)
	{
	    $equiv{$lowercase_mapping} = 1;
	    if ($mapped{$lowercase_mapping})
	    {
		$equiv{$mapped{$lowercase_mapping}} = 1;
	    }
	}
	if ($titlecase_mapping)
	{
	    $equiv{$titlecase_mapping} = 1;
	    if ($mapped{$titlecase_mapping})
	    {
		$equiv{$mapped{$titlecase_mapping}} = 1;
	    }
	}
	my @sorted = sort { hex($a) <=> hex($b) } keys %equiv;
	my $lowest = $sorted[0];
	for my $entry (@sorted)
	{
	    $mapped{$entry} = $lowest;
	}
	print "$lowest: \[ ", (join ", ", @sorted), " \]\n";
    }
}
close IN;

for my $code_point (sort { hex($a) <=> hex($b) } keys %mapped)
{
    my $target = $mapped{$code_point};
    print SPEC qq/"\\u$code_point" { print(0x$code_point); }\n/;
    print OUTPUT "input char $code_point matches $target case-insensitively\n";
}

print SPEC "[^] { }\n";

close SPEC;
close OUTPUT;

print "Wrote $spec_file and $output_file.\n";
