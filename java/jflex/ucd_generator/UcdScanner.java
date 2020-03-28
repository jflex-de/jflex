package jflex.ucd_generator;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import jflex.ucd_generator.scanner.BinaryPropertiesFileScanner;
import jflex.ucd_generator.scanner.DerivedAgeScanner;
import jflex.ucd_generator.scanner.EnumeratedPropertyFileScanner;
import jflex.ucd_generator.scanner.PropertyAliasesScanner;
import jflex.ucd_generator.scanner.PropertyValueAliasesScanner;
import jflex.ucd_generator.scanner.ScriptExtensionsScanner;
import jflex.ucd_generator.scanner.UnicodeDataScanner;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;

public class UcdScanner {
  private final UcdVersion ucdVersion;
  final UnicodeData unicodeData;

  UcdScanner(UcdVersion ucdVersion) {
    this.ucdVersion = ucdVersion;
    this.unicodeData = new UnicodeData(ucdVersion.version());
  }

  /** Scans all UCD data files. */
  public UnicodeData scan() throws IOException {

    scanPropertyAliases();
    scanPropertyValueAliases();
    scanUnicodeData();
    scanPropList();
    scanDerivedCoreProperties();
    scanScripts();
    scanScriptExtensions();
    scanBlocks();
    scanLineBreak();
    scanGraphemeBreakProperty();
    scanSentenceBreakProperty();
    scanWordBreakProperty();
    scanDerivedAge();

    return unicodeData;
  }

  void scanPropertyAliases() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.PropertyAliases);
    if (file != null) {
      PropertyAliasesScanner scanner =
          new PropertyAliasesScanner(Files.newReader(file, Charsets.UTF_8), unicodeData);
      scanner.scan();
    }
  }

  void scanPropertyValueAliases() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.PropertyValueAliases);
    if (file != null) {
      PropertyValueAliasesScanner scanner =
          new PropertyValueAliasesScanner(Files.newReader(file, Charsets.UTF_8), unicodeData);
      scanner.scan();
    }
  }

  void scanUnicodeData() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.UnicodeData);
    UnicodeDataScanner scanner =
        new UnicodeDataScanner(Files.newReader(file, Charsets.UTF_8), ucdVersion, unicodeData);
    scanner.scan();
  }

  void scanPropList() throws IOException {
    scanBinaryProperties(unicodeData, ucdVersion.getFile(UcdFileType.PropList));
  }

  void scanDerivedCoreProperties() throws IOException {
    scanBinaryProperties(unicodeData, ucdVersion.getFile(UcdFileType.DerivedCoreProperties));
  }

  void scanScripts() throws IOException {
    scanEnumeratedProperty(
        unicodeData,
        ucdVersion.getFile(UcdFileType.Scripts),
        /*defaultPropertyName=*/ "Script",
        "Unknown");
  }

  void scanScriptExtensions() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.ScriptExtensions);
    if (file != null) {
      ScriptExtensionsScanner scanner =
          new ScriptExtensionsScanner(Files.newReader(file, Charsets.UTF_8), unicodeData);
      scanner.scan();
    }
  }

  void scanBlocks() throws IOException {
    scanEnumeratedProperty(
        unicodeData,
        ucdVersion.getFile(UcdFileType.Blocks),
        /*defaultPropertyName=*/ "Block",
        "No_Block");
  }

  void scanLineBreak() throws IOException {
    scanEnumeratedProperty(
        unicodeData,
        ucdVersion.getFile(UcdFileType.LineBreak),
        /*defaultPropertyName=*/ "Line_Break",
        "XX");
  }

  void scanGraphemeBreakProperty() throws IOException {
    scanEnumeratedProperty(
        unicodeData,
        ucdVersion.getFile(UcdFileType.GraphemeBreakProperty),
        "Grapheme_Cluster_Break",
        "Other");
  }

  void scanSentenceBreakProperty() throws IOException {
    scanEnumeratedProperty(
        unicodeData,
        ucdVersion.getFile(UcdFileType.SentenceBreakProperty),
        "Sentence_Break",
        "Other");
  }

  void scanWordBreakProperty() throws IOException {
    scanEnumeratedProperty(
        unicodeData, ucdVersion.getFile(UcdFileType.WordBreakProperty), "Word_break", "Other");
  }

  void scanDerivedAge() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.DerivedAge);
    if (file != null) {
      Preconditions.checkState(file.exists(), "File does not exist " + file.getAbsolutePath());
      DerivedAgeScanner scanner =
          new DerivedAgeScanner(Files.newReader(file, Charsets.UTF_8), unicodeData, "Age");
      scanner.scan();
    }
  }

  /** Scans any binary properties file. */
  private static void scanBinaryProperties(UnicodeData unicodeData, File file) throws IOException {
    if (file != null) {
      BinaryPropertiesFileScanner scanner =
          new BinaryPropertiesFileScanner(Files.newReader(file, Charsets.UTF_8), unicodeData);
      scanner.scan();
    }
  }

  /** Scans any enumerated properties file. */
  private static void scanEnumeratedProperty(
      UnicodeData unicodeData, File file, String defaultPropertyName, String defaultPropertyValue)
      throws IOException {
    if (file != null) {
      EnumeratedPropertyFileScanner scanner =
          new EnumeratedPropertyFileScanner(
              Files.newReader(file, Charsets.UTF_8),
              unicodeData,
              defaultPropertyName,
              defaultPropertyValue);
      // ImmutableList<String> before = ImmutableList.copyOf(unicodeData.propertyValueIntervals());
      scanner.scan();
      // SetView<String> diff =
      //     Sets.difference(
      //         new HashSet<>(unicodeData.propertyValueIntervals()), new HashSet<>(before));
      // System.out.println(diff);
    }
  }

  public UcdVersion ucdVersion() {
    return ucdVersion;
  }
}
