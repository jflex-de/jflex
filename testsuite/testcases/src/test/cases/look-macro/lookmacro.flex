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

