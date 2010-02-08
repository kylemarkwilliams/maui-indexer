package maui.main;

import gnu.trove.TIntHashSet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.util.ProgressNotifier;
import org.wikipedia.miner.util.text.CaseFolder;
import org.wikipedia.miner.util.text.TextProcessor;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import maui.filters.MauiFilter;
import maui.stemmers.*;
import maui.stopwords.*;
import maui.vocab.Vocabulary;

public class ExperimentsStep2Loo {
	
	Vector<Double> correctStatistics = new Vector<Double>();
	Vector<Double> precisionStatistics = new Vector<Double>();
	Vector<Double> recallStatistics = new Vector<Double>();

	/** Name of directory */
	String inputDirectoryName = null;
	
	
	/** Name of model */
	String modelName = null;

	/** Vocabulary name */
	String vocabularyName = "lcsh";

	/** Format of the vocabulary {skos,text} */
	String vocabularyFormat = "skos";

	/** Document language {en,es,de,fr,...} */
	String documentLanguage = "en";

	/** Document encoding */
	String documentEncoding = "default";

	/** Debugging mode? */
	boolean debugMode = false;

	/** Maximum length of phrases */
	private int maxPhraseLength = 5;

	/** Minimum length of phrases */
	private int minPhraseLength = 1;

	/** Minimum number of occurences of a phrase */
	private int minNumOccur = 1;

	/** Wikipedia object */
	private Wikipedia wikipedia = null;
	
	private String language = "en";

	/** Name of the server with the mysql Wikipedia data */
	private String wikipediaServer = "localhost";

	/** Name of the database with Wikipedia data */
	private String wikipediaDatabase = "enwiki_20090306";

	/** Name of the directory with Wikipedia data in files */
	private String wikipediaDataDirectory = null;

	/** Should Wikipedia data be cached first? */
	private boolean cacheWikipediaData = false;
	
	/** Vocabulary object */
	transient Vocabulary vocabulary;

	/** Use basic features  
	 * TFxIDF & First Occurrence */
	boolean useBasicFeatures = false;

	/** Use keyphraseness feature */
	boolean useKeyphrasenessFeature = false;

	/** Use frequency features
	 * TF & IDF additionally */
	boolean useFrequencyFeatures = false;

	/** Use occurrence position features
	 * LastOccurrence & Spread */
	boolean usePositionsFeatures = false;

	/** Use thesaurus features
	 * Node degree  */
	boolean useNodeDegreeFeature = false;

	/** Use length feature */
	boolean useLengthFeature = false;

	/** Use basic Wikipedia features 
	 *  Wikipedia keyphraseness & Total Wikipedia keyphraseness */
	boolean useBasicWikipediaFeatures = false;

	/** Use all Wikipedia features 
	 * Inverse Wikipedia frequency & Semantic relatedness*/
	boolean useAllWikipediaFeatures = false;

	/** Maui filter object */
	private MauiFilter mauiFilter = null;

	/** Stemmer to be used */
	private Stemmer stemmer = new SremovalStemmer();

	/** Llist of stopwords to be used */
	private Stopwords stopwords = null; //new StopwordsEnglish();

	/** The number of phrases to extract. */
	int topicsPerDocument = 10;

	public Stopwords getStopwords() {
		return stopwords;
	}

	public void setStopwords(Stopwords stopwords) {
		this.stopwords = stopwords;
	}

	public Stemmer getStemmer() {
		return stemmer;
	}

	public void setStemmer(Stemmer stemmer) {
		this.stemmer = stemmer;
	}

	public void setWikipedia(Wikipedia wikipedia) {
		this.wikipedia = wikipedia;
	}

	public String getWikipediaDatabase() {
		return wikipediaDatabase;
	}

	public void setWikipediaDatabase(String wikipediaDatabase) {
		this.wikipediaDatabase = wikipediaDatabase;
	}

	public String getWikipediaServer() {
		return wikipediaServer;
	}

	public void setWikipediaServer(String wikipediaServer) {
		this.wikipediaServer = wikipediaServer;
	}

	public String getWikipediaDataDirectory() {
		return wikipediaDataDirectory;
	}

	public void setWikipediaDataDirectory(String wikipediaDataDirectory) {
		this.wikipediaDataDirectory = wikipediaDataDirectory;
	}

	
	public void loadVocabulary(String vocabularyDirectory, String vocabularyName) {
		if (vocabulary != null)
			return;
		try {
			vocabulary = new Vocabulary(vocabularyName, "skos", vocabularyDirectory);
			vocabulary.setStemmer(stemmer);
			
			if (!vocabularyName.equals("lcsh")) {
				vocabulary.setStopwords(stopwords);
			}
			vocabulary.setLanguage(language);
			vocabulary.initialize();
		} catch (Exception e) {
			System.err.println("Failed to load vocabulary!");
			e.printStackTrace();
		}
	}

	
	public boolean getCachWikipediaData() {
		return cacheWikipediaData;
	}

	public void setCachWikipediaData(boolean cacheWikipediaData) {
		this.cacheWikipediaData = cacheWikipediaData;
	}

	public int getMinNumOccur() {
		return minNumOccur;
	}

	public void setMinNumOccur(int minNumOccur) {
		this.minNumOccur = minNumOccur;
	}

	public int getMaxPhraseLength() {
		return maxPhraseLength;
	}

	public void setMaxPhraseLength(int maxPhraseLength) {
		this.maxPhraseLength = maxPhraseLength;
	}

	public int getMinPhraseLength() {
		return minPhraseLength;
	}

	public void setMinPhraseLength(int minPhraseLength) {
		this.minPhraseLength = minPhraseLength;
	}

	public boolean getDebug() {
		return debugMode;
	}

	public void setDebug(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public String getEncoding() {
		return documentEncoding;
	}

	public void setEncoding(String documentEncoding) {
		this.documentEncoding = documentEncoding;
	}

	public String getVocabularyName() {
		return vocabularyName;
	}

	public void setVocabularyName(String vocabularyName) {
		this.vocabularyName = vocabularyName;
	}

	public String getDocumentLanguage() {
		return documentLanguage;
	}

	public void setDocumentLanguage(String documentLanguage) {
		this.documentLanguage = documentLanguage;
	}

	public String getVocabularyFormat() {
		return vocabularyFormat;
	}

	public void setVocabularyFormat(String vocabularyFormat) {
		this.vocabularyFormat = vocabularyFormat;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getDirName() {
		return inputDirectoryName;
	}

	public void setDirName(String inputDirectoryName) {
		this.inputDirectoryName = inputDirectoryName;
	}

	public void setBasicFeatures(boolean useBasicFeatures) {
		this.useBasicFeatures = useBasicFeatures;
	}

	public void setKeyphrasenessFeature(boolean useKeyphrasenessFeature) {
		this.useKeyphrasenessFeature = useKeyphrasenessFeature;
	}

	public void setFrequencyFeatures(boolean useFrequencyFeatures) {
		this.useFrequencyFeatures = useFrequencyFeatures;
	}

	public void setPositionsFeatures(boolean usePositionsFeatures) {
		this.usePositionsFeatures = usePositionsFeatures;
	}

	public void setNodeDegreeFeature(boolean useNodeDegreeFeature) {
		this.useNodeDegreeFeature = useNodeDegreeFeature;
	}

	public void setLengthFeature(boolean useLengthFeature) {
		this.useLengthFeature = useLengthFeature;
	}

	public void setBasicWikipediaFeatures(boolean useBasicWikipediaFeatures) {
		this.useBasicWikipediaFeatures = useBasicWikipediaFeatures;
	}

	public void setAllWikipediaFeatures(boolean useAllWikipediaFeatures) {
		this.useAllWikipediaFeatures = useAllWikipediaFeatures;
	}

	public void setNumTopics(int topicsPerDocument) {
		this.topicsPerDocument = topicsPerDocument;
	}

	/**
	 * Collects the file names
	 */
	public HashSet<String> collectStems() throws Exception {

		HashSet<String> stems = new HashSet<String>();

		try {
			File dir = new File(inputDirectoryName);

			for (String file : dir.list()) {
				if (file.endsWith(".txt")) {
					String stem = file.substring(0, file.length() - 4);

					File keys = new File(inputDirectoryName + "/" + stem
							+ ".key");
					if (keys.exists()) {
						stems.add(stem);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Problem reading directory "
					+ inputDirectoryName);
		}
		return stems;
	}

	/**
	 * Builds the model from the training data
	 */
	public void buildModel(HashSet<String> fileNames) throws Exception {

		// Check whether there is actually any data
		if (fileNames.size() == 0) {
			throw new Exception("Couldn't find any data in "
					+ inputDirectoryName);
		}

		FastVector atts = new FastVector(3);
		atts.addElement(new Attribute("filename", (FastVector) null));
		atts.addElement(new Attribute("document", (FastVector) null));
		atts.addElement(new Attribute("keyphrases", (FastVector) null));
		Instances data = new Instances("keyphrase_training_data", atts, 0);

		// Build model
		mauiFilter = new MauiFilter();

		mauiFilter.setDebug(getDebug());

		mauiFilter.setInputFormat(data);

	
		// set features configurations
		mauiFilter.setBasicFeatures(useBasicFeatures);
		mauiFilter.setKeyphrasenessFeature(useKeyphrasenessFeature);
		mauiFilter.setFrequencyFeatures(useFrequencyFeatures);
		mauiFilter.setPositionsFeatures(usePositionsFeatures);
		mauiFilter.setLengthFeature(useLengthFeature);
		mauiFilter.setThesaurusFeatures(useNodeDegreeFeature);
		mauiFilter.setBasicWikipediaFeatures(useBasicWikipediaFeatures);
		mauiFilter.setAllWikipediaFeatures(useAllWikipediaFeatures);
	
		// mauiFilter.setVocabularyFormat("skos");
		// mauiFilter.setVocabularyName("lcsh");
		// mauiFilter.loadThesaurus(getStemmer(), getStopwords());
		
		mauiFilter.setVocabulary(vocabulary);
		
		System.err.println("-- Reading the Documents... ");

		for (String fileName : fileNames) {

			double[] newInst = new double[3];

			newInst[0] = (double) data.attribute(0).addStringValue(fileName);

			File documentTextFile = new File(inputDirectoryName + "/"
					+ fileName + ".txt");
			File documentTopicsFile = new File(inputDirectoryName + "/"
					+ fileName + ".key");

			try {

				InputStreamReader is;
				if (!documentEncoding.equals("default")) {
					is = new InputStreamReader(new FileInputStream(
							documentTextFile), documentEncoding);
				} else {
					is = new InputStreamReader(new FileInputStream(
							documentTextFile));
				}

				// Reading the file content
				StringBuffer txtStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					txtStr.append((char) c);
				}
				is.close();

				// Adding the text of the document to the instance
				newInst[1] = (double) data.attribute(1).addStringValue(
						txtStr.toString());

			} catch (Exception e) {

				System.err.println("Problem with reading " + documentTextFile);
				e.printStackTrace();
				newInst[1] = Instance.missingValue();
			}

			try {

				InputStreamReader is;
				if (!documentEncoding.equals("default")) {
					is = new InputStreamReader(new FileInputStream(
							documentTopicsFile), documentEncoding);
				} else {
					is = new InputStreamReader(new FileInputStream(
							documentTopicsFile));
				}

				// Reading the content of the keyphrase file
				StringBuffer keyStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					keyStr.append((char) c);
				}

				// Adding the topics to the file
				newInst[2] = (double) data.attribute(2).addStringValue(
						keyStr.toString());

			} catch (Exception e) {

				System.err
						.println("Problem with reading " + documentTopicsFile);
				e.printStackTrace();
				newInst[2] = Instance.missingValue();
			}

			data.add(new Instance(1.0, newInst));

			mauiFilter.input(data.instance(0));
			data = data.stringFreeStructure();
		}
		mauiFilter.batchFinished();

		while ((mauiFilter.output()) != null) {
		}
		;
	}

	private HashMap<String,Vector<String>> mauisTopics = new HashMap<String,Vector<String>>();
	
	/**
	 * Builds the model from the files
	 */
	public void extractKeyphrases(HashSet<String> fileNames) throws Exception {

		// Check whether there is actually any data
		if (fileNames.size() == 0) {
			throw new Exception("Couldn't find any data in "
					+ inputDirectoryName);
		}


		// mauiFilter.setTraining(false);

		FastVector atts = new FastVector(3);
		atts.addElement(new Attribute("filename", (FastVector) null));
		atts.addElement(new Attribute("doc", (FastVector) null));
		atts.addElement(new Attribute("keyphrases", (FastVector) null));
		Instances data = new Instances("keyphrase_training_data", atts, 0);

		System.err.println("-- Extracting Keyphrases... ");

		

		
		
		
		for (String fileName : fileNames) {
			
			String keys = "";
			double[] newInst = new double[3];

			newInst[0] = (double) data.attribute(0).addStringValue(fileName);

			File documentTextFile = new File(inputDirectoryName + "/"
					+ fileName + ".txt");
			File documentTopicsFile = new File(inputDirectoryName + "/"
					+ fileName + ".key");

			try {

				InputStreamReader is;
				if (!documentEncoding.equals("default")) {
					is = new InputStreamReader(new FileInputStream(
							documentTextFile), documentEncoding);
				} else {
					is = new InputStreamReader(new FileInputStream(
							documentTextFile));
				}

				// Reading the file content
				StringBuffer txtStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					txtStr.append((char) c);
				}
				is.close();

				// Adding the text of the document to the instance
				newInst[1] = (double) data.attribute(1).addStringValue(
						txtStr.toString());

			} catch (Exception e) {
				System.err.println("Problem with reading " + documentTextFile);
				e.printStackTrace();
				newInst[1] = Instance.missingValue();
			}

			try {

				InputStreamReader is;
				if (!documentEncoding.equals("default")) {
					is = new InputStreamReader(new FileInputStream(
							documentTopicsFile), documentEncoding);
				} else {
					is = new InputStreamReader(new FileInputStream(
							documentTopicsFile));
				}

				// Reading the content of the keyphrase file
				StringBuffer keyStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					keyStr.append((char) c);
				}
				
			keys = keyStr.toString();
				
				// Adding the topics to the file
				newInst[2] = (double) data.attribute(2).addStringValue(keys);

			} catch (Exception e) {
				if (debugMode) {
					System.err.println("No existing topics for "
							+ documentTextFile);
				}
				newInst[2] = Instance.missingValue();
			}

			data.add(new Instance(1.0, newInst));

			mauiFilter.input(data.instance(0));

			data = data.stringFreeStructure();
			if (debugMode) {
				System.err.println("-- Processing document: " + fileName);
			}
			Instance[] topRankedInstances = new Instance[topicsPerDocument + 5];
			Instance inst;

			// Iterating over all extracted keyphrases (inst)
			while ((inst = mauiFilter.output()) != null) {

				int index = (int) inst.value(mauiFilter.getRankIndex()) - 1;

				if (index < topicsPerDocument + 5) {
					topRankedInstances[index] = inst;
				}
			}

			if (debugMode) {
				System.err.println("-- Keyphrases and feature values:");
			}
			FileOutputStream out = null;
			PrintWriter printer = null;

			String outputFile = documentTopicsFile.getAbsolutePath().replace(".key",".key1");
			File documentTopicsOutFile = new File(outputFile);
			if (!documentTopicsOutFile.exists()) {
				out = new FileOutputStream(documentTopicsOutFile);
				if (!documentEncoding.equals("default")) {
					printer = new PrintWriter(new OutputStreamWriter(out,
							documentEncoding));
				} else {
					printer = new PrintWriter(out);
				}
			}
			
			double numExtracted = 0, numCorrect = 0;
			Vector<String> topics = new Vector<String>();
			
			HashSet<String> done = new HashSet<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in) );
		   
			int i = 0;
			while (i < topicsPerDocument && topics.size() <= topicsPerDocument) {

				if (topRankedInstances[i] != null) {
					String topic = topRankedInstances[i].
					stringValue(mauiFilter.getNormalizedFormIndex());
					topic = topic.toLowerCase();
					System.out.println("Looking at " + topic);
					int j = topic.indexOf('(');
					if (j != -1) {
						topic = topic.substring(0, j-1);
						System.out.println("has bracket and became  >" + topic + "<");
					}
						if (!topics.contains(topic)) {
							System.out.println("Added " + topic);
							topics.add(topic);
						
						} else {
							System.out.println("ignored " + topic);
						}
						br.readLine();
										i++;
							
					if (!topRankedInstances[i].isMissing(topRankedInstances[i]
							.numAttributes() - 1)) {
						numExtracted += 1.0;
					}
					if ((int) topRankedInstances[i].value(topRankedInstances[i]
							.numAttributes() - 1) == 1) {
						numCorrect += 1.0;
					}
					if (printer != null) {
						printer.print(topRankedInstances[i]
								.stringValue(mauiFilter.getOutputFormIndex()));

						printer.println();
					}
					if (debugMode) {
						System.err.println(topRankedInstances[i]);
					}
				}
			}
			
			mauisTopics.put(fileName+".key", topics);
			
			if (numExtracted > 0) {
				double totalCorrect = (double)keys.split("\n").length;

				if (debugMode) {
					System.err.println("-- " + numCorrect + " correct; "
							+ numExtracted + "; extracted " + totalCorrect
							+ " total.");
				}

				correctStatistics.addElement(new Double(numCorrect));
				double precision = (double) numCorrect / numExtracted;
				precisionStatistics.addElement(new Double(precision));
				double recall = (double) numCorrect / totalCorrect;
				recallStatistics.addElement(new Double(recall));

			}
			if (printer != null) {
				printer.flush();
				printer.close();
				out.close();
			}
		}

	
		//	mauiFilter.batchFinished();
	}
	
	
	



	private void loadWikipedia(boolean cacheData) throws Exception {

		wikipedia = new Wikipedia("localhost", "enwiki_20090306", "root", null);

		TextProcessor textProcessor = new CaseFolder();

		File dataDirectory = new File(
				"/Users/alyona/Data/wikipedia/data/20090306");

		if (cacheData) {
			ProgressNotifier progress = new ProgressNotifier(5);
			// cache tables that will be used extensively
			TIntHashSet validPageIds = wikipedia.getDatabase().getValidPageIds(
					dataDirectory, 2, progress);
			wikipedia.getDatabase().cachePages(dataDirectory, validPageIds,
					progress);
			wikipedia.getDatabase().cacheAnchors(dataDirectory, textProcessor,
					validPageIds, 2, progress);
			wikipedia.getDatabase().cacheInLinks(dataDirectory, validPageIds,
					progress);
			wikipedia.getDatabase().cacheGenerality(dataDirectory,
					validPageIds, progress);
		}
	}
	
	
	private void printEvaluationResults() {

		if (correctStatistics.size() != 0) {

			double[] st = new double[correctStatistics.size()];
			for (int i = 0; i < correctStatistics.size(); i++) {
				st[i] = correctStatistics.elementAt(i).doubleValue();
			}
			double avg = Utils.mean(st);
			double stdDev = Math.sqrt(Utils.variance(st));

			if (correctStatistics.size() == 1) {
				System.err
						.println("\n-- Evaluation results based on 1 document:");

			} else {
				System.err.println("\n-- Evaluation results based on "
						+ correctStatistics.size() + " documents:");
			}
			System.err
					.println("Avg. number of correct keyphrases per document: "
							+ Utils.doubleToString(avg, 2) + " +/- "
							+ Utils.doubleToString(stdDev, 2));

			st = new double[precisionStatistics.size()];
			for (int i = 0; i < precisionStatistics.size(); i++) {
				st[i] = precisionStatistics.elementAt(i).doubleValue();
			}
			double avgPrecision = Utils.mean(st);
			double stdDevPrecision = Math.sqrt(Utils.variance(st));

			System.err.println("Precision: "
					+ Utils.doubleToString(avgPrecision*100, 2) + " +/- "
					+ Utils.doubleToString(stdDevPrecision, 2));

			st = new double[recallStatistics.size()];
			for (int i = 0; i < recallStatistics.size(); i++) {
				st[i] = recallStatistics.elementAt(i).doubleValue();
			}
			double avgRecall = Utils.mean(st);
			double stdDevRecall = Math.sqrt(Utils.variance(st));

			System.err.println("Recall: " + Utils.doubleToString(avgRecall*100, 2)
					+ " +/- " + Utils.doubleToString(stdDevRecall, 2));

			double fMeasure = 2 * avgRecall * avgPrecision
					/ (avgRecall + avgPrecision);
			System.err.println("F-Measure: "
					+ Utils.doubleToString(fMeasure*100, 2));

			System.err.println("");
		}		
	}

	
	/**
	 * The main method.  
	 * @throws Exception 
	 */
	public static void main(String[] ops) throws Exception {

		ExperimentsStep2Loo modelBuilder = new ExperimentsStep2Loo();

		
	
		// general settings
		modelBuilder.setModelName("test");
		modelBuilder
			//	 .setDirName("/Users/alyona/Documents/corpora/term_assignment/FAO_30/documents3");
		 .setDirName("/Users/alyona/Data/waikato_thesis/all");

		modelBuilder.setDebug(true);
	
		modelBuilder.setNumTopics(10);

		// wikipedia-specific features
		modelBuilder.setVocabularyName("lcsh");
		modelBuilder.setVocabularyFormat("skos");
		
		String dataDirectory = "../Maui1.2/";

		String vocabularyDirectory = dataDirectory +  "data/vocabularies/";
		String modelDirectory = dataDirectory +  "data/models";
		modelBuilder.loadVocabulary(vocabularyDirectory, "lcsh");
		
		// features
		modelBuilder.setBasicFeatures(true);
		modelBuilder.setKeyphrasenessFeature(true);
		modelBuilder.setFrequencyFeatures(true);
		modelBuilder.setPositionsFeatures(true);
		modelBuilder.setLengthFeature(true);
		modelBuilder.setNodeDegreeFeature(true);
		modelBuilder.setBasicWikipediaFeatures(true);
		modelBuilder.setAllWikipediaFeatures(true);

		modelBuilder.setEncoding("default");
		
	
		
		HashSet<String> testing;
		HashSet<String> training;

		HashSet<String> fileNames = modelBuilder.collectStems();
		int i = 0;
		for (String fileTest : fileNames) {

			// in leave one out, each file will be used for testing, while all the others for training!
			testing = new HashSet<String>(1);
			training = new HashSet<String>(fileNames.size() - 1);

			testing.add(fileTest);

			for (String fileTrain : fileNames) {
				if (!fileTrain.equals(fileTest)) {
					training.add(fileTrain);

				}
			}

			if (modelBuilder.getDebug() == true) {
				System.err.println(" Training run " + i + " on " + training.size() + " documents");
			}
			modelBuilder.buildModel(training);
			
			if (modelBuilder.getDebug() == true) {
				System.err.println(" Test run " + i + " on " + testing.size() + " documents");
			}
			
			modelBuilder.extractKeyphrases(testing);
			
			
			i++;
		
			
		}
	//	modelBuilder.computeConsistency();
		modelBuilder.printEvaluationResults();
	}




}
