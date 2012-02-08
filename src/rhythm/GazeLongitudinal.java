package rhythm;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class GazeLongitudinal extends GazeGenerator {
	@Override
	protected double themeStartAway(Sentence s) {
		return 1 - adjust(s, 1 - super.themeStartAway(s));
	}

	@Override
	protected double rhemeEndTowards(Sentence s) {
		return adjust(s, super.rhemeEndTowards(s));
	}
	
	private double adjust(Sentence s, double p0) {
		int sessions = s.get(Features.SESSION_INDEX, 0);
		boolean last = s.is(Features.LAST_SESSION);
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