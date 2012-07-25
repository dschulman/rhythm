package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.CLASS;
import static rhythm.Features.has_;
import static rhythm.WordClass.Punctuation;
import static rhythm.WordClass.Verb;

public class ClauseChunk implements Processor {
	public void process(Context c, Sentence s) {
		Intervals clauses = new Intervals();
		int low = Integer.MAX_VALUE;
		int high = Integer.MIN_VALUE;
		boolean hasVerb = false;
		for (Interval phrase : s.get(Features.PHRASES)) {
			low = Math.min(phrase.low(), low);
			high = Math.max(phrase.high(), high);
			hasVerb = hasVerb || 
				any(s.tokensIn(phrase), has_(CLASS, Verb));
			if (hasVerb && (s.size() > high)) {
				Token next = s.token(high);
				if (next.has(CLASS, Punctuation)) {
					clauses.add(low, high);
					low = Integer.MAX_VALUE;
					high = Integer.MIN_VALUE;
					hasVerb = false;
				}
			}
		}
		if (low < high) {
			// bit of a hack: ignore punctuation at end
			if ((low < (high-1)) || !s.token(low).has(CLASS, Punctuation))
				clauses.add(low, high);
		}
		s.set(Features.CLAUSES, clauses);
	}
}
