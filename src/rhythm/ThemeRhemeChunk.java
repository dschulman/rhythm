package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.is_;

public class ThemeRhemeChunk extends Processor {
	public void process(Sentence s) {
		Intervals themes = new Intervals();
		Intervals rhemes = new Intervals();
		for (Interval clause : s.get(Features.CLAUSES))
			split(s, clause, themes, rhemes);
		s.put(Features.THEMES, themes);
		s.put(Features.RHEMES, rhemes);
	}

	private void split(Sentence s, Interval clause, Intervals themes, Intervals rhemes) {
		Interval pre = null, verb = null, post = null;
		for (Interval phrase : s.get(Features.PHRASES).in(clause)) {
			if (verb==null) {
				if (phrase.get(Features.PHRASE_TYPE)==PhraseType.VerbPhrase)
					verb = new Interval(phrase.low(), phrase.high());
				else
					pre = expand(pre, phrase);
			} else
				post = expand(post, phrase);
		}
		
		boolean preFocus = anyFocused(s, pre);
		boolean verbFocus = anyFocused(s, verb);
		boolean postFocus = anyFocused(s, post);
		
		// heuristics from Hiyakumoto, Prevost and Cassell
		if (!preFocus && !verbFocus && !postFocus) { // case 1
			maybeAdd(themes, pre); maybeAdd(rhemes, verb, post);
		}
		else if (preFocus && verbFocus && !postFocus) { // case 2
			maybeAdd(themes, pre); maybeAdd(rhemes, verb, post);
		}
		else if (preFocus && !verbFocus && postFocus) { // case 3
			maybeAdd(themes, pre, verb); maybeAdd(rhemes, post);
		}
		else if (!preFocus && verbFocus && postFocus) { // case 4
			maybeAdd(themes, pre, verb); maybeAdd(rhemes, post);
		}
		else if (preFocus && verbFocus && postFocus) { // case 5
			maybeAdd(themes, pre, verb); maybeAdd(rhemes, post);
		}
		else if (preFocus && !verbFocus && !postFocus) { // case 6
			maybeAdd(rhemes, pre); maybeAdd(themes, verb, post);
		}
		else if (!preFocus && verbFocus && !postFocus) { // case 7
			maybeAdd(themes, pre); maybeAdd(rhemes, verb, post);
		}
		else if (!preFocus && !verbFocus && postFocus) { // case 8
			maybeAdd(themes, pre, verb); maybeAdd(rhemes, post);
		}
	}
	
	private Interval expand(Interval x, Interval y) {
		if (x==null)
			return y;
		else if (y==null)
			return x;
		else
			return new Interval(
				Math.min(x.low(), y.low()), 
				Math.max(x.high(), y.high()));
	}
	
	private boolean anyFocused(Sentence s, Interval i) {
		return (i != null) && any(s.tokensIn(i), is_(Features.NEW));
	}
	
	private void maybeAdd(Intervals ints, Interval i) {
		if (i != null)
			ints.add(i);
	}
	
	private void maybeAdd(Intervals ints, Interval i1, Interval i2) {
		if ((i1!=null) || (i2!=null))
			ints.add(expand(i1, i2));
	}
}
