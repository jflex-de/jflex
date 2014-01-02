#!/bin/bash
#
# First, download the zipped UCD (excluding the Unihan database)
# from the public Unicode.org site, using something like:
#
#    PROMPT$ mkdir ~/temp/Unicode-<version> && cd ~/temp/Unicode-<version>
#    PROMPT$ wget ftp://www.unicode.org/Public/zipped/<version>/UCD.zip
#
# Then unzip the file.
#
# Next, run this script (using the X.Y version string), e.g.:
# 
#    PROMPT$ src/test/sh/create.all.unicode.test.case.files.sh  6.3  /home/s/temp/Unicode-6.3
#
# which will populate the directories under src/test/cases/unicode-*/ 
# with test files for the given Unicode version.
#
# Next, svn add the newly created test files, using something like:
#
#   PROMPT$ svn add src/test/cases/*/*<underscore-version>*
#
# Next, run all tests:
#
#   PROMPT$ mvn test
#
# One test should fail: the new caseless test.  You can make it succeed
# by capturing the stderr output from the failing test, editing
# it to remove everything except the jflex output from the failing
# test, then saving the result to src/test/cases/unicode-caseless/
# as UnicodeCaseless_<underscore-version>-flex.output .  The first
# line in this file should be blank, and the backslashes should be
# converted to forward slashes (except in the \uXXXX unicode char
# escapes).
#

if [[ -z "$1" || -z "$2" ]]; then
    echo "Usage: $0 <Unicode-version> <Unicode-ucd-dir>"
    exit 1;
fi

scriptdir=${0%/*}
cd $scriptdir
cd ..

version="$1"
ucd="$2"

cd cases/unicode-blocks/
../../perl/create.unicode-blocks.test.case.files.pl -v $version -b $ucd/Blocks.txt

cd ../unicode-caseless/
../../perl/create.unicode-caseless.test.files.pl -v $version -d $ucd/UnicodeData.txt 

cd ../unicode-compatibility-properties/
../../perl/create.unicode-compatibility-properties.test.case.files.pl -v $version -u $ucd/UnicodeData.txt -p $ucd/PropList.txt -d $ucd/DerivedCoreProperties.txt 

cd ../unicode-derived-core-properties/
../../perl/create.unicode-derived-core-properties.test.case.files.pl -v $version -d $ucd/DerivedCoreProperties.txt 

cd ../unicode-digit/
../../perl/create.unicode-digit.test.case.files.pl -v $version -d $ucd/UnicodeData.txt 

cd ../unicode-general-category/
../../perl/create.unicode-general-category.test.case.files.pl -v $version -d $ucd/UnicodeData.txt 

cd ../unicode-grapheme-break/
../../perl/create.unicode-grapheme-break.test.case.files.pl -v $version -d $ucd/auxiliary/GraphemeBreakProperty.txt 

cd ../unicode-letter/
../../perl/create.unicode-letter.test.case.files.pl -v $version -d $ucd/UnicodeData.txt 

cd ../unicode-line-break/
../../perl/create.unicode-line-break.test.case.files.pl -v $version -l $ucd/LineBreak.txt 

cd ../unicode-lowercase/
../../perl/create.unicode-lowercase.test.case.files.pl -v $version -d $ucd/DerivedCoreProperties.txt 

cd ../unicode-misc-properties/
../../perl/create.unicode-misc-properties.test.case.files.pl -v $version -d $ucd/UnicodeData.txt 

cd ../unicode-proplist/
../../perl/create.unicode-proplist.test.case.files.pl -v $version -p $ucd/PropList.txt 

cd ../unicode-scripts/
if [[ $version =~ ^[1-5]\. ]]; then  # Major version <= 5
    ../../perl/create.unicode-scripts.test.case.files.pl -v $version -s $ucd/Scripts.txt 
else
    ../../perl/create.unicode-scripts.test.case.files.pl -v $version -s $ucd/Scripts.txt -e $ucd/ScriptExtensions.txt -a $ucd/PropertyValueAliases.txt
fi

cd ../unicode-sentence-break/
../../perl/create.unicode-sentence-break.test.case.files.pl -v $version -d $ucd/auxiliary/SentenceBreakProperty.txt 

cd ../unicode-space/
../../perl/create.unicode-space.test.case.files.pl -v $version -d $ucd/PropList.txt 

cd ../unicode-uppercase/
../../perl/create.unicode-uppercase.test.case.files.pl -v $version -d $ucd/DerivedCoreProperties.txt 

cd ../unicode-word/
../../perl/create.unicode-word.test.case.files.pl -v $version -u $ucd/UnicodeData.txt -d $ucd/DerivedCoreProperties.txt 

cd ../unicode-word-break/
../../perl/create.unicode-word-break.test.case.files.pl -v $version -d $ucd/auxiliary/WordBreakProperty.txt
 
cd ../unicode-age/
../../perl/create.unicode-age.test.case.files.pl -v $version -d $ucd/DerivedAge.txt
