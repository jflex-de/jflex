%%

%unicode 11.0
%public
%class EmojiData_Extended_Pictographic_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Extended_Pictographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
