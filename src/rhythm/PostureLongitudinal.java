package rhythm;

import static rhythm.MathUtil.invLogit;
import static rhythm.MathUtil.logit;

public class PostureLongitudinal implements Processor {
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if ("posture".equals(b.type()))
				b.probability(adjust(c, b.probability()));	
	}
	
	protected double adjust(Context c, double p) {
		if ((p > 0) && (p < 1)) {
			int sessions = c.get(Features.SESSION_INDEX, 0);
			double minutes = c.get(Features.TIME_OFFSET, 0.0)/60;
			double a = c.get(Features.LONGITUDINAL_EFFECT_MULTIPLIER, 1.0);
			p = invLogit(logit(p) + 0.16*sessions*a - 0.03*minutes*a - 0.02*sessions*minutes*a);
		}
		return p;
	}
}
