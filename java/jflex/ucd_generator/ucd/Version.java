package jflex.ucd_generator.ucd;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import java.util.List;

public class Version implements Comparable<Version> {

  final int major;
  final int minor;
  final int patch;

  public Version(String version) {
    List<String> parts = Splitter.on(".").splitToList(version);
    this.major = Integer.parseInt(parts.get(0));
    this.minor = parts.size() > 1 ? Integer.parseInt(parts.get(1)) : -1;
    this.patch = parts.size() > 2 ? Integer.parseInt(parts.get(2)) : -1;
  }

  @Override
  public int compareTo(Version other) {
    if (this.major != other.major) {
      return this.major - other.major;
    }
    if (this.minor != other.minor) {
      return this.minor - other.minor;
    }
    return this.patch - other.patch;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Version)) {
      return false;
    }
    Version other = (Version) o;
    return this.major == other.major && this.minor == other.minor && this.patch == other.patch;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.major, this.minor, this.patch);
  }

  @Override
  public String toString() {
    return makeString('.', true);
  }

  public String unicodeClassName() {
    return String.format("Unicode_%s", makeString('_', false));
  }

  private String makeString(char sep, boolean includePatch) {
    StringBuilder v = new StringBuilder();
    v.append(major);
    if (minor != -1) {
      v.append(sep);
      v.append(minor);
    }
    if (includePatch && minor != -1 && patch != -1) {
      v.append(sep);
      v.append(patch);
    }
    return v.toString();
  }
}
