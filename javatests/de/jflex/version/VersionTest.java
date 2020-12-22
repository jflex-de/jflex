package de.jflex.version;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;

/** Test for {@link Version}. */
public class VersionTest {

  @Test
  public void version_major() {
    Version v = new Version("42");
    assertThat(v.major).isEqualTo(42);
    assertThat(v.minor).isEqualTo(-1);
    assertThat(v.patch).isEqualTo(-1);
  }

  @Test
  public void version_major_minor() {
    Version v = new Version("1.2");
    assertThat(v.major).isEqualTo(1);
    assertThat(v.minor).isEqualTo(2);
    assertThat(v.patch).isEqualTo(-1);
  }

  @Test
  public void version_major_patch() {
    Version v = new Version("1.2.3");
    assertThat(v.major).isEqualTo(1);
    assertThat(v.minor).isEqualTo(2);
    assertThat(v.patch).isEqualTo(3);
  }

  @Test
  public void compare_eaxact() {
    ImmutableSortedSet<Version> versions =
        ImmutableSortedSet.<Version>orderedBy(Version.EXACT_VERSION_COMPARATOR)
            .add(new Version("1.2.1"))
            .add(new Version("1.2.0"))
            .add(new Version("1.0"))
            .add(new Version("10.0"))
            .build();
    assertThat(versions)
        .containsExactly(
            new Version(1, 0), new Version(1, 2, 0), new Version(1, 2, 1), new Version(10, 0))
        .inOrder();
  }

  @Test
  public void compare_majorMinor() {
    ImmutableSortedSet<Version> versions =
        ImmutableSortedSet.<Version>orderedBy(Version.MAJOR_MINOR_COMPARATOR)
            .add(new Version("1.2.1"))
            .add(new Version("1.2.0"))
            .add(new Version("1.0"))
            .add(new Version("10.0"))
            .build();
    assertThat(versions)
        .containsExactly(new Version(1, 0), new Version(1, 2), new Version(10, 0))
        .inOrder();
  }
}
