# Installation and running guide for Maui #



## Download ##

Select the “Downloads” tab. When installing Maui for the first time, choose the archive file maui1.2\_all-incl.tar.gz for download. Move the archive file to the preferred installation directory and extract its contents. In a shell (e.g. Terminal on Mac) this can be done with the following command:

> `gzip –dc maui1.2_all-incl.tar.gz | tar xf –`

The extracted directory Maui1.2 contains the following subdirectories:
  * data – stopwords, vocabularies and test documents for different tasks
  * lib – required jar libraries
  * src – source code
  * doc – Javadoc documentation

Maui is written in Java 5.0. Before running the main classes, they need to be compiled. There are two choices for doing this:
  1. Use an IDE (integrated development environment) like [Eclipse](http://www.eclipse.org).
  1. Use command line scripts in a shell, e.g. Terminal, Xterm, C shell.

## Using Maui in Eclipse ##
Import the project into the workspace: File --> Import, then follow the instructions. Make sure that all jar files in the lib directory are added to the build path:

Project --> Properties --> Java Build Path --> “Libraries” tab --> “Add jars…”

Make sure that the correct JRE System Library is used. If JVM Java 1.5.0. is not the last item in the list of libraries, click “Edit…” and choose it in Alternate JRE.

If everything is done correctly, Eclipse will automatically compile Maui without error messages. Now the main class files can be run by clicking on them in the maui.main package and opening Run --> “Open Run Dialogue…”. In the Arguments tab, Program arguments window, set the options as described in Section E.5. In the VM arguments window, if necessary, increase the size of memory: `–Xmx1200m`.

## Using Maui in a shell ##

You can either set up Java CLASSPATH to include all libraries required to run Maui, or use the following commands:

1.  Compile the MauiModelBuilder:

```
Maui1.2 zelandiya$ javac -cp "lib/*:src" src/maui/main/MauiModelBuilder.java
```

2. Run the MauiModelBuilder (set the options accordingly, I have used tagging in this example):

```
Maui1.2 zelandiya$ java -cp "lib/*:src" maui.main.MauiModelBuilder -l data/automatic_tagging/train/ -m test -v none`

-- Building the model...
-- Reading the input documents...
Done!
```

3. Compile the MauiTopicExtractor

```
Maui1.2 zelandiya$ javac -cp "lib/*:src" src/maui/main/MauiTopicExtractor.java
```

4. Run the MauiTopicExtractor

```
Maui1.2 zelandiya$ java -cp "lib/*:src" maui.main.MauiTopicExtractor -l data/automatic_tagging/test/ -m test -v none
Extracting keyphrases with options: -l data/automatic_tagging/test/ -m test -v none -f null -e default -i en -n 10 -t maui.stemmers.PorterStemmer -s maui.stopwords.StopwordsEnglish     
-- Loading the model... 
-- Extracting keyphrases... 

-- Evaluation results based on 1 document:
Avg. number of correct keyphrases per document: 4 +/- 0
Precision: 40 +/- 0
Recall: 66.67 +/- 0
F-Measure: 50
```

This did not generate any new files, because the test directory already contains _.key_ files but if you run it on a directory with just the documents, it will generate a list of keywords for each document.

Installation problems and usage questions? Post on [Maui User Group](https://groups.google.com/forum/#!forum/kea-and-maui-support)

## Data preparation ##

See also [Usage](Usage.md)

Follow the examples in the directory _data_, which contains sample documents for training and testing Maui for different tasks. **Training** means creating a topic indexing model; **testing** means generating topics for new documents.

For example, _data/automatic\_tagging_ contains two directories: _train_ and _test_. The first contains three documents and their manually assigned topics from which Maui creates the model, while the second contains a document for which Maui will compute topics. The topics provided for this document are used for evaluation.

Each document is stored in a separate file with extension _.txt_, in plain text form. The topics are saved one per line in corresponding _.key_ files; each line may have an optional number that indicates how many people agreed on this topic. Note that the supplied train directories contain very little training data, and are only intended for demonstration and testing purposes. When using Maui, either provide your own training data, or download it from [MultiplyIndexedData](MultiplyIndexedData.md). Choose a data set that is similar to the one for which topics are to be extracted.


## Examples ##

See also [Usage](Usage.md)

The fastest way to understand how Maui works is to look at the Examples class. Maui can be also applied to new data using MauiModelBuilder and MauiTopicExtractor.

The example script `maui.main.Examples` demonstrates how to use Maui for three kinds of topic indexing: tagging, term assignment and indexing with Wikipedia. It can be used as a source of code snippets for a direct access from other programs. It can also be used as a test script to check whether Maui is installed correctly.

The simplest kind of topic indexing is **tagging**:
> `java maui.main.Examples tagging`
Alternatively, choose `term_assignment` or `indexing_with_wikipedia` as the argument, instead of tagging. The debugging output will show the created model and the generated topics, which will be evaluated against the manually assigned topics stored in the test directories.

In case of **term\_assignment**, Examples will use the controlled vocabulary `data/vocabularies/agrovoc_sample.rdf`. This is a subset of the original Agrovoc thesaurus, used for demonstration. [Resources](Resources.md) shows where to download the complete Agrovoc vocabulary and where to find other vocabularies in SKOS format.

In case of **indexing\_with\_wikipedia**, Examples will require access to the Wikipedia database, which should be installed as a part of [Wikipedia Miner](http://www.wikipedia-miner.sourceforge.net). Change the parameters accordingly in the main method of Examples and recompile it.

Maui can be applied directly to the document collections in the data directory or to the new collections supplied by the user. In either case, a model needs to be created first using MauiModelBuilder. Then MauiTopicExtractor can be applied to generate topics for new documents.

The examples below assume that the Java CLASSPATH is set up to access all required libraries.

For automatic tagging, the directory name and model name are the only required arguments, e.g.:
> `java maui.main.MauiModelBuilder –l data/automatic_tagging/train/ –m test –d`
> `java maui.main.MauiTopicExtractor –l data/automatic_tagging/test/ –m test –d`

For term assignment, the vocabulary name and format need to be supplied, e.g.:

> `java maui.main.MauiModelBuilder –l data/term_assignment/train/ –m test –v agrovoc_sample –f skos –d`

> `java maui.main.MauiTopicExtractor –l data/term_assignment/test/ –m test –v agrovoc_sample –f skos –d`

For topic indexing with Wikipedia, the vocabulary should be set to wikipedia and database access should be supplied, e.g.:

> `java maui.main.MauiModelBuilder –l data/wikipedia_indexing/train/ –m indexing_model –v wikipedia –w enwiki@localhost`

> `java maui.main.MauiTopicExtractor –l data/wikipedia_indexing/test/ –m indexing_model –v wikipedia –w enwiki@localhost`

## Wikipedia Miner installation (optional) ##

[Wikipedia Miner](http://www.wikipedia-miner.sourceforge.net) is required for topic indexing with Wikipedia. It is also used for computing encyclopedic features in other tasks, although this is optional.

  1. [Download the package and the data](https://sourceforge.net/projects/wikipedia-miner/files/).
  1. Follow the installation guide in the readme file distributed with the package.

Once installed, Maui will require the location of the server (e.g. `localhost`), the name of the database containing Wikipedia data (e.g. `en_20090306`), and, optionally, the name of the directory with cvs files containing Wikipedia data, which Wikipedia Miner loads into memory for quick access. The latter is advisable if many documents are processed at a time. It requires approximately 3MB RAM.
