package rhythm;

public class HeadnodGenerator extends Processor {
	public void process(Sentence s) {
		// headnod to mark new words in a rheme
		for (Interval rheme : s.get(Features.RHEMES))
			for (Token t : s.tokensIn(rheme))
				if (t.is(Features.NEW))
					s.add(new Behavior("headnod", t.index(), t.index()+1));
	}
}
