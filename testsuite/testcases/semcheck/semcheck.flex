%%

%class Semcheck
%ignorecase

%%

"foo" / [A-Z]	{  }
~"foo" / "bar"  {  }
"bar" / !"foo"  {  }
!"foo" / "bar"  {  }

