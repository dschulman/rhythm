package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;

public class PostureDialogueGenerator implements Processor {
	private double changeTurnChangeTopicStartP = DefaultChangeTurnChangeTopicStartP;
	private double keepTurnChangeTopicStartP = DefaultKeepTurnChangeTopicStartP;
	private double keepTopicStartP = DefaultKeepTopicStartP;
	private double changeTurnChangeTopicEndP = DefaultChangeTurnChangeTopicEndP;
	private double changeTurnKeepTopicEndP = DefaultChangeTurnKeepTopicEndP;
	private double keepTurnEndP = DefaultKeepTurnEndP;
	
	public static final double DefaultChangeTurnChangeTopicStartP = 0.54;
	public static final double DefaultKeepTurnChangeTopicStartP = 0;
	public static final double DefaultKeepTopicStartP = 0.13;
	public static final double DefaultChangeTurnChangeTopicEndP = 0.04;
	public static final double DefaultChangeTurnKeepTopicEndP = 0.11;
	public static final double DefaultKeepTurnEndP = 0;

	public PostureDialogueGenerator changeTurnChangeTopicStartP(double p) {
		changeTurnChangeTopicStartP = p;
		return this;
	}
	
	public PostureDialogueGenerator keepTurnChangeTopicStartP(double p) {
		keepTurnChangeTopicStartP = p;
		return this;
	}
	
	public PostureDialogueGenerator keepTopicStartP(double p) {
		keepTopicStartP = p;
		return this;
	}
	
	public PostureDialogueGenerator changeTurnChangeTopicEndP(double p) {
		changeTurnChangeTopicEndP = p;
		return this;
	}
	
	public PostureDialogueGenerator changeTurnKeepTopicEndP(double p) {
		changeTurnKeepTopicEndP = p;
		return this;
	}
	
	public PostureDialogueGenerator keepTurnEndP(double p) {
		keepTurnEndP = p;
		return this;
	}
	
	public void process(Context c, Sentence s) {
		// TODO is this wrong if a non-starting clause changes topic?
		boolean shift = any(s.get(CLAUSES), has_(TOPIC_SHIFT));
		s.addBehavior("posture", 0)
		 .probability(shift ? 
			(s.is(TURN_START) ? 
				changeTurnChangeTopicStartP :
				keepTurnChangeTopicStartP) :
			keepTopicStartP);
		
		// TODO need external annotation: will our next turn shift the topic?
		shift = false;
		s.addBehavior("posture", s.size())
		 .probability(s.is(TURN_END) ? 
			(shift ? 
				changeTurnChangeTopicEndP :
				changeTurnKeepTopicEndP) :
			keepTurnEndP);
	}
}
