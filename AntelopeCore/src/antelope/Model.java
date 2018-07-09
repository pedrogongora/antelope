/*  **************************************************************************
 *
 *  Generic model behaviour.
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

package antelope;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDPairing;

public interface Model {
    public void                 initBDDEngine();
    public void                 makeTransitionsAsynchronous();
    public void                 makeTransitionsStrictlyAsynchronous();
    public int                  getVarNum();
    public long                 getTransitionsBDDCreationTime();
    public SortedSet<String>    getVariables();
    public BDD                  getTransitionsBDD();
    public BDD                  getBooleanConstantBDD(boolean b);
    public BDD                  getVariableBDD(String varname);
    public BDD                  getVariableBDD(int varNum);
    public BDD                  getVariableNegBDD(String varname);
    public BDD                  getVariableNegBDD(int varNum);
    public BDD                  getVarSet();
    public BDD                  getPrimedVarSet();
    public BDDPairing           getToPrimePairing();
    public BDDPairing           getFromPrimePairing();
    public Set<Set<String>>     labelsBDDToStringSet(BDD bdd);
    public List<byte[]>         labelsBDDToArray(BDD bdd);
    public String               getEFPFormula(boolean isConjunctive);
}
