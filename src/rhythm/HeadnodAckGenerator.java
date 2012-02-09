package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.DiscourseMarker.Ack;
import static rhythm.Features.*;

public class HeadnodAckGenerator implements Processor {
	public void process(Context c, Sentence s) {
		// looking for short, backchannel-like utterances (e.g., "ok, great")
		// TODO this should be in language analysis, not behavior generation
		// A quick heuristic rule for that:
		// 1) Starts a turn (i.e. must be responding to partner)
		// 1) Short (<= 8 tokens)
		// 2) Starts with a discourse marker
		// 3) Includes an acknowledgment-type discourse marker ("ok")
		if (s.is(TURN_START) && (s.size()<=8)) {
			Intervals dm = s.get(DISCOURSE_MARKERS);
			if (dm.containing(0) != null)
				if (any(dm, has_(MARKER_TYPE, Ack)))
					s.addBehavior("headnod", 0);
		}
	}
}
