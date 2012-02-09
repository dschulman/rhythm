package rhythm;

import static rhythm.Features.FILTERED;

import java.util.List;

import com.google.common.base.Objects;

public class ConflictFilter implements Processor {
	// For any conflicting, overlapping behaviors, mark one as filtered (by priority)
	// A bit messy because it's working hard to avoid N^2 comparisons.
	public void process(Context c, Sentence s) {
		List<Behavior> behaviors = s.behaviors();
		for (int i=0; i<behaviors.size(); i++) {
			Behavior lhs = behaviors.get(i);
			if (lhs.is(FILTERED))
				continue;
			for (int j=i+1; j<behaviors.size(); j++) {
				Behavior rhs = behaviors.get(j);
				if (rhs.is(FILTERED))
					continue;
				if (rhs.low() >= lhs.high())
					break;
				if (conflict(lhs, rhs)) {
					if (lhs.priority() < rhs.priority()) {
						lhs.set(FILTERED);
						break;
					} else
						rhs.set(FILTERED);
				}
			}
		}
	}
	
	private boolean conflict(Behavior lhs, Behavior rhs) {
		// TODO implement a degrees-of-freedom model
		return Objects.equal(lhs.type(), rhs.type());
	}
}
