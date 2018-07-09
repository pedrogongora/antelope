<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%
String cp = request.getContextPath();
 %>
<html>


<head>
  <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
  <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
  <title>ANTELOPE - <decorator:title default="UNAM" /></title>
  <link href="<%= cp %>/img/style.css" rel="stylesheet" type="text/css">
    <link   type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/custom-theme/jquery-ui-1.8.16.custom.css" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.6.2.min.js"> </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.8.16.custom.min.js"> </script>
  <decorator:head />
</head>

  <body>
<center><div id="all_site">
	<div id="fixed_menu">
  	    <div class="about_top"><small>
<!--  	        <a href="index.jsp">Main</a>  -->
  	        <a href="about.jsp">Main</a>
  	        &nbsp;|&nbsp;
  	        <a href="index.jsp">Use Antelope</a>
  	        &nbsp;|&nbsp;
  	        <a href="about_models.jsp">About Predefined Models</a>
  	        &nbsp;|&nbsp;
  	        <a href="download.jsp">Download</a>
  	        &nbsp;|&nbsp;
  	        <a href="usersmanual.pdf">User's Manual</a>
  	    </small></div>
	</div>

	<br/><br/><br/>

  	<div id="header">
  		<a href="/AntelopeWEB/content/index.jsp">Antelope</a>
        </div>
		<hr/>
<!--
  	<div id="header">
  		<h1><a href="/AntelopeWEB/content/index.jsp">Antelope</a></h1>
  		<h2>(Analyzer of Networks through TEmporal LOgic sPEcifications)</h2>
  		<h1><a href="http://c3.fisica.unam.mx/">Centro de Ciencias de la Complejidad</a></h1>
  		<h1><a href="http://www.unam.mx/">Universidad Nacional Aut&oacute;noma de M&eacute;xico</a></h1>
  		<hr/>
  	</div>
-->
    <div id="content">
      <decorator:body />
    </div>
    
    <hr/>

    
<div class="footer">
	<small>
		ANTELOPE<br/>
		Analyzer of Networks Through Temporal Logic Specifications<br/> 
		<a href='http://www.unam.mx'>Universidad Nacional Aut&oacute;noma de M&eacute;xico</a><br />
		Contact: <a href='mailto:drosenbl@unam.mx'>David A. Rosenblueth</a>,
        <a href='mailto:pedro.gongora@gmail.com'>Pedro Arturo G&oacute;ngora</a>
	</small>
	<br/><br/>    
	<!--div id="marcadores">
		<ul>
			<li>
				<a href="http://del.icio.us/post?url=http://turing.iimas.unam.mx:8080/AntelopeWEB&amp;title=Antelope+application+WEB" title="Bookmark this post on del.icio.us." rel="nofollow" target="_blank">
				<img class="flag" src="<%= cp %>/img/favorites/delicious.png" alt="Delicious" /></a>
			</li>
			<li>
				<a href="http://digg.com/submit?phase=2&amp;url=http://turing.iimas.unam.mx:8080/AntelopeWEB&amp;title=Antelope+application+WEB" title="Digg this post on digg.com." rel="nofollow" target="_blank">
				<img src="<%= cp %>/img/favorites/digg.png" alt="Digg" /></a>
			</li>
			<li>
				<a href="http://www.stumbleupon.com/submit?url=http://turing.iimas.unam.mx:8080/AntelopeWEB&amp;title=Antelope+application+WEB" title="Thumb this up at StumbleUpon." rel="nofollow" target="_blank">
				<img src="<%= cp %>/img/favorites/stumbleit.png" alt="StumbleUpon" /></a>
			</li>
			<li>
				<a href="http://twitter.com/home/?status=http://turing.iimas.unam.mx:8080/AntelopeWEB+--+Antelope+application+WEB" title="Share on Twitter." rel="nofollow" target="_blank">
				<img src="<%= cp %>/img/favorites/twitter.png" alt="Twitter" /></a>
			</li>
			<li>
				<a href="http://www.google.com/bookmarks/mark?op=add&amp;bkmk=http://turing.iimas.unam.mx:8080/AntelopeWEB&amp;title=Antelope+application+WEB" title="Bookmark this post on Google." rel="nofollow" target="_blank">
				<img src="<%= cp %>/img/favorites/google.png" alt="Google" /></a>
			</li>
			<li>
				<a href="http://bookmarks.yahoo.com/myresults/bookmarklet?u=http://turing.iimas.unam.mx:8080/AntelopeWEB&amp;t=Antelope+application+WEB" title="Bookmark this post on Yahoo." rel="nofollow" target="_blank">
				<img src="<%= cp %>/img/favorites/yahoo.png" alt="Yahoo" /></a>
			</li>
		</ul>
	</div--><!-- /marcadores -->
</div><!-- /footer -->
</div><!--all_site--></center>
  </body>
</html>
