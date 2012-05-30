package rhythm;

import static com.google.common.base.Preconditions.checkArgument;

public class Behavior extends Interval {
	private final String type;
	private int priority = DEFAULT_PRIORITY;
	private double probability = CERTAIN;
	
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
	
	public static final double CERTAIN = 1;
	
	public double probability() {
		return probability;
	}
	
	public boolean uncertain() {
		return probability < CERTAIN;
	}
	
	public Behavior probability(double p) {
		checkArgument(p>=0 && p<=1, "probability %s is not in [0,1]", p);
		this.probability = p;
		return this;
	}
	
	@Override
	public String toString() {
		return type + super.toString();
	}
}
