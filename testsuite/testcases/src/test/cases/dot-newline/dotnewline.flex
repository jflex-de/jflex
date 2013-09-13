import java.util.List;
import java.util.ArrayList;

%%

%{
  private class Interval {
    Interval(int start, int end) { this.start = start; this.end = end; }
    int start;
    int end;
  }
  private List<Interval> dotIntervals = new ArrayList<Interval>();
  private List<Interval> newlineIntervals = new ArrayList<Interval>();
%} 

%public
%class Dotnewline
%integer
%standalone
%line
%column
%unicode

%%

.   { if (1 == yytext().length()) {
        int ch = (int)yytext().charAt(0); 
        if (dotIntervals.size() == 0) {
          Interval interval = new Interval(ch, ch);
          dotIntervals.add(interval);
        } else {
           Interval lastInterval = dotIntervals.get(dotIntervals.size() - 1);
           if (ch == lastInterval.end + 1) {
             lastInterval.end = ch;
           } else {
             Interval interval = new Interval(ch, ch);
             dotIntervals.add(interval);
           }
        }
      } else {
        System.out.print("--.--");
        for (int i = 0 ; i < yytext().length() ; ++i) {
          System.out.format("\\u%04X", (int)yytext().charAt(i));
        }
        System.out.println("--");
      }
    }
    
\R  { if (1 == yytext().length()) {
        int ch = (int)yytext().charAt(0); 
        if (newlineIntervals.size() == 0) {
          Interval interval = new Interval(ch, ch);
          newlineIntervals.add(interval);
        } else {
          Interval lastInterval = newlineIntervals.get(newlineIntervals.size() - 1);
          if (ch == lastInterval.end + 1) {
            lastInterval.end = ch;
          } else {
            Interval interval = new Interval(ch, ch);
            newlineIntervals.add(interval);
          }
        }
      } else {
        System.out.print("--\\R--");
        for (int i = 0 ; i < yytext().length() ; ++i) {
          System.out.format("\\u%04X", (int)yytext().charAt(i));
        }
        System.out.println("--");
      }
    }

<<EOF>> { for (Interval interval : dotIntervals) {
            System.out.format("--.--[\\u%04X-\\u%04X]--\n", interval.start, interval.end);
          }
          for (Interval interval : newlineIntervals) {
            System.out.format("--\\R--[\\u%04X-\\u%04X]--\n", interval.start, interval.end);
          }
          return -1;
        }

[^] { System.out.format("Should never get here - char \\u%04X is matched by neither dot (.) nor (\\R)\n", (int)yytext().charAt(0)); }