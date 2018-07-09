<%@ page import="utils.*" %>
<%
	String id = request.getParameter("id");   		
	FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
	fa.remove(Long.parseLong(id));
	ManageFormulas.save(fa);
	response.sendRedirect("define.jsp");
%> 

