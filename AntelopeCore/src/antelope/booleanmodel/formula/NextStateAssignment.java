/*  **************************************************************************
 *
 *  The formulas determines the next state of the variable.
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

package antelope.booleanmodel.formula;

public class NextStateAssignment {

    private Variable variable;
    private Formula formula1;
    private Formula formula2; // might be null in case of deterministic transitions

    public NextStateAssignment( Variable        variable,
                                Formula    formula1,
                                Formula    formula2) {
        this.variable  = variable;
        this.formula1 = formula1;
        this.formula2 = formula2;
    }
    
    public Variable getVariable() {
        return variable;
    }
    
    public Formula getFormula1() {
        return formula1;
    }
    
    public Formula getFormula2() {
        return formula2;
    }
    
    public String toString() {
        return variable + " := " + formula1 + (formula2==null ? "" : (", " + formula2)) + ";";
    }
}
