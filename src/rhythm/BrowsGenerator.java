package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;
import static rhythm.Information.Rheme;

public class BrowsGenerator implements Processor {
	public void process(Context c, Sentence s) {
		for (Interval info : s.get(INFORMATION_STRUCTURE))
			if (info.has(INFORMATION, Rheme))
				for (Interval p : s.get(Features.PHRASES).in(info))
					if (p.get(Features.PHRASE_TYPE)==PhraseType.NounPhrase)
						if (any(s.tokensIn(p), is_(NEW)))
							s.add(new Behavior("brows", p.low(), p.high()));
	}
}
