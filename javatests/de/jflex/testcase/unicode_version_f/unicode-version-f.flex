package de.jflex.testcase.unicode_version_f;
%%
%unicode 1.29.4

%public
%class UnicodeVersionF

%% 

. {
  System.out.println("Character: <" + yytext() + ">");
}

