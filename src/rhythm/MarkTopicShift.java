package rhythm;

import static rhythm.Features.*;

public class MarkTopicShift implements Processor {
	public void process(Context c, Sentence s) {
		// mark a shift whenever a clause starts with an appropriate marker
		for (Interval clause : s.get(CLAUSES)) {
			Interval dm = s.get(DISCOURSE_MARKERS).containing(clause.low());
			if (dm != null) {
				DiscourseMarker type = dm.get(MARKER_TYPE);
				if (type.marksTopicShift)
					clause.set(TOPIC_SHIFT, type);
			}
		}
	}
}
