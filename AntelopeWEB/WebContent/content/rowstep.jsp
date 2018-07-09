<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="utils.*" %>
<%@ page import="antelope.*" %>
<%@ page import="java.io.*" %>

	<%
		String input = request.getParameter("fullRow");   
		String direction = request.getParameter("direction");   
		String table = session.getAttribute("tableWithDefinitions")+"";
		String modelMode = session.getAttribute("tableWithDefinitionsMode")+"";
		String fileName = session.getAttribute("fileName").toString();
		String f2 = session.getAttribute("formula").toString();
		String modo = session.getAttribute("modo").toString();  
	
	    AntelopeAccess antilopeAccess = Starter.getAntelopeAccessEngine(application);
                    
        
            long iniLoad = System.currentTimeMillis();
        if (modelMode.equals("file")) {
            antilopeAccess.loadModel(table);
        } else if (modelMode.equals("inline")) {
            if (fileName.toLowerCase().endsWith(".eqn")) {
                antilopeAccess.loadEquationsModel(new StringReader(table));
            } else {
                antilopeAccess.loadTableModel(new StringReader(table));
            }
        }
	    //antilopeAccess.loadTableModel(new StringReader(table));
            long endLoad = System.currentTimeMillis();
            
            long iniAsync = 0, endAsync = 0;
	    if(modo.equals("Strictly Asynchronous")) {
                iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelStrictlyAsynchronous();
                endAsync = System.currentTimeMillis();
	    }
	    if(modo.equals("Asynchronous")) {
                iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelAsynchronous();
                endAsync = System.currentTimeMillis();
	    }
		
		
		int index = 0;
        StringBuffer sb = new StringBuffer("E"+direction+" (true");
        SortedSet<String> sortedVariables = antilopeAccess.getModelVariables();
       
        for (String varname : sortedVariables) {
            char c = input.charAt(index);
            if (c != '*') {
                sb.append(" & ");
                if (c == '0')        sb.append("~" + varname);
                else if (c == '1')   sb.append(varname);
            }
            index++;
        }
        sb.append(")");
        String formula = sb.toString();
        long ini = System.currentTimeMillis();
        List<byte[]> caja = antilopeAccess.getLabelsArray(formula);
        long end = System.currentTimeMillis();
        String result = ServletUpload.getTable(caja, sortedVariables);
        
        FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
        String formulaDescription = fa.getFormulaName(f2);
	%> 
  <title>Basin of attraction</title>
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
<div id="dialog">
	<table cellspacing="0" cellpadding="0">
		<tr>
			<td>Uploaded file name:</td>
			<td class="remarca2"><%= fileName %></td>
		</tr>
		<tr>
			<td>Property:</td>
			<td class="remarca2"><%= formulaDescription %>:&nbsp;<font color="black"><%= f2 %></font></td>
		</tr>
		<tr>
			<td>Mode:</td>
			<td class="remarca2"><%= modo %></td>
		</tr>
		<tr>
			<td>Result:</td>
			<td class="remarca2"><%= formula %></td>
		</tr>
		<tr>
			<td>Net loading (BDD construction) time:</td>
			<td class="remarca2"><%= (endLoad-iniLoad) %> milliseconds</td>
		</tr>
                <tr>
                        <td>Asynchronous net conversion time (if applicable):</td>
                        <td class="remarca2"><%= (endAsync-iniAsync) %> milliseconds</td>
                </tr>
		<tr>
			<td>Property verification time:</td>
			<td class="remarca2"><%= (end-ini) %> milliseconds</td>
		</tr>
	</table>
</div>
		
<div id="grid">
		<br/><br/>
		<%= result %>
		<br/>
		<div id="dialog">
		  		<table cellspacing="0" cellpadding="0">
		  			<tr><th>&nbsp;</th></tr>
		  			<tr>
		  				<td>
		  				<ul>
		  					<li><a href='result.jsp'>Back to the results page</a></li>
		  					<li><a href='upload.jsp'>Upload a gene regulatory network</a></li>
                            <li><a href='index.jsp'>Use a predefined gene regulatory network</a></li>
		  					<li><a href='define.jsp'>Save and edit custom properties</a></li>
		  					<li><a href='exit.jsp'>Exit from Antelope</a></li>
		  				</ul>
		  				</td>
		  			</tr>
		  		</table>
		</div>
<div id="dialog-modal" title="">
    <img src="<%=request.getContextPath()%>/content/ajax-loader.gif" />
    <p>Contacting Antelope.</p>
    <p>This may take some minutes, please wait ...</p>
</div>

</div>

