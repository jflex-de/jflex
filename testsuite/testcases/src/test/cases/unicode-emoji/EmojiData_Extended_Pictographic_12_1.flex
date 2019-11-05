%%

%unicode 12.1
%public
%class EmojiData_Extended_Pictographic_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Extended_Pictographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
