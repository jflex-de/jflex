/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Anttask                                                           *
 * Copyright (C) 2001       Rafal Mantiuk <Rafal.Mantiuk@bellstream.pl>    *
 * Copyright (C) 2003       changes by Gerwin Klein <lsf@jflex.de>         *
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

package JFlex.anttask;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import JFlex.Main;
import JFlex.Options;

import java.io.*;

/**
 * JFlex task class
 *
 * @author Rafal Mantiuk
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
public class JFlexTask extends Task {
  private File destinationDir;
  private File inputFile;
  private JFlexWrapper wrapper = new JFlexWrapper();

  private boolean verbose = false;
  private boolean generateDot = false;
  //write graphviz .dot files for the generated automata (alpha)
  private boolean displayTime = false; //display generation time statistics
  private File skeletonFile = null;

	// found out by looking into .flex file 
	private String className = null;
	private String packageName = null;
	
	/** the actual output directory (outputDir = destinationDir + package)) */
	private File outputDir = null;

  public void execute() throws BuildException {
   	try {
      if (inputFile == null) 
        throw new BuildException("You must specify the input file for JFlex!");

			if (!inputFile.canRead()) 
				throw new BuildException("Cannot read input file "+inputFile);

			try {
      	findPackageAndClass();        
        normalizeOutdir();
        File destFile = new File(outputDir, className + ".java");
        
        if (inputFile.lastModified() > destFile.lastModified()) {      
          configure();      
          Main.generate(inputFile);
      
          if (!verbose)
            System.out.println("Generated: " + destFile.getName());
        }
      } catch (IOException e1) {
        throw new BuildException("IOException: " + e1.toString());
      }
    } catch (JFlex.GeneratorException e) {
      throw new BuildException("JFlex: generation failed!");
    }
  }

	/**
	 * Peek into .flex file to get package and class name
	 * 
	 * @throws IOException  if there is a problem reading the .flex file 
	 */
	public void findPackageAndClass() throws IOException {
		// find name of the package and class in jflex source file
		packageName = null;
		className = null;

		LineNumberReader reader = new LineNumberReader(new FileReader(inputFile));

		while (className == null || packageName == null) {
			String line = reader.readLine();
			if (line == null)	break;

			if (packageName == null) {
				int index = line.indexOf("package");
				if (index >= 0) {
					index += 7;

					int end = line.indexOf(';', index);
					if (end >= index) {
						packageName = line.substring(index, end);
						packageName = packageName.trim();
					}
				}
			}

			if (className == null) {
				int index = line.indexOf("%class");
				if (index >= 0) {
					index += 6;

					className = line.substring(index);
					className = className.trim();
				}
			}
		}

		// package name may be null, but class name not
		if (className == null) className = "Yylex";
	}

	/**
	 * Configures JFlex according to the settings in this class
	 */
	public void configure() {
		wrapper.setTimeStatistics(displayTime);
		wrapper.setVerbose(verbose);
		wrapper.setGenerateDot(generateDot);
		wrapper.setSkeleton(skeletonFile);
		Options.setDir( outputDir.toString() );
	}

	/**
	 * Sets the actual output directory if not already set. 	
	 *
	 * Uses javac logic to determine output dir = dest dir + package name
	 * If not destdir has been set, output dir = parent of input file
	 * 
	 * Assumes that package name is already set. 
	 */
  public void normalizeOutdir() {
  	if (outputDir != null) return;
  	
    // find out what the destination directory is. Append packageName to dest dir.      
    File destDir;
    
    // this is not the default the jflex logic, but javac-like 
    if (destinationDir != null) {
      if (packageName == null) {
    		destDir = destinationDir;
      }         
      else {
        String path = packageName.replace('.', File.separatorChar);
        destDir = new File(destinationDir,path);
      }
    } else { //save parser to the same dir as .flex
      destDir = new File(inputFile.getParent());
    }
    
    setOutdir(destDir);     
  }

	/**
	 * @return package name of input file
	 * 
	 * @see JFlexTask.findPackageAndClass
	 */
	public String getPackage() {
		return packageName;
	}

	/**
	 * @return class name of input file
	 * 
	 * @see JFlexTask.findPackageAndClass
	 */
	public String getClassName() {
		return className;
	}

  public void setDestdir(File destinationDir) {
    this.destinationDir = destinationDir;
  }

	public void setOutdir(File outDir) {
		this.outputDir = outDir;
	}

  public void setFile(File file) {
    this.inputFile = file;
  }

  public void setGenerateDot(boolean genDot) {
    this.generateDot = genDot;
  }

  public void setTimeStatistics(boolean displayTime) {
    this.displayTime = displayTime;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public void setSkeleton(File skeleton) {
    this.skeletonFile = skeleton;
  }
 
  public void setSkipMinimization(boolean skipMin) {
    setNomin(skipMin);
  }
  
  public void setNomin(boolean b) {
  	Options.no_minimize = b;
  }

  public void setNobak(boolean b) {
    Options.no_backup = b;
  }

  public void setSwitch(boolean b) {
    Options.gen_method = Options.SWITCH;
  }

  public void setTable(boolean b) {
    Options.gen_method = Options.TABLE;
  }

  public void setPack(boolean b) {
    Options.gen_method = Options.PACK;
  }
}
