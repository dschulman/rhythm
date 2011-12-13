package rhythm;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Tag extends Processor {
	private final POSTaggerME tagger;
	
	public Tag(Configuration c) throws IOException {
		InputStream in = c.openModelStream("tag", "en-pos-maxent.bin");
		try {
			tagger = new POSTaggerME(new POSModel(in));
		} finally {
			in.close();
		}
	}
	
	@Override
	public void process(Sentence s) {
		String[] tags = tagger.tag(s.tokensTextArray());
		int n = 0;
		for (Token t : s.tokens())
			t.put(Features.TAG, tags[n++]);
	}

}
