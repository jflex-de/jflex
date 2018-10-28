package ucd_generator;

public enum UcdFileType {
  DerivedAge, // Common across all versions
  UnicodeData, // Always exists since version 1
  Blocks,
  DerivedCoreProperties,
  GraphemeBreakProperty,
  LineBreak,
  PropertyAliases,
  PropertyValueAliases,
  PropList,
  SentenceBreakProperty,
  Scripts,
  ScriptExtensions,
  WordBreakProperty,
}
