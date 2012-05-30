package rhythm;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class GazeLongitudinal implements Processor {
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if ("gaze".equals(b.type())) {
				double p = b.probability();
				if ((p > 0) && (p < 1)) {
					b.probability(
						b.has(Features.DIRECTION, "AWAY") ?
						1 - adjust(c, 1 - p) : adjust(c, p));
				}
			}
	}
	
	private double adjust(Context c, double p0) {
		int sessions = c.get(Features.SESSION_INDEX, 0);
		boolean last = c.is(Features.LAST_SESSION);
		double adjust = 0.2*sessions - (last ? 0.8 : 0);
		
		// Where did this come from?
		// Assume that gaze-aways are generated by a Poisson process
		// and p0 is the probability of no events in a unit of time
		// in the absence of any adjustment.  Treating the adjustment
		// as a multiplicative effect on rate (linear on log-rate), 
		// this gives the adjusted probability of no events.
		return pow(p0, exp(adjust));
	}
}
