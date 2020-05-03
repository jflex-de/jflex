/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import jflex.base.Build;
import jflex.logging.Out;

/**
 * JFlex main application frame (GUI mode only)
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public final class MainFrame extends Frame implements Handles {

  private static final long serialVersionUID = 3296137982410640865L;

  private volatile boolean choosing;

  private String fileName = "";
  private String dirName = "";

  private Button quit;
  private Button generate;
  private Button specChoose;
  private Button dirChoose;

  private TextField spec;
  private TextField dir;

  private TextArea messages;

  private GeneratorThread thread;

  private OptionsDialog dialog;

  /** Constructor for MainFrame. */
  public MainFrame() {
    super("JFlex " + Build.VERSION);
    buildContent();

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            quit();
          }
        });

    pack();
    setVisible(true);
  }

  private void buildContent() {
    setBackground(SystemColor.control);

    generate = new Button("Generate");
    quit = new Button("Quit");
    Button options = new Button("Options");
    dirChoose = new Button("Browse");
    dir = new TextField(10);
    specChoose = new Button("Browse");
    spec = new TextField(10);
    messages = new TextArea(10, 80);

    messages.setEditable(false);
    Font font = messages.getFont();
    if (font != null) messages.setFont(new Font("Monospaced", font.getStyle(), font.getSize()));
    else messages.setFont(new Font("Monospaced", Font.PLAIN, 12));

    Out.setGUIMode(messages);

    generate.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            generate();
          }
        });

    options.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            showOptions();
          }
        });

    quit.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            quit();
          }
        });

    specChoose.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            specChoose();
          }
        });

    dirChoose.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dirChoose();
          }
        });

    spec.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            fileName = spec.getText();
            generate();
          }
        });

    spec.addTextListener(
        new TextListener() {
          @Override
          public void textValueChanged(TextEvent e) {
            fileName = spec.getText();
          }
        });

    dir.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dirName = dir.getText();
            generate();
          }
        });

    dir.addTextListener(
        new TextListener() {
          @Override
          public void textValueChanged(TextEvent e) {
            dirName = dir.getText();
          }
        });

    GridPanel north = new GridPanel(5, 4, 10, 10);
    north.setInsets(new Insets(10, 5, 5, 10));

    north.add(4, 0, quit);
    north.add(4, 1, generate);
    north.add(4, 2, options);

    north.add(0, 0, BOTTOM, new Label("Lexical specification:"));
    north.add(0, 1, 2, 1, spec);
    north.add(2, 1, specChoose);

    north.add(0, 2, BOTTOM, new Label("Output directory:"));
    north.add(0, 3, 2, 1, dir);
    north.add(2, 3, dirChoose);

    Panel center = new Panel(new BorderLayout());
    center.add("North", new Label("Messages:"));
    center.add("Center", messages);

    add("North", north);
    add("Center", center);

    setEnabledAll(false);
  }

  /** showOptions. */
  protected void showOptions() {
    if (dialog == null) {
      dialog = new OptionsDialog(this);
    }
    dialog.setVisible(true);
  }

  /**
   * getPreferredSize.
   *
   * @return a {@link java.awt.Dimension} object.
   */
  @Override
  public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    d.width = messages.getPreferredSize().width;
    return d;
  }

  private void setEnabledAll(boolean generating) {
    quit.setEnabled(!generating);
    generate.setEnabled(!generating);
    dirChoose.setEnabled(!generating);
    dir.setEnabled(!generating);
    specChoose.setEnabled(!generating);
    spec.setEnabled(!generating);
  }

  private void generate() {
    // workaround for a weird AWT bug
    if (choosing) return;

    setEnabledAll(true);

    thread = new GeneratorThread(this, fileName, dirName);
    thread.start();
  }

  /**
   * generationFinished.
   *
   * @param success a boolean.
   */
  public void generationFinished(boolean success) {
    setEnabledAll(false);

    if (success) messages.append(Out.NL + "Generation finished successfully." + Out.NL);
    else messages.append(Out.NL + "Generation aborted." + Out.NL);
  }

  @SuppressWarnings("SystemExitOutsideMain") // this is an alternative Main class
  private void quit() {
    setVisible(false);
    System.exit(0);
  }

  private void dirChoose() {
    choosing = true;

    /* Hacky way to let the user select directories on MacOS. We're counting
    on this setting being ignored on other platforms */
    System.setProperty("apple.awt.fileDialogForDirectories", "true");
    FileDialog d = new FileDialog(this, "Choose directory", FileDialog.LOAD);

    d.setVisible(true);

    if (d.getDirectory() != null) {
      dir.setText((new File(d.getDirectory())).getAbsolutePath());
    }
    System.setProperty("apple.awt.fileDialogForDirectories", "false");

    choosing = false;
  }

  private void specChoose() {
    choosing = true;

    FileDialog d = new FileDialog(this, "Choose file", FileDialog.LOAD);

    d.setFile("*.flex");
    d.setVisible(true);

    if (d.getFile() != null) {
      fileName = d.getDirectory() + d.getFile();
      dir.setText(d.getDirectory());
      spec.setText(fileName);
    }

    choosing = false;
  }
}
