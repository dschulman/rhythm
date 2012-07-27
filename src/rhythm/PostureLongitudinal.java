package rhythm;

import static rhythm.MathUtil.invLogit;
import static rhythm.MathUtil.logit;

public class PostureLongitudinal implements Processor {
	private double sessionB = DefaultSessionB;
	private double minuteB = DefaultMinuteB;
	private double sessionMinuteB = DefaultSessionMinuteB;
	
	public static final double DefaultSessionB = 0.16;
	public static final double DefaultMinuteB = -0.03;
	public static final double DefaultSessionMinuteB = -0.02;
	
	public PostureLongitudinal sessionB(double b) {
		sessionB = b;
		return this;
	}
	
	public PostureLongitudinal minuteB(double b) {
		minuteB = b;
		return this;
	}
	
	public PostureLongitudinal sessionMinuteB(double b) {
		sessionMinuteB = b;
		return this;
	}
	
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
			p = invLogit(logit(p) + sessionB*sessions*a + minuteB*minutes*a + sessionMinuteB*sessions*minutes*a);
		}
		return p;
	}
}
