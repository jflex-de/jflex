import java.util.Arrays;

public class GenerateOver2GbInputFile {
  public static void main(String... args) {
    long max = (long)Integer.MAX_VALUE + 200;
    String line = "";
    int lastMegs = 0;
    for (long pos = 0L; pos <= max ; pos += line.length()) {
      char[] c = new char[67];
      Arrays.fill(c, '.'); 
      line = String.format("%010d: ", pos) + new String(c) + "\n";
      System.out.print(line);
      int megs = (int)(pos / 1_000_000L);
      if (megs > lastMegs) System.err.println(pos);
      lastMegs = megs;
    }
  }
}
