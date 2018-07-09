# Antelope 
## (Analyzer of Networks through TEmporal LOgic sPEcifications)

![Image of and Antelope](antelope.png)

Antelope [1], is a  [model checker](https://en.wikipedia.org/wiki/Model_checking) for analyzing and constructing branching-time Boolean Gene Regulatory Networks [2].

I've made most of the coding, except for the jsp-based UI, and we released it in 2011, along with the publication of [1]. Antelope was available at a [IIMAS](https://www.iimas.unam.mx/en/home) server, but as it's former server is not available anymore, I've decided to share the source code here (mainly for educational purposes). Antelope still runs on current JRE versions, however, it may require some effort to compile and run from sources.

Antilope consist of 3 modules: __AntelopeCore__, __AntelopeWEB__ and __EmbeddedAntelope__.

#### AntelopeCore
This is the main model checker, it contains parsers (generated with [JavaCC](https://javacc.org/)) for model specification files and for the Hybrid CTL formulas. The core relies on the [JavaBDD](http://javabdd.sourceforge.net/) library for efficient BDD manipulation. This module includes a very simple REPL user interface for quick testing.

#### AntelopeWEB
Antelope's web based user interface. It was designed to run on [Apache Tomcat](http://tomcat.apache.org/) ver. 7.

#### EmbeddedAntelope
This module was designed to distribute Antelope without having to install and configure any web application server. It uses the then new embedded Tomcat distribution.

1. Arellano G, Argil J, Azpeitia E, Benítez M, Carrillo M, Góngora P, Rosenblueth DA and Alvarez-Buylla ER: ["Antelope": a hybrid-logic model checker for branching-time Boolean GRN analysis](http://www.biomedcentral.com/1471-2105/12/490/abstract). BMC Bioinformatics 12:490. 2011.
2. Thomas R, D′Ari R: Biological Feedback. CRC Press 1990.
