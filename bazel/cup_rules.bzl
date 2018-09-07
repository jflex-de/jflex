""" Skylark rules for cup. """

def cup(name, srcs, parser = "Parser", symbols = "Symbols", interface = False):
    # CUP can only read from stdion, which Skylark rules don't support. Use a genrule for now.
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
