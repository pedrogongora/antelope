  <%
  	String err = (String) request.getAttribute("msg");
        String msg2 = (String) request.getAttribute("msg2");
        String nombre = request.getParameter("nombre");   
        String formula = request.getParameter("formula");
        String descripcion = request.getParameter("descripcion");
        
        err = (err == null ? "" : err);
        msg2 = (msg2 == null ? "" : msg2);
        nombre = (nombre == null ? "" : nombre);
        formula = (formula == null ? "" : formula);
        descripcion = (descripcion == null ? "" : descripcion);
        
        String ajax = request.getContextPath() + "/SyntaxChecker";
   %>
  <title>Define a custom property</title>
  
     
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
        var pos = $( "#formulaInput" ).offset();
        var width = $( "#formulaInput" ).width();
        $( "#errorDiv" ).css({ "left": (pos.left + width + 10) + "px", "top":pos.top + "px" });
        if ($( "#errorDiv" ).html() == '') {
            $( "#errorDiv" ).hide();
        } else {
            $( "#errorDiv" ).show();
        }
    }
    $(document).ready(function() {
        $( "#formulaInput" ).keyup(function (evt) {
            $('#errorDiv').load(
                    '<%=ajax%>',
                    {formula: $('#formulaInput').val()},
                    toggleHint);
        });
        $( "#errorDiv" ).addClass('ui-state-highlight');
        toggleHint();
    });
</script>
  	<form method="post" action="../servlet/AddProp" >	
  		<div id="dialog">
  		<center>
		<table cellspacing='0' cellpadding='0'>
			<tr><th colspan="2">Enter a property information</td></tr>
			<tr>
                            <td>Name:</td>
                            <td><input type="text" name="nombre" value="<%=nombre%>"></td>
                        </tr>
                        <tr>
                            <td valign="top">Formula:</td>
                            <td>
                                <input type="text" name="formula" id="formulaInput" size="50" max="300" value="<%=formula%>" />
                                <div id="errorDiv"><%=msg2%></div>
                            </td>
                        </tr>
			<tr>
				<td valign="top">Comments:</td>
				<td>
                                    <textarea rows="4" cols="50" name="descripcion"><%=descripcion%></textarea>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<input class="btn" type="submit" value="Add property" />
					<input class="btn" type="button" value="Cancel" onclick="window.location='<%=request.getContextPath()%>/content/define.jsp'"/>
				</td>
			</tr>
		</table>
		<font color="red"><%= err %></font>
		<br/>
		<table>				
           <tr><th colspan="2">Propositional operators:</th></tr>
           <tr><td>Operator</td>   <td>Description</td></tr>
           <tr><td>true</td>       <td>true</td></tr>
           <tr><td>false</td>      <td>false</td></tr>
           <tr><td>~ f</td>        <td>not f</td></tr>
           <tr><td>f & g</td>      <td>f and g</td></tr>
           <tr><td>f -> g</td>      <td>f implies g</td></tr>
           <tr><td>f = g</td>      <td>f if and only if g</td></tr>
		   
           <tr><th colspan="2">CTL operators:</th></tr>
           <tr><td>Operator</td>     <td>Description</td></tr>
           <tr><td>EX f</td>         <td>there exists a path such that, in the next state f</td></tr>
           <tr><td>AX f</td>         <td>for all paths, in the next state f</td></tr>
           <tr><td>EF f</td>         <td>there exists a path such that, in a future state f (potentially f)</td></tr>
           <tr><td>AF f</td>         <td>for all paths, in a future state f (inevitably f)</td></tr>
           <tr><td>EG f</td>         <td>there exists a path such that, in all states f (f is a potential invariant)</td></tr>
           <tr><td>AG f</td>         <td>for all paths, in all states f (f is invariant)</td></tr>
           <tr><td>E(f U g)</td>     <td>there exists a path such that, f until g</td></tr>
           <tr><td>A(f U g)</td>     <td>for all paths, f until g</td></tr>

           <tr><th colspan="2">Hybrid operators:</th></tr>
           <tr><td>Operator</td>  <td>Description</td></tr>
           <tr><td>@s.f</td>      <td>at the state named s, f holds</td></tr>
           <tr><td>!s.f</td>      <td>at the current state (named s), f holds</td></tr>
           <tr><td>]s.f</td>      <td>there exists a state (named s) such that, f holds</td></tr>

           <tr><th colspan="2">Referencing states:</th></tr>
           <tr><td>Example</td>     <td>Description</td></tr>
           <tr><td>1011b</td>       <td style="max-width: 400px">state with 1st gene active, 2nd gene inactive, and 3rd and 4th genes active (in lexicographical or ASCII order, in a model with 4 genes)</td></tr>
           <tr><td>Bh</td>          <td>idem using hexadecimal notation</td></tr>
           <tr><td>11</td>          <td>idem using decimal notation</td></tr>
		</table>
		</center>
		</div>
    </form>
  </body>
