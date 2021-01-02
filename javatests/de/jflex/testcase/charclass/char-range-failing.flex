package de.jflex.testcase.charclass;

%%

BadRange = [b-a]

%%

{BadRange}	 		{ }
[^] { }
