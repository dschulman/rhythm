package rhythm;

public class Feature<T> {
	private final String name;
	
	public Feature(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static <T> Feature<T> named(String name) {
		return new Feature<T>(name);
	}
}
