package controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import antelope.AntelopeAccess;

import antelope.ModelCheckingResults;
import utils.*;

public class Preload extends BaseController {
	private static final long serialVersionUID = 1L;
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String fileName = request.getParameter("prec");   
		String formula = request.getParameter("formula");
		formula = (formula.equals("-- Use a custom property --")
                        ? request.getParameter("customFormula")
                        : formula);
		String modo = request.getParameter("modo"); 
                boolean useEFP = (
                        formula.equals(DropdownBox.CONJUNCTIVE_EFP_VALUE) ||
                        formula.equals(DropdownBox.DISJUNCTIVE_EFP_VALUE)
                        );
		
		String table = ServletUpload.getTextFromFile(fileName);
		session.setAttribute("tableWithDefinitions", ManageFormulas.MODEL_PATH + fileName);
		session.setAttribute("tableWithDefinitionsMode", "file");
		//session.setAttribute("tableWithDefinitions", table);
		
	    AntelopeAccess antilopeAccess = Starter.getAntelopeAccessEngine(getServletContext());
            
	    
        //antilopeAccess.loadTableModel(new StringReader(table));
            //long iniLoad = System.currentTimeMillis();
	    antilopeAccess.loadModel(ManageFormulas.MODEL_PATH + fileName);
            //long endLoad = System.currentTimeMillis();
            //session.setAttribute("modelLoadingTime", ""+(endLoad-iniLoad));
            
	    if(modo.equals("1")) {
	    	session.setAttribute("modo", "Synchronous");
                session.setAttribute("asyncConvertTime", "-");
	    }
	    if(modo.equals("2")) {
                long iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelStrictlyAsynchronous();
                long endAsync = System.currentTimeMillis();
	    	session.setAttribute("modo", "Strictly Asynchronous");
                session.setAttribute("asyncConvertTime", ""+(endAsync-iniAsync));
	    }
	    if(modo.equals("3")) {
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
            //session.setAttribute("verificationTime", (end-ini)+"");
            ModelCheckingResults results = antilopeAccess.getLabelsArray(formula);
            session.setAttribute("verificationTime", results.getFormulaVerificationTime()+"");
            session.setAttribute("modelLoadingTime", results.getModelBDDCreationTime()+"");
            
	    SortedSet<String> modelVariables = antilopeAccess.getModelVariables();
		//String result = ServletUpload.getTable(labels, modelVariables);
            String result = ServletUpload.getTable(results.getLabelsArray(), modelVariables);
		
		session.setAttribute("modelVariables", modelVariables);	
		session.setAttribute("formula", formula);
		session.setAttribute("result", result);
		session.setAttribute("fileName", fileName);
		
		response.sendRedirect("../content/result.jsp");
	}
	
}// fin de clase *****
