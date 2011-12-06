package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.is_;

public class BrowsGenerator implements Processor {
	public void process(Sentence s) {
		for (Interval rheme : s.get(Features.RHEMES))
			for (Interval p : s.get(Features.PHRASES).in(rheme))
				if (p.get(Features.PHRASE_TYPE)==PhraseType.NounPhrase)
					if (any(s.tokensIn(p), is_(Features.NEW)))
						s.add(new Behavior("brows", p.low(), p.high()));
	}
}
