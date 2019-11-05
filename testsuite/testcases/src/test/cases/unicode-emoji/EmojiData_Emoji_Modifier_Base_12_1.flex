%%

%unicode 12.1
%public
%class EmojiData_Emoji_Modifier_Base_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji_Modifier_Base} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
