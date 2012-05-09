package rhythm;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class AffectiveContent {
	public final Affect affect;
	public final double strength;
	
	public AffectiveContent(Affect affect, double strength) {
		Preconditions.checkArgument((strength > 0) && (strength <= 1));
		this.affect = affect;
		this.strength = strength;
	}

	public static final AffectiveContent NEUTRAL = 
		new AffectiveContent(Affect.Neutral, 1);
	
	@Override
	public int hashCode() {
		return Objects.hashCode(affect, strength);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AffectiveContent))
			return false;
		AffectiveContent ac = (AffectiveContent) o;
		return affect==ac.affect && strength==ac.strength;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.addValue(affect)
			.add("strength", strength)
			.toString();
	}
	
	// Parses strings of the form "affect" or "affect(strength)"
	// e.g. happy(0.5)
	// unspecified strength defaults to 0.5
	public static AffectiveContent valueOf(String s) {
		int openParen = s.indexOf("(");
		int closeParen = s.lastIndexOf(")");
		double strength = 0.5;
		if ((openParen != -1) && (closeParen == s.length()-1)) {
			strength = Double.parseDouble(s.substring(openParen+1, closeParen));
			s = s.substring(0, openParen);
		}
		return new AffectiveContent(Affect.valueOf(s), strength);
	}
}
