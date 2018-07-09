<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="utils.*" %>

  <title>Remove wilcards</title>

<body>
<script>
    function go() {
        $( "#dialog-modal" ).dialog('open');
        return true;
    }
    $(document).ready(function () {
        $( "#dialog-modal" ).dialog({
            height: 140,
            modal: true,
            autoOpen: false,
            closeOnEscape: false,
            open: function(event, ui) { $(".ui-dialog-titlebar-close").hide() }
        });
    });
</script>
	<%
		String fullRow = request.getParameter("fullRow");   
		BuildChild bh = new BuildChild(fullRow);
		List<String> lista = bh.build();
		
		SortedSet<String> modelVariables = (SortedSet<String>)session.getAttribute("modelVariables");
		out.write("<div id='grid'><center>");
		out.write("<table cellspacing='0' cellpadding='0'>");
		out.write("<tr>");
		out.write("<th>&nbsp;</th>");
		for(String ss : modelVariables) {
			out.write("<th>" + ss + "</th>");
		}
		out.write("</tr>");
		
		for(String s : lista) {
			out.write("<tr>");
			
        	out.write("<td><select onChange=\"javascript:if (this.value!='') {go(); location=this.value;}\">");
        	out.write("<option value=\"\">Choose an action:</option>");
        	out.write("<option value=\"basin.jsp?r="+s+"\">Compute basin of attraction</option>");
        	out.write("<option value=\"rowstep.jsp?fullRow="+s+"&direction=Y\">Compute successors</option>");
        	out.write("<option value=\"rowstep.jsp?fullRow="+s+"&direction=X\">Compute predecessors</option>");
        	out.write("</select></td>");
        	
			//out.write("<td><input type='button' value='Basin of attraction' onclick='location=\"basin.jsp?r="+s+"\"'/></td>");
			
			for(int i=0; i<s.length(); i++) {
				out.write("<td>");
				out.write(s.substring(i, i+1));
				out.write("</td>");
			}
			out.write("</tr>");
		}
		out.write("</table><br/>");
		//out.write("<input class='btn' type='button' value='back' onclick='history.back(-1)'/>");
		out.write("</center>");
		out.write("</div>");
	%> 
	<div id="grid">
		<center>
			<table cellspacing='0' cellpadding='0'>
		  			<tr><th>&nbsp;</th></tr>
		  			<tr>
		  				<td>
		  				<ul>
		  					<li><a href='result.jsp'>Back to the previous page</a></li>
		  					<li><a href='upload.jsp'>Upload a gene regulatory network</a></li>
                            <li><a href='index.jsp'>Use a predefined gene regulatory network</a></li>
		  					<li><a href='define.jsp'>Save and edit custom properties</a></li>
		  					<li><a href='exit.jsp'>Exit from Antelope</a></li>
		  				</ul>
		  				</td>
		  			</tr>
			</table>
		</center>
	</div>
<div id="dialog-modal" title="">
    <img src="<%=request.getContextPath()%>/content/ajax-loader.gif" />
    <p>Contacting Antelope.</p>
    <p>This may take some minutes, please wait ...</p>
</div>
</body>


