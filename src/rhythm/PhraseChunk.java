package rhythm;

import static rhythm.PhraseType.*;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Span;

public class PhraseChunk implements Processor {
	private final ChunkerME chunker;
	
	public PhraseChunk(Configuration conf) throws IOException {
		InputStream in = conf.openModelStream("chunk", "en-chunker.bin");
		try {
			chunker = new ChunkerME(new ChunkerModel(in));
		} finally {
			in.close();
		}
	}
	
	public void process(Context c, Sentence s) {
		String[] text = s.tokensTextArray();
		String[] tags = s.tokensArray(Features.TAG, String.class);
		Intervals phrases = new Intervals();
		int at = 0;
		for (Span span : chunker.chunkAsSpans(text, tags)) {
			for (; at<span.getStart(); at++)
				addPhrase(phrases, at, at+1, "O");
			addPhrase(phrases, span.getStart(), span.getEnd(), span.getType());
			at = span.getEnd();
		}
		for (; at<s.size(); at++)
			addPhrase(phrases, at, at+1, "O");
		s.set(Features.PHRASES, phrases);
	}
	
	private void addPhrase(Intervals phrases, int low, int high, String tag) {
		Interval phrase = phrases.add(low,  high);
		phrase.set(Features.TAG, tag);
		phrase.maybeSet(Features.PHRASE_TYPE, asPtype(tag));
	}

	private PhraseType asPtype(String s) {
		if (s.startsWith("NP"))
			return NounPhrase;
		else if (s.startsWith("VP"))
			return VerbPhrase;
		else if (s.startsWith("ADVP"))
			return AdverbPhrase;
        else if (s.startsWith("ADJP"))
        	return AdjectivePhrase;
        else if (s.startsWith("PP"))
        	return PrepositionPhrase;
        else if (s.startsWith("SBAR"))
        	return SubordinatingConjunctionPhrase;
        else if (s.startsWith("CONJ"))
        	return CoordinatingConjuctionPhrase;
        else if (s.startsWith("INTJ"))
        	return InterjectionPhrase;
        else if (s.startsWith("O"))
        	return NonPhraseToken;
        else
        	return null;
	}
}
