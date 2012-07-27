package rhythm;

import static rhythm.Features.DIRECTION;
import static rhythm.Features.INFORMATION_STRUCTURE;

import java.util.Iterator;

public class GazeGenerator implements Processor {
	private double themeAtStartP = DefaultThemeAtStartP;
	private double themeAfterStartP = DefaultThemeAfterStartP;
	private double rhemeAtEndP = DefaultRhemeAtEndP;
	private double rhemeBeforeEndP = DefaultRhemeBeforeEndP;
	
	public static final double DefaultThemeAtStartP = 1.0;
	public static final double DefaultThemeAfterStartP = 0.7;
	public static final double DefaultRhemeAtEndP = 1.0;
	public static final double DefaultRhemeBeforeEndP = 0.73;
	
	public GazeGenerator themeAtStartP(double p) {
		themeAtStartP = p;
		return this;
	}
	
	public GazeGenerator themeAfterStartP(double p) {
		themeAfterStartP = p;
		return this;
	}
	
	public GazeGenerator rhemeAtEndP(double p) {
		rhemeAtEndP = p;
		return this;
	}
	
	public GazeGenerator rhemeBeforeEndP(double p) {
		 rhemeBeforeEndP = p;
		 return this;
	}
	
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
			 .probability(isStart ? themeAtStartP : themeAfterStartP)
			 .set(DIRECTION, "AWAY");
			break;
		case Rheme:
			s.addBehavior("gaze", info)
			 .priority(5)
			 .probability(isEnd ? rhemeAtEndP : rhemeBeforeEndP)
			 .set(DIRECTION, "TOWARDS");
			break;
		}
	}
}
