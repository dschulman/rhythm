package rhythm;

public class Behavior extends Interval {
	private final String type;
	private int priority = DEFAULT_PRIORITY;
	
	public Behavior(String type, int low, int high) {
		super(low, high);
		this.type = type;
	}

	public String type() {
		return type;
	}
	
	public static final int DEFAULT_PRIORITY = 0;
	
	public int priority() { 
		return priority;
	}
	
	public Behavior priority(int priority) {
		this.priority = priority;
		return this;
	}
	
	@Override
	public String toString() {
		return type + super.toString();
	}
}
