%%

%unicode 3.0
%public
%class UnicodePropList_Bidi_Arabic_Digit_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Bidi: Arabic Digit} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
