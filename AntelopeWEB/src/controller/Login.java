package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends BaseController {
	private static final long serialVersionUID = 1L;
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  	String usr = request.getParameter("usr");   
		String psw = request.getParameter("psw");

		if(usr.equals("antelope") && psw.equals("unam")) {
			response.sendRedirect("../content/index.jsp");
		} else {
			response.sendRedirect("../content/login.jsp?error=1");
		}
	}
	
}// fin de clase *****
