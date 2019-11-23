package jflex.testcase.charclass;

%%

BadRange = [b-a]

%%

{BadRange}	 		{ }
[^] { }
