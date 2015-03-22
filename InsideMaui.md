# Details about topic indexing with Maui #

This wiki page describes how Maui performs topic indexing.




## Ways of topic indexing ##

Topic indexing can be realized in three ways:

  1. **Term assignment** (also referred to as _keyphrase indexing_ and _subject indexing_) uses domain-specific thesauri as a source of topics.
  1. **Keyphrase extraction** (also called _keyword extraction_) identifies phrases that are prominent in a given text. However, it can be made more consistent if topics are chosen with respect to Wikipedia.
  1. **Tagging** is an example of _keyphrase extraction_, where tags are freely chosen and are unrestricted.

## Candidate generation and filtering ##

Maui implements a two-stage algorithm for performing these tasks automatically.

The first stage, **candidate generation**, identifies candidate topics in a given document.
Candidates are either mappings from phrases to terms in the vocabulary (thesaurus in term assignment or Wikipedia in controlled keyphrase extraction), or document phrases (in tagging).

The second stage, **filtering**, analyzes the properties, or features, of the candidate topics and filters out the most significant ones. Maui utilizes several kinds of features:

  * **Frequency statistics**, such as term frequency, inverse document frequency, TFxIDF;
  * **Occurrence positions** in the document text, e.g. beginning and end, spread of occurrences;
  * **Keyphraseness**, computed based on topics assigned previously in the training data, or particular behaviour of terms in Wikipedia corpus;
  * **Semantic relatedness**, computed using semantic relations encoded in provided thesauri, if applicable, or using statistics from the Wikipedia corpus;

Maui uses machine learning to capture the typical feature values of topics assigned manually to training documents and then applies the generated model to assign topics to unseen documents.

## Software inside Maui ##

**Kea**

> Maui builds on the keyphrase extraction algorithm [Kea](http://nzdl.org/kea) in that it  utilizes the two-step process of automatic indexing: candidate selection and filtering. Major parts of Kea became parts of Maui without any further modifications. Other parts, like feature computation, were extended with new elements.

**Weka**

> Maui inherits from Kea the machine learning toolkit [Weka](http://www.cs.waikato.ac.nz/ml/weka/) for creating the topic indexing model from documents with topics assigned by people and applying it to new documents. However, while Kea only containes a cut-down version of Weka (several classes), Maui includes the complete library. This gives more opportunities to experienced users for tailoring Maui’s code to specific data sets.

**Jena**

> In order to make Maui applicable for topic indexing with many kinds of controlled vocabularies, the [Jena library](http://jena.sourceforge.net/) is included. It reads RDF-formatted thesauri and stores them in memory for a quick access. Any vocabulary in RDF format (specifically SKOS) can be used:

  * [Agrovoc](http://www.fao.org/aims/ag_download.htm) - Agricultural thesaurus developed by the FAO of UN;
  * [MeSH](http://thesauri.cs.vu.nl/eswc06/mesh/rdf/meshdata.rdf) - Medical Subject Headings by the National Library of Medicine;
  * [HEP](http://invenio-demo.cern.ch/help/hacking/bibclassify-hep-taxonomy) - High Energy Physics thesaurus;
  * [Library of Congress Subject Headings](http://id.loc.gov/authorities/search/) (untested)
  * and [others](http://esw.w3.org/topic/SkosDev/DataZone).

**Wikipedia Miner**

> For accessing Wikipedia data, Maui utilizes [Wikipedia Miner](http://wikipedia-miner.sourceforge.net). This package converts regular Wikipedia dumps into MySql database format and provides an object-oriented access to parts of Wikipedia like articles, disambiguation pages and hyperlinks. Wikipedia Miner also implements an algorithm for computing semantic relatedness between articles that Maui uses to disambiguate documents to Wikipedia articles and for computing semantic features.