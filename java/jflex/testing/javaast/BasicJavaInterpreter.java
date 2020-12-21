package jflex.testing.javaast;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.joining;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class BasicJavaInterpreter {
  private BasicJavaInterpreter() {}

  public static ImmutableMap<String, Object> parseJavaClass(File javaSrc) throws IOException {
    assertThat(javaSrc.exists()).isTrue();

    TypeDeclaration classDeclaration = doParseJavaClass(javaSrc);
    ImmutableMap<SimpleName, Object> fields =
        BasicJavaInterpreter.interpretValues(BasicJavaInterpreter.getJavaFields(classDeclaration));

    ImmutableMap.Builder<String, Object> classData = ImmutableMap.builder();
    for (Map.Entry<SimpleName, Object> field : fields.entrySet()) {
      classData.put(field.getKey().toString(), field.getValue());
    }
    return classData.build();
  }

  private static TypeDeclaration doParseJavaClass(File javaSrc) throws IOException {
    ASTParser parser = ASTParser.newParser(AST.JLS15);
    char[] source = Files.asCharSource(javaSrc, StandardCharsets.UTF_8).read().toCharArray();
    parser.setSource(source);
    parser.setResolveBindings(true);
    CompilationUnit result = (CompilationUnit) parser.createAST(null);
    TypeDeclaration classDeclaration = (TypeDeclaration) result.types().get(0);
    return classDeclaration;
  }

  private static ImmutableMap<SimpleName, VariableDeclarationFragment> getJavaFields(
      TypeDeclaration classDeclaration) {
    ImmutableMap.Builder<SimpleName, VariableDeclarationFragment> map = ImmutableMap.builder();
    FieldDeclaration[] javaFields = classDeclaration.getFields();
    for (FieldDeclaration fd : javaFields) {
      // A field has only one fragment
      Object f = fd.fragments().get(0);
      if (f instanceof VariableDeclaration) {
        VariableDeclaration field = (VariableDeclaration) f;
        map.put(field.getName(), (VariableDeclarationFragment) field);
      }
    }
    return map.build();
  }

  private static ImmutableMap<SimpleName, Object> interpretValues(
      ImmutableMap<SimpleName, VariableDeclarationFragment> javaFields) {
    return ImmutableMap.copyOf(
        Maps.transformValues(
            javaFields, variableDecl -> interpretValue(variableDecl.getInitializer())));
  }

  private static Object interpretValue(ASTNode expr) {
    if (expr instanceof StringLiteral) {
      // "abc"
      return interpretString((StringLiteral) expr);
    } else if (expr instanceof NumberLiteral) {
      // a = 0x123;
      return interpretNumber(expr.toString());
    } else if (expr instanceof ArrayInitializer) {
      // a = {foo, bar};
      return interpretArray((ArrayInitializer) expr);
    } else if (expr instanceof InfixExpression) {
      // a = "b" + "c"
      Object infixEval = interpretInfix((InfixExpression) expr).evaluate();
      return infixEval;
    } else {
      throw new UnsupportedOperationException("Unimplemented field type " + expr);
    }
  }

  private static Infix interpretInfix(InfixExpression expr) {
    return Infix.create(
        expr.getOperator(), expr.getLeftOperand(), expr.getRightOperand(), expr.extendedOperands());
  }

  private static long interpretNumber(String string) {
    if (string.startsWith("0x")) {
      return Long.parseLong(string.substring(2), 16);
    } else {
      return Long.parseLong(string);
    }
  }

  private static ImmutableList<String> interpretArray(ArrayInitializer expr) {
    ImmutableList.Builder<String> list = ImmutableList.builder();
    for (Object stringLiteral : expr.expressions()) {
      list.add((String) interpretValue((ASTNode) stringLiteral));
    }
    return list.build();
  }

  private static String interpretString(StringLiteral expr) {
    return expr.getLiteralValue();
  }

  @AutoValue
  abstract static class Infix {
    abstract InfixExpression.Operator operator();

    abstract List<Expression> expressions();

    public static Infix create(
        InfixExpression.Operator operator,
        Expression leftOperand,
        Expression rightOperand,
        List<Expression> extendedOperands) {
      return new AutoValue_BasicJavaInterpreter_Infix(
          operator,
          ImmutableList.<Expression>builder()
              .add(leftOperand)
              .add(rightOperand)
              .addAll(extendedOperands)
              .build());
    }

    public Object evaluate() {
      if (operator().toString().equals("+")) {
        if (expressions().stream().allMatch(e -> e instanceof StringLiteral)) {
          return expressions().stream()
              .map(x -> interpretString((StringLiteral) x))
              .collect(joining());
        } else {
          throw new UnsupportedOperationException(
              "Operator not implemented on "
                  + expressions().stream()
                      .map(x -> x.getClass().getSimpleName())
                      .collect(toImmutableList()));
        }
      } else {
        throw new UnsupportedOperationException("Operator not implemented: " + operator());
      }
    }
  }
}
