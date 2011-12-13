package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.has_;

public class ClauseChunk extends Processor {
	public void process(Sentence s) {
		Intervals clauses = new Intervals();
		int low = Integer.MAX_VALUE;
		int high = Integer.MIN_VALUE;
		boolean hasVerb = false;
		for (Interval phrase : s.get(Features.PHRASES)) {
			low = Math.min(phrase.low(), low);
			high = Math.max(phrase.high(), high);
			hasVerb = hasVerb || 
				any(s.tokensIn(phrase), has_(Features.CLASS, WordClass.Verb));
			if (hasVerb && (s.size() > high)) {
				Token next = s.tokens().get(high);
				if (next.get(Features.CLASS) == WordClass.Punctuation) {
					clauses.add(low, high);
					low = Integer.MAX_VALUE;
					high = Integer.MIN_VALUE;
					hasVerb = false;
				}
			}
		}
		if (low < high)
			clauses.add(low, high);
		s.put(Features.CLAUSES, clauses);
	}
}
