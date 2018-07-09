/*  **************************************************************************
 *
 *  A model described by a set of boolean formulas.
 *  For each variable there may be a formula representing its next state as a
 *  function of other variables.
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


// --------------------------------------------------------------------------
// | Notes:                                                                 |
// |------------------------------------------------------------------------|
// | - When using the native buddy, allsat() always return an array list    |
// |   with varnum cells, the non used cells contain null references.       |
// |   By contrast, the pure-java implementation returns an array list      |
// |   using only the necessary cells.                                      |
// --------------------------------------------------------------------------

package antelope.booleanmodel;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDPairing;

import antelope.BDDFactoryManager;
//import antelope.NominalCounter;
import antelope.Model;
import antelope.ProgramErrorException;
import antelope.booleanmodel.formula.*;

public class BooleanModel implements Model {
    
    private final static Logger log = Logger.getLogger(Model.class.getName());

    private SortedSet<String> variables;
    private String varArray[];
    private Map<String,NextStateAssignment> transitions;
    private Map<String,Integer> varIndex;
    private BDD normalVars;
    private BDD primedVars;
    private BDDPairing toPrime;
    private BDDPairing fromPrime;
    private BDD transitionsBDD;
    private BDDFactory factory;
    private int variablesSize;
    private long transitionsBDDCreationTime = 0;
    
    // do not compute transitions BDD unless it's needed
    private boolean computedTransitionsBDD      = false;
    private boolean isAsync                     = false;
    private boolean isStrictlyAsync             = false;
    private boolean computedAsyncTransitionsBDD = false;
    
    public BooleanModel() {
        variables = new TreeSet<String>();
        transitions = new TreeMap<String,NextStateAssignment>();
    }
    
    public int getVarNum() {
        return varArray.length;
    }

    public BooleanModel(SortedSet<String> variables, Map<String,NextStateAssignment> transitions) {
        this.variables = variables;
        this.transitions = transitions;
    }
    
    public SortedSet<String> getVariables() {
        return variables;
    }
    
    public Map<String,NextStateAssignment> getTransitions() {
        return transitions;
    }
    
    public void addVariable(String var) {
        variables.add(var);
    }
    
    public void addTransition(NextStateAssignment assignment) {
        transitions.put(assignment.getVariable().getName(), assignment);
    }
    
    public void initBDDEngine() {
        BDDFactoryManager.initFactory();
        factory = BDDFactoryManager.getFactory();
        enumerateVariables();
        factory.setVarNum(totalVarNum());
        if (log.isLoggable(Level.FINEST)) {
            log.finest("varNum:"+totalVarNum()+" (2+2*"+variables.size()+")");
        }
        //computeTransitionsBDD();
        normalVars = null;
        primedVars = null;
        toPrime = factory.makePair();
        fromPrime = factory.makePair();
        for (int ii=0; ii<variables.size(); ii++) {
            // build primed vars bdd
            if (primedVars == null) {
                primedVars = factory.ithVar(varPrimedBDDIndex(varArray[ii]));
            } else {
                primedVars.andWith(factory.ithVar(varPrimedBDDIndex(varArray[ii])));
            }
            // build vars (not primed) bdd
            if (normalVars == null) {
                normalVars = factory.ithVar(varBDDIndex(varArray[ii]));
            } else {
                normalVars.andWith(factory.ithVar(varBDDIndex(varArray[ii])));
            }
            // build var pairings (for pre and post)
            toPrime.set(varBDDIndex(varArray[ii]) ,varPrimedBDDIndex(varArray[ii]));
            fromPrime.set(varPrimedBDDIndex(varArray[ii]), varBDDIndex(varArray[ii]));
        }
        variablesSize = variables.size();
    }

    public long getTransitionsBDDCreationTime() {
        return transitionsBDDCreationTime;
    }
    
    public BDD getTransitionsBDD() {
        if (!computedTransitionsBDD) computeTransitionsBDD();
        if (isAsync) makeAsync();
        return transitionsBDD;
    }
    
    public BDD getVariableBDD(int varNum) {
        return getVariableBDD(varArray[varNum]);
    }
    
    public BDD getVariableBDD(String varname) {
        if (variables.contains(varname)) {
            return factory.ithVar(varBDDIndex(varname));
        } else {
            throw new ProgramErrorException("There is no variable called \"" +
                varname +
                "\" in the current model");
        }
    }
    
    public BDD getVariableNegBDD(int varNum) {
        return getVariableNegBDD(varArray[varNum]);
    }
    
    public BDD getVariableNegBDD(String varname) {
        if (variables.contains(varname)) {
            return factory.nithVar(varBDDIndex(varname));
        } else {
            throw new ProgramErrorException("There is no variable called \"" +
                varname +
                "\" in the current model");
        }
    }
    
    public BDD getBooleanConstantBDD(boolean b) {
        if (b)  return factory.one();
        else    return factory.zero();
    }
    
    public BDDPairing getToPrimePairing() {
        return toPrime;
    }
    
    public BDDPairing getFromPrimePairing() {
        return fromPrime;
    }
    
    public BDD getVarSet() {
        return normalVars;
    }
    
    public BDD getPrimedVarSet() {
        return primedVars;
    }
    
    public Set<Set<String>> labelsBDDToStringSet(BDD bdd) {
        Set<Set<String>> set = new java.util.HashSet<Set<String>>();
        Iterator it = bdd.allsat().iterator();
        while (it.hasNext()) {
            byte varAssign[] = (byte[]) it.next();
            // when using native buddy, the array _always_ has varnum cells,
            // the non-used cells are null-references & we must skip them
            // (this is not documented)
            if (varAssign == null) continue;
            Set<String> state = new java.util.TreeSet<String>();
            for (int ii=0; ii<varAssign.length; ii++) {
                if (!isPrimed(ii) && varAssign[ii]!=0) {
                    String varname = getVarName(ii);
                    if (varAssign[ii]<0) varname = varname + "*";
                    state.add(varname);
                }
            }
            set.add(state);
        }
        return set;
    }
    
    private int totalVarNum() {
        return variables.size()*2;
    }
    private int varBDDIndex(String var) {
        return varIndex.get(var)*2;
    }
    private int varPrimedBDDIndex(String var) {
        return varIndex.get(var)*2 + 1;
    }
    private boolean isPrimed(int idx) {
        return (idx % 2) == 1;
    }
    private String getVarName(int idx) {
        int idx2 = ((idx % 2) == 1) ? (idx-1)/2 : idx/2;
        return varArray[idx2];
    }
    
    private void enumerateVariables() {
        varIndex = new TreeMap<String,Integer>();
        varArray = new String[variables.size()];
        int ii=0;
        for (String var : variables) {
            varIndex.put(var, ii);
            varArray[ii] = var;
            ii++;
        }
    }
    
    private void computeTransitionsBDD() {
        long time0 = System.currentTimeMillis();
        Set<String> assignmentVars = transitions.keySet();
        transitionsBDD = factory.one();
        for (String pvar : assignmentVars) {
            NextStateAssignment assign = transitions.get(pvar);
            if (log.isLoggable(Level.FINEST)) {
                log.finest("converting to bdd assignment:"+assign);
            }
            Formula f1 = assign.getFormula1();
            Formula f2 = assign.getFormula2();
            BDD bdd1 = booleanFormulaBDD(factory, f1);
            bdd1.biimpWith(factory.ithVar(varPrimedBDDIndex(pvar)));
            if (f2 != null) {
                BDD bdd2 = booleanFormulaBDD(factory, f2);
                bdd2.biimpWith(factory.ithVar(varPrimedBDDIndex(pvar)));
                bdd1.orWith(bdd2);
            }
            transitionsBDD.andWith(bdd1);
        }
        long time1 = System.currentTimeMillis();
        this.transitionsBDDCreationTime = time1 - time0;
        computedTransitionsBDD = true;
    }
    
    private BDD booleanFormulaBDD(BDDFactory factory, Formula f) {
        BDD bdd = null;
        if (f instanceof BooleanConstant) {
            boolean value = ((BooleanConstant) f).getValue();
            if (value) {
                bdd = factory.one();
            } else {
                bdd = factory.zero();
            }
        } else if (f instanceof Variable) {
            String varname = ((Variable) f).getName();
            bdd = factory.ithVar(varBDDIndex(varname));
        } else if (f instanceof Not) {
            Formula sub = ((Not) f).getSubformula();
            BDD subBDD = booleanFormulaBDD(factory, sub);
            bdd = subBDD.not();
        } else if (f instanceof And) {
            Formula sub1 = ((And) f).getLeftSubformula();
            Formula sub2 = ((And) f).getRightSubformula();
            BDD subBDD1 = booleanFormulaBDD(factory, sub1);
            BDD subBDD2 = booleanFormulaBDD(factory, sub2);
            bdd = subBDD1.andWith(subBDD2);
        } else if (f instanceof Or) {
            Formula sub1 = ((Or) f).getLeftSubformula();
            Formula sub2 = ((Or) f).getRightSubformula();
            BDD subBDD1 = booleanFormulaBDD(factory, sub1);
            BDD subBDD2 = booleanFormulaBDD(factory, sub2);
            bdd = subBDD1.orWith(subBDD2);
        } else if (f instanceof Implication) {
            Formula sub1 = ((Implication) f).getLeftSubformula();
            Formula sub2 = ((Implication) f).getRightSubformula();
            BDD subBDD1 = booleanFormulaBDD(factory, sub1);
            BDD subBDD2 = booleanFormulaBDD(factory, sub2);
            bdd = subBDD1.impWith(subBDD2);
        } else if (f instanceof Iff) {
            Formula sub1 = ((Iff) f).getLeftSubformula();
            Formula sub2 = ((Iff) f).getRightSubformula();
            BDD subBDD1 = booleanFormulaBDD(factory, sub1);
            BDD subBDD2 = booleanFormulaBDD(factory, sub2);
            bdd = subBDD1.biimpWith(subBDD2);
        }
        return bdd;
    }
    
    // replace synchronous transitions with new asynchronous transitions
    public void makeTransitionsStrictlyAsynchronous() {
        //if (!transitionsBDDComputed) computeTransitionsBDD();
        //BDD tmp = computeAsynchronousTransitions();
        //transitionsBDD.free();
        //transitionsBDD = tmp;
        isAsync         = true;
        isStrictlyAsync = true;
    }
    
    // divide synchronous transitions into new asynchronous transitions
    // but leaving the original transitions in the model
    public void makeTransitionsAsynchronous() {
        //if (!transitionsBDDComputed) computeTransitionsBDD();
        //BDD tmp = computeAsynchronousTransitions();
        //transitionsBDD.orWith(tmp);
        isAsync         = true;
        isStrictlyAsync = false;
    }
    
    private void makeAsync() {
        if ( !computedAsyncTransitionsBDD ) {
            BDD tmp = computeAsynchronousTransitions();
            if ( isStrictlyAsync ) {
                transitionsBDD.free();
                transitionsBDD = tmp;
            } else {
                transitionsBDD.orWith(tmp);
            }
            computedAsyncTransitionsBDD = true;
        }
    }
    
    private BDD computeAsynchronousTransitions() {
        int varNum = getVarNum();
        BigInteger maxNom = BigInteger.ONE.shiftLeft(varNum).subtract(BigInteger.ONE);
        //BDD asynTrans = factory.one();
        BDD asynTrans = factory.zero();
        antelope.NominalCounter counter
            = new antelope.NominalCounter(maxNom,"BooleanModel.makeTransitionsAsynchronous()");
        antelope.ctl.Nominal nom = new antelope.ctl.Nominal(counter);
        antelope.RecursiveStateLabeling sl = new antelope.RecursiveStateLabeling(this); // FIXME
        
        // loop through each state (nominal)
        int asdf = 0; //FIXME
        while (counter.leqMaxValue()) {
            // compute the nominal's bdd
            BDD nomBDD = factory.one();
            for (int ii=0; ii<varNum; ii++) {
                BDD bit = null;
                if (nom.testBit(ii))    bit = getVariableBDD(ii);
                else                    bit = getVariableNegBDD(ii);
                nomBDD.andWith(bit);
            }
            //System.out.println("nominal: (" + (asdf++) +") "+nomBDD);
            // compute nominal's successors
            BDD x1 = nomBDD.and(transitionsBDD);
            BDD x2 = x1.exist(normalVars);
            x2.replaceWith(fromPrime);
            // iterate through successors
            Iterator it = x2.iterator(normalVars);
            while (it.hasNext()) {
                BDD nextSuccBDD = (BDD) it.next();
                //System.out.println("\tsuccessor: " + nextSuccBDD); 
                // which vars change ?
                BDD xorBDD = nomBDD.xor(nextSuccBDD);
                //System.out.println("\txor: " + xorBDD);
                // is this a loop ?
                if (xorBDD.isZero()) {
                    // then include the loop
                    //BDD tmp = nomBDD.id().replaceWith(toPrime);
                    //BDD newTrans = nomBDD.id().biimpWith(tmp);
                    //asynTrans.andWith(newTrans);
                    //System.out.println("\tthis is a loop");
                    BDD nextState = factory.one();
                    for (int ii=0; ii<varNum; ii++) {
                        BDD varBDD = getVariableBDD(ii);
                        BDD tmp3 = x2.imp(varBDD);
                        BDD tmp4 = x2.id().impWith(varBDD.not());
                        boolean isNonDeterministic = !tmp3.isOne() && !tmp4.isOne();
                        tmp3.free();
                        tmp4.free();
                        varBDD.free();
                        //System.out.println("\tbuilding var: "+ii+", nonDet="+isNonDeterministic+", testBit="+nom.testBit(ii));
                        if (!isNonDeterministic) { // skip non-det vars
                            BDD bit = null;
                            if (nom.testBit(ii))    bit = factory.ithVar(varPrimedBDDIndex(varArray[ii]));
                            else                    bit = factory.nithVar(varPrimedBDDIndex(varArray[ii]));
                            
                            //nextState.andWith(nomBDD.id().biimpWith(bit));
                            nextState.andWith(bit);
                        }
                    }
                    //System.out.println("\tnextState:"+nextState);
                    //asynTrans.andWith(nomBDD.id().biimpWith(nextState));
                    asynTrans.orWith(nomBDD.id().andWith(nextState));
                } else {
                    // this transition goes to another state
                    // then iterate through changed vars
                    //System.out.println("\tthis is not a loop, iterating vars");
                    for (int var=0; var<varNum; var++) {
                        BDD varBDD = getVariableBDD(var);
                        BDD tmp1 = nextSuccBDD.imp(varBDD).andWith(nomBDD.imp(varBDD));
                        BDD tmp2 = nextSuccBDD.id().impWith(varBDD.not()).andWith(nomBDD.id().impWith(varBDD.not()));
                        //BDD tmp3 = x2.imp(varBDD);
                        //BDD tmp4 = x2.id().impWith(varBDD.not());
                        BDD tmp5 = nextSuccBDD.imp(varBDD);
                        boolean changed = !tmp1.isOne() && !tmp2.isOne();
                        //boolean isNonDeterministic = !tmp3.isOne() && !tmp4.isOne();
                        boolean toPositive = tmp5.isOne();
                        //System.out.println("\tvar: " + var+", changed="+changed+", isNonDeterministic="+isNonDeterministic+", toPositive="+toPositive);
                        //System.out.println("\tvar: " + var+", changed="+changed+", toPositive="+toPositive);
                        // does var changed ?
                        if (changed) {
                            // then create a separate transition for this var
                            BDD nextState = factory.one();
                            for (int ii=0; ii<varNum; ii++) {
                                BDD bit = null;
                                //if (ii == var && isNonDeterministic) continue;
                                if (ii == var) {
                                    if (toPositive)  bit = factory.ithVar(varPrimedBDDIndex(varArray[ii]));
                                    else                            bit = factory.nithVar(varPrimedBDDIndex(varArray[ii]));
                                } else {
                                    if (nom.testBit(ii))    bit = factory.ithVar(varPrimedBDDIndex(varArray[ii]));
                                    else                    bit = factory.nithVar(varPrimedBDDIndex(varArray[ii]));
                                }
                                
                                nextState.andWith(bit);
                            }
                            nextState.replaceWith(toPrime);
                            //BDD newTrans = nextState.biimpWith(nomBDD.id());
                            //asynTrans.andWith(nextState);
                            //System.out.println("\tnextState:"+nextState);
                            asynTrans.orWith(nomBDD.id().andWith(nextState));
                        }
                        tmp1.free();
                        tmp2.free();
                        //tmp3.free();
                        //tmp4.free();
                        tmp5.free();
                    }
                }
                xorBDD.free();
                nextSuccBDD.free();
            }
            nomBDD.free();
            x1.free();
            x2.free();
            counter.increment();
        }
        return asynTrans;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Variables:\n");
        for (String s : variables) {
            sb.append("\t");
            sb.append(s);
            sb.append("\n");
        }
        sb.append("\nTransitions:\n");
        for (String s : transitions.keySet()) {
            sb.append("\t");
            sb.append(transitions.get(s));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getEFPFormula(boolean isConjunctive) {
        StringBuilder sb = new StringBuilder();
        boolean isTail = false;
        NextStateAssignment assignment = null;
        String op = (isConjunctive ? " & " : " | ");
        boolean isNondeterministic = false;
        for (String s : transitions.keySet()) {
            if (isTail) sb.append(" & ");
            else        isTail = true;
            assignment = transitions.get(s);
            isNondeterministic = assignment.getFormula2() != null;
            if (isNondeterministic) {
                sb.append("(");
            }
            sb.append("(");
            sb.append(assignment.getVariable().getName());
            sb.append(" = ");
            sb.append(assignment.getFormula1().toString());
            sb.append(")");
            if (isNondeterministic) {
                sb.append(op);
                sb.append("(");
                sb.append(assignment.getVariable().getName());
                sb.append(" = ");
                sb.append(assignment.getFormula2().toString());
                sb.append(")");
                sb.append(")");
            }
        }
        return sb.toString();
    }

    public List<byte[]> labelsBDDToArray(BDD bdd) {
        List<byte[]> set = new ArrayList<byte[]>();
        byte[] row = null;
        int j;
        String varname;
        Iterator it = bdd.allsat().iterator();
        while (it.hasNext()) {
            byte varAssign[] = (byte[]) it.next();
            // when using native buddy, the array _always_ has varnum cells,
            // the non-used cells are null-references & we must skip them
            // (this is not documented)
            if (varAssign != null) {
                row = new byte[variablesSize];
                for (int ii = 0; ii < varAssign.length; ii++) {
                    if (!isPrimed(ii)) {
                        varname = getVarName(ii);
                        j = encontrado(varname);

                        row[j] = varAssign[ii];
                    }
                }
                set.add(row);
            }
        }
        return set;
    }

    private int encontrado(String varname) {
        int i = 0;
        for (String s : variables) {
            if (s.equals(varname)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
