<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%
	String err = session.getAttribute("uploadError").toString();
        err = err.replaceAll("antelope.ProgramErrorException: ", "");
        //err = err.replaceAll("Syntax error:", "Syntax error:\n");
        err = err.replaceAll(":", ":\n");
        String path = request.getContextPath() + "/content";
%>
<title>Error</title>
<body>
	<center>
            <img src="<%=path%>/silhouette.png" alt="500: internal server error" title="500: internal server error" height="30%" />
            <h2>There were a syntax error in the uploaded file</h2>
            <center>
                Error message:
                <pre><%= err %></pre>
            </center>
            Please check your file and go
            <a href="upload.jsp">back to the upload page</a>.
	</center>
</body>
