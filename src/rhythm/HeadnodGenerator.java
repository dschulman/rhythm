package rhythm;

import static rhythm.Features.INFORMATION;
import static rhythm.Features.INFORMATION_STRUCTURE;
import static rhythm.Information.Rheme;

public class HeadnodGenerator implements Processor {
	public void process(Context c, Sentence s) {
		// headnod to mark new words in a rheme
		for (Interval info : s.get(INFORMATION_STRUCTURE))
			if (info.has(INFORMATION, Rheme))
				for (Token t : s.tokensIn(info))
					if (t.is(Features.NEW))
						s.addBehavior("headnod", t);
	}
}
