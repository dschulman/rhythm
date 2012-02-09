package rhythm;

import java.io.IOException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Rhythm {
	private final ToSentences toSentences;
	private final ImmutableList<Processor> procs;
	private Compiler output;
	
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
			new MarkMarkers(conf),
			new MarkTopicShift(),
			new IntonationGenerator(),
			new BeatGenerator(),
			new BrowsGenerator(),
			new HeadnodGenerator(),
			new HeadnodAckGenerator(),
			new PostureMonologueGenerator(),
			new GazeGenerator(),
			new UpdateContext());
		this.output = new BeatXmlCompiler();
	}
	
	public Iterable<String> process(Context c, String input) {
		Iterable<Sentence> ss = toSentences.process(input);
		for (Sentence s : ss)
			for (Processor p : procs)
				p.process(c, s);
		return Iterables.transform(ss, output);
	}
	
	public void setOutput(Compiler output) {
		this.output = output;
	}
}
