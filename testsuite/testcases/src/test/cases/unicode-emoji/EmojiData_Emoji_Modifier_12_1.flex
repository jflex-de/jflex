%%

%unicode 12.1
%public
%class EmojiData_Emoji_Modifier_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji_Modifier} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
