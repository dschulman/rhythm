package rhythm;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Sentence extends Features {
	private final ImmutableList<Token> tokens;
	private final List<Behavior> behaviors = Lists.newArrayList();
	
	public Sentence(Iterable<String> tokens) {
		ImmutableList.Builder<Token> b = ImmutableList.builder();
		int n=0;
		for (String t : tokens)
			b.add(new Token(t, n++));
		this.tokens = b.build();
	}
	
	public Sentence(String... tokens) {
		this(Arrays.asList(tokens));
	}
	
	public int size() {
		return tokens.size();
	}
	
	public ImmutableList<Token> tokens() {
		return tokens;
	}
	
	public ImmutableList<Token> tokensIn(Interval i) {
		return tokens.subList(i.low(), i.high());
	}
	
	public <T> Iterable<T> tokens(final Feature<T> f) {
		return Iterables.transform(tokens, new Function<Token, T>() {
			public T apply(Token t) {
				return t.get(f);
			}
		});
	}
	
	public <T> T[] tokensArray(Feature<T> f, Class<T> type) {
		return Iterables.toArray(tokens(f), type);
	}
	
	public Iterable<String> tokensText() {
		return Iterables.transform(tokens, new Function<Token, String>() {
			public String apply(Token t) {
				return t.text();
			}
		});
	}
	
	public String[] tokensTextArray() {
		return Iterables.toArray(tokensText(), String.class);
	}
	
	public void add(Behavior b) {
		behaviors.add(b);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("tokens", tokens)
			.add("features", super.toString())
			.add("behaviors", behaviors)
			.toString();
	}
}
