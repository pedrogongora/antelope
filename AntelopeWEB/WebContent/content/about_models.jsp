
  <title>Antelope: Analyzer of Networks through TEmporal LOgic
sPEcifications</title>

<body>


<h1>About Antelope&#39;s Predefined Models</h1>

<h2><a name="index">Index</a></h2>

<ol>
<li><a href="#flower">The Flower Organ Specification GRN</a></li>
<li><a href="#root">Root Stem Cell Niche GRN</a></li>
<li><a href="#oscillations">Oscillations</a></li>
</ol>

<h2><a name="flower">The Flower Organ Specification GRN</a></h2>

During the last years, an experimentally grounded GRN model for flower organ specification in
<i>Arabidopsis thaliana</i> has been built and investigated (Mendoza and Alvarez-Buylla 1998, 1999;
Espinosa-Soto et al., 2004; Chaos et al., 2006; Alvarez-Buylla et al., 2010). This model incorporates the
complex regulatory interactions among the so-called ABC genes (<i>AP1, AP2, AP3, PI, AG</i>) and among
other genes that are key to this process. This network has been useful to provide a dynamic account of
the process of flower organ specification, and has also yielded novel predictions regarding, for
example, gene interactions and the role of stochasticity in this process. This network model has ten
steady states, each of which corresponds to a particular cell type in the early development of the flower.
Since this is a well described and continuously updated network, it has become a model network in the
study of developmental and biological GRNs (Alvarez-Buylla et al., 2010).

<p>Download Antelope&#39;s <a href="../modelos/FlowerGRN.tbl">Flower GRN model</a>.

<center>
    <img src="FlowerGRN.png"
         />
</center>



<h3>References</h3>

<p>Alvarez-Buylla ER, Ben&iacute;tez M, Corvera-Poir&eacute; A, Chaos Cador A,
de Folter S, Gamboa de Buen A, Garay-Arroyo A, Garc&iacute;a-Ponce B,
Jaimes-Miranda F, P&eacute;rez-Ruiz RV, Pi&ntilde;eyro-Nelson A,
S&aacute;nchez-Corrales YE. (2010).
Flower Development. The Arabidopsis Book, 8(1):1-57.

<p>Chaos, A., Aldana, M., Espinosa-Soto, C., Garc&iacute;a Ponce de Le&oacute;n,
B., Garay-Arroyo, A., and Alvarez-Buylla, E.R. (2006).
From genes to flower patterns and evolution: dynamic models of gene regulatory
networks. J. Plant Growth Regul. 25: 278-289.

<p>Espinosa-Soto, C., Padilla-Longoria, P., and Alvarez-Buylla, E. (2004).
A gene regulatory network model for cell-fate determination during Arabidopsis
thaliana flower development that is robust and recovers experimental gene
expression profiles. Plant Cell 6: 2923-2939.

<p>Mendoza, L., and Alvarez-Buylla, E.R. (1998).
Dynamics of the genetic regulatory network for Arabidopsis thaliana flower
morphogenesis. J. Theor. Biol. 193: 307-319.

<p>Mendoza, L., Thieffry, D., and Alvarez-Buylla, E.R. (1999).
Genetic control of flower morphogenesis in Arabidopsis thaliana a logical analysis.
Bioinformatics 15: 593-606.

<p><a href="#index">Go back to the index</a>




<h2><a name="root">Root Stem Cell Niche GRN</a></h2>

Recent experimental work has uncovered some of the genetic components required to maintain the
<i>Arabidopsis thaliana</i> root stem cell niche (SCN) and its structure. Two main pathways are involved.
One pathway depends on the genes SHORTROOT and SCARECROW and the other depends on the
PLETHORA genes, which have been proposed to constitute the auxin readouts. Available experimental
data for this system have been integrated into a dynamic gene regulatory network (Azpeitia et al.,
2010). This model reproduces some features of the system under study and also helps postulate
predictions for additional components and interactions. The recovered gene expression configurations
are stable to perturbations and the models are able to recover the observed gene expression profiles of
almost all the mutants described so far. However, there are still relatively few data available for this
system and this GRN is constantly being updated.

<p>Download Antelope&#39;s <a href="../modelos/RootGRN.eqn">Root Stem Cell Niche GRN model</a>.

<center>
    <img src="RootGRN.png"
         />
</center>



<h3>References</h3>

<p>Azpeitia E, Ben&iacute;tez M, Vega I, Villarreal C, Alvarez-Buylla ER (2010).
Single-cell and coupled GRN models of cell patterning in the Arabidopsis
thaliana root stem cell niche. BMC Syst Biol 4:134.

<p><a href="#index">Go back to the index</a>



<h2><a name="oscillations">Oscillations</a></h2>

This model was specifically designed with the purpose of obtaining cyclic steady
states, and was then used to test if Antelope could correctly detect them.
This model has three nodes. Each node negatively regulates the activity of one
node and has only one regulator. This gives rise to a circuit where node A
represses node B, node B represses node C and node C represses node A.
Hence, the model has two cyclic steady states: one cyclic steady state of size
6 and one steady state of size 2.

<p>Download Antelope&#39;s <a href="../modelos/Oscillations.eqn">Oscillations model</a>.

<p><a href="#index">Go back to the index</a>

<center>
    <img src="oscillations.png"
         />
</center>


</body>

