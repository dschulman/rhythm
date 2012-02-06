package rhythm;

public class PostureMonologueGenerator extends Processor {
	public void process(Sentence s) {
		if (!generateOnTopicShift(s))
			generateOffTopicShift(s);
	}
		
	public boolean generateOnTopicShift(Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT))
				if (Math.random() < shift(s, true)) {
					s.add(new Behavior("posture", clause.low(), clause.low()+1));
					anyShifts = true;
				}
		return anyShifts;
	}
	
	public void generateOffTopicShift(Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			if (Math.random() < shift(s, false))
				s.add(new Behavior("posture", clause.low(), clause.low()+1));
	}
	
	protected double shift(Sentence s, boolean onTopicShift) {
		return onTopicShift ? 0.84 : 0.16;
	}
}
