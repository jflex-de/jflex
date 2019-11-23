package jflex.testcase.large_input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
%%

%public
%class LargeInputScanner
%line
%char
%unicode
%type State

%% 

^.+ "\n" { if (yychar > Integer.MAX_VALUE) {return State.AFTER_2GB;} }

<<EOF>>    { return State.END_OF_FILE; }
