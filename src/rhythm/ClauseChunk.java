package rhythm;

import static rhythm.Features.CLASS;
import static rhythm.Features.PHRASE_TYPE;
import static rhythm.WordClass.Punctuation;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.collect.ImmutableSet;

public class ClauseChunk implements Processor {
	public void process(Context c, Sentence s) {
		Intervals clauses = new Intervals();
		int low = Integer.MAX_VALUE;
		int high = Integer.MIN_VALUE;
		boolean hasVerb = false;
		for (Peek3Iterator<Interval> it = peek3(s.get(Features.PHRASES)); it.hasNext();) {
			Interval phrase = it.next();
			low = Math.min(phrase.low(), low);
			high = Math.max(phrase.high(), high);
			hasVerb = hasVerb || phrase.has(PHRASE_TYPE, PhraseType.VerbPhrase);
			if (hasVerb && !isPotentialBreak(s, it.peek2())) {
				if (isEnding(s, it.peek()) || 
					isCommaBreak(s, it.peek(), it.peek2(), it.peek3())) {
					clauses.add(low, high);
					low = Integer.MAX_VALUE;
					high = Integer.MIN_VALUE;
					hasVerb = false;
					it.next(); // skip the break punctuation (not part of either clause)
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
	
	private final ImmutableSet<String> ENDINGS =
		ImmutableSet.of(".", "!", "?", ":", ";");
	
	private boolean isEnding(Sentence s, Interval i) {
		if ((i==null) || !i.single()) return false;
		Token t = s.token(i.low());
		return t.has(CLASS, Punctuation) && ENDINGS.contains(t.text());
	}
	
	private boolean isComma(Sentence s, Interval i) {
		if ((i==null) || !i.single()) return false;
		Token t = s.token(i.low());
		return t.has(CLASS, Punctuation) && ",".equals(t.text());
	}
	
	private boolean isPotentialBreak(Sentence s, Interval i) {
		return isEnding(s, i) || isComma(s, i);
	}
	
	private boolean isCommaBreak(Sentence s, Interval i1, Interval i2, Interval i3) {
		if ((i1==null) || (i2==null) || (i3==null)) return false;
		return isComma(s, i1) &&
			   !i3.has(PHRASE_TYPE, PhraseType.NonPhraseToken) &&
			   (i2.get(PHRASE_TYPE) != i3.get(PHRASE_TYPE));
	}
	
	private static <T> Peek3Iterator<T> peek3(Iterable<T> it) {
		return new Peek3Iterator<T>(it.iterator());
	}
	
	private final static class Peek3Iterator<T> implements Iterator<T> {
		private final Iterator<T> it;
		private boolean hasNext1, hasNext2, hasNext3;
		private T next1, next2, next3;
		
		public Peek3Iterator(Iterator<T> it) {
			this.it = it;
			if (it.hasNext()) {
				hasNext1 = true;
				next1 = it.next();
			}
			if (it.hasNext()) {
				hasNext2 = true;
				next2 = it.next();
			}
			if (it.hasNext()) {
				hasNext3 = true;
				next3 = it.next();
			}
		}

		public boolean hasNext() {
			return hasNext1;
		}

		public T next() {
			if (!hasNext1)
				throw new NoSuchElementException();
			T next = next1;
			hasNext1 = hasNext2;
			next1 = next2;
			hasNext2 = hasNext3;
			next2 = next3;
			hasNext3 = it.hasNext();
			next3 = hasNext3 ? it.next() : null;
			return next;
		}

		public T peek() {
			return next1;
		}
		
		public T peek2() {
			return next2;
		}
		
		public T peek3() {
			return next3;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
