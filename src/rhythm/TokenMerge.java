package rhythm;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.DetokenizationDictionary;
import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.tokenize.Detokenizer.DetokenizationOperation;
import opennlp.tools.tokenize.DictionaryDetokenizer;

public class TokenMerge implements Processor {
	private final Detokenizer detokenizer;
	
	public TokenMerge(Configuration conf) throws IOException {
		InputStream in = conf.openModelStream("detoken", "en-detokenizer.xml");
		try {
			detokenizer = new DictionaryDetokenizer(new DetokenizationDictionary(in));
		} finally {
			in.close();
		}
	}
	
	public void process(Context c, Sentence s) {
		DetokenizationOperation[] ops = detokenizer.detokenize(s.tokensTextArray());
		for (int n=0; n<ops.length; n++)
			switch (ops[n]) {
			case MERGE_TO_LEFT:
				if (n > 0) {
					s.token(n-1).set(Features.MERGE_RIGHT);
					s.token(n).set(Features.MERGE_LEFT);
				}
				break;
			case MERGE_TO_RIGHT:
				if (n < (s.size()-1)) {
					s.token(n).set(Features.MERGE_RIGHT);
					s.token(n).set(Features.MERGE_LEFT);
				};
				break;
			}
	}
}
