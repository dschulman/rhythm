package rhythm;

import static rhythm.WordClass.Adjective;
import static rhythm.WordClass.Noun;
import static rhythm.WordClass.Verb;

public class MarkNew implements Processor {	
	public void process(Context c, Sentence s) {
		for (Token t : s.tokens()) {
			WordClass wc = t.get(Features.CLASS);
			if ((wc==Noun) || (wc==Verb) || (wc==Adjective))
				if (c.addDiscourseEntity(t))
					t.put(Features.NEW);
		}
	}
}
