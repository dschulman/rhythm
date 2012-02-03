package rhythm;

public class MonologuePostureShiftGenerator extends Processor {
	// Following Cassell, Nagano, and Bickmore (2001):
	public void process(Sentence s) {
		if (!generateAtTopicShift(s))
			generateOffTopicShift(s);
	}
		
	public boolean generateAtTopicShift(Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT))
				if (Math.random() < 0.84) {
					s.add(new Behavior("posture", clause.low(), clause.low()+1));
					anyShifts = true;
				}
		return anyShifts;
	}

	public void generateOffTopicShift(Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			if (Math.random() < 0.16)
				s.add(new Behavior("posture", clause.low(), clause.low()+1));
	}
}
