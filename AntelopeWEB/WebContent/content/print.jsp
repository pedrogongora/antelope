<%
String r = "";
Object obj = request.getParameter("resultado");
if(obj!=null) {
	r = obj.toString();
}
%>
  <title>Print Results</title>
<body>
   <%= r %>
</body>
