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


/**
 * Wrapper class for JFlex application. In case of any changes in JFlex
 * it should reduce impact of those changes on jflex ant task.
 *
 * FIXME: eventually eliminate in favour of central options handling class 
 *
 * @author Rafal Mantiuk
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
class JFlexWrapper {

    public void setTimeStatistics( boolean set )
    {
        JFlex.Out.TIME = set;
    }

    public void setVerbose( boolean set )
    {
        JFlex.Out.VERBOSE = set;
    }

    public void setGenerateDot( boolean set )
    {
         JFlex.Out.DOT = set;
    }

}
