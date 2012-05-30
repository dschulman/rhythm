package rhythm;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.concat;

import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Rhythm {
	private final Tokenizer tokenizer;
	private final ImmutableList<Processor> analysis;
	private final ImmutableList<Processor> generation;
	private final ImmutableList<Processor> filtering;
	private final Compiler output;
	
	public Rhythm(
		Tokenizer tokenizer,
		Iterable<Processor> analysis,
		Iterable<Processor> generation,
		Iterable<Processor> filtering,
		Compiler output) {
		this.tokenizer = tokenizer;
		this.analysis = copyOf(analysis);
		this.generation = copyOf(generation);
		this.filtering = copyOf(filtering);
		this.output = output;
	}
	
	public static ImmutableList<Processor> stdAnalysis(Configuration conf) throws IOException {
		return ImmutableList.of(
			new Tag(conf),
			new TagFeatureExtract(),
			new Lemmatize(conf),
			new MarkNew(),
			new PhraseChunk(conf),
			new ClauseChunk(),
			new ThemeRhemeChunk(),
			new MarkMarkers(conf),
			new MarkTopicShift());
	}
	
	public static ImmutableList<Processor> basicGeneration() {
		return ImmutableList.of(
			new IntonationGenerator(),
			new BeatGenerator(),
			new BrowsGenerator(),
			//new HeadnodGenerator(),
			new HeadnodAckGenerator(),
			new PostureMonologueGenerator(),
			new GazeGenerator(),
			new FaceGenerator());
	}
	
	public static ImmutableList<Processor> longitudinalGeneration() {
		return ImmutableList.of(
			new IntonationGenerator(),
			new BeatGenerator(),
			new BrowsGenerator(),
			//new HeadnodGenerator(),
			new HeadnodAckLongitudinal(),
			new PostureLongitudinal.Monologue(),
			new GazeLongitudinal(),
			new FaceLongitudinal(),
			new ArticulationRateLongitudinal());
	}
	
	public static ImmutableList<Processor> stdFiltering() {
		return ImmutableList.of(
			new UpdateContext(),
			new ProbabilityFilter(),
			new ConflictFilter());
	}
	
	public static Rhythm createStd(Configuration conf, Iterable<Processor> generation) throws IOException {
		return new Rhythm(
			new Tokenizer(conf), 
			stdAnalysis(conf), 
			generation, 
			stdFiltering(),
			new BeatXmlCompiler());
	}
	
	public Rhythm setTokenizer(Tokenizer tokenizer) {
		return new Rhythm(tokenizer, analysis, generation, filtering, output);
	}
	
	public Rhythm setAnalysis(Iterable<Processor> analysis) {
		return new Rhythm(tokenizer, analysis, generation, filtering, output);
	}
	
	public Rhythm setGeneration(Iterable<Processor> generation) {
		return new Rhythm(tokenizer, analysis, generation, filtering, output);
	}
	
	public Rhythm setFiltering(Iterable<Processor> filtering) {
		return new Rhythm(tokenizer, analysis, generation, filtering, output);
	}
	
	public Rhythm setOutput(Compiler output) {
		return new Rhythm(tokenizer, analysis, generation, filtering, output);
	}
	
	public Iterable<String> process(Context c, String input) {
		Iterable<Sentence> ss = tokenizer.process(input);
		for (Sentence s : ss)
			for (Processor p : concat(analysis, generation, filtering))
				p.process(c, s);
		return Iterables.transform(ss, output);
	}
	
	private static final Joiner JoinSentences = 
		Joiner.on(' ').skipNulls();
	
	public String processAll(Context c, String input) {
		return JoinSentences.join(process(c, input));
	}
}
