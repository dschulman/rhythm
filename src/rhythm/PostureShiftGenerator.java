package rhythm;

public class PostureShiftGenerator extends Processor {
	public interface Model {
		double onTopicShiftP();
		double offTopicShiftP();
	}
	
	public static final Model Cassell2001Monologue = new Model() {
		public double onTopicShiftP() { return 0.84; }
		public double offTopicShiftP() { return 0.16; }
	};
	
	// TODO should vary by whether the sentence starts/ends the turn
	public static final Model Cassell2001Dialogue = new Model() {
		public double onTopicShiftP() { return 0.54; }
		public double offTopicShiftP() { return 0.21; }
	};
	
	private final Model model;
	
	public PostureShiftGenerator(Model model) {
		this.model = model;
	}
	
	public void process(Sentence s) {
		if (!generateOnTopicShift(s))
			generateOffTopicShift(s);
	}
		
	public boolean generateOnTopicShift(Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT))
				if (Math.random() < model.onTopicShiftP()) {
					s.add(new Behavior("posture", clause.low(), clause.low()+1));
					anyShifts = true;
				}
		return anyShifts;
	}

	public void generateOffTopicShift(Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			if (Math.random() < model.offTopicShiftP())
				s.add(new Behavior("posture", clause.low(), clause.low()+1));
	}
}
