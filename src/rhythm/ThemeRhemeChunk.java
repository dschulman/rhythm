package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.is_;
import static rhythm.Information.*;

public class ThemeRhemeChunk implements Processor {
	public void process(Context c, Sentence s) {
		Intervals infoStruct = new Intervals();
		for (Interval clause : s.get(Features.CLAUSES))
			split(s, clause, infoStruct);
		s.set(Features.INFORMATION_STRUCTURE, infoStruct);
	}

	private void split(Sentence s, Interval clause, Intervals out) {
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
			maybeAdd(out, Theme, pre); maybeAdd(out, Rheme, verb, post);
		}
		else if (preFocus && verbFocus && !postFocus) { // case 2
			maybeAdd(out, Theme, pre); maybeAdd(out, Rheme, verb, post);
		}
		else if (preFocus && !verbFocus && postFocus) { // case 3
			maybeAdd(out, Theme, pre, verb); maybeAdd(out, Rheme, post);
		}
		else if (!preFocus && verbFocus && postFocus) { // case 4
			maybeAdd(out, Theme, pre, verb); maybeAdd(out, Rheme, post);
		}
		else if (preFocus && verbFocus && postFocus) { // case 5
			maybeAdd(out, Theme, pre, verb); maybeAdd(out, Rheme, post);
		}
		else if (preFocus && !verbFocus && !postFocus) { // case 6
			maybeAdd(out, Rheme, pre); maybeAdd(out, Theme, verb, post);
		}
		else if (!preFocus && verbFocus && !postFocus) { // case 7
			maybeAdd(out, Theme, pre); maybeAdd(out, Rheme, verb, post);
		}
		else if (!preFocus && !verbFocus && postFocus) { // case 8
			maybeAdd(out, Theme, pre, verb); maybeAdd(out, Rheme, post);
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
	
	private void maybeAdd(Intervals out, Information info, Interval i) {
		if (i != null) {
			i.set(Features.INFORMATION, info);
			out.add(i);
		}
	}
	
	private void maybeAdd(Intervals out, Information info, Interval i1, Interval i2) {
		if ((i1!=null) || (i2!=null))
			maybeAdd(out, info, expand(i1, i2));
	}
}
