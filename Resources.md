# Additional resources for automatic keyphrase extraction and term assignment #



Apart from the three collections listed in the MultiplyIndexedData, Maui was also tested on several term assignment collections, where each document had a single set of manually assigned topics.


## FAO-780 data set for term assignment ##

  * [Download FAO-780](http://maui-indexer.googlecode.com/files/fao780.tar.gz)

This data set contains 780 documents with terms from Agrovoc assigned by professionals. When using this data set, please cite Medelyan (2009) or Medelyan and Witten (2008), see [Publications](Publications.md).


## Other data sets on the web ##

[NUS Keyphrase Corpus](http://aye.comp.nus.edu.sg/downloads/keyphraseCorpus/) can be used for training and testing keyphrase extaction and tagging. To use it with Maui, the data first needs to be converted into the required format.


[SemEval-2010 Keyphrase Extraction](http://docs.google.com/Doc?id=ddshp584_46gqkkjng4) will soon publish their data for the participants of the shared task. This data set will be similar to the NUS Keyphrase corpus.

See also: MultiplyIndexedData


## Vocabularies ##

For term assignment, a number of vocabularies in SKOS format are available on the web:

  * [Library of Congress Subject Headings LSCH](http://id.loc.gov/authorities/search/)
  * [Medical Subject Headings thesaurus MeSH](http://thesauri.cs.vu.nl/eswc06/mesh/rdf/meshdata.rdf)

Please note:
Maui user [Amrita](http://groups.google.com/group/kea-and-maui-support/browse_thread/thread/d290b81c653249e2#) recommends to use [HIVE to convert original MeSH vocabulary into SKOS](http://code.google.com/p/hive-mrc/wiki/MeshToSKOS).

  * FAO’s agricultural thesaurus Agrovoc: [general info](http://www.fao.org/agrovoc/) and [download site](http://aims.fao.org/en/website/Download/sub).
  * [List of other SKOS thesauri at FAO](http://aims.fao.org/en/website/Knowledge-Organization-Systems-%28KOS%29/sub)
  * [DESY’s High Energy Physics HEP thesaurus](http://invenio-demo.cern.ch/help/hacking/bibclassify-hep-taxonomy)
  * [W3C’s list of SKOS thesauri](http://esw.w3.org/topic/SkosDev/DataZone)

## Competing systems and demos ##

The topic indexing blog provides a [list of tools for automatic keyphrase extraction, terminology extraction, tagging and other tasks](http://maui-indexer.blogspot.com/2009/07/useful-web-resources-related-to.html).