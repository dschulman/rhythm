package rhythm;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.collect.Lists;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ToSentences {
	private final SentenceDetectorME sentDetector;
	private final TokenizerME tokenizer;
	
	public ToSentences(Configuration conf) throws IOException {
		InputStream in = conf.openModelStream("sentdetect", "en-sent.bin");
		try {
			sentDetector = new SentenceDetectorME(new SentenceModel(in));
 		} finally {
 			in.close();
 		}
				
		in = conf.openModelStream("tokenize", "en-token.bin");
		try {
			tokenizer = new TokenizerME(new TokenizerModel(in));
		} finally {
			in.close();
		}
	}
	
	public Iterable<Sentence> process(String input) {
		String[] rawSs = sentDetector.sentDetect(input);
		List<Sentence> ss = Lists.newArrayListWithCapacity(rawSs.length);
		for (String raw : rawSs)
			ss.add(new Sentence(tokenizer.tokenize(raw)));
		if (!ss.isEmpty()) {
			// TODO one input may not be one turn; allow user annotations
			ss.get(0).put(Features.TURN_START);
			ss.get(ss.size()-1).put(Features.TURN_END);
		}
		return ss;
	}
}
