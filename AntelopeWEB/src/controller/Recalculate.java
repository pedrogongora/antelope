package controller;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import antelope.AntelopeAccess;

import antelope.ModelCheckingResults;
import utils.ServletUpload;
import utils.Starter;

import java.util.*;
import utils.DropdownBox;

public class Recalculate extends BaseController {
	private static final long serialVersionUID = 1L;
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String formula = request.getParameter("formula");
		formula = (formula.equals("-- Use a custom property --")
                        ? request.getParameter("customFormula")
                        : formula);
		String table = session.getAttribute("tableWithDefinitions")+"";
		String mode = session.getAttribute("tableWithDefinitionsMode")+"";
                String fileName = session.getAttribute("fileName").toString();
		String modo = (String) session.getAttribute("modo");
                boolean useEFP = (
                        formula.equals(DropdownBox.CONJUNCTIVE_EFP_VALUE) ||
                        formula.equals(DropdownBox.DISJUNCTIVE_EFP_VALUE)
                        );
		
		
	    AntelopeAccess antilopeAccess = Starter.getAntelopeAccessEngine(getServletContext());
	    
            //long iniLoad = System.currentTimeMillis();
        if (mode.equals("file")) {
            antilopeAccess.loadModel(table);
        } else if (mode.equals("inline")) {
            if (fileName.toLowerCase().endsWith(".eqn")) {
                antilopeAccess.loadEquationsModel(new StringReader(table));
            } else {
                antilopeAccess.loadTableModel(new StringReader(table));
            }
        }
            //long endLoad = System.currentTimeMillis();
            //session.setAttribute("modelLoadingTime", ""+(endLoad-iniLoad));
        
	    if(modo.equals("Synchronous")) {
	    	session.setAttribute("modo", "Synchronous");
                session.setAttribute("asyncConvertTime", "-");
	    }
	    if(modo.equals("Strictly Asynchronous")) {
                long iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelStrictlyAsynchronous();
                long endAsync = System.currentTimeMillis();
	    	session.setAttribute("modo", "Strictly Asynchronous");
                session.setAttribute("asyncConvertTime", ""+(endAsync-iniAsync));
	    }
	    if(modo.equals("Asynchronous")) {
                long iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelAsynchronous();
                long endAsync = System.currentTimeMillis();
                session.setAttribute("modo", "Asynchronous");
                session.setAttribute("asyncConvertTime", ""+(endAsync-iniAsync));
	    }
            
            if (useEFP) formula = antilopeAccess.getEFPFormula(formula.equals(DropdownBox.CONJUNCTIVE_EFP_VALUE));
            //long ini = System.currentTimeMillis();
	    //List<byte[]> labels = antilopeAccess.getLabelsArray(formula);
            //long end = System.currentTimeMillis();
            //request.getSession().setAttribute("verificationTime", ""+(end-ini));
            ModelCheckingResults results = antilopeAccess.getLabelsArray(formula);
            session.setAttribute("verificationTime", results.getFormulaVerificationTime()+"");
            session.setAttribute("modelLoadingTime", results.getModelBDDCreationTime()+"");
	    SortedSet<String> modelVariables = antilopeAccess.getModelVariables();
		//String result = ServletUpload.getTable(labels, modelVariables);
            String result = ServletUpload.getTable(results.getLabelsArray(), modelVariables);

		session.setAttribute("formula", formula);
		session.setAttribute("result", result);
		
		response.sendRedirect("../content/result.jsp");
	}
	
}// fin de clase *****
