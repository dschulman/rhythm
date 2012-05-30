package rhythm;

public class PostureMonologueGenerator implements Processor {
	public void process(Context c, Sentence s) {
		// TODO use a separate filter pass
		if (!generateOnTopicShift(c, s))
			generateOffTopicShift(c, s);
	}
		
	public boolean generateOnTopicShift(Context c, Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT)) {
				s.addBehavior("posture", clause)
				 .priority(15)
				 .probability(shift(c, s, true));
				anyShifts = true;
			}
		return anyShifts;
	}
	
	public void generateOffTopicShift(Context c, Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			s.addBehavior("posture", clause)
			 .probability(shift(c, s, false));
	}
	
	protected double shift(Context c, Sentence s, boolean onTopicShift) {
		return onTopicShift ? 0.84 : 0.16;
	}
}
