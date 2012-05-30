package rhythm;

import static rhythm.Features.*;

public class FaceGenerator implements Processor {
	@Override
	public void process(Context c, Sentence s) {
		// can set affect on either the context or sentence level (sentence has priority)
		AffectiveContent ac = s.get(AFFECT, c.get(AFFECT, AffectiveContent.NEUTRAL));
		s.addBehavior("face", 0, s.size())
		 .probability(ac.strength)
		 .set(EXPRESSION, ac.affect);
	}
}
