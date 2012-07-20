package rhythm;

import static rhythm.Features.*;
import static rhythm.MathUtil.*;

public class FaceLongitudinal implements Processor {
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if ("face".equals(b.type())) {
				double p = b.probability();
				if ((p > 0) && (p < 1)) {
					int sessions = c.get(SESSION_INDEX, 0);
					int last = c.is(LAST_SESSION) ? 1 : 0;
					double a = c.get(LONGITUDINAL_EFFECT_MULTIPLIER, 1.0);
					b.probability(invLogit(logit(p) - 0.16*a*sessions + 0.98*a*last));
				}
			}
	}
}
