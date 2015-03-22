# How to use Maui #


See also: [Installation](Installation.md)



## Preparing the data ##
After Maui is installed, there are two ways of using it: from the command line and from the Java code. Either way, the input data is required first. The _data_ directory in Maui's download package contains some examples of input data.

### Formatting the document files ###
Each document has to be stored individually in text form in a file with extension _.txt_. Maui takes as an input the name of the directory with such files. If a model needs to be created first, the same directory should contain main topics assigned manually to each document.

### Formatting the topic files ###
The topic sets need to be stored individually in text form, one topic per line, in a file with the same name as the document text, but with the extension _.key_.

### Output ###
If Maui is used to generate main topics for new documents, it will create _.key_ files for each document in the input directory. If topics are generated, but _.key_ files are already existent, the existing topics are used as gold standard for the evaluation of automatically extracted ones.

## Command line usage ##
Maui can be used directly from the command line.

The general command is:
> `java maui.main.MauiModelBuilder (or maui.main.MauiTopicExtractor) -l directory -m model -v vocabulary -f {skos|text} -w database@server`

### Two modes of topic indexing ###

**MauiModelBuilder** is used when a topic indexing model is created from documents with existing topics.

**MauiTopicExtractor** is used when a model is created, to assign topics to new documents.

Examples with experimental data are supplied in the Maui package. The following commands refer to the directories with this data.

### Examples of command lines for different topic indexing tasks ###

1. **Automatic tagging** and **keyphrase extraction** - when topics are extracted from document text itself.

> `MauiModelBuilder -l data/automatic_tagging/train/ -m tagging_model`

> `MauiTopicExtractor -l data/automatic_tagging/test/ -m tagging_model`

2. **Term assignment** - when topics are taken from a controlled vocabulary in [SKOS format](http://esw.w3.org/topic/SkosDev/DataZone)

> `MauiModelBuilder -l data/term_assignment/train/ -m assignment_model -v agrovoc -f skos`

> `MauiTopicExtractor -l data/term_assignment/test/ -m assignment_model -v agrovoc -f skos`

3. **Topic indexing with Wikipedia** - when topics are Wikipedia article titles. Note in this case [WikipediaMiner](http://wikipedia-miner.sourceforge.net/) needs to be installed and running first.

> `MauiModelBuilder -l data/wikipedia_indexing/train/ -m indexing_model -v wikipedia -w enwiki@localhost`

> `MauiTopicExtractor -l data/wikipedia_indexing/test/ -m indexing_model -v wikipedia -w enwiki@localhost`

For **terminology extraction** use the command line argument _-n_ set to a high value to extract all possible candidate topics in the document.