%%

%unicode 10.0
%public
%class EmojiData_Emoji_Component_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji_Component} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
