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
			if (isStart || Math.random()<themeStartAway(c, s))
				s.addBehavior("gaze", info).priority(1).set(DIRECTION, "AWAY");
			break;
		case Rheme:
			if (isEnd || Math.random()<rhemeEndTowards(c, s))
				s.addBehavior("gaze", info).priority(5).set(DIRECTION, "TOWARDS");
			break;
		}
	}
	
	protected double themeStartAway(Context c, Sentence s) { return 0.7; }
	protected double rhemeEndTowards(Context c, Sentence s) { return 0.73; }
}
