package rhythm;

public class PostureSchulman2011<P extends Enum<P>> implements Model<P> {
	public static enum Param {
		Session, Minutes, Session_Minutes;
	}
	
	public static final Model<Param> Default = new Model<Param> () {
		public double value(Sentence s, Param param) {
			switch(param) {
			case Session: return 0.16;
			case Minutes: return -0.03;
			case Session_Minutes: return -0.02;
			default: return 0;
			}
		}
	};
	
	private final Model<P> base;
	private final Model<Param> adjust;
	
	public PostureSchulman2011(Model<P> base, Model<Param> adjust) {
		this.base = base;
		this.adjust = adjust;
	}
	
	public static <P extends Enum<P>> PostureSchulman2011<P> adjust(Model<P> base) {
		return new PostureSchulman2011<P> (base, Default);
	}
	
	public static <P extends Enum<P>> PostureSchulman2011<P> adjust(Model<P> base, Model<Param> adjust) {
		return new PostureSchulman2011<P> (base, adjust);
	}
	
	public PostureSchulman2011(Model<P> base) {
		this(base, Default);
	}
	
	public double value(Sentence s, P param) {
		double p = base.value(s, param);
		if ((p > 0) && (p < 1)) {
			int sessions = s.get(Features.SESSION_INDEX, 0);
			double minutes = s.get(Features.TIME_OFFSET, 0.0)/60;
			p = invLogit(
				logit(p) +
				adjust.value(s, Param.Session)*sessions +
				adjust.value(s, Param.Minutes)*minutes + 
				adjust.value(s, Param.Session_Minutes)*sessions*minutes);
		}
		return p;
	}

	// TODO these probably belong in a math utils
	double logit(double p) { return Math.log(p/(1-p)); }
	double invLogit(double q) { return 1/(1+Math.exp(-q)); }
	
	public static final Model<PostureShiftMonologueGenerator.Param> Monologue =
		adjust(PostureShiftMonologueGenerator.Cassell2001);
	public static final Model<PostureShiftDialogueGenerator.Param> Dialogue =
		adjust(PostureShiftDialogueGenerator.Cassell2001);
}
