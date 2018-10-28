package velocity;

import static com.google.common.truth.Truth.assertThat;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

public class TemplateVarsTest {

  private Foo foo;

  @Before
  public void createFoo() {
    foo = new Foo();
  }

  @Test
  public void toVelocityContext_notSet() {
    assertThrows(NullPointerException.class, () -> foo.toVelocityContext());
  }

  @Test
  public void toVelocityContext() {
    foo.bar = "hello";
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("bar")).isEqualTo("hello");
  }

  @Test
  public void toVelocityContext_nonPublicField() {
    foo.bar = "ignored";
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("secret")).isNull();
  }

  static class Foo extends TemplateVars {
    @SuppressWarnings("WeakerAccess") // Only public fields are exposed to the template.
    public String bar;

    @SuppressWarnings("unused") // unused, but that's for test
    String secret;
  }
}
