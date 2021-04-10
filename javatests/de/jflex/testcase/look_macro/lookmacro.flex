package de.jflex.testcase.look_macro;
%%
%public
%class Lookmacro
%int
%debug 

foo="<foo>"|":"
bar=([:letter:])+ 

%%

{bar}/{foo} { return 1; }
(.) { return 0; }

