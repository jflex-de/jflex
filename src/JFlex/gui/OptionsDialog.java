/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4                                                               *
 * Copyright (C) 1998-2003  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package JFlex.gui;

import java.awt.*;

import JFlex.Options;

/**
 * A dialog for setting JFlex options
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class OptionsDialog extends Dialog {

  private TextField skelDir;

  private Button ok;
  private Button cancel;
  private Button defaults;

  private Checkbox skel;
  private Checkbox dump;
  private Checkbox verbose;
  private Checkbox tableG;
  private Checkbox switchG;
  private Checkbox packG;       

  /**
   * Create a new options dialog
   * 
   * @param owner
   */
  public OptionsDialog(Frame owner) {
    super(owner, "Options");
    setup();
  }

  public void setup() {
    ok = new Button("Ok");
    cancel = new Button("Cancel");
    
    skel = new Checkbox("use skeleton file");
    dump = new Checkbox("dump");
    verbose = new Checkbox("verbose");

    CheckboxGroup codeG = new CheckboxGroup();
    tableG = new Checkbox("table",Options.gen_method == Options.TABLE, codeG);
    switchG = new Checkbox("switch",Options.gen_method == Options.SWITCH, codeG);
    packG = new Checkbox("pack",Options.gen_method == Options.PACK, codeG);
    
  }

}
