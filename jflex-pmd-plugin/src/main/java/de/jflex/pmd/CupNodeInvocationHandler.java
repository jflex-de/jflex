package de.jflex.pmd;

import java_cup.runtime.Symbol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CupNodeInvocationHandler implements InvocationHandler {
  private final JFlexParser parser;
  private final Symbol node;

  public CupNodeInvocationHandler(JFlexParser jFlexParser, Symbol node) {
    this.parser = jFlexParser;
    this.node = node;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    final String methodName = method.getName();
    throw new UnsupportedOperationException("Method " + method + " not supported on cup symbol");
  }
}
