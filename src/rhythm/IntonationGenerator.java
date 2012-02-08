package rhythm;

public class IntonationGenerator implements Processor {
	// TODO this is English-specific?; need a way to indicate that?
	public void process(Context c, Sentence s) {
		for (Interval i : s.get(Features.INFORMATION_STRUCTURE)) {
			boolean theme = i.has(Features.INFORMATION, Information.Theme);
			for (Token t : s.tokensIn(i))
				if (t.is(Features.NEW)) {
					Behavior b = new Behavior("intonation-accent", t.index(), t.index()+1);
					b.put(Features.ACCENT, theme ? "L+H*" : "H*");
					s.add(b);
				}
			Behavior b = new Behavior("intonation", i.low(), i.high());
			b.put(Features.ENDTONE, theme ? "L-H%" : "L-L%");
			s.add(b);
		}
	}
}
