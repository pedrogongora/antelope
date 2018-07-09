  <%
    String msg="";
  	Object error = request.getParameter("error");
  	if(error!=null) {
  		msg = "Invalid credentials. Please try Again";
  	}
   %>
   
<SCRIPT language="JavaScript">
<!--
//window.location="content/login.jsp";
window.location="index.jsp";
//-->
</SCRIPT>
<!--
   <title>Login Form</title>
  <body>
    <form method="post" action="../servlet/Login">
      <div id="dialog">
      <center>
	      <table cellspacing="0" cellpadding="0">
	        <tr><th colspan="2">Authentication form</th></tr>
	        <tr><td>User:</td><td><input type="text" name="usr"/></td></tr>
	        <tr><td>Password:</td><td><input type="password" name="psw"/></td></tr>
	        <tr><td>&nbsp;</td><td><input class="btn" type="submit" value="login"/></td></tr>
	      </table>
	  </center>
      </div>	
      <h3><%= msg %></h3>
    </form>
</body>
-->
