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
}
