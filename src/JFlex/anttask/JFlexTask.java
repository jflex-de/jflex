/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Anttask                                                           *
 * Copyright (C) 2001       Rafal Mantiuk <Rafal.Mantiuk@bellstream.pl>    *
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
    private boolean generateDot = false; //write graphviz .dot files for the generated automata (alpha)
    private boolean skipMin = false; //skip minimization step (alpha status, use with care
    private boolean displayTime = false; //display generation time statistics
    private File skeletonFile = null;

    public void execute() throws BuildException {
        
        try {
            
            if( inputFile == null ) {
                throw new BuildException("You must specify the input file for JFlex!");
            }
            
            processFile( inputFile );
            
        }
        catch( JFlex.GeneratorException e ) {
            throw new BuildException( "JFlex: generation failed!" );
        }
    }

    public void setDestdir( File destinationDir ) {
        this.destinationDir = destinationDir;
    }
    
    public void setFile( File file ) {
        this.inputFile = file;
    }

    public void setGenerateDot( boolean genDot )
    {
        this.generateDot = genDot;
    }

    public void setTimeStatistics( boolean displayTime )
    {
        this.displayTime = displayTime;
    }

    public void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    public void setSkeleton( File skeleton )
    {
        this.skeletonFile = skeleton;
    }
    
    public void setSkipMinimization( boolean skipMin )
    {
        this.skipMin = skipMin;
    }

    protected void processFile( File file ) throws BuildException
    {
        try {

            // find name of the package and class in jflex source file
            String packageName = null;
            String className = null;
            {
            LineNumberReader reader = new LineNumberReader( new InputStreamReader( new FileInputStream( file ) ) );


            for( ; ; )
            {
                String line = reader.readLine();
                if( line == null ) break;

                if( packageName == null ) {
                    int index  = line.indexOf( "package" );
                    if( index != -1 ) {
                        index += 7;

                        int end = line.indexOf( ';', index );
                        if( end != -1 ) {
                            packageName = line.substring( index, end );
                            packageName = packageName.trim();
                        }

                    }
                }

                if( className == null ) {
                    int index  = line.indexOf( "%class" );
                    if( index != -1 ) {
                        index += 6;

                        className = line.substring( index );
                        className = className.trim();

                    }
                }

                // We have all the necessary information. No further parsing is necessary
                if( className != null && packageName != null ) break;


            }
            if( className == null )
              className = "Yylex";

            }

            // find out what the destination directory is. Append packageName to dest dir.
            StringBuffer destDir = new StringBuffer();
            {

                if( destinationDir != null ) {
                    destDir.append( destinationDir.getAbsolutePath() );

                    if( packageName != null ) {
                        destDir.append( File.separatorChar );
                        String path = packageName.replace( '.', File.separatorChar );
                        destDir.append( path );
                    }

                } else { //save parser to the same dir as .flex
                    destDir.append( file.getParent() );
                }
            }

            File destFile = new File( destDir.toString() + File.separator + className + ".java" );

            if( file.lastModified() > destFile.lastModified() ) {

                //configure JFlex
                wrapper.setTimeStatistics( displayTime );
                wrapper.setVerbose( verbose );
                wrapper.setGenerateDot( generateDot );
                wrapper.setSkipMinimization( skipMin );
                wrapper.setSkeleton( skeletonFile );
                wrapper.setDestinationDir( destDir.toString() );

                wrapper.generate( file );

                if( !verbose ) System.out.println( "Generated: " + destFile.getName() );
            }
        }
        catch( IOException e )
        {
            throw new BuildException( "IOException: " + e.toString() );
        }
    }

}

