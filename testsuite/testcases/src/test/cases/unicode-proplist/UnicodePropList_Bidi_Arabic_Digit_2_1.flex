%%

%unicode 2.1
%public
%class UnicodePropList_Bidi_Arabic_Digit_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Arabic Digit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
