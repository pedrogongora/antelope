/*  **************************************************************************
 *
 *  Simple parser test, reads string from std. input and prints the parse tree.
 *
 *  Author: Pedro A. Góngora <pedro.gongora@gmail.com>
 *
 *  Copyright (C) 2010 Pedro A. Góngora
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *  *************************************************************************/

package antelope.ctl.parser;

import antelope.ctl.*;

public class ParserTest {

    public static void main(String args[]) throws Exception {
        CTLParser parser = new CTLParser(System.in);
        StateFormula f = parser.formula();
        System.out.println(f);
    }
}
