package maui.main;

/*
 *    MauiModelBuilder.java
 *    Copyright (C) 2009 Olena Medelyan
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
import gnu.trove.TIntHashSet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.util.ProgressNotifier;
import org.wikipedia.miner.util.text.CaseFolder;
import org.wikipedia.miner.util.text.TextProcessor;

import maui.stemmers.PorterStemmer;
import maui.stopwords.StopwordsEnglish;

/**
 * Demonstrates how to use Maui for three types of topic extraction 
 * 1. Keyphrase extraction - extracting significant phrases from 
 *  the document, also suitable for automatic tagging. 
 * 2. Term assignment - indexing documents with terms
 * from a controlled vocabulary in SKOS or text format. 
 * 3. Indexing with Wikipedia - indexing documents with 
 * terms from Wikipedia, also suitable for
 * keyphrase extraction and tagging, or any case where there is no controlled
 * vocabulary available, but consistency is required.
 * 
 * @author Olena Medelyan (olena@cs.waikato.ac.nz)
 * 
 */
public class Examples {

	private MauiTopicExtractor topicExtractor;
	private MauiModelBuilder modelBuilder;

	private Wikipedia wikipedia;


	public Examples (boolean cacheData) throws Exception {
		loadWikipedia(cacheData);
	}
	
	/**
	 * Sets general parameters: debugging printout, language specific options
	 * like stemmer, stopwords.
	 * @throws Exception 
	 */
	private void setGeneralOptions()  {
		modelBuilder.setDebug(true);
		modelBuilder.setStemmer(new PorterStemmer());
		modelBuilder.setStopwords(new StopwordsEnglish());
		modelBuilder.setDocumentLanguage("en");
		modelBuilder.setMaxPhraseLength(5);
		modelBuilder.setWikipedia(wikipedia);
		
		topicExtractor.setDebug(true);
		topicExtractor.setStemmer(new PorterStemmer());
		topicExtractor.setStopwords(new StopwordsEnglish());
		topicExtractor.setDocumentLanguage("en");
		topicExtractor.setNumTopics(10);
		topicExtractor.setWikipedia(wikipedia);
	}

	/**
	 * Set true for all features that will be used
	 */
	private void setFeatures() {
		modelBuilder.setBasicFeatures(true);
		modelBuilder.setKeyphrasenessFeature(true);
		modelBuilder.setFrequencyFeatures(true);
		modelBuilder.setPositionsFeatures(true);
		modelBuilder.setLengthFeature(true);
		modelBuilder.setNodeDegreeFeature(true);
		modelBuilder.setBasicWikipediaFeatures(true);
		modelBuilder.setAllWikipediaFeatures(true);
	}

	/**
	 * Demonstrates how to perform automatic tagging. Also applicable to
	 * keyphrase extraction.
	 * 
	 * @throws Exception
	 */
	public void testAutomaticTagging() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();
		
		// Directories with train & test data
		String trainDir = "data/automatic_tagging/train";
		String testDir = "data/automatic_tagging/test";

		// name of the file to save the model
		String modelName = "test";

		// Settings for the model builder
		modelBuilder.setDirName(trainDir);
		modelBuilder.setModelName(modelName);
		
		
		// change to 1 for short documents
		modelBuilder.setMinNumOccur(3);

		// Run model builder
		HashSet<String> fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		// Settings for topic extractor
		topicExtractor.setDirName(testDir);
		topicExtractor.setModelName(modelName);
	
		
		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}

	/**
	 * Demonstrates how to perform term assignment. Applicable to any vocabulary
	 * in SKOS or text format.
	 * 
	 * @throws Exception
	 */
	public void testTermAssignment() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();
		
		// Directories with train & test data
		String trainDir = "data/term_assignment/train";
		String testDir = "data/term_assignment/test";

		// Vocabulary
		String vocabulary = "agrovoc";
		String format = "skos";

		// name of the file to save the model
		String modelName = "test";
		HashSet<String> fileNames;

		// Settings for the model builder
		modelBuilder.setDirName(trainDir);
		modelBuilder.setModelName(modelName);
		modelBuilder.setVocabularyFormat(format);
		modelBuilder.setVocabularyName(vocabulary);
		
		// Run model builder
		fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		// Settings for topic extractor
		topicExtractor.setDirName(testDir);
		topicExtractor.setModelName(modelName);
		topicExtractor.setVocabularyName(vocabulary);
		topicExtractor.setVocabularyFormat(format);
		
		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}

	/**
	 * Demonstrates how to perform topic indexing
	 * with Wikipedia.
	 * 
	 * @throws Exception
	 */
	public void testIndexingWithWikipedia() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		// Directories with train & test data
		String trainDir = "data/wikipedia_indexing/train";
		String testDir = "data/wikipedia_indexing/test";

		// Vocabulary
		String vocabulary = "wikipedia";
	
		// name of the file to save the model
		String modelName = "test";
		HashSet<String> fileNames;

		// Settings for the model builder
		modelBuilder.setDirName(trainDir);
		modelBuilder.setModelName(modelName);
		modelBuilder.setVocabularyName(vocabulary);
		
		// Run model builder
		fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		// Settings for topic extractor
		topicExtractor.setDirName(testDir);
		topicExtractor.setModelName(modelName);
		topicExtractor.setVocabularyName(vocabulary);
		
		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
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
		}
	}

	/**
	 * Main method for running the three types of topic indexing. Comment out
	 * the required one.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Date todaysDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE, dd-MMM-yyyy HH:mm:ss");
		String formattedDate1 = formatter.format(todaysDate);

		Examples tester = new Examples(true);

		//tester.testAutomaticTagging();
		//tester.testTermAssignment();
		tester.testIndexingWithWikipedia();

		todaysDate = new java.util.Date();
		String formattedDate2 = formatter.format(todaysDate);
		System.err.print("Run from " + formattedDate1);
		System.err.println(" to " + formattedDate2);
	}

}
