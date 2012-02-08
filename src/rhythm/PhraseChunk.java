package rhythm;

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
		for (Span span : chunker.chunkAsSpans(text, tags)) {
			Interval phrase = phrases.add(span.getStart(), span.getEnd());
			phrase.set(Features.TAG, span.getType());
			phrase.maybeSet(Features.PHRASE_TYPE, asPtype(span.getType()));
		}
		s.set(Features.PHRASES, phrases);
	}

	private PhraseType asPtype(String s) {
		if (s.startsWith("NP"))
			return PhraseType.NounPhrase;
		else if (s.startsWith("VP"))
			return PhraseType.VerbPhrase;
		else if (s.startsWith("ADVP"))
			return PhraseType.AdverbPhrase;
        else if (s.startsWith("ADJP"))
        	return PhraseType.AdjectivePhrase;
        else if (s.startsWith("PP"))
        	return PhraseType.PrepositionPhrase;
        else if (s.startsWith("O"))    
        	return PhraseType.CoordinatingConjuctionPhrase;
        else
        	return null;
	}
}
