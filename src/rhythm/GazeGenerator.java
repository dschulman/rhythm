package rhythm;

import static rhythm.Features.INFORMATION_STRUCTURE;

import java.util.Iterator;

public class GazeGenerator extends Processor {
	public void process(Sentence s) {
		boolean first = true;
		Iterator<Interval> it = s.get(INFORMATION_STRUCTURE).iterator();
		while (it.hasNext()) {
			boolean isStart = first && s.is(Features.TURN_START);
			Interval info = it.next();
			boolean isEnd = !it.hasNext() && s.is(Features.TURN_END);
			generate(s, info, isStart, isEnd);
			first = false;
		}
	}

	private void generate(Sentence s, Interval info, boolean isStart, boolean isEnd) {
		switch (info.get(Features.INFORMATION)) {
		case Theme:
			if (isStart || Math.random()<themeStartAway(s))
				gaze(s, info, "AWAY");
			break;
		case Rheme:
			if (isEnd || Math.random()<rhemeEndTowards(s))
				gaze(s, info, "TOWARDS");
			break;
		}
	}
	
	protected double themeStartAway(Sentence s) { return 0.7; }
	protected double rhemeEndTowards(Sentence s) { return 0.73; }
	
	private void gaze(Sentence s, Interval i, String dir) {
		Behavior b = new Behavior("gaze", i.low(), i.high());
		b.put(Features.DIRECTION, dir);
		s.add(b);
	}
}
