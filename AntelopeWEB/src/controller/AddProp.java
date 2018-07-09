package controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.AntelopeAccess;
import antelope.CTLSyntaxErrorMessage;
import antelope.ctl.parser.ParseException;

import utils.*;

public class AddProp extends BaseController {
	private static final long serialVersionUID = 1L;
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext application = getServletContext();
		AntelopeAccess antelopeAccess = (AntelopeAccess)application.getAttribute("AntelopeAccessEngine");
		
		String nombre = request.getParameter("nombre");   
		String formula = request.getParameter("formula");
		String descripcion = request.getParameter("descripcion");
                nombre = (nombre == null
                        ? ""
                        : nombre.trim());
                descripcion = (descripcion == null
                        ? ""
                        : descripcion);
		String msg = valida(nombre, descripcion, antelopeAccess);
                String msg2 = validaFormula(formula, antelopeAccess);
                request.setAttribute("msg2", msg2);
		if(msg.length()>0 || msg2.length()>0) {
                    //response.sendRedirect("../content/manage.jsp?err='"+msg+"'");
                    request.setAttribute("msg", msg);
                    getServletContext().getRequestDispatcher("/content/manage.jsp").forward(request, response);
                    return;
		}
		
		Formula f = new Formula(nombre, formula.trim(), descripcion);
		FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
		if(fa==null) {
			fa = new FormulaArray();
			fa.add(f);
			application.setAttribute("FormulaArray", fa);
		} else {
			fa.add(f);
		}
		ManageFormulas.save(fa);
	
		response.sendRedirect("../content/define.jsp");
	}


	private String valida(String nombre, String descripcion, AntelopeAccess antelopeAccess) {
		if(nombre.length() == 0) return "Invalid name: please provide a name for identifying this property";
		if(nombre.length()  < 3) return "Invalid name: please use a name with 3 o more characters";
		return "";
	}

	private String validaFormula(String formula, AntelopeAccess antelopeAccess) {
		if(formula.trim().length()<1) return "Invalid formula";
		try {
			antelopeAccess.checkCTLFormulaSyntax(formula);
		} catch (ParseException ex) {
			return "<div style='font-size: small; color: red'><div>"
                        + CTLSyntaxErrorMessage.getCurrentTokenMsg(ex)
                        + "</div>"
                        + "<br/><div>"
                        + CTLSyntaxErrorMessage.getExpectedTokensMsg(ex)
                        + "</div></div>";
		}
		return "";
	}
	
}// fin de clase *****
