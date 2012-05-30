package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;

public class PostureDialogueGenerator implements Processor {
	public void process(Context c, Sentence s) {
		// TODO is this wrong if a non-starting clause changes topic?
		boolean shift = any(s.get(CLAUSES), has_(TOPIC_SHIFT));
		s.addBehavior("posture", 0).probability(shiftAtStart(c, s, shift, s.is(TURN_START)));
		
		// TODO need external annotation: will our next turn shift the topic?
		s.addBehavior("posture", s.size()).probability(shiftAtEnd(c, s, false, s.is(TURN_END)));
	}
	
	protected double shiftAtStart(Context c, Sentence s, boolean newTopic, boolean newTurn) {
		return newTopic ? (newTurn ? 0.54 : 0) : 0.13;
	}
	
	protected double shiftAtEnd(Context c, Sentence s, boolean newTopic, boolean newTurn) {
		return newTurn ? (newTopic ? 0.04 : 0.11) : 0;
	}
}
