/*  **************************************************************************
 *
 *  Description.
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

import java.io.*;
import java.math.*;
import java.util.*;

import net.sf.javabdd.BDD;

import antelope.*;
import antelope.booleanmodel.*;
import antelope.ctl.parser.ParseException;

public class Test1 {
    
    public static void main(String args[]) throws Exception {
        test0();
    }
    
    private static void test0() {
        try {
            String f = "!";
            AntelopeAccess access = new AntelopeAccess();
            System.out.println("Checking \"" + f + "\":" + access.checkCTLFormulaSyntax(f));
        } catch (ParseException e) {
            System.out.println(CTLSyntaxErrorMessage.getCurrentTokenMsg(e));
            System.out.println(CTLSyntaxErrorMessage.getExpectedTokensMsg(e));
        }
    }
    
    private static void test1() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String rowRE = "^(\\s*)(0|1)+[|](0|1|[*])(\\s*)$";
        while (true) {
            System.out.print("> ");
            String s = in.readLine();
            if (in == null) break;
            System.out.println(s.matches(rowRE));
        }
    }
    
    private static void test2() throws Exception {
        String f = "/home/pedrogl/progs/antelope/Source Verificador_espanol/reglas_raiz.txt";
        TableLoader t = new TableLoader(new FileReader(new File(f)));
        Model m = t.getModel();
        System.out.println(m);
    }
    
    private static void test3() throws Exception {
        String f = "/home/pedrogl/progs/antelope/Source Verificador_espanol/reglas_raiz.txt";
        TableLoader t = new TableLoader(new FileReader(new File(f)));
        Model m = t.getModel();
        m.initBDDEngine();
        System.out.println(m);
        System.out.println(m.getTransitionsBDD());
    }
    
    private static void test4() throws Exception {
        String tables = "/home/pedrogl/progs/antelope/Source Verificador_espanol/reglas_raiz.txt";
        TableLoader t = new TableLoader(new FileReader(new File(tables)));
        Model model = t.getModel();
        model.initBDDEngine();
        StateLabeling sl = new RecursiveStateLabeling(model);
        String formula = "EG true";
        Set<Set<String>> labels = sl.computeLabels(formula);
        System.out.println("Sat(" + formula + ") =");
        for (Set<String> o : labels) {
            System.out.println("\t"+o);
            //for (String s : o) System.out.print(s+" ");
            //System.out.println("\t");
        }
    }
    
    private static void test5() throws Exception {
        String tables = "model1.txt";
        TableLoader t = new TableLoader(new FileReader(new File(tables)));
        Model model = t.getModel();
        model.initBDDEngine();
        BDD bdd1 = model.getVariableBDD("p");
        BDD bdd2 = model.getVariableBDD("q");
        BDD bdd4 = bdd1.orWith(bdd2);
        //BDD bdd3 = bdd1.orWith(bdd2);
        //System.out.println(bdd1);
        //System.out.println(bdd2);
        //System.out.println(bdd3);
        //System.out.println(bdd4);
        //System.out.println(bdd3.equals(bdd1));
        //System.out.println(bdd3.equals(bdd4));
        //System.out.println(bdd3==(bdd1));
        System.out.println(bdd1==(bdd4));
    }
    
    private static void test6() throws Exception {
        //String s = "11111000010000000";
        String s = "1100000001111000010000000";
        BigInteger max = new BigInteger(s,2);
        BigInteger n = BigInteger.ZERO;
        NominalCounter c = new NominalCounter(max);
        
        for (int ii=s.length()-1; ii>=0; ii--) {
            if (c.testBit(ii)) System.out.print("1");
            else System.out.print("0");
        }
        System.out.println("");
        System.out.println(s);
        
        long ini, end=0, total,loops;
        
        // using BigIntegers
        loops=0;
        ini = System.currentTimeMillis();
        while (!n.equals(max)) {
            loops++;
            n = n.add(BigInteger.ONE);
        }
        end = System.currentTimeMillis();
        total = end-ini;
        System.out.println("Time: " + total + "ms ("+loops+" loops)");
        
        //using my counter
        loops=0;
        ini = System.currentTimeMillis();
        while (c.leqMaxValue()) {
            loops++;
            c.increment();
        }
        end = System.currentTimeMillis();
        total = end-ini;
        System.out.println("Time: " + total + "ms ("+loops+" loops)");
        
        //using longs
        loops=0;
        ini = System.currentTimeMillis();
        long states = max.longValue();
        while (loops < states) {
            loops++;
        }
        end = System.currentTimeMillis();
        total = end-ini;
        System.out.println("Time: " + total + "ms ("+loops+" loops)");
        
        
        for (int ii=s.length()-1; ii>=0; ii--) {
            if (c.testBit(ii)) System.out.print("1");
            else System.out.print("0");
        }
        System.out.println("");
        System.out.println(s);
    }
    
    /*private static void test7() throws Exception {
        String tables = "model1.txt";
        //String tables = "model2.txt";
        TableLoader t = new TableLoader(new FileReader(new File(tables)));
        Model model = t.getModel();
        model.initBDDEngine();
        RecursiveStateLabeling sl = new RecursiveStateLabeling(model);
        
        System.out.println("synchronous model:");
        for (int num=0; num<8; num++) {
            System.out.println(Integer.toBinaryString(num) + ":");
            antelope.ctl.Nominal nom = new antelope.ctl.Nominal(num + "");
            BDD nomBDD = sl.computeLabelsBDD(null,nom);
            // compute nominal's successors
            BDD x1 = nomBDD.and(model.getTransitionsBDD());
            BDD x2 = x1.exist(model.getVarSet());
            x2.replaceWith(model.getFromPrimePairing());
            // how many successors ?
            long satCount = (long) x2.satCount(model.getPrimedVarSet());
            System.out.println("\t" + satCount + " successors");
            Iterator it = x2.iterator(model.getVarSet());
            while (it.hasNext()) {
                BDD o = (BDD) it.next();
                System.out.println("\tBDD:   " + o);
            }
            //System.out.println();
    }
        
        System.out.println("\nasynchronous model:");
        model.makeTransitionsAsynchronous();
        for (int num=0; num<8; num++) {
            System.out.println(Integer.toBinaryString(num) + ":");
            antelope.ctl.Nominal nom = new antelope.ctl.Nominal(num + "");
            BDD nomBDD = sl.computeLabelsBDD(null,nom);
            // compute nominal's successors
            BDD x1 = nomBDD.and(model.getTransitionsBDD());
            BDD x2 = x1.exist(model.getVarSet());
            x2.replaceWith(model.getFromPrimePairing());
            // how many successors ?
            long satCount = (long) x2.satCount(model.getPrimedVarSet());
            System.out.println("\t" + satCount + " successors");
            Iterator it = x2.iterator(model.getVarSet());
            while (it.hasNext()) {
                BDD o = (BDD) it.next();
                System.out.println("\tBDD:   " + o);
            }
            //System.out.println();
        }
    }*/

    private static void test8() throws Exception {
        AntelopeAccess access = new AntelopeAccess();
        String f = "asc &";
        System.out.println(access.checkCTLFormulaSyntax(f));
    }

    private static void test9() throws Exception {
        TableLoader t = new TableLoader(new java.io.FileReader("reglas_flor.tbl"));
        System.out.println(t.getModel());
    }
}
