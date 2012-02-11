package rhythm;

import static rhythm.Features.*;
import static rhythm.MathUtil.*;

public class FaceLongitudinal extends FaceGenerator {
	@Override
	protected double strength(Context c, AffectiveContent ac) {
		double p = super.strength(c, ac);
		if ((p > 0) && (p < 1)) {
			int sessions = c.get(SESSION_INDEX, 0);
			int last = c.is(LAST_SESSION) ? 1 : 0;
			p = invLogit(logit(p) - 0.16*sessions + 0.98*last);
		}
		return p;
	}

}
