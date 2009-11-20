package maui.vocab;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.openrdf.concepts.skos.core.Concept;
import org.openrdf.elmo.sesame.SesameManager;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import maui.stemmers.Stemmer;
import maui.stopwords.Stopwords;
import maui.vocab.store.VocabularyStore;

public class VocabularySesame implements Vocabulary, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Location of the rdf version of the controlled vocabulary it needs to be
	 * in the SKOS format!
	 */
	private static File SKOS;
	/**
	 * Location of the vocabulary's *.en file containing all terms of the
	 * vocabularies and their ids.
	 */
	private static File EN;
	/**
	 * Location of the vocabulary's *.use file containing ids of non-descriptor
	 * with the corresponding ids of descriptors.
	 */
	private static File USE;
	/**
	 * Location of the vocabulary's *.rel file containing semantically related
	 * terms for each descriptor in the vocabulary.
	 */
	private static File REL;

	private String vocabularyFormat;

	/** index : descriptor --> id */
	private HashMap<String, String> termIdIndex;

	/** reverse index : id --> descriptor */
	private HashMap<String, String> idTermIndex;

	/** normalized descriptor --> list of all possible meanings */
	private HashMap<String, Vector<String>> listsOfSenses;

	/** non-descriptor id --> descriptors id */
	private HashMap<String, String> nonDescriptorIndex = null;

	/** id --> list of related ids */
	private HashMap<String, Vector<String>> listsOfRelatedTerms = null;

	/** id-relatedId --> relation */
	private HashMap<String, String> relationIndex = null;

	/** Document language */
	private String language = "en";

	/** Document encoding */
	private String encoding = "UTF-8";

	/** Default stemmer to be used */
	private Stemmer stemmer;

	/** List of stopwords to be used */
	private Stopwords stopwords;

	/** Normalization to lower case - defaulte no */
	private boolean toLowerCase = true;

	/** Normalization via alphabetic reordering - default true */
	private boolean reorder = true;

	private boolean debugMode = false;

	//private SesameManager manager;

	public VocabularySesame() throws Exception {
	}

	/**
	 * Builds the vocabulary indexes from SKOS Sesame Store.
	 */
	private void buildSKOS(SesameManager manager) throws Exception {

		if (debugMode) {
			System.err
					.println("--- Building the Vocabulary index from the SKOS file...");
		}

		termIdIndex = new HashMap<String, String>();
		idTermIndex = new HashMap<String, String>();
		listsOfSenses = new HashMap<String, Vector<String>>();

		nonDescriptorIndex = new HashMap<String, String>();
		listsOfRelatedTerms = new HashMap<String, Vector<String>>();
		relationIndex = new HashMap<String, String>();

		try {

			int count = 1;

			for (Concept concept : manager.findAll(Concept.class)) {

				// id of the concept (Resource), e.g. "c_4828"
				String id = concept.getQName().getNamespaceURI()
						+ concept.getQName().getLocalPart();

				/*
				 * For prefLabels
				 */
				String descriptor = concept.getSkosPrefLabel();

				String descriptorNormalized = normalizePhrase(descriptor);

				if (descriptorNormalized.length() >= 1) {
					Vector<String> ids = listsOfSenses
							.get(descriptorNormalized);
					if (ids == null)
						ids = new Vector<String>();
					ids.add(id);
					listsOfSenses.put(descriptorNormalized, ids);

					termIdIndex.put(descriptor.toLowerCase(), id);
					idTermIndex.put(id, descriptor);
				}

				/*
				 * For altLabels
				 */

				String non_descriptor;
				Set<String> altLabels = concept.getSkosAltLabels();
				altLabels.addAll(concept.getSkosHiddenLabels());
				for (String a : altLabels) {
					non_descriptor = a;
					addNonDescriptor(count, id, non_descriptor);
					count++;
				}

				/*
				 * For any other relations
				 */
				Set<Concept> broaders = concept.getSkosBroaders();
				for (Concept b : broaders) {
					String relatedId = b.getSkosPrefLabel();

					Vector<String> relatedIds = listsOfRelatedTerms.get(id);
					if (relatedIds == null)
						relatedIds = new Vector<String>();

					relatedIds.add(relatedId);

					listsOfRelatedTerms.put(id, relatedIds);

					relationIndex.put(id + "-" + relatedId, "broader");
				}
				Set<Concept> narrowers = concept.getSkosBroaders();
				for (Concept b : narrowers) {
					String relatedId = b.getSkosPrefLabel();

					Vector<String> relatedIds = listsOfRelatedTerms.get(id);
					if (relatedIds == null)
						relatedIds = new Vector<String>();

					relatedIds.add(relatedId);

					listsOfRelatedTerms.put(id, relatedIds);

					relationIndex.put(id + "-" + relatedId, "narrower");
				}

				Set<Concept> related = concept.getSkosBroaders();
				for (Concept b : related) {
					String relatedId = b.getSkosPrefLabel();

					Vector<String> relatedIds = listsOfRelatedTerms.get(id);
					if (relatedIds == null)
						relatedIds = new Vector<String>();

					relatedIds.add(relatedId);

					listsOfRelatedTerms.put(id, relatedIds);

					relationIndex.put(id + "-" + relatedId, "related");
					relationIndex.put(relatedId + "-" + id, "related");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (debugMode) {
			System.err.println("--- Statistics about the vocabulary: ");
			System.err.println("\t" + termIdIndex.size() + " terms in total");
			System.err.println("\t" + nonDescriptorIndex.size()
					+ " non-descriptive terms");
			System.err.println("\t" + listsOfRelatedTerms.size()
					+ " terms have related terms");
		}

	}

	private void addNonDescriptor(int count, String idDescriptor,
			String nonDescriptor) {

		String idNonDescriptor = "d_" + count;
		count++;

		String normalizedNonDescriptor = normalizePhrase(nonDescriptor);
		if (normalizedNonDescriptor.length() >= 1) {
			Vector<String> ids = listsOfSenses.get(normalizedNonDescriptor);
			if (ids == null)
				ids = new Vector<String>();
			ids.add(idNonDescriptor);
			listsOfSenses.put(normalizedNonDescriptor, ids);
		}

		termIdIndex.put(nonDescriptor.toLowerCase(), idNonDescriptor);
		idTermIndex.put(idNonDescriptor, nonDescriptor);

		nonDescriptorIndex.put(idNonDescriptor, idDescriptor);
	}

	@Override
	public boolean containsNormalizedEntry(String phrase) {
		return listsOfSenses.containsKey(normalizePhrase(phrase));
	}

	@Override
	public String getID(String phrase) {
		String id = termIdIndex.get(phrase.toLowerCase());
		if (id != null) {
			if (nonDescriptorIndex.containsKey(id))
				id = nonDescriptorIndex.get(id);
		}
		return id;
	}

	@Override
	public Vector<String> getRelated(String id) {
		return listsOfRelatedTerms.get(id);
	}

	@Override
	public Vector<String> getRelated(String id, String relation) {
		Vector<String> related = new Vector<String>();
		Vector<String> all_related = listsOfRelatedTerms.get(id);
		if (all_related != null) {

			for (String rel_id : all_related) {
				String rel = relationIndex.get(id + "-" + rel_id);

				if (rel != null) {
					if (rel.equals(relation))
						related.add(rel_id);
				}
			}
		}
		return related;
	}

	@Override
	public Vector<String> getSenses(String phrase) {
		String normalized = normalizePhrase(phrase);

		Vector<String> senses = new Vector<String>();
		if (listsOfSenses.containsKey(normalized)) {
			for (String senseId : listsOfSenses.get(normalized)) {
				// 1. retrieve a descriptor if this sense is a non-descriptor
				if (nonDescriptorIndex.containsKey(senseId))
					senseId = nonDescriptorIndex.get(senseId);

				senses.add(senseId);
			}
		}
		return senses;
	}

	@Override
	public String getTerm(String id) {
		return idTermIndex.get(id);
	}

	@Override
	public void initialize(VocabularyStore store) throws Exception {
		buildSKOS(store.getSesameManager());
	}

	@Override
	public boolean isAmbiguous(String phrase) {
		Vector<String> meanings = listsOfSenses.get(normalizePhrase(phrase));
		if (meanings == null || meanings.size() == 1) {
			return false;
		}
		return true;
	}

	@Override
	public String normalizePhrase(String phrase) {

		if (toLowerCase) {
			phrase = phrase.toLowerCase();
		}

		if (toLowerCase) {
			phrase = phrase.toLowerCase();
		}
		StringBuffer result = new StringBuffer();
		char prev = ' ';
		int i = 0;
		while (i < phrase.length()) {
			char c = phrase.charAt(i);

			// we ignore everything after the "/" symbol and everything in
			// brackets
			// e.g. Monocytes/*immunology/microbiology -> monocytes
			// e.g. Vanilla (Spice) -> vanilla
			if (c == '/' || c == '(')
				break;

			if (c == '-' || c == '&' || c == '.' || c == '.')
				c = ' ';

			if (c == '*' || c == ':') {
				prev = c;
				i++;
				continue;
			}

			if (c != ' ' || prev != ' ')
				result.append(c);

			prev = c;
			i++;
		}

		phrase = result.toString().trim();

		if (reorder || stopwords != null || stemmer != null) {
			phrase = pseudoPhrase(phrase);
		}
		if (phrase.equals("")) {
			// to prevent cases where the term is a stop word (e.g. Back).
			return result.toString();
		} else {
			return phrase;
		}
	}

	@Override
	public String pseudoPhrase(String str) {
		String result = "";
		String[] words = str.split(" ");
		if (reorder) {
			Arrays.sort(words);
		}
		for (String word : words) {

			if (stopwords != null) {
				if (stopwords.isStopword(word)) {
					continue;
				}
			}

			int apostr = word.indexOf('\'');
			if (apostr != -1) {
				word = word.substring(0, apostr);
			}

			if (stemmer != null) {
				word = stemmer.stem(word);
			}
			result += word + " ";
		}
		return result.trim();
	}

	@Override
	public void setDebug(boolean debugMode) {
		this.debugMode = debugMode;

	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;

	}

	@Override
	public void setLanguage(String language) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLowerCase(boolean toLowerCase) {
		this.toLowerCase = toLowerCase;

	}

	@Override
	public void setReorder(boolean reorder) {
		this.reorder = reorder;
	}

	@Override
	public void setStemmer(Stemmer stemmer) {
		this.stemmer = stemmer;

	}

	@Override
	public void setStopwords(Stopwords stopwords) {
		this.stopwords = stopwords;

	}

}
