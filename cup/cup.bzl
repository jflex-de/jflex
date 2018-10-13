"""Bazel rules for cup. """

# CUP can only read from stdin, which Skylark rules don't support. Use a genrule for now.
def cup(name, srcs, parser = "Parser", symbols = "Symbols", interface = False):
    """Generate a parser with CUP.

    Args:
      name: name of the rule
      srcs: list of cup specifications
      parser: name of the generated parser class
      symbols: name of the generated symbols class
      interface: whether to generate an interface
    """
    opts = "-parser {parser} -symbols {symbols}".format(parser = parser, symbols = symbols)
    if interface:
        opts = opts + " -interface"
    cmd = ("$(location //cup:cup_bin) -destdir $(@D) " + opts + " < $<")
    native.genrule(
        name = name,
        srcs = srcs,
        tools = ["//cup:cup_bin"],
        outs = [parser + ".java", symbols + ".java"],
        cmd = cmd,
    )
