package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;

public class PostureDialogueGenerator implements Processor {
	public void process(Context c, Sentence s) {
		// TODO is this wrong if a non-starting clause changes topic?
		boolean shift = any(s.get(CLAUSES), has_(TOPIC_SHIFT));
		s.addBehavior("posture", 0)
		 .probability(shift ? (s.is(TURN_START) ? 0.54 : 0) : 0.13);
		
		// TODO need external annotation: will our next turn shift the topic?
		shift = false;
		s.addBehavior("posture", s.size())
		 .probability(s.is(TURN_END) ? (shift ? 0.04 : 0.11) : 0);
	}
}
