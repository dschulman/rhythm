package rhythm;

public class UpdateContext implements Processor {
	public void process(Context c, Sentence s) {
		int offset = c.get(Features.OFFSET, 0);
		c.put(Features.OFFSET, offset+1);
		
		double seconds = c.get(Features.TIME_OFFSET, 0.0);
		c.put(Features.TIME_OFFSET, seconds+(s.size()/2)); // assume 120 WPM
	}
}
