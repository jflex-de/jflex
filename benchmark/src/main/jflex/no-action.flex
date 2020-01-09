package de.jflex.benchmark;

/*
  A scanner with minimal action code, to measure inner matching loop
  performance.
*/

%%

%public
%class NoAction

%int

%{
  private int matches;
%}

SHORT = "a"
LONG  = "b"+

%%

{SHORT}  { matches++; }
{LONG}   { matches++; }

[^]      { /* nothing */ }

<<EOF>>  { return matches; }
