package rhythm;

import static rhythm.Features.*;

public class ArticulationRateLongitudinal implements Processor {
	private static final double INTERCEPT = -2.070;
	private static final double SESSION = -0.015;
	private static final double POSITION = -0.045;
	private static final double POS_SCALE = 109.3616 / 2; //109.3616;
	private static final double POS_CENTER = 0; // 152.8551;
	
	public void process(Context c, Sentence s) {
		int session = c.get(SESSION_INDEX, 0);
		int offset = c.get(OFFSET, 0);
		double adjust = SESSION*session + POSITION*(offset-POS_CENTER)/POS_SCALE;
		
		// TODO should also look at simple acknowledgements
		for (Interval dm : s.get(DISCOURSE_MARKERS)) {
			double baseline = Math.exp(-INTERCEPT);
			double affected = Math.exp(-(INTERCEPT+adjust));
			long rate = Math.round(100*(affected-baseline)/baseline);
			if (rate != 0)
				s.addBehavior("articulation-rate", dm).set(RATE, rate);
		}
	}
}
