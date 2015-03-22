# Examples of topic indexing with Maui #



All examples are produced using data sets available for download under [Resources](Resources.md) and [MultiplyIndexedData](MultiplyIndexedData.md).


## Automatic tagging with Maui ##

We run a test on 180 documents from [CiteULike](http://www.citeulike.org). For each document, tags on which at least two human taggers agreed were considered as correct. All other tags as incorrect. Maui achieved F-Measure of nearly 50%, i.e. a half of the top 10 automatically extracted tags were correct.

Here are three examples of different performance levels. Numbers in brackets indicate how many people agreed on this tag. Bold indicates a correct term. Italic indicates that Maui matched a tag assigned by one person.

**Excellent performance** (F-Measure 80%). [Different time courses of learning-related activity in the prefrontal cortex and striatum](http://www.citeulike.org/article/101973)

<table border='1'>
<tr>
<td><b>At least two taggers</b></td>
<td><b>One tagger</b></td>
<td><b>Maui</b></td>
</tr>

<tr>

<td valign='top'>
learning (5)<br></br>
striatum (4)<br></br>
monkey (3)<br></br>
prefrontal cortex (2)<br></br>
reversal (2)<br>
</td>

<td valign='top'>
basalganglia, rt, dlpfc,<br></br>
prefrontal, caudate, pfc,<br></br>
stimulusresponseassociation,<br></br>
neurophysiology, dynamics,<br></br>
striatothalamocortical<br>
</td>

<td valign='top'>
<b>learning</b><br></br>
<b>striatum</b><br></br>
<b>monkey</b><br></br>
<b>prefrontal cortex</b><br></br>
<i>basal ganglia</i>
</td>

</tr>
</table>

**Average performance** (F-Measure 46%). [Global and regional drivers of accelerating CO2 emissions](http://www.citeulike.org/article/1322886)

<table border='1'>
<tr>
<td><b>At least two taggers</b></td>
<td><b>One tagger</b></td>
<td><b>Maui</b></td>
</tr>
<tr>

<td valign='top'>
co2 (3)<br></br>
emissions (3)<br></br>
carbon (2)<br></br>
ipcc (2)<br></br>
economics (2)<br></br>
climate (2)<br></br>
projections (2)<br></br>
regional (2)<br>
</td>

<td valign='top'>
global, scenarios,<br></br>
sres, china<br>
</td>

<td valign='top'>
<b>co2</b><br></br>
<b>emissions</b><br></br>
<b>carbon</b><br></br>
<i>global</i><br></br>
energy<br>
</td>
</tr>
</table>

**Poor performance** (F-Measure 0%). [Drought sensitivity shapes species distribution patterns in tropical forests.](http://www.citeulike.org/article/1272477)

<table border='1'>
<tr>
<td><b>At least two taggers</b></td>
<td><b>One tagger</b></td>
<td><b>Maui</b></td>
</tr>
<tr>

<td valign='top'>
precipitation (2)<br></br>
drought (2)<br></br>
ecology (2)<br></br>
vegetation (2)<br></br>
climate (2)<br>
</td>

<td valign='top'>
tropical, tropic<br>
</td>

<td valign='top'>
<i>tropical</i><br></br>
tropical forests<br></br>
drought sensitivity<br></br>
species<br></br>
regional<br>
</td>
</tr>
</table>




&lt;hr&gt;



## Assignment of Wikipedia terms to Computer Science articles ##

In this experiment, 20 computer science technical reports were used. Each document received topics from 15 teams of graduate students. [Multiple data set](MultiplyIndexedData.md) was used to compute Maui's consistency with the consistency of people with each other. On average Maui outperforms 8 of the human teams.

Consistency analysis is used to show three different levels of Maui's performance. Numbers in brackets indicate how many human teams chose that topic.

**Excellent performance**. Maui: 52%, people 42%

Document _Occam's razor: The cutting edge for parser technology_
<table border='1'>
<tr>
<td>
<b>Most frequent topics by 15 teams</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Yacc (13)<br></br>
Parsing (12)<br></br>
Compiler-compiler (9)<br></br>
Backus Naur form (9)<br></br>
Compiler (6)<br>
</td>
<td valign='top'>
Yacc (13)<br></br>
Parsing (12)<br></br>
Compiler-compiler (9)<br></br>
Compiler (6)<br></br>
Programming language (4)<br>
</td>
</tr>
</table>


**Average performance**. Maui: 30%, people 53%

Document _A Safe, Efficient Regression Test Selection Technique_
<table border='1'>
<tr>
<td>
<b>Most frequent topics by 15 teams</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Regression testing (15)<br></br>
Software maintenance (13)<br></br>
Control flow graph (10)<br></br>
Software testing (9)<br></br>
Algorithm (7)<br>
</td>
<td valign='top'>
Software maintenance (13)<br></br>
Algorithm (7)<br></br>
Test suite (2)<br></br>
Computer software (1)<br></br>
Control flow (0)<br>
</td>
</tr>
</table>


**Poor performance**. Maui: 17%, people 17%

Document _Cone trees in the UGA graphics system_
<table border='1'>
<tr>
<td>
<b>Most frequent topics by 15 teams</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Hierarchical model (7)<br></br>
3D computer graphics (7)<br></br>
Visualization (graphic) (6)<br></br>
Tree (data structure) (5)<br></br>
Computer graphics (3)<br>
</td>
<td valign='top'>
Computer graphics (3)<br></br>
Visualization (2)<br></br>
PARC (company) (2)<br></br>
Visual display unit (0)<br></br>
Graphics (0)<br>
</td>
</tr>
</table>




&lt;hr&gt;



## Assignment of Agrovoc terms to agricultural documents ##

These experiments resulted from the collaboration with the [Food and Agriculture Organization (FAO) of the United Nations](http://www.fao.org). Each document is automatically indexed with terms from the domain-specific thesaurus [Agrovoc](http://www.fao.org/agrovoc). The results are compared to those assigned by professionals.

### English agricultural documents ###

Six professional indexers at FAO indexed 30 documents each. Using consistency measure, we computed the agreement among the indexers and their agreement with Maui. Maui's consistency is only slightly worse than the consistency of professionals.

Here are examples of three performance levels that Maui achieved:


**Excellent performance.** Maui 51% vs. Indexers 40%

Document _The dynamics of sanitary and technical requirement assisting the poor_

<table border='1'>
<tr>
<td>
<b>Topics by 6 professional indexers</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Food safety (5)<br></br>
Livestock (5)<br></br>
Standards (5)<br></br>
Poverty (5)<br></br>
Developing countries (4)<br></br>
Food chains (4)<br></br>
Phytosanitary measures (4)<br></br>
Animal production (2)<br>
</td><td>Food safety (5)<br></br>
Livestock (5)<br></br>
Standards (5)<br></br>
Developing countries (4)<br></br>
Food chains (4)<br></br>
Animal health (2)<br></br>
FAO (2)<br></br>
Risk management (2)<br>
</td>
</tr>
</table>


**Average performance.** Maui 29% vs. Indexers 35%

Document _Climate change and the forest sector_

<table border='1'>
<tr>
<td>
<b>Topics by 6 professional indexers</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Climatic change (6)<br></br>
International agreements (5)<br></br>
Forests (4)<br></br>
Greenhouse effect (4)<br></br>
Legislation (4)<br></br>
Forestry policies (4)<br></br>
Pollution control (4)<br></br>
Greenhouse gases (3)</td><td>Climatic change (6)<br></br>
Forests (4)<br></br>
Greenhouse gases (3)<br></br>
Forest management (3)<br></br>
Property (0)<br></br>
Climate (0)<br></br>
Land use (0)<br></br>
Forest products (0)<br>
</td>
</tr>
</table>


**Poor performance.** Maui 7% vs. Indexers 35%

Document _Phosphorus limitation of microbial processes in tropical forests_

<table border='1'>
<tr>
<td>
<b>Topics by 6 professional indexers</b>
</td>
<td>
<b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td>Tropical rain forests (6)<br></br>
Phosphorus (6)<br></br>
Soil chemicophysical properties (3)<br></br>
Soil fertility (3)<br></br>
Soil microorganisms (3)<br></br>
Soil biology (2)<br></br>
Microorganisms (2)<br></br>
Biodegradation (2)<br>
</td><td>
Carbon (2)<br></br>
Costa Rica (2)<br></br>
Tropical forests (0)<br></br>
Respiration (0)<br></br>
Rain forests (0)<br></br>
Primary productivity (0)<br></br>
Forests (0)<br></br>
Soil (0)<br>
</td>
</tr>
</table>

<br></br>

### French and Spanish agricultural documents ###

After training on 66 French and 46 Spanish documents, no modification to code other than a new stemmer, stopwords list and encoding. Matching terms are shown in bold.

French document _Relations terres-eau dans les bassins versant ruraux_.

<table border='1'>
<tr>
<td><b>Topics by professionals</b></td>
<td><b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td><b>Impact sur l'environnement</b><br></br>
<b>Ressource en eau</b><br></br>
<b>Utilisation des terres</b><br></br>
Aménagement de bassin versant<br></br>
Conservation de l'eau<br></br>
Qualité de l'eau<br></br>
Utilisation de l'eau<br></br>
Réglementation<br></br>
Eau de ruissellement<br></br>
Développement rural<br></br>
Organisation socioéconomique<br>
</td><td valign='top'>
<b>Impact sur l'environnement</b><br></br>
<b>Ressource en eau</b><br></br>
<b>Utilisation des terres</b><br></br>
Bassin versant<br></br>
Pollution de l'eau <br></br>
Changement climatique<br></br>
Pollution atmosphérique<br></br>
Pollution par l'agriculture<br></br>
Étude de cas<br></br>
Métal lourd<br>
</td>
</tr>
</table>



Spanish document _Evaluación de los recursos forestales mundiales 2000_.

<table border='1'>
<tr>
<td><b>Topics by professionals</b></td>
<td><b>Topics assigned by Maui</b>
</td>
</tr>
<tr>
<td valign='top'><b>Ordenación forestal</b><br></br>
<b>Plantación forestal</b><br></br>
<b>Productos forestales</b><br></br>
<b>Recursos forestales</b><br></br>
Ciencias forestales<br></br>
Reforestación<br></br>
Sostenibilidad<br></br>
Transferencia de tecnología<br></br>

</td><td valign='top'>
<b>Ordenación forestal</b><br></br>
<b>Plantación forestal</b><br></br>
<b>Productos forestales</b><br></br>
<b>Recursos forestales</b><br></br>
Inventarios forestales<br></br>
Tierras forestales<br></br>
Bosques <br></br>
Biodiversidad<br></br>
Utilización de la tierra<br></br>
Muestra<br></br>

</td>
</tr>
</table>




&lt;hr&gt;



## Indexing with Medical Subject Headings ##

After training on 450 documents from PubMed, Maui assigned MeSH terms like the following.

Document _Determining lifestyle correlates of body mass index using multilevel analyses_.

<table border='1'>
<tr>
<td>
<b>assigned by professionals</b>
</td>
<td>
<b>assigned by Maui</b>
</td>
</tr>
<tr>
<td>
Adult<br></br>
Aged<br></br>
Body Mass Index<br></br>
Female<br></br>
Humans<br></br>
Life Style<br></br>
Linear Models<br></br>
Longitudinal Studies<br></br>
Male<br></br>
Middle Aged<br></br>
Norway<br></br>
Obesity<br></br>
Questionnaires<br></br>
Risk Factors<br></br>
Urban Population<br>
</td>
<td valign='top'>
Aged<br></br>
Body Mass Index<br></br>
Cardiovascular Diseases<br></br>
Humans<br></br>
Longitudinal Studies<br></br>
Middle Aged<br></br>
Motor Activity<br></br>
Norway<br></br>
Risk Factors<br>
</td>
</tr>
</table>




&lt;hr&gt;



## Subject indexing with High Energy Physics thesaurus terms ##

Example document from CERN Document server
_Two-loop electroweak corrections to Higgs production at hadron colliders._

<table border='1'>
<tr>
<td>
<b>assigned by professionals</b>
</td><td>
<b>assigned by Maui</b>
</td>
</tr>
<tr>
<td valign='top'>
quark: top<br></br>
Higgs mass<br></br>
standard model<br></br>
LHC-B<br></br>
quantum chromodynamic<br></br>
spontaneous symmetry breaking<br></br>
Higgs boson<br></br>
symmetry breaking<br></br>
intermediate boson: mass<br>
</td>
<td valign='top'>
quark: top<br></br>
Higgs particle<br></br>
standard model<br></br>
LHC-B<br></br>
quantum chromodynamics<br></br>
cross section<br></br>
Higgs particle: mass<br></br>
hadron: production<br></br>
gluon: fusion<br></br>
boson<br>
</td>
</tr>
</table>