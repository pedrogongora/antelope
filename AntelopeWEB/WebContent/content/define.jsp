<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="utils.*" %>


<%
		Object tabla = session.getAttribute("result");
		boolean pintaColumnaAdicional = true; 
		if(tabla==null || tabla.toString().trim().length()<5) {
			pintaColumnaAdicional = false;
		}
		
		
		boolean flag = true;
		FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
		if(fa==null) {
			fa = ManageFormulas.load();
			if(fa==null) {
				fa = new FormulaArray();
			}
			application.setAttribute("FormulaArray", fa);
			flag = false;
		}
		DropdownBox dd = new DropdownBox(fa);
		String formulasTable = dd.buildTable(pintaColumnaAdicional);
		
		String aviso = "Browse the properties or:";
		if(pintaColumnaAdicional) {
			aviso = "Browse and <u><label class='remarca2' title='Click on the desired formula to use it with the current uploaded model'>use</label></u>&nbsp;the<br/> available properties or:";
		}
%>
   <title>Browse properties</title>
  <body>
  <% 
  		if (flag) {
  		%>
		<center>
		<table>
			<tr>
				<td valign="top">
		  			<div id="dialog">
		  					<table cellspacing="0" cellpadding="0">
		  						<tr><th>Current properties</th></tr>
		  						<tr>
		  							<td>
		  								<%= aviso %>
			  							<ul>
				  							<li><a href='manage.jsp'>Add a property</a></li>
				  							<li><a href='index.jsp'>Go to main interface</a></li>
				  							<li><a href='exit.jsp'>Exit from Antelope</a></li>
			  							</ul>
		  							</td>
		  						</tr>
		  					</table>
		  			</div><!-- dialog -->
	  			</td>
	  			<td>
	  				<div id='grid'>
	  				<%= formulasTable %>
	  				</div>
	  			</td>
  			</tr>
  		</table>
  		</center>
  		<%
		} else {
			out.write("There are no formulas at this moment.<br/>");
		}
  %>
  </body>
 
