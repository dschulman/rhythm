package rhythm;

public class ProbabilityFilter implements Processor {
	// randomly filter stochastically-generated behaviors
	public void process(Context c, Sentence s) {
		for (Behavior b : s.behaviors())
			if (b.uncertain() && Math.random()>=b.probability())
				b.set(Features.FILTERED);
	}
}
