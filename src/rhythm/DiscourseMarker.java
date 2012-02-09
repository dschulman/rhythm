package rhythm;

public enum DiscourseMarker {
	Ack(false),
	Push(true), 
	Pop(true), 
	Next(true), 
	Digress(true), 
	Return(true);

	public final boolean marksTopicShift;
	
	DiscourseMarker(boolean marksTopicShift) {
		this.marksTopicShift = marksTopicShift;
	}
}
