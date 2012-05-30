package rhythm;

import static rhythm.Features.*;
import static rhythm.MathUtil.invLogit;
import static rhythm.MathUtil.logit;

public class HeadnodAckLongitudinal implements Processor {
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if ("headnod".equals(b) && b.at(0) && s.is(TURN_START))
				adjust(c, b);
	}
	
	protected void adjust(Context c, Behavior b) {
		double p = b.probability();
		if ((p > 0) && (p < 1)) {
			int sessions = c.get(SESSION_INDEX, 0);
			int last = c.is(LAST_SESSION) ? 1 : 0;
			double alliance = c.get(ALLIANCE, 0.0);
			b.probability(invLogit(logit(p)
				+ 0.06*sessions 
				- 0.04*last 
				- 0.28*alliance
				+ 0.06*sessions*alliance));
		}
	}
}
