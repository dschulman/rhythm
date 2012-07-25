package rhythm;

public class TagFeatureExtract implements Processor {
	public void process(Context c, Sentence s) {
		for (Token t : s.tokens()) {
			String tag = t.get(Features.TAG);
			t.maybeSet(Features.CLASS, asWordClass(tag));
			t.maybeSet(Features.NUMBER, asWordNumber(tag));
			t.maybeSet(Features.PERSON, asWordPerson(tag));
		}
	}
	
	private WordClass asWordClass(String tag) {
		if (isNoun(tag))
			return WordClass.Noun;
		else if (tag.startsWith("JJ"))
			return WordClass.Adjective;
		else if (tag.startsWith("WP"))
			return WordClass.Pronoun;
		else if (tag.startsWith("PRP"))
			return WordClass.PersonalPronoun;
		else if (tag.startsWith("VB"))
			return WordClass.Verb;
		else if (tag.startsWith("RB"))
			return WordClass.Adverb;
		else if (tag.startsWith("CD"))
			return WordClass.Numeral;
		else if (tag.startsWith("IN"))
			return WordClass.Preposition;
		else if (tag.startsWith("DT"))
			return WordClass.Determiner;
		else if (tag.startsWith("CC"))
			return WordClass.CoordinatingConjunction;
		else if ((tag.length()==1) && !Character.isLetterOrDigit(tag.charAt(0)))
			return WordClass.Punctuation;
		else
			return null;
	}
		
	private WordNumber asWordNumber(String tag) {
		if (isNoun(tag))
			return tag.endsWith("S") ? WordNumber.Plural : WordNumber.Singular;
		else
			return null;
	}

	private boolean isNoun(String tag) { 
		return tag.startsWith("NN") || tag.startsWith("NP");
	}
		
	private WordPerson asWordPerson(String tag) {
		if ("VBZ".equals(tag))
			return WordPerson.ThirdPerson;
		else
			return null;
	}
}
