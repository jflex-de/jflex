%%

%unicode 10.0
%public
%class EmojiData_Emoji_Presentation_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Emoji_Presentation} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
