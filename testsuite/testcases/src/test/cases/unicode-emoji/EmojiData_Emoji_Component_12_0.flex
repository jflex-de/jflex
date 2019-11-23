%%

%unicode 12.0
%public
%class EmojiData_Emoji_Component_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji_Component} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
