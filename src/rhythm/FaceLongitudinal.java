package rhythm;

import static rhythm.Features.*;
import static rhythm.MathUtil.*;

public class FaceLongitudinal implements Processor {
	private double sessionB = DefaultSessionB;
	private double lastB = DefaultLastB;
	
	public static final double DefaultSessionB = -0.16;
	public static final double DefaultLastB = 0.98;
	
	public FaceLongitudinal sessionB(double b) {
		sessionB = b;
		return this;
	}
	
	public FaceLongitudinal lastB(double b) {
		lastB = b;
		return this;
	}
	
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if ("face".equals(b.type())) {
				double p = b.probability();
				if ((p > 0) && (p < 1)) {
					int sessions = c.get(SESSION_INDEX, 0);
					int last = c.is(LAST_SESSION) ? 1 : 0;
					double a = c.get(LONGITUDINAL_EFFECT_MULTIPLIER, 1.0);
					b.probability(invLogit(logit(p) + sessionB*a*sessions + lastB*a*last));
				}
			}
	}
}
