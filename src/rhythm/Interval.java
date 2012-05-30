package rhythm;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class Interval extends Features {
	private final int low, high;
	
	public Interval(int low, int high) {
		checkArgument(low >= 0, "negative interval: %s", low);
		checkArgument(high >= low, "improper interval: [%s,%s)", low, high);
		this.low = low;
		this.high = high;
	}
	
	public int low() {
		return low;
	}
	
	public int high() {
		return high;
	}
	
	public boolean empty() {
		return high <= low;
	}
	
	public boolean contains(int x) {
		return (x >= low) && (x < high);
	}
	
	public boolean contains(Interval i) {
		return (i.low >= low) && (i.low < high) &&
			   (i.high >= low) && (i.high <= high);
	}
	
	public boolean overlaps(Interval i) {
		return contains(i.low) || i.contains(low);
	}
	
	public boolean at(int x) {
		return (x==low) && empty();
	}
	
	@Override
	public String toString() {
		return "["+low+","+high+")->" + super.toString();
	}
	
	public static final Ordering<Interval> CompareLow = new Ordering<Interval>() {
		public int compare(Interval lhs, Interval rhs) {
			return Ints.compare(lhs.low, rhs.low);
		}
	};

	public static final Ordering<Interval> CompareHigh = new Ordering<Interval>() {
		public int compare(Interval lhs, Interval rhs) {
			return Ints.compare(lhs.high, rhs.high);
		}
	};
}
