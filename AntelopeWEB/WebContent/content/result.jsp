<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="utils.*" %>
	<%
		String tabla = (String) session.getAttribute("result");
		String fileName = (String) session.getAttribute("fileName");
		String formula = (String) session.getAttribute("formula");
		String modo = (String) session.getAttribute("modo");      
                String verificationTime = (String) session.getAttribute("verificationTime");
                String modelLoadingTime = (String) session.getAttribute("modelLoadingTime");
                String asyncConvertTime = (String) session.getAttribute("asyncConvertTime");
                session.setAttribute("verificationTime", "");
                session.setAttribute("modelLoadingTime", "");
                session.setAttribute("asyncConvertTime", "");
	
		FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
		DropdownBox dd = new DropdownBox(fa);
		String ddBox = dd.buildDropDown();
		String formulaDescription = fa.getFormulaName(formula);
        
        String ajax = request.getContextPath() + "/SyntaxChecker";
	%> 
  
  <title>Results</title>
  <body>
<style>
#errorDiv {
    width: 25em;
    margin: 0;
    padding: 0;
    position: absolute;
    border-collapse: collapse;
    border-spacing: 0px;
    background-color: white;
    -moz-box-shadow: 5px 5px 5px #888;
    box-shadow: 5px 5px 5px #888;
    font-size: smaller;
    z-index: 30;
}
</style>
<script>
    function toggleHint() {
        var pos = $( "#customFormula" ).offset();
        var width = $( "#customFormula" ).width();
        $( "#errorDiv" ).css({ "left": (pos.left + width + 10) + "px", "top":pos.top + "px" });
        if ($( "#errorDiv" ).html() == '') {
            $( "#errorDiv" ).hide();
        } else {
            $( "#errorDiv" ).show();
        }
    }
    function changePropertySelect(selectedItem) {
        if (selectedItem == '-- Use a custom property --') {
            $('#customFormula').show("normal");
            $('#customFormula').focus();
        } else {
            $('#customFormula').hide("slow");
            $('#customFormula').removeClass('ui-state-error');
            $( "#errorDiv" ).hide();
        }
    }
    function go() {
        $( "#dialog-modal" ).dialog('open');
        return true;
    }
    $(document).ready(function () {
        $('#customFormula').hide();
        $( "#customFormula" ).keyup(function (evt) {
            $('#errorDiv').load('<%=ajax%>', {formula: $('#customFormula').val()}, toggleHint);
        });
        $( "#errorDiv" ).addClass('ui-state-highlight');
        toggleHint();
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
					<td class="remarca2"><%= formulaDescription %>:&nbsp;<font color="black"><%= formula %></font></td>
				</tr>
				<tr>
					<td>Mode:</td>
					<td class="remarca2"><%= modo %></td>
				</tr>
				<tr>
					<td>Net loading (BDD construction) time:</td>
					<td class="remarca2"><%= modelLoadingTime %> milliseconds</td>
				</tr>
				<tr>
					<td>Asynchronous net conversion time (if applicable):</td>
					<td class="remarca2"><%= asyncConvertTime %> milliseconds</td>
				</tr>
				<tr>
					<td>Property verification time:</td>
					<td class="remarca2"><%= verificationTime %> milliseconds</td>
				</tr>
			</table>
		</div>
	  <br/>
	  
	  <div id="grid">
	  <%= tabla %>
	  </div>
	  <br/><br/>
	  <form method="post" action="../servlet/Recalculate">	  
		  <div id="dialog">
		  		<table cellspacing="0" cellpadding="0">
		  			<tr><th>&nbsp;</th></tr>
		  			<tr>
		  				<td>
		  				<ul>
		  					<li>
                                                            Use other property <%= ddBox %> with the same gene regulatory network and
                                                            <input type ="submit" value="Submit" class="btn" onclick="return go();">
                                                            <input type="text" name="customFormula" id="customFormula" size="50" max="300" value="" />
                                                            <div id="errorDiv"></div>
                                                        </li>
		  					<li><a href='upload.jsp'>Upload a gene regulatory network</a></li>
                            <li><a href='index.jsp'>Use a predefined gene regulatory network</a></li>
		  					<li><a href='define.jsp'>Save and edit custom properties</a></li>
		  					<li><a href='exit.jsp'>Exit from Antelope</a></li>
		  				</ul>
		  				</td>
		  			</tr>
		  		</table>
		  </div>
	  </form>
<div id="dialog-modal" title="">
    <img src="<%=request.getContextPath()%>/content/ajax-loader.gif" />
    <p>Contacting Antelope.</p>
    <p>This may take some minutes, please wait ...</p>
</div>
  </body>
