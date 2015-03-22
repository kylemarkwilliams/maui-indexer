## Summary ##
Maui automatically identifies main topics in text documents. Depending on the task, topics are tags, keywords, keyphrases, vocabulary terms, descriptors, index terms or titles of Wikipedia articles.


Maui performs the following **tasks**:

  * term assignment with a controlled vocabulary (or thesaurus)
  * subject indexing
  * topic indexing with terms from Wikipedia
  * keyphrase extraction
  * terminology extraction
  * automatic tagging

It can also be used for terminology extraction and semi-automatic topic indexing.

### Live Demo ###
You can try out this [live Maui demo](http://maui-indexer.appspot.com/) by just copying and pasting a piece of text of your choice or uploading a document in Word or PDF format.

### Support ###
Feel free to post any support questions in the [Maui user group](http://groups.google.com/group/kea-and-maui-support), but please make sure you have read the Wiki pages: [Download](Download.md), [Installation](Installation.md) and [Usage](Usage.md).

If you would like to use Maui commercially and need help optimising its performance, you can contact me via my consultancy [Entopix](http://www.entopix.com).

## Domain and language independence ##

Maui has been successfully tested on computer science, agricultural, medicine, physics, biology, bioinformatics documents, as well as on blog posts and news articles.

It supplies stemmers and stopwords for English, French and Spanish, but can be extended to work in many other languages, including languages that require special encoding.

Examples are provided in [Maui's Wiki pages](Examples.md)

## Background ##

Maui has been developed by [Alyona Medelyan](http://www.medelyan.com) as a part of her PhD project, under supervision of [Ian H. Witten](http://www.cs.waikato.ac.nz/~ihw) and [Eibe Frank](http://www.cs.waikato.ac.nz/~eibe) in the [Department of Computer Science](http://www.cs.waikato.ac.nz) at the [University of Waikato](http://www.waikato.ac.nz/), New Zealand. The PhD was sponsored by a research grant from Google.

Maui builds on the keyphrase extraction algorithm [Kea](http://www.nzdl.org/kea), but provides additional functionalities: it allows the assignment of topics to documents based on terms from Wikipedia using [Wikipedia Miner](http://wikipedia-miner.sourceforge.net/). Maui also has many new features that help identify topics more accurately.

Read more about how Maui works in the Wiki pages InsideMaui and in [publications about Maui](Publications.md).