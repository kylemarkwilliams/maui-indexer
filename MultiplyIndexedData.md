# Multiply indexed data #

This page describes three collections available for download that were used in experiments with Maui. See [Examples](Examples.md) or Medelyan, 2009 ([Publications](Publications.md)) for examples of topics automatically assigned to these documents by Maui.

Unlike collections with just one topic set per document (some are listed in [Resources](Resources.md)), these three collections contain topic sets assigned to each document by different people. This allows to measure the agreement between the people, which provides a direct comparison to the performance of the algorithm.

## FAO-30 data set for term assignment ##

FAO-30 is a test set containing 30 agricultural documents, each indexed with terms from [Agrovoc](http://www.fao.org/agrovoc) by 6 professional indexers at the [Food and Agriculture Organization of the UN](http://www.fao.org).

  * [Download FAO-30](http://maui-indexer.googlecode.com/files/fao30.tar.gz)

When using this test set, please cite Medelyan (2009) or Medelyan and Witten (2008), see [Publications](Publications.md).

## WIKI-20 data set for topic indexing with Wikipedia ##

WIKI-20 is a test set with 20 computer science technical reports, each indexed with terms from [Wikipedia](http://www.wikipedia.org) by 15 teams of computer science graduate and undergraduate students. The test set was created in an indexing competition, which ensured high quality of assigned topics.

  * [Download WIKI-20](http://maui-indexer.googlecode.com/files/wiki20.tar.gz)

When using this test set, please cite Medelyan (2009) or Medelyan et al. (2008), see [Publications](Publications.md).

## CiteULike-180 data set for automatic tagging ##

CiteULike-180 is the only test set listed here that was created in natural environments. It has been automatically extracted from the large data set of tags assigned to the bookmarking platform [CiteULike](http://www.citeulike.org).

The following restrictions were applied to extract this test set:
  * each tag should be assigned by at least two people
  * each tagger should have at least two co-tagger
  * each tagger should have tagged at least three documents
  * the document should be available for free download

The resulting set contains 180 science articles from HighWire and Nature, with tags assigned by 332 voluntary taggers on CiteULike.

  * [Download CiteULike-180](http://maui-indexer.googlecode.com/files/citeulike180.tar.gz)

When using this test set, please cite Medelyan (2009) or Medelyan et al. (2009), see [Publications](Publications.md).


## How to measure consistency ##

Consistency, or _inter-indexer consistency_ is measured using a traditional measure used in library science, proposed by [Rolling (1981)](http://eric.ed.gov/ERICWebPortal/recordDetail?accno=EJ246036).

The formula is _2C/(A+B)_, where _C_ is the number of topics two sets have in common and _A_ and _B_ are the total numbers of topics in each set.

Given two topic sets:

  * _complex systems, network, small world_
  * _theoretical, small world, networks, dynamics_

The intersection, or the set of topics the two sets have in common (after stemming) is

  * _network, small world_

This gives the Rolling consistency of 2×2/(3+4) = 0.57. To compute precision and recall, one of the sets needs to be seen as the gold standard. If the first set was assigned automatically, the precision is 2/3 = 0.66 and recall is 2/4 = 0.5. The F-measure is 2×0.66×0.5/(0.66+0.5) = 0.57, the same as Rolling.

**Note**: To compute indexing consistency of a person or an algorithm, the values need to be computed for each document and co-indexer and then averaged.

