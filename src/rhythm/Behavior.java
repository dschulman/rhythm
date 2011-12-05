package rhythm;

public class Behavior extends Interval {
	private final String type;
	
	public Behavior(String type, int low, int high) {
		super(low, high);
		this.type = type;
	}

	@Override
	public String toString() {
		return type + super.toString();
	}
}
