package rhythm;

import com.google.common.base.Objects;

public class Token extends Features {
	private final String text;
	private final int index;
	
	public Token(String text, int index) {
		this.text = text;
		this.index = index;
	}
	
	public String text() {
		return text;
	}
	
	public int index() {
		return index;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("text", text)
			.add("features", super.toString())
			.toString();
	}
}
