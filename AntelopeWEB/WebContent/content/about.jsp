

  <title>Antelope: Analyzer of Networks through TEmporal LOgic
sPEcifications</title>

<body>
<center>
<!--  		<h1><a href="/AntelopeWEB/content/index.jsp">Antelope</a></h1> -->
  		<h2>(Analyzer of Networks through TEmporal LOgic sPEcifications)</h2>
  		<h2>Ver. 1.1.1</h2>
  		<div class="institutions">
                <h1><a href="http://c3.fisica.unam.mx/">Centro de Ciencias de la Complejidad</a></h1>
  		<h1><a href="http://www.unam.mx/">Universidad Nacional Aut&oacute;noma de M&eacute;xico</a></h1>
                </div>
  		<hr/>
</center>

<center>
<a href="<%=request.getContextPath()%>/content/index.jsp" class="image">
<img
 style="height: 250px;" alt="Antelope" src="Impala1.jpg">
</center>
</a>
<center> <h6> Photo: Hiram Rosales Nanduca </h6> </center>

<div class="imagen_der"> <img src="figure1.eps-1D.png"/> </div>
Antelope [<a 
 href="#References">1</a>], is a&nbsp; <a
 href="http://en.wikipedia.org/wiki/Model_checking">model checker</a>
for analyzing and constructing <span style="font-weight: bold;">branching-time</span> Boolean GRNs 
[<a href="#References">2</a>],
where a state can have <span style="font-weight: bold;">more than one possible future</span>.
 
Branching time can be used for representing:<br>
<ol>
<li> asynchrony, </li>
<li> incompletely specified behavior (e.g., missing experiments), and </li>
<li> interaction with the environment. </li>
</ol>

Many existing GRN software systems having branching time employ such a
device almost exclusively for asynchrony.
Moreover, such systems usually traverse the dynamics of the GRN 
<span style="font-weight: bold;">forward</span>, selecting only one possible future with a 
<span style="font-weight: bold;">random device</span>.
Antelope, by contrast, traverses the dynamics of the GRN
<span style="font-weight: bold;">backward</span>, without having to employ a random device.

As a simple example, consider a GRN with the following interaction diagram:
<br>
<br>
<center>
    <img src="figure3.eps-1D.png"/>
</center>
<br>
where gene <em>x</em> activates itself but represses gene <em>y</em>,
and gene <em>y</em> activates itself.
From this diagram, the next value of <em>y</em> is not determined
when either both <em>x</em> and <em>y</em> are active
or both are inactive.
This indetermination can be represented with stars in the logical rules (A).
The state diagram appears in (B).

<center>
<table cellpadding=10>
<tr> <td> <img src="table_figs3_4.eps-1D.png"/> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td> <td> <img src="figure4.eps-1D.png"/> </td> </tr>
<tr> <td align=center> <small> A </small> </td> <td align=center> <small> B </small> </td>
</table>
</center>
<br>
Note that there are <span style="font-weight: bold;">two</span> kinds of stationary state:
<ul>
<li><span style="font-weight: bold;">stable</span> stationary states (<em>s</em><sub>1</sub> and <em>s</em><sub>2</sub>), and</li>
<li><span style="font-weight: bold;">unstable</span> stationary states (<em>s</em><sub>0</sub> and <em>s</em><sub>3</sub>).</li>
</ul>
<br>
<span style="font-weight: bold;">Antelope</span> provides a language for reasoning about these two kinds of 
stationary state and numerous other properties of states.
<br>
<br>


<center><a href="<%=request.getContextPath()%>/content/index.jsp">Try Antelope here</a></center>
<br>
<br>

<hr style="width: 100%; height: 2px;"><br>
<span style="font-weight: bold;"><a name="References"></a>References</span><br>
<ol>
  <li>Arellano G, Argil J, Azpeitia E, Ben&iacute;tez M, Carrillo M, G&oacute;ngora P, Rosenblueth DA and Alvarez-Buylla ER:
&#8220;Antelope&#8221;: a hybrid-logic model checker for branching-time Boolean GRN analysis.<br>
Accepted in BMC Bioinformatics.<br>
  </li>
  <li>Thomas R, D&prime;Ari R: Biological Feedback. CRC Press 1990.
  </li>
</ol>

</body>

