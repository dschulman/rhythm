package rhythm;

import java.util.Set;

import com.google.common.base.Objects;

public final class AnnotatedToken {
	private final Token token;
	private final Set<Behavior> starting, ending;
	
	public AnnotatedToken(Token token, 
			Set<Behavior> starting,
			Set<Behavior> ending) {
		this.token = token;
		this.starting = starting;
		this.ending = ending;
	}
	
	public Token token() {
		return token;
	}
	
	public Set<Behavior> starting() {
		return starting;
	}
	
	public Set<Behavior> ending() {
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
