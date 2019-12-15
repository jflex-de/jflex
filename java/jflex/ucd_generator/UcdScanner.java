package jflex.ucd_generator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import jflex.ucd_generator.scanner.BinaryPropertiesFileScanner;
import jflex.ucd_generator.scanner.EnumeratedPropertyFileScanner;
import jflex.ucd_generator.scanner.PropertyAliasesScanner;
import jflex.ucd_generator.scanner.PropertyValueAliasesScanner;
import jflex.ucd_generator.scanner.UnicodeDataScanner;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;

public class UcdScanner {
  private final UcdVersion ucdVersion;
  private final UnicodeData.Builder unicodeDataBuilder;

  UcdScanner(UcdVersion ucdVersion) {
    this.ucdVersion = ucdVersion;
    this.unicodeDataBuilder = UnicodeData.builder(ucdVersion.version());
  }

  public UnicodeData scan() throws IOException {
    scanPropertyAliases();
    scanPropertyValueAliases();
    scanUnicodeData();
    scanPropList();
    scanDerivedCoreProperties();
    scanScripts();
    // TODO scripts extension
    scanBlocks();

    return unicodeDataBuilder.build();
  }

  private void scanPropertyAliases() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.PropertyAliases);
    if (file != null) {
      PropertyAliasesScanner scanner =
          new PropertyAliasesScanner(Files.newReader(file, Charsets.UTF_8), unicodeDataBuilder);
      scanner.scan();
    }
  }

  private void scanPropertyValueAliases() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.PropertyValueAliases);
    if (file != null) {
      PropertyValueAliasesScanner scanner =
          new PropertyValueAliasesScanner(
              Files.newReader(file, Charsets.UTF_8), unicodeDataBuilder);
      scanner.scan();
    }
  }

  private void scanUnicodeData() throws IOException {
    File file = ucdVersion.getFile(UcdFileType.UnicodeData);
    UnicodeDataScanner scanner =
        new UnicodeDataScanner(
            Files.newReader(file, Charsets.UTF_8), ucdVersion, unicodeDataBuilder);
    scanner.scan();
  }

  private void scanPropList() throws IOException {
    scanBinaryProperties(unicodeDataBuilder, ucdVersion.getFile(UcdFileType.PropList));
  }

  private void scanDerivedCoreProperties() throws IOException {
    scanBinaryProperties(unicodeDataBuilder, ucdVersion.getFile(UcdFileType.DerivedCoreProperties));
  }

  private void scanScripts() throws IOException {
    scanEnumeratedProperty(unicodeDataBuilder, ucdVersion.getFile(UcdFileType.Scripts));
  }

  private void scanBlocks() throws IOException {
    scanEnumeratedProperty(unicodeDataBuilder, ucdVersion.getFile(UcdFileType.Blocks));
  }

  private static void scanBinaryProperties(UnicodeData.Builder unicodeDataBuilder, File file)
      throws IOException {
    if (file != null) {
      BinaryPropertiesFileScanner scanner =
          new BinaryPropertiesFileScanner(
              Files.newReader(file, Charsets.UTF_8), unicodeDataBuilder);
      scanner.scan();
    }
  }

  private static void scanEnumeratedProperty(UnicodeData.Builder unicodeDataBuilder, File file)
      throws IOException {
    if (file != null) {
      EnumeratedPropertyFileScanner scanner =
          new EnumeratedPropertyFileScanner(
              Files.newReader(file, Charsets.UTF_8), unicodeDataBuilder);
      scanner.scan();
    }
  }
}
