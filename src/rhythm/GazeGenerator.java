package rhythm;

import static rhythm.Features.INFORMATION_STRUCTURE;

import java.util.Iterator;

public class GazeGenerator extends Processor {
	public static enum Param {
		ThemeStart, RhemeEnd;
	}
	
	public static final Model<Param> Torres1997 = new Model<Param>() {
		public double value(Sentence s, Param param) {
			switch (param) {
			case ThemeStart: return 0.7;
			case RhemeEnd: return 0.73;
			default: return 0;
			}
		}
	}; 
	
	private final Model<Param> m;
	public GazeGenerator(Model<Param> m) {
		this.m = m;
	}
	
	public GazeGenerator() {
		this(Torres1997);
	}
	
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
			if (isStart || Math.random()<m.value(s, Param.ThemeStart))
				gaze(s, info, "AWAY");
			break;
			
		case Rheme:
			if (isEnd || Math.random()<m.value(s, Param.RhemeEnd))
				gaze(s, info, "TOWARDS");
			break;
		}
	}
	
	private void gaze(Sentence s, Interval i, String dir) {
		Behavior b = new Behavior("gaze", i.low(), i.high());
		b.put(Features.DIRECTION, dir);
		s.add(b);
	}
}
