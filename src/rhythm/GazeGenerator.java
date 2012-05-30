package rhythm;

import static rhythm.Features.DIRECTION;
import static rhythm.Features.INFORMATION_STRUCTURE;

import java.util.Iterator;

public class GazeGenerator implements Processor {
	public void process(Context c, Sentence s) {
		boolean first = true;
		Iterator<Interval> it = s.get(INFORMATION_STRUCTURE).iterator();
		while (it.hasNext()) {
			boolean isStart = first && s.is(Features.TURN_START);
			Interval info = it.next();
			boolean isEnd = !it.hasNext() && s.is(Features.TURN_END);
			generate(c, s, info, isStart, isEnd);
			first = false;
		}
	}

	private void generate(Context c, Sentence s, Interval info, boolean isStart, boolean isEnd) {
		switch (info.get(Features.INFORMATION)) {
		case Theme:
			s.addBehavior("gaze", info)
			 .priority(1)
			 .probability(isStart ? 1 : 0.7)
			 .set(DIRECTION, "AWAY");
			break;
		case Rheme:
			s.addBehavior("gaze", info)
			 .priority(5)
			 .probability(isEnd ? 1 : 0.73)
			 .set(DIRECTION, "TOWARDS");
			break;
		}
	}
}
