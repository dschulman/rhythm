package rhythm;

public class IntonationGenerator implements Processor {
	// TODO this is English-specific?; need a way to indicate that?
	public void process(Context c, Sentence s) {
		for (Interval i : s.get(Features.INFORMATION_STRUCTURE)) {
			boolean theme = i.has(Features.INFORMATION, Information.Theme);
			for (Token t : s.tokensIn(i))
				if (t.is(Features.NEW))
					s.addBehavior("intonation-accent", t)
					 .set(Features.ACCENT, theme ? "L+H*" : "H*");
			s.addBehavior("intonation", i)
			 .set(Features.ENDTONE, theme? "L-H%" : "L-L%");
		}
	}
}
