%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases4_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Control} { setCurCharBlock("Cc"); }
\p{gc:Format} { setCurCharBlock("Cf"); }
\p{gc:Unassigned} { setCurCharBlock("Cn"); }
\p{gc:Private_Use} { setCurCharBlock("Co"); }
\p{gc:Lowercase_Letter} { setCurCharBlock("Ll"); }
\p{gc:Modifier_Letter} { setCurCharBlock("Lm"); }
\p{gc:Other_Letter} { setCurCharBlock("Lo"); }
\p{gc:Titlecase_Letter} { setCurCharBlock("Lt"); }
\p{gc:Uppercase_Letter} { setCurCharBlock("Lu"); }
\p{gc:Spacing_Mark} { setCurCharBlock("Mc"); }
\p{gc:Enclosing_Mark} { setCurCharBlock("Me"); }
\p{gc:Nonspacing_Mark} { setCurCharBlock("Mn"); }
\p{gc:Decimal_Number} { setCurCharBlock("Nd"); }
\p{gc:Letter_Number} { setCurCharBlock("Nl"); }
\p{gc:Other_Number} { setCurCharBlock("No"); }
\p{gc:Connector_Punctuation} { setCurCharBlock("Pc"); }
\p{gc:Dash_Punctuation} { setCurCharBlock("Pd"); }
\p{gc:Close_Punctuation} { setCurCharBlock("Pe"); }
\p{gc:Final_Punctuation} { setCurCharBlock("Pf"); }
\p{gc:Initial_Punctuation} { setCurCharBlock("Pi"); }
\p{gc:Other_Punctuation} { setCurCharBlock("Po"); }
\p{gc:Open_Punctuation} { setCurCharBlock("Ps"); }
\p{gc:Currency_Symbol} { setCurCharBlock("Sc"); }
\p{gc:Modifier_Symbol} { setCurCharBlock("Sk"); }
\p{gc:Math_Symbol} { setCurCharBlock("Sm"); }
\p{gc:Other_Symbol} { setCurCharBlock("So"); }
\p{gc:Line_Separator} { setCurCharBlock("Zl"); }
\p{gc:Paragraph_Separator} { setCurCharBlock("Zp"); }
\p{gc:Space_Separator} { setCurCharBlock("Zs"); }
