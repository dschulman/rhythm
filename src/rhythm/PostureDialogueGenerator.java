package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;

public class PostureDialogueGenerator extends Processor {
	public void process(Sentence s) {
		// TODO is this wrong if a non-starting clause changes topic?
		boolean shift = any(s.get(CLAUSES), has_(TOPIC_SHIFT));
		if (Math.random() < shiftAtStart(s, shift, s.is(TURN_START)))
			s.add(new Behavior("posture", 0, 0));
		
		// TODO need external annotation: will our next turn shift the topic?
		if (Math.random() < shiftAtEnd(s, false, s.is(TURN_END)))
			s.add(new Behavior("posture", s.size(), s.size()));
	}
	
	protected double shiftAtStart(Sentence s, boolean newTopic, boolean newTurn) {
		return newTopic ? (newTurn ? 0.54 : 0) : 0.13;
	}
	
	protected double shiftAtEnd(Sentence s, boolean newTopic, boolean newTurn) {
		return newTurn ? (newTopic ? 0.04 : 0.11) : 0;
	}
}
