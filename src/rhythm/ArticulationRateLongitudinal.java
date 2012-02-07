package rhythm;

public class ArticulationRateLongitudinal extends Processor {
	private static final double INTERCEPT = -2.070;
	private static final double SESSION = -0.015;
	private static final double POSITION = -0.045;
	private static final double POS_SCALE = 109.3616 / 2; //109.3616;
	private static final double POS_CENTER = 0; // 152.8551;
	
	public void process(Sentence s) {
		int session = s.get(Features.SESSION_INDEX, 0);
		int offset = s.get(Features.OFFSET, 0);
		double adjust = SESSION*session + POSITION*(offset-POS_CENTER)/POS_SCALE;
		
		// TODO should also look at simple acknowledgements
		for (Interval dm : s.get(Features.DISCOURSE_MARKERS)) {
			double baseline = Math.exp(-INTERCEPT);
			double affected = Math.exp(-(INTERCEPT+adjust));
			long rate = Math.round(100*(affected-baseline)/baseline);
			if (rate != 0) {
				Behavior b = new Behavior("articulation-rate", dm.low(), dm.high());
				b.put(Features.RATE, rate);
				s.add(b);
			}
		}
	}
}
