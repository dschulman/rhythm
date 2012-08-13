package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.DiscourseMarker.Ack;
import static rhythm.Features.*;

import com.google.common.collect.Iterables;

public class HeadnodAckGenerator implements Processor {
	private double ackP = DefaultAckP;
	private double otherMarkerP = DefaultOtherMarkerP;
	private double noMarkerP = DefaultNoMarkerP;
	
	// these rates have no empirical basis, sorry:
	public static final double DefaultAckP = 0.8;
	public static final double DefaultOtherMarkerP = 0.5;
	public static final double DefaultNoMarkerP = 0.2;
	
	public HeadnodAckGenerator ackP(double p) {
		ackP = p;
		return this;
	}
	
	public HeadnodAckGenerator otherMarkerP(double p) {
		otherMarkerP = p;
		return this;
	}
	
	public HeadnodAckGenerator noMarkerP(double p) {
		noMarkerP = p;
		return this;
	}
	
	private static final Interval beginning = new Interval(0, 8);
	
	public void process(Context c, Sentence s) {
		// Trying to put headnods on the beginnings of turn that do some
		// grounding.  We should really be looking at what the user says,
		// not just the agent, so this is some very hand-wavy heuristics
		// instead...
		//
		// We look for discourse markers appearing near the beginning of
		// the turn (within 8 tokens), and generate a headnod stochastically:
		// 1) high prob: if there's an acknowledgement marker ("ok")
		// 2) medium prob: any other discourse markers
		// 3) low prob: no markers at all.
		if (s.is(TURN_START)) {
			double p = generate(s.get(DISCOURSE_MARKERS).startingIn(beginning));
			if (p > 0)
				s.addBehavior("headnod", 0).probability(p);
		}
	}
	
	private double generate(Iterable<Interval> markers) {
		if (any(markers, has_(MARKER_TYPE, Ack)))
			return ackP;
		else if (!Iterables.isEmpty(markers))
			return otherMarkerP;
		else
			return noMarkerP;
	}
}
