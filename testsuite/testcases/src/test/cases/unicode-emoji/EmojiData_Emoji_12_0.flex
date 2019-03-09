%%

%unicode 12.0
%public
%class EmojiData_Emoji_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
