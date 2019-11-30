# Velocity wrapper

Makes invocation of the [Velocity engine][velocity] easier.
- extend `TemplateVars` to automatically add all fields in the 
  Velocity context, using reflection.
- simply call `Velocity.render(...)`

[velocity]: https://velocity.apache.org/
