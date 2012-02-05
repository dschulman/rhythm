package rhythm;

public class InferContext extends Processor {
	private int offset = 0;
	private double seconds = 0;
	
	public void process(Sentence s) {
		s.put(Features.SESSION_INDEX, 0);
		s.put(Features.OFFSET, offset);
		s.put(Features.TIME_OFFSET, seconds);
		offset++;
		seconds += s.size()/2;  // assuming about 120 WPM
	}

	@Override
	public void reset() {
		offset = 0;
		seconds = 0;
	}
}
