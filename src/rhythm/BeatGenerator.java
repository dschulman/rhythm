package rhythm;

public class BeatGenerator implements Processor {
	public void process(Sentence s) {
		// put a beat on any new word in a rheme
		for (Interval rheme : s.get(Features.RHEMES)) {
			for (Token t : s.tokensIn(rheme))
				if (t.is(Features.NEW))
					s.add(new Behavior("beat", t.index(), t.index()+1));
		}
	}
}
