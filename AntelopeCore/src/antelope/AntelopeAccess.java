/*  **************************************************************************
 *
 *  Simple frontend to Antelope functionality.
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

import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import antelope.booleanmodel.*;
import antelope.booleanmodel.formula.parser.*;
import antelope.ctl.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import antelope.ctl.parser.*;

public class AntelopeAccess {

    private static Logger log = Logger.getLogger(AntelopeAccess.class.getName());
    
    private Model model;
    
    public void loadModel(String filename) {
        log.log(Level.FINE, "Loading model: " + filename);
        try {
            FileReader reader = new FileReader(filename);
            if (filename.toLowerCase().endsWith(".eqn")) {
                loadEquationsModel(reader);
            } else {
                loadTableModel(reader);
            }
        } catch (Exception e) {
            throw new ProgramErrorException(e);
        }
        log.log(Level.FINE, "Loaded model: " + filename);
    }

    public void loadTableModel(Reader reader) {
        log.log(Level.FINE, "Loading table model in reader: " + reader);
        //loadEquationsModel(reader);
        TableLoader t = new TableLoader(reader);
        model = t.getModel();
        log.log(Level.FINE, "Loaded table model in reader: " + reader);
        log.log(Level.FINE, "Initializing BDD engine ...");
        model.initBDDEngine();
        log.log(Level.FINE, "Initializing BDD engine ...");
    }

    public void loadEquationsModel(Reader reader) {
        log.log(Level.FINE, "Loading equations model in reader: " + reader);
        try {
            Parser parser = new Parser(reader);
            model = parser.equation_list();
        } catch (ParseException e) {
            throw new ProgramErrorException("Error parsing model.\n" + e.getMessage(), e);
        }
        log.log(Level.FINE, "Loading equations model in reader: " + reader);
        log.log(Level.FINE, "Initializing BDD engine ...");
        model.initBDDEngine();
        log.log(Level.FINE, "BDD engine initialized");
    }
    
    public void makeModelAsynchronous() {
        log.log(Level.FINE, "Making model async ...");
        model.makeTransitionsAsynchronous();
        log.log(Level.FINE, "Finished making model async ");
    }
    
    public void makeModelStrictlyAsynchronous() {
        log.log(Level.FINE, "Making model strictly async ...");
        model.makeTransitionsStrictlyAsynchronous();
        log.log(Level.FINE, "Finished making model strictly async ");
    }
    
    public String getEFPFormula(boolean isConjunctive) {
        return model.getEFPFormula(isConjunctive);
    }
    
    public SortedSet<String> getModelVariables() {
        return model.getVariables();
    }
    
    public Set<Set<String>> getLabelsSet(String formula) {
        StateLabeling sl = new RecursiveStateLabeling(model);
        Set<Set<String>> labels = sl.computeLabels(formula);
        return labels;
    }
    
    //public List<byte[]> getLabelsArray(String formula) {
    public ModelCheckingResults getLabelsArray(String formula) {
        log.log(Level.FINE, "computing SAT(" + formula + ") ...");
        StateLabeling sl = new RecursiveStateLabeling(model);
        ModelCheckingResults results = sl.computeLabels2(formula);
        log.log(Level.FINE, "Finished computing SAT(" + formula + ")");
        return results;
    }
    
    public boolean checkCTLFormulaSyntax(String formula) throws antelope.ctl.parser.ParseException {
        antelope.ctl.parser.CTLParser parser
            = new antelope.ctl.parser.CTLParser(new java.io.StringReader(formula));
        parser.formula();
        return true;
    }
}
