package rhythm;

import static rhythm.Features.*;

public class FaceGenerator implements Processor {
	@Override
	public void process(Context c, Sentence s) {
		// can set affect on either the context or sentence level (sentence has priority)
		AffectiveContent ac = s.get(AFFECT, c.get(AFFECT, AffectiveContent.NEUTRAL));
		Affect a = Math.random() < strength(c, ac) ? ac.affect : Affect.Neutral;
		// TODO should be setting expression only on CHANGES?
		s.addBehavior("face", 0, s.size()).set(EXPRESSION, a);
	}
	
	protected double strength(Context c, AffectiveContent ac) {
		return ac.strength;
	}
}
