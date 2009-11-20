package maui.vocab;

import maui.vocab.store.VocabularyStore;

public class VocabularyFactory {
	
	public static final int JENA = 1;
	public static final int SESAME = 2;
	public static final int TEXT = 3;

	private static int vocabulary = 1;

	public static void selectVocabulary(int vocabulary) {
		VocabularyFactory.vocabulary = vocabulary;
	}

	public static Vocabulary getVocabulary() {
		if (vocabulary == JENA)
			try {
				return new VocabularyJena();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(vocabulary == SESAME) {
			try {
				return new VocabularySesame();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(vocabulary == TEXT) {
			try {
				return new VocabularyText();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			return null;
		return null;

	}

}
