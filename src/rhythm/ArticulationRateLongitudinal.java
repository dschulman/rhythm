package rhythm;

import static rhythm.Features.*;

public class ArticulationRateLongitudinal implements Processor {
	private double interceptB = DefaultInterceptB;
	private double sessionB = DefaultSessionB;
	private double offsetB = DefaultOffsetB;
	private double offsetScale = DefaultOffsetScale;
	private double offsetCenter = DefaultOffsetCenter;
	
	public static final double DefaultInterceptB = -2.070;
	public static final double DefaultSessionB = -0.015;
	public static final double DefaultOffsetB = -0.045;
	public static final double DefaultOffsetScale = 109.3616;
	public static final double DefaultOffsetCenter = 0; // 152.8551;
	
	public ArticulationRateLongitudinal interceptB(double b) {
		interceptB = b;
		return this;
	}
	
	public ArticulationRateLongitudinal sessionB(double b) {
		sessionB = b;
		return this;
	}
	
	public ArticulationRateLongitudinal offsetB(double b) {
		offsetB = b;
		return this;
	}
	
	public ArticulationRateLongitudinal offsetScale(double scale) {
		offsetScale = scale;
		return this;
	}
	
	public ArticulationRateLongitudinal offsetCenter(double center) {
		offsetCenter = center;
		return this;
	}
	
	public void process(Context c, Sentence s) {
		int session = c.get(SESSION_INDEX, 0);
		int offset = c.get(OFFSET, 0);
		double a = c.get(LONGITUDINAL_EFFECT_MULTIPLIER, 1.0);
		double adjust = sessionB*a*session + offsetB*a*(offset-offsetCenter)/offsetScale;
		
		// TODO should also look at simple acknowledgements
		for (Interval dm : s.get(DISCOURSE_MARKERS)) {
			double baseline = Math.exp(-interceptB);
			double affected = Math.exp(-(interceptB+adjust));
			long rate = Math.round(100*(affected-baseline)/baseline);
			if (rate != 0)
				s.addBehavior("articulation-rate", dm).set(RATE, rate);
		}
	}
}
