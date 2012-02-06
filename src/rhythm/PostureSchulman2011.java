package rhythm;

public class PostureSchulman2011 {
	public static class Monologue extends PostureMonologueGenerator {
		@Override
		protected double shift(Sentence s, boolean onTopicShift) {
			return adjust(s, super.shift(s, onTopicShift));
		}
	}
	
	public static class Dialogue extends PostureDialogueGenerator {
		@Override
		protected double shiftAtStart(Sentence s, boolean newTopic, boolean newTurn) {
			return adjust(s, super.shiftAtStart(s, newTopic, newTurn));
		}

		@Override
		protected double shiftAtEnd(Sentence s, boolean newTopic, boolean newTurn) {
			return adjust(s, super.shiftAtEnd(s, newTopic, newTurn));
		}
	}
	
	private static double adjust(Sentence s, double p) {
		if ((p > 0) && (p < 1)) {
			int sessions = s.get(Features.SESSION_INDEX, 0);
			double minutes = s.get(Features.TIME_OFFSET, 0.0)/60;
			p = invLogit(logit(p) + 0.16*sessions - 0.03*minutes - 0.02*sessions*minutes);
		}
		return p;
	}

	// TODO these probably belong in a math utils
	public static double logit(double p) { return Math.log(p/(1-p)); }
	public static double invLogit(double q) { return 1/(1+Math.exp(-q)); }
}
