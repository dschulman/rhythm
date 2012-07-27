package rhythm;

public class PostureMonologueGenerator implements Processor {
	private double changeTopicP = DefaultChangeTopicP;
	private double keepTopicP = DefaultKeepTopicP;
	
	public static final double DefaultChangeTopicP = 0.84;
	public static final double DefaultKeepTopicP = 0.16;
	
	public PostureMonologueGenerator changeTopicP(double p) {
		changeTopicP = p;
		return this;
	}
	
	public PostureMonologueGenerator keepTopicP(double p) {
		keepTopicP = p;
		return this;
	}
	
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
				 .probability(changeTopicP);
				anyShifts = true;
			}
		return anyShifts;
	}
	
	public void generateOffTopicShift(Context c, Sentence s) {
		for (Interval clause : s.get(Features.CLAUSES))
			s.addBehavior("posture", clause)
			 .probability(keepTopicP);
	}
}
