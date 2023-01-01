%%

// Tests dot file generation (no particular DFA, just any)
// see also https://github.com/jflex-de/jflex/issues/769

%public
%class Dot
%int

%%

[^] { return 1; }
