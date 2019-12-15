package jflex.ucd_generator.scanner;

public abstract class AbstractScriptExtensionsScanner {

  private int start;
  private int end;
  private String propertyName;
  private Map<String, NamedRangeSet> scriptIntervals = new HashMap<String, NamedRangeSet>();
  private boolean[] scriptExtensionsCodePoint;
  private Set<String> scripts = new HashSet<String>();

  private void addPropertyValueIntervals() {
    // Add script property value for missing code points
    for (String script : scripts) {
      NamedRangeSet intervals = scriptIntervals.get(script);
      if (null == intervals) {
        intervals = new NamedRangeSet();
        scriptIntervals.put(script, intervals);
      }
      for (NamedRange range : unicodeVersion.propertyValueIntervals.get(script).getRanges()) {
        for (int ch = range.start; ch <= range.end; ++ch) {
          if (!scriptExtensionsCodePoint[ch]) {
            intervals.add(new NamedRangeSet(new NamedRange(ch, ch)));
          }
        }
      }
    }
    for (Map.Entry<String, NamedRangeSet> entry : scriptIntervals.entrySet()) {
      String script = entry.getKey();
      NamedRangeSet intervals = entry.getValue();
      for (NamedRange range : intervals.getRanges()) {
        unicodeVersion.addInterval(propertyName, script, range.start, range.end);
      }
    }
  }
}
