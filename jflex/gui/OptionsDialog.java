/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.6.1                                                             *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

import java.awt.*;
import java.awt.event.*;

import java.io.File;

import jflex.GeneratorException;
import jflex.Options;
import jflex.Skeleton;


/**
 * A dialog for setting JFlex options
 * 
 * @author Gerwin Klein
 * @version JFlex 1.6.1
 */
public class OptionsDialog extends Dialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = 6807759416163314769L;

  private Frame owner;

  private Button skelBrowse;
  private TextField skelFile;

  private Button ok;
  private Button defaults;

  private Checkbox dump;
  private Checkbox verbose;
  private Checkbox time;

  private Checkbox no_minimize; 
  private Checkbox no_backup;
  private Checkbox input_stream_ctor;

  private Checkbox jlex;
  private Checkbox dot;

  private Checkbox legacy_dot;

  /**
   * Create a new options dialog
   * 
   * @param owner
   */
  public OptionsDialog(Frame owner) {
    super(owner, "Options");

    this.owner = owner;
    
    setup();
    pack();
    
    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        close();
      }
    });
  }

  public void setup() {
    // create components
    ok = new Button("Ok");
    defaults = new Button("Defaults");    
    skelBrowse = new Button(" Browse");
    skelFile = new TextField();
    skelFile.setEditable(false);

    legacy_dot = new Checkbox( " dot (.) matches [^\\n] instead of "
                             + "[^\\n\\r\\000B\\u000C\\u0085\\u2028\\u2029]");

    dump = new Checkbox(" dump");
    verbose = new Checkbox(" verbose");
    time = new Checkbox(" time statistics");

    no_minimize = new Checkbox(" skip minimization");
    no_backup = new Checkbox(" no backup file");
    input_stream_ctor = new Checkbox(" InputStream ctor");

    jlex = new Checkbox(" JLex compatibility");
    dot = new Checkbox(" dot graph files");
    
    // setup interaction
    ok.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    } );

    defaults.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setDefaults();
      }
    } );

    skelBrowse.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        skelBrowse();
      }
    } );

    verbose.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.verbose = verbose.getState();                    
      }
    } );

    dump.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.dump = dump.getState();                    
      }
    } );

    jlex.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.jlex = jlex.getState();
        // JLex compatibility implies that dot (.) metachar matches [^\n]
        legacy_dot.setState(false);
        legacy_dot.setEnabled( ! jlex.getState());
      }
    } );

    no_minimize.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.no_minimize = no_minimize.getState();                    
      }
    } );
    
    no_backup.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.no_backup = no_backup.getState();                    
      }
    } );

    dot.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.dot = dot.getState();                    
      }
    } );

    legacy_dot.addItemListener( new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            Options.legacy_dot = legacy_dot.getState();
        }
    } );

    time.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.time = time.getState();                    
      }
    } );
      
    input_stream_ctor.addItemListener( new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Options.emitInputStreamCtor = input_stream_ctor.getState();
      }
    });

    // setup layout
    GridPanel panel = new GridPanel(4,5,10,10);
    panel.setInsets( new Insets(10,5,5,10) );
    
    panel.add(3,0,ok);
    panel.add(3,1,defaults);
     
    panel.add(0,0,2,1,Handles.BOTTOM,new Label("skeleton file:"));
    panel.add(0,1,2,1,skelFile);
    panel.add(2,1,1,1,Handles.TOP, skelBrowse);
     
    panel.add(0,4,4,1,legacy_dot);

    panel.add(0,2,1,1,dump);
    panel.add(0,3,1,1,verbose);

    panel.add(1,2,1,1,time);
    panel.add(1,3,1,1,no_minimize);

    panel.add(2,2,1,1,no_backup);
    panel.add(2,3,1,1,input_stream_ctor);

    panel.add(3,2,1,1,jlex);
    panel.add(3,3,1,1,dot);

    add("Center", panel);
    
    updateState();
  }
  
  private void skelBrowse() {
    FileDialog d = new FileDialog(owner , "Choose file", FileDialog.LOAD);
    d.setVisible(true);
    
    if (d.getFile() != null) {
      File skel = new File(d.getDirectory()+d.getFile());
      try {
        Skeleton.readSkelFile(skel);
        skelFile.setText(skel.toString());
      }
      catch (GeneratorException e) {
        // do nothing
      }
    }
  }

  private void updateState() {
    legacy_dot.setState(Options.legacy_dot);
  
    dump.setState(Options.dump);
    verbose.setState(Options.verbose);
    time.setState(Options.time);

    no_minimize.setState(Options.no_minimize);
    no_backup.setState(Options.no_backup);
    input_stream_ctor.setState(Options.emitInputStreamCtor);
  
    jlex.setState(Options.jlex);
    dot.setState(Options.dot);
  }

  private void setDefaults() {
    Options.setDefaults();
    Skeleton.readDefault();
    skelFile.setText("");
    updateState();
    legacy_dot.setEnabled(!jlex.getState());
  }

  public void close() {
    setVisible(false);
  }

}
