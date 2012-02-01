package rhythm;

import com.google.common.base.Objects;

public final class AnnotatedToken {
	private final Token token;
	private final Iterable<Behavior> starting, ending;
	
	public AnnotatedToken(Token token, 
			Iterable<Behavior> starting,
			Iterable<Behavior> ending) {
		this.token = token;
		this.starting = starting;
		this.ending = ending;
	}
	
	public Token token() {
		return token;
	}
	
	public Iterable<Behavior> starting() {
		return starting;
	}
	
	public Iterable<Behavior> ending() {
		return ending;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AnnotatedToken)) 
			return false;
		AnnotatedToken a = (AnnotatedToken) o;
		return token.equals(a.token) &&
				starting.equals(a.starting) &&
				ending.equals(a.ending);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(token, starting, ending);
	}
}
