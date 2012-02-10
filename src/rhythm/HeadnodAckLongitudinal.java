package rhythm;

import static rhythm.Features.*;
import static rhythm.MathUtil.invLogit;
import static rhythm.MathUtil.logit;

public class HeadnodAckLongitudinal extends HeadnodAckGenerator {
	@Override
	protected double headnod(Context c, Sentence s) {
		double p = super.headnod(c, s);
		if ((p > 0) && (p < 1)) {
			int sessions = c.get(SESSION_INDEX, 0);
			int last = c.is(LAST_SESSION) ? 1 : 0;
			double alliance = c.get(ALLIANCE, 0.0);
			return invLogit(logit(p)
				+ 0.06*sessions 
				- 0.04*last 
				- 0.28*alliance
				+ 0.06*sessions*alliance);
		}
		return p;
	}
}
