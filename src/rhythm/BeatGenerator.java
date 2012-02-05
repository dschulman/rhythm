package rhythm;

import static rhythm.Features.*;
import static rhythm.Information.Rheme;

public class BeatGenerator extends Processor {
	public void process(Sentence s) {
		// put a beat on any new word in a rheme
		for (Interval info : s.get(INFORMATION_STRUCTURE))
			if (info.has(INFORMATION, Rheme))
				for (Token t : s.tokensIn(info))
					if (t.is(NEW))
						s.add(new Behavior("beat", t.index(), t.index() + 1));
	}
}
