package java.velocity;

import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/** Instance holder of the Velocity runtime. */
public class Velocity {

  private static RuntimeInstance velocityRuntimeInstance = new RuntimeInstance();

  static SimpleNode parsedTemplateForResource(String templateStr, String resourceName) {
    try {
      return velocityRuntimeInstance.parse(templateStr, resourceName);
    } catch (ParseException e) {
      throw new AssertionError(e);
    }
  }

  private Velocity() {}
}
