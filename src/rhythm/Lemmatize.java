package rhythm;

import java.io.IOException;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class Lemmatize extends Processor {
	private final IDictionary wordnet;
	private final WordnetStemmer stemmer;
	
	public Lemmatize(Configuration conf) throws IOException {
		wordnet = new Dictionary(conf.modelPath("wordnet", "wordnet"));
		if (!wordnet.open())
			throw new IOException();
		stemmer = new WordnetStemmer(wordnet); 
	}
	
	public void process(Sentence s) {
		for (Token t : s.tokens()) {
			List<String> lemmas = stemmer.findStems(t.text(), getPos(t));
			if (!lemmas.isEmpty())
				t.put(Features.LEMMA, lemmas.get(0));
		}
	}
	
	private POS getPos(Token t) {
		WordClass wc = t.get(Features.CLASS);
		if (wc == null) return null;
		switch (wc) {
		case Noun: return POS.NOUN;
		case Verb: return POS.VERB;
		case Adjective: return POS.ADJECTIVE;
		case Adverb: return POS.ADVERB;
		default: return null;
		}
	}
}
