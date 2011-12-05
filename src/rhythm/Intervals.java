package rhythm;

import java.util.Iterator;
import java.util.NavigableSet;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Sets;

public class Intervals implements Iterable<Interval> {
	private final NavigableSet<Interval> ints = Sets.newTreeSet(Interval.CompareLow);

	public Iterator<Interval> iterator() {
		return ints.iterator();
	}
	
	public Interval add(int low, int high) {
		Interval i = new Interval(low, high);
		add(i);
		return i;
	}
	
	public void add(Interval i) {
		Preconditions.checkArgument(!anyOverlap(i));
		ints.add(i);
	}
	
	public boolean anyOverlap(Interval i) {
		Interval floor = ints.floor(i);
		Interval ceiling = ints.ceiling(i);
		return ((floor != null) && i.overlaps(floor)) ||
				((ceiling != null) && i.overlaps(ceiling));
	}
	
	public Iterable<Interval> in(final Interval i) {
		return new Iterable<Interval>() {
			public Iterator<Interval> iterator() {
				final Iterator<Interval> inner = ints.tailSet(i).iterator();
				return new AbstractIterator<Interval>() {
					protected Interval computeNext() {
						if (inner.hasNext()) {
							Interval next = inner.next();
							if (i.contains(next))
								return next;
						}
						return endOfData();
					}
				};
			}
		};
	}
	
	@Override
	public String toString() {
		return ints.toString();
	}
}
