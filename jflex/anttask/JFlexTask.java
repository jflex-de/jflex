/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Anttask                                                           *
 * Copyright (C) 2001       Rafal Mantiuk <Rafal.Mantiuk@bellstream.pl>    *
 * Copyright (C) 2003       changes by Gerwin Klein <lsf@jflex.de>         *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.anttask;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import jflex.Main;
import jflex.Options;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JFlex task class
 *
 * @author Rafal Mantiuk
 * @version JFlex 1.6.1
 */
public class JFlexTask extends Task {
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+(\\S+)\\s*;");
    private static final Pattern CLASS_PATTERN = Pattern.compile("%class\\s+(\\S+)");
    
    private File inputFile;

	// found out by looking into .flex file 
	private String className = null;
	private String packageName = null;

  /** for javac-like dest dir behaviour */
  private File destinationDir;
	
	/** the actual output directory (outputDir = destinationDir + package)) */
	private File outputDir = null;

  public JFlexTask() {
    // ant default is different from the rest of JFlex
    setVerbose(false);
    setUnusedWarning(true);
    Options.progress = false;
  }

  public void execute() throws BuildException {
   	try {
      if (inputFile == null) 
        throw new BuildException("Input file needed. Use <jflex file=\"your_scanner.flex\"/>");

			if (!inputFile.canRead()) 
				throw new BuildException("Cannot read input file "+inputFile);

			try {
      	findPackageAndClass();        
        normalizeOutdir();
        File destFile = new File(outputDir, className + ".java");
        
        if (inputFile.lastModified() > destFile.lastModified()) {      
          Main.generate(inputFile);      
          if (!Options.verbose)
            System.out.println("Generated: " + destFile.getName());
        }
      } catch (IOException e1) {
        throw new BuildException("IOException: " + e1.toString());
      }
    } catch (jflex.GeneratorException e) {
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
                Matcher matcher = PACKAGE_PATTERN.matcher(line);
                if (matcher.find()) {
                    packageName = matcher.group(1);
				}
			}

			if (className == null) {
                Matcher matcher = CLASS_PATTERN.matcher(line);
                if (matcher.find()) {
                    className = matcher.group(1);
                }
			}
		}

		// package name may be null, but class name not
		if (className == null) className = "Yylex";
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
	 * @see #findPackageAndClass()
	 */
	public String getPackage() {
		return packageName;
	}

	/**
	 * @return class name of input file
	 * 
	 * @see #findPackageAndClass()
	 */
	public String getClassName() {
		return className;
	}

  public void setDestdir(File destinationDir) {
    this.destinationDir = destinationDir;
  }

	public void setOutdir(File outDir) {
		this.outputDir = outDir;
    Options.setDir(outputDir);
	}

  public void setFile(File file) {
    this.inputFile = file;
  }

  public void setGenerateDot(boolean genDot) {
    setDot(genDot);
  }

  public void setTimeStatistics(boolean displayTime) {
    Options.time = displayTime;
  }
  
  public void setTime(boolean displayTime) {
    setTimeStatistics(displayTime);
  }

  public void setVerbose(boolean verbose) {
    Options.verbose = verbose;
    Options.unused_warning = verbose;
  }
  
  public void setUnusedWarning(boolean warn) {
    Options.unused_warning = warn;
  }

  public void setSkeleton(File skeleton) {
    Options.setSkeleton(skeleton);
  }
 
  public void setSkel(File skeleton) {
    setSkeleton(skeleton);
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

  public void setPack(boolean b) {
    /* no-op - this is the only available generation method */
  }

  public void setDot(boolean b) {
    Options.dot = b;
  }

  public void setDump(boolean b) {
    Options.dump = b;
  }
  
  public void setJLex(boolean b) {    
    Options.jlex = b;
  }

  public void setLegacyDot(boolean b) {
    Options.legacy_dot = b;
  }
    
  // TODO: In the JFlex version after 1.6, this option will cease to exist
  public void setInputStreamCtor(boolean b) {
    Options.emitInputStreamCtor = b; 
  }
}
