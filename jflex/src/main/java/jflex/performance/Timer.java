/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.performance;

/**
 * Very simple timer for code generation time statistics.
 *
 * <p>Not very exact, measures user time, not processor time.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class Timer {

  /* the timer stores start and stop time from currentTimeMillis() */
  private long startTime, stopTime;

  /* flag if the timer is running (if stop time is valid) */
  private boolean running;

  /** Construct a new timer that starts immediately. */
  public Timer() {
    startTime = System.currentTimeMillis();
    running = true;
  }

  /** Start the timer. If it is already running, the old start time is lost. */
  public void start() {
    startTime = System.currentTimeMillis();
    running = true;
  }

  /** Stop the timer. */
  public void stop() {
    stopTime = System.currentTimeMillis();
    running = false;
  }

  /**
   * Return the number of milliseconds the timer has been running.
   *
   * <p>(up till now, if it still runs, up to the stop time if it has been stopped)
   *
   * @return a long.
   */
  public long diff() {
    if (running) return System.currentTimeMillis() - startTime;
    else return stopTime - startTime;
  }

  /**
   * Return a string representation of the timer.
   *
   * @return a string displaying the diff-time in readable format (h m s ms)
   * @see Timer#diff
   */
  @Override
  public String toString() {
    long diff = diff();

    long millis = diff % 1000;
    long secs = (diff / 1000) % 60;
    long mins = (diff / (1000 * 60)) % 60;
    long hs = (diff / (1000 * 3600)) % 24;
    long days = diff / (1000 * 3600 * 24);

    if (days > 0) return days + "d " + hs + "h " + mins + "m " + secs + "s " + millis + "ms";

    if (hs > 0) return hs + "h " + mins + "m " + secs + "s " + millis + "ms";

    if (mins > 0) return mins + "m " + secs + "s " + millis + "ms";

    if (secs > 0) return secs + "s " + millis + "ms";

    return millis + "ms";
  }
}
