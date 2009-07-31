package jflex;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum DataFileType {
  PROPERTY_ALIASES("PropertyAliases") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      PropertyAliasesScanner scanner = new PropertyAliasesScanner(reader, version);
      scanner.scan();
    }
  },
    
  PROPERTY_VALUE_ALIASES("PropertyValueAliases") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      PropertyValueAliasesScanner scanner 
        = new PropertyValueAliasesScanner(reader, version);
      scanner.scan();
    }
  },
  UNICODE_DATA("UnicodeData") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      UnicodeDataScanner scanner = new UnicodeDataScanner(reader, version);
      scanner.scan();
    }
  },

  PROPLIST("PropList") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      // Before Unicode 3.1, PropList-X.X.X.txt used a different format.
      // Before Unicode 2.0, PropList-X.X.X.txt did not exist.
      if (version.majorMinorVersion.equals("2.0")
          || version.majorMinorVersion.equals("2.1") 
          || version.majorMinorVersion.equals("3.0")) {
        ArchaicPropListScanner scanner 
          = new ArchaicPropListScanner(reader, version);
        scanner.scan();
      } else {
        BinaryPropertiesFileScanner scanner 
          = new BinaryPropertiesFileScanner(reader, version);
        scanner.scan();
      }
    }
  },

  DERIVED_CORE_PROPERTIES("DerivedCoreProperties") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      BinaryPropertiesFileScanner scanner
        = new BinaryPropertiesFileScanner(reader, version);
      scanner.scan();
    }
  },

  SCRIPTS("Scripts") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      // Prior to Unicode 5.0, the default Script property value is "Common".
      // From Unicode 5.0 onward, the default Script property value is "Unknown".
      // Prior to Unicode 3.1, Scripts(-X.X.X).txt did not exist.
      String defaultPropertyValue = "Unknown";
      if (version.majorMinorVersion.equals("3.1") 
          || version.majorMinorVersion.equals("3.2")
          || version.majorMinorVersion.equals("4.0")
          || version.majorMinorVersion.equals("4.1")) {
        defaultPropertyValue = "Common";
      }
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      EnumeratedPropertyFileScanner scanner = new EnumeratedPropertyFileScanner
        (reader, version, "Script", defaultPropertyValue);
      scanner.scan();
    }
  },

  BLOCKS("Blocks") {
    public void scan(URL url, UnicodeVersion version) throws IOException {
      Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
      EnumeratedPropertyFileScanner scanner = new EnumeratedPropertyFileScanner
        (reader, version, "Block", "No_Block");
      scanner.scan(); 
    }
  };
  
  DataFileType(String fileNamePrefix) {
    // Archaic link: <a href="UnicodeData-1.1.5.txt">
    // Modern link: <a href="UnicodeData.txt">
    // Beta link: <a href="UnicodeData-5.1.0d12.txt">
    nonBetaHTMLLinkPattern 
      = Pattern.compile("<a\\s+href\\s*=\\s*\"(" + fileNamePrefix 
                        + "(?:|-\\d+(?:\\.\\d+){2})\\.txt)\"\\s*>",
                        Pattern.CASE_INSENSITIVE);
  }
  
  private Pattern nonBetaHTMLLinkPattern;

  /**
   * Attempts to find a non-beta file name for this Unicode data file type in
   * the passed-in HTML directory listing.
   * 
   * @param htmlDirectoryListing The directory listing to search
   * @return The full file name, if found; otherwise, null.
   */
  public String getFileName(String htmlDirectoryListing) {
    Matcher matcher = nonBetaHTMLLinkPattern.matcher(htmlDirectoryListing);
    return matcher.find() ? matcher.group(1) : null;
  }
  
  public abstract void scan(URL url, UnicodeVersion version) throws IOException;
}
