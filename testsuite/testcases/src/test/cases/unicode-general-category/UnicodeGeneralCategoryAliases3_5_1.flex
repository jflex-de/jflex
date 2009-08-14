%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases3_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Control} { setCurCharBlock("Cc"); }
\p{Format} { setCurCharBlock("Cf"); }
\p{Unassigned} { setCurCharBlock("Cn"); }
\p{Private_Use} { setCurCharBlock("Co"); }
\p{Lowercase_Letter} { setCurCharBlock("Ll"); }
\p{Modifier_Letter} { setCurCharBlock("Lm"); }
\p{Other_Letter} { setCurCharBlock("Lo"); }
\p{Titlecase_Letter} { setCurCharBlock("Lt"); }
\p{Uppercase_Letter} { setCurCharBlock("Lu"); }
\p{Spacing_Mark} { setCurCharBlock("Mc"); }
\p{Enclosing_Mark} { setCurCharBlock("Me"); }
\p{Nonspacing_Mark} { setCurCharBlock("Mn"); }
\p{Decimal_Number} { setCurCharBlock("Nd"); }
\p{Letter_Number} { setCurCharBlock("Nl"); }
\p{Other_Number} { setCurCharBlock("No"); }
\p{Connector_Punctuation} { setCurCharBlock("Pc"); }
\p{Dash_Punctuation} { setCurCharBlock("Pd"); }
\p{Close_Punctuation} { setCurCharBlock("Pe"); }
\p{Final_Punctuation} { setCurCharBlock("Pf"); }
\p{Initial_Punctuation} { setCurCharBlock("Pi"); }
\p{Other_Punctuation} { setCurCharBlock("Po"); }
\p{Open_Punctuation} { setCurCharBlock("Ps"); }
\p{Currency_Symbol} { setCurCharBlock("Sc"); }
\p{Modifier_Symbol} { setCurCharBlock("Sk"); }
\p{Math_Symbol} { setCurCharBlock("Sm"); }
\p{Other_Symbol} { setCurCharBlock("So"); }
\p{Line_Separator} { setCurCharBlock("Zl"); }
\p{Paragraph_Separator} { setCurCharBlock("Zp"); }
\p{Space_Separator} { setCurCharBlock("Zs"); }
