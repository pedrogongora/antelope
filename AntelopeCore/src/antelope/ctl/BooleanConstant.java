/*  **************************************************************************
 *
 *  Node of a PCTL formula parse tree.
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

package antelope.ctl;

public class BooleanConstant extends StateFormula {

    private boolean value;

    public BooleanConstant(boolean     value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public int type() {
        return StateFormula.TYPE_BOOLEAN_CONST;
    }
    
    public String toString() {
        return "" + value;
    }
}
