class Sample {
    public static void main(String argv[]) throws java.io.IOException {
	Yylex yy = new Yylex(System.in);
	Yytoken t;
	while ((t = yy.yylex()) != null)
	    System.out.println(t);
    }
}

class Yytoken {
  Yytoken (int index, String text) {
	m_index = index;
	m_text = text;
  }

  public int m_index;
  public String m_text;
  public String toString() {
      return "Token #"+m_index+": "+m_text;
  }
}

%%

%state IN_ATTRIBUTE
%state IN_TAG
%state IN_DATA
%state IN_CDATA

ALPHA=[A-Za-z]
DIGIT=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGIT}
WHITE_SPACE=([\ \n\r\t\f])+

%% 

<YYINITIAL> {WHITE_SPACE} { }
<YYINITIAL> "<" { 
	yybegin(IN_TAG);
	return (new Yytoken(0, yytext())); 
}

<IN_TAG> {WHITE_SPACE} { }
<IN_TAG> "![CDATA[" {
	yybegin(IN_CDATA);
	return (new Yytoken(1, yytext()));
}
<IN_TAG> ({ALPHA_NUMERIC}|["-_."])+ {
	return (new Yytoken(2, yytext()));
}
<IN_TAG> "=" {
	return (new Yytoken(3, yytext()));
}
<IN_TAG> "\"" {
	yybegin(IN_ATTRIBUTE);
	return (new Yytoken(4, yytext()));
}
<IN_TAG> "/" {
	return (new Yytoken(5, yytext()));
}
<IN_TAG> ">" {
	yybegin(IN_DATA);
	return (new Yytoken(6, yytext()));
}

<IN_ATTRIBUTE> ([^\"])* {
	return (new Yytoken(7, yytext()));
}
<IN_ATTRIBUTE> "\"" {
	yybegin(IN_TAG);
	return (new Yytoken(3, yytext()));
}

<IN_DATA> ([^<])* {
	return (new Yytoken(8, yytext()));
}
<IN_DATA> "<" {
	yybegin(IN_TAG);	
	return (new Yytoken(0, yytext()));
}

<IN_CDATA> "]]>" {
	yybegin(IN_DATA);	
	return (new Yytoken(9, yytext()));
}
<IN_CDATA> ([^"]>"]|"]"[^"]"]|[^"]"]">"|"]]"[^">"]|[^"]"]"]>")* {
	return (new Yytoken(10, yytext()));
}
