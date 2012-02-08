package rhythm;

public class PostureLongitudinal {
	public static class Monologue extends PostureMonologueGenerator {
		@Override
		protected double shift(Context c, Sentence s, boolean onTopicShift) {
			return adjust(c, super.shift(c, s, onTopicShift));
		}
	}
	
	public static class Dialogue extends PostureDialogueGenerator {
		@Override
		protected double shiftAtStart(Context c, Sentence s, boolean newTopic, boolean newTurn) {
			return adjust(c, super.shiftAtStart(c, s, newTopic, newTurn));
		}

		@Override
		protected double shiftAtEnd(Context c, Sentence s, boolean newTopic, boolean newTurn) {
			return adjust(c, super.shiftAtEnd(c, s, newTopic, newTurn));
		}
	}
	
	private static double adjust(Context c, double p) {
		if ((p > 0) && (p < 1)) {
			int sessions = c.get(Features.SESSION_INDEX, 0);
			double minutes = c.get(Features.TIME_OFFSET, 0.0)/60;
			p = invLogit(logit(p) + 0.16*sessions - 0.03*minutes - 0.02*sessions*minutes);
		}
		return p;
	}

	// TODO these probably belong in a math utils
	public static double logit(double p) { return Math.log(p/(1-p)); }
	public static double invLogit(double q) { return 1/(1+Math.exp(-q)); }
}
