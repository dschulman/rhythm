package rhythm;

public class PostureShiftGenerator extends Processor {
	public static enum Param {
		OnTopicP, OffTopicP;
	};

	public static final Model<Param> Cassell2001 = new Model<Param>() {
		public double value(Sentence s, Param param) {
			switch (param) {
			case OnTopicP:
				return 0.84;
			case OffTopicP:
				return 0.16;
			default:
				return 0;
			}
		}
	};
	
	private final Model<Param> m;
	
	public PostureShiftGenerator(Model<Param> m) {
		this.m = m;
	}
	
	public void process(Sentence s) {
		if (!generateOnTopicShift(s))
			generateOffTopicShift(s);
	}
		
	public boolean generateOnTopicShift(Sentence s) {
		boolean anyShifts = false;
		for (Interval clause : s.get(Features.CLAUSES))
			if (clause.has(Features.TOPIC_SHIFT))
				if (Math.random() < m.value(s, Param.OnTopicP)) {
					s.add(new Behavior("posture", clause.low(), clause.low()+1));
					anyShifts = true;
				}
		return anyShifts;
	}

	public void generateOffTopicShift(Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			if (Math.random() < m.value(s, Param.OffTopicP))
				s.add(new Behavior("posture", clause.low(), clause.low()+1));
	}
}
