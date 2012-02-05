package rhythm;

import static rhythm.Features.INFORMATION;
import static rhythm.Features.INFORMATION_STRUCTURE;
import static rhythm.Information.Rheme;

public class HeadnodGenerator extends Processor {
	public void process(Sentence s) {
		// headnod to mark new words in a rheme
		for (Interval info : s.get(INFORMATION_STRUCTURE))
			if (info.has(INFORMATION, Rheme))
				for (Token t : s.tokensIn(info))
					if (t.is(Features.NEW))
						s.add(new Behavior("headnod", t.index(), t.index()+1));
	}
}
