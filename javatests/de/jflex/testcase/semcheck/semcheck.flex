package de.jflex.testcase.semcheck;
%%

%class Semcheck
%ignorecase

%%

"foo" / [A-Z]	{  }
~"foo" / "bar"  {  }
"bar" / !"foo"  {  }
!"foo" / "bar"  {  }

