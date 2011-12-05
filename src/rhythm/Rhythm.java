package rhythm;

import java.io.IOException;

import com.google.common.collect.ImmutableList;

public class Rhythm {
	private final ToSentences toSentences;
	private final ImmutableList<Processor> procs;
	
	public Rhythm(Configuration conf) throws IOException {
		this.toSentences = new ToSentences(conf);
		this.procs = ImmutableList.<Processor>of(
			new Tag(conf),
			new TagFeatureExtract(),
			new Lemmatize(conf),
			new MarkNew(),
			new PhraseChunk(conf),
			new ClauseChunk(),
			new ThemeRhemeChunk(),
			new BeatGenerator());
	}
	
	public Iterable<Sentence> process(String input) {
		Iterable<Sentence> ss = toSentences.process(input);
		for (Sentence s : ss)
			for (Processor p : procs)
				p.process(s);
		return ss;
	}
}
