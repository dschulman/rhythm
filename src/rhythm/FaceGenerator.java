package rhythm;

import static rhythm.Features.*;

public class FaceGenerator implements Processor {
	@Override
	public void process(Context c, Sentence s) {
		// TODO how to handle facial expressions spanning turns?
		if (s.has(AFFECT)) {
			AffectiveContent ac = s.get(AFFECT);
			if (Math.random() < strength(c, ac))
				s.addBehavior("face", 0, s.size()).set(EXPRESSION, ac.affect);
		}
	}
	
	protected double strength(Context c, AffectiveContent ac) {
		return ac.strength;
	}
}
