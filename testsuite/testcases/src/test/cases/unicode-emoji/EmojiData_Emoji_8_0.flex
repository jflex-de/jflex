%%

%unicode 8.0
%public
%class EmojiData_Emoji_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
