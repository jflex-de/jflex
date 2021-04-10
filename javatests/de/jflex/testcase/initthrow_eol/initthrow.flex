package de.jflex.testcase.initthrow_eol;

import java.io.IOException;


%%

%class Initthrow
%int
%initthrow   IOException      


%init{
  if (!in.ready()) {
    throw new UnsupportedOperationException("Input reader is not ready");
  }
%init}

%%

dummy { }
