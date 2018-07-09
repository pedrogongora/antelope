<%@ page isErrorPage="true" import="java.io.*" %>
<%
    String path = request.getContextPath() + "/content";
%>
<title>Error</title>
<body>
<script>
    function returnObjById( id )
    {
        if (document.getElementById)
            var returnVar = document.getElementById(id);
        else if (document.all)
            var returnVar = document.all[id];
        else if (document.layers)
            var returnVar = document.layers[id];
        return returnVar;
    }
    function toggle(item) {
        if(item.style.visibility == 'visible') { item.style.visibility = 'hidden'; }
	else { item.style.visibility = 'visible'; }
    }
</script>
	<center>
            <img src="<%=path%>/silhouette.png" alt="500: internal server error" title="500: internal server error" height="30%" />
            <h2>An unexpected error ocurred</h2>
            <center>
                Error message:
                <pre><%=exception.getMessage()%></pre>
            </center>
            Please try Antelope&#39;s
            <a href="<%=request.getContextPath()%>/">main page</a>
        <br/>
        <br/>
        <a href="#" onclick="toggle(returnObjById('details'))">Display information for administrators:</a>
        <br/>
	</center>
<div id="details" style="visibility: hidden">
<pre>
<%
StringWriter sw = new StringWriter();
PrintWriter pw = new PrintWriter(sw);
exception.printStackTrace(pw);
out.print(sw);
sw.close();
pw.close();
%>
</pre>
</div>
</body>
