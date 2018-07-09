<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="utils.*" %>
<%

		FormulaArray fa = (FormulaArray)application.getAttribute("FormulaArray");
		if(fa==null) {
			fa = ManageFormulas.load();
			if(fa==null) {
				fa = new FormulaArray();
			}
			application.setAttribute("FormulaArray", fa);
		}

		DropdownBox dd = new DropdownBox(fa);
		String ddBox = dd.buildDropDown();
        
        String ajax = request.getContextPath() + "/SyntaxChecker";
%>

  <title>Upload a gene Network</title>  
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
	  	<form method="post" action="../servlet/ServletUpload" enctype="multipart/form-data">
	  		<div id="dialog">
	  		<center>
		    	<table>
		    		<tr>
		    			<td>Upload your gene regulatory network:</td>
		    			<td><input type="file" name="tabla" size="35" /></td>
		    		</tr>
		    		<tr>
		    			<td>Select a property:</td>
                                        <td>
                                            <%= ddBox %>
                                            <input type="text" name="customFormula" id="customFormula" size="50" max="300" value="" />
                                            <div id="errorDiv"></div>
                                        </td>
		    		</tr>
		    		<tr>
		    			<td>Select a mode:</td>
		    			<td>
		    				<select name="modo">
		    					<option value="1">Synchronous</option>
		    					<option value="2">Asynchronous</option>
		    					<option value="3">Synchronous-Asynchronous</option>
		    				</select>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td>&nbsp;</td>
		    			<td><input class="btn" type="submit" value="Submit" onclick="return go();"></td>
		    		</tr>
		    		<tr>
		    			<td colspan="2"><hr/></td>
		    		</tr>
		    		<tr>
		    			<td colspan="2">
			    			<ul>
				    			<li><a href='index.jsp'>Use a predefined gene regulatory network</a></li>
				    			<li><a href='define.jsp'>Define / edit custom properties</a></li>
				    			<li><a href='exit.jsp'>Exit from Antelope</a></li>
			    			</ul>
		    			</td>
		    		</tr>
		    	</table>
		    	</center>    	
	    	</div><!-- dialog -->
	    </form>
<div id="dialog-modal" title="">
    <img src="<%=request.getContextPath()%>/content/ajax-loader.gif" />
    <p>Contacting Antelope.</p>
    <p>This may take some minutes, please wait ...</p>
</div>

  </body>
