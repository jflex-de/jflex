package velocity;

import static com.google.common.truth.Truth.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TemplateVarsTest {

  private Foo foo;

  @Before
  public void createFoo() {
    foo = new Foo();
  }

  @Test
  @Ignore // use assertThrows
  public void toVelocityContext_notSet() {
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("bar")).isNull();
  }

  @Test
  public void toVelocityContext() {
    foo.bar = "hello";
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("bar")).isEqualTo("hello");
  }

  @Test
  @Ignore
  public void toVelocityContext_nonPublicField() {
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("secret")).isNull();
  }

  static class Foo extends TemplateVars {
    @SuppressWarnings("WeakerAccess") // Only public fields are exposed to the template.
    public String bar;

    @SuppressWarnings("unused") // used by reflection
    String secret;
  }
}
