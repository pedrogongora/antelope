/*  **************************************************************************
 *
 *  Creates a boolean model by reading a file containing tables with truth values.
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

package antelope.booleanmodel;

import java.io.BufferedReader;
//*import java.io.File;
//import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.logging.Logger;

import antelope.ProgramErrorException;
import antelope.booleanmodel.formula.*;

public class TableLoader {
    
    private static Logger log = Logger.getLogger(TableLoader.class.getName());
    
    //private String filename;
    private Reader reader;
    private BufferedReader in;
    private String nextLine;
    private int lineNumber;
    private BooleanModel model;
    
    /**
    * Read file and create a model
    * */
    public TableLoader(Reader reader) {
        //this.filename = file.getAbsolutePath();
        this.reader = reader;
        readFile();
    }
    
    //public void reloadFile() {
    //    readFile();
    //}
    
    //public void loadFile(String filename) {
    //    this.filename = filename;
    //    readFile();
    //}
    
    public BooleanModel getModel() {
        return model;
    }
    
    private void readFile() {
        try {
            //in = new BufferedReader( new FileReader(filename) );
            in = new BufferedReader( reader );
            model = new BooleanModel();
            lineNumber = 0;
            
            //log.info("reading file: " + filename);
            
            readNext();
            skipBlankLines();
            
            if ( nextLine == null ) {
                throw new ProgramErrorException("Empty file");
            }
            
            // read tables
            while ( nextLine != null ) {
                readTable();
            }
            
            in.close();
        } catch (Exception e) {
            // first try to close the file
            if ( in != null ) {
                try {
                    in.close();
                } catch (Exception x) {}
            }
            // then report the error
            if (e instanceof ProgramErrorException) {
                ProgramErrorException e2 = (ProgramErrorException) e;
                throw e2;
            } else {
                String msg = "An error occurred reading the model at line " +
                    lineNumber +
                    ": " +
                    e.getMessage();
                throw new ProgramErrorException(msg, e);
            }
        }
    }
    
    private void readTable() throws IOException {
        skipBlankLines();
        
        // Expecting variable names, the first is the table name
        String headerRE  = "^(\\s*)" // begin of line
            + "[a-zA-Z]([a-zA-Z_0-9]*)" // table name
            + "(\\s*)_(_+)(\\s*)" // ___-separator (more than one _)
            + "[a-zA-Z]([a-zA-Z_0-9]*)" // at least one input
            + "((\\s)+[a-zA-Z]([a-zA-Z_0-9]*))*" // other inputs
            + "(\\s*)$"; // end of line
        nextLine = nextLine.trim();
        
        log.finest("Reading header: \""+nextLine+"\"");
        
        if ( !nextLine.matches(headerRE) ) {
            throw new ProgramErrorException("Syntax error: " +
                "a table header was expected in line no. " +
                lineNumber +
                ": \"" + nextLine + "\"");
        }
        
        String headTail[] = nextLine.split("(\\s*)_(_)+(\\s*)");
        String varNames[] = headTail[1].split("(\\s+)");
        Variable output = new Variable(headTail[0]);
        Variable inputs[] = new Variable[varNames.length];
        model.addVariable(output.getName());
        
         log.finest("tail: "+ headTail[1]);
         log.finest("Table " +  output + ": ");
        
        for (int ii=0; ii<varNames.length; ii++) {
            model.addVariable(varNames[ii]);
            inputs[ii] = new Variable(varNames[ii]);
        }
        
        readNext();
        skipBlankLines();
        
        readRows(output, inputs);
    }
    
    private void readRows(Variable output, Variable inputs[]) throws IOException {
        int numInputs = inputs.length;
        int numRows = (int) Math.pow((double) 2, (double) numInputs);
        int initialLine = lineNumber;
        boolean isNondeterministic = false;
        Formula f1 = null, f2 = null;
        
        log.finest("to read "+numRows+" rows ("+numInputs+" inputs)");
        
        // read rows, each row induces a minterm
        for (int ii=0; ii<numRows; ii++) {
            // 0,1 inputs, then |, then 0,1,*
            String rowRE = "^(\\s*)(0|1)+[|](0|1|[*])(\\s*)$";
            if ( nextLine==null || !nextLine.matches(rowRE) ) {
                throw new ProgramErrorException("Syntax error: " +
                    "a table row was expected in line no. " +
                    lineNumber +
                    ": \"" + nextLine + "\"");
            }
            String in_out[] = nextLine.trim().split("[|]");
            char row[] = in_out[0].toCharArray();
            isNondeterministic = isNondeterministic || in_out[1].equals("*");
            
            // does this minterm must be included?
            if ( !in_out[1].equals("0") ) {
                // initialize minterm (there is at least one input)
                Formula minterm;
                if ( row[0] == '1' ) {
                    minterm = inputs[0];
                } else {
                    minterm = new Not(inputs[0]);
                }
                
                // build the rest of the minterm
                for (int jj=1; jj<numInputs; jj++) {
                        if ( row[jj] == '1' ) {
                        minterm = new And(inputs[jj], minterm);
                    } else {
                        minterm = new And(new Not(inputs[jj]), minterm);
                    }
                }
                
                // add (logical or) minterm to maxterms
                if (f2 == null) {
                    f2 = minterm;
                } else {
                    f2 = new Or(minterm, f2);
                }
                if ( !in_out[1].equals("*") ) {
                    if (f1 == null) {
                        f1 = minterm;
                    } else {
                        f1 = new Or(minterm, f1);
                    }
                }
            }
            
            readNext();
            skipBlankLines();
        }
        
        // add the transition to the model
        if ( !isNondeterministic ) f2 = null;
        // is this always false?
        if ( f1 == null) f1 = new BooleanConstant(false);
        model.addTransition( new NextStateAssignment(output, f1, f2) );
    }
    
    private void readNext() throws IOException {
        nextLine = in.readLine();
        lineNumber++;
        if ( nextLine != null && nextLine.indexOf("//") != -1 ) {
            nextLine = nextLine.substring(0, nextLine.indexOf("//"));
        }
    }
    
    private void skipBlankLines() throws IOException {
        while ( nextLine != null && nextLine.trim().equals("") ) {
            log.finest("skipping: \""+nextLine+"\"");
            readNext();
        }
    }
}
