package rhythm;

import com.google.common.base.Function;

public interface Compiler extends Function<Sentence, String> {
	public String apply(Sentence s);
	
	public static final Compiler ToString = new Compiler() {
		public String apply(Sentence s) {
			return s.toString();
		}
	};
}
