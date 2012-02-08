package rhythm;

public class PostureMonologueGenerator implements Processor {
	public void process(Context c, Sentence s) {
		if (!generateOnTopicShift(c, s))
			generateOffTopicShift(c, s);
	}
		
	public boolean generateOnTopicShift(Context c, Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT))
				if (Math.random() < shift(c, s, true)) {
					s.add(new Behavior("posture", clause.low(), clause.low()+1));
					anyShifts = true;
				}
		return anyShifts;
	}
	
	public void generateOffTopicShift(Context c, Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			if (Math.random() < shift(c, s, false))
				s.add(new Behavior("posture", clause.low(), clause.low()+1));
	}
	
	protected double shift(Context c, Sentence s, boolean onTopicShift) {
		return onTopicShift ? 0.84 : 0.16;
	}
}
