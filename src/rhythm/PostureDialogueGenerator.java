package rhythm;

import static com.google.common.collect.Iterables.any;
import static rhythm.Features.*;

public class PostureDialogueGenerator extends Processor {
	public static enum Param {
		Start_ChangeTopic_NewTurn,
		Start_ChangeTopic_KeepTurn,
		Start_KeepTopic_NewTurn,
		Start_KeepTopic_KeepTurn,
		End_ChangeTopic_NewTurn,
		End_ChangeTopic_KeepTurn,
		End_KeepTopic_NewTurn,
		End_KeepTopic_KeepTurn;
	}
	
	public static final Model<Param> Cassell2001 = new Model<Param>() {
		public double value(Sentence s, Param param) {
			switch(param) {
			case Start_ChangeTopic_NewTurn: return 0.54;
			case Start_KeepTopic_NewTurn: return 0.13;
			// Cassell2001 says about 0.14/sec; assuming a Poisson process the
			// prob. of generating 1+ shifts in a 1-sec window is about 0.13
			case Start_KeepTopic_KeepTurn: return 0.13;
			case End_ChangeTopic_NewTurn: return 0.04;
			case End_KeepTopic_NewTurn: return 0.11;
			default: return 0;
			}
		}
	};
	
	private final Model<Param> m;
	public PostureDialogueGenerator(Model<Param> m) {
		this.m = m;
	}
	
	public PostureDialogueGenerator() {
		this(Cassell2001);
	}
	
	public void process(Sentence s) {
		if (Math.random() < m.value(s, shiftAtStart(s)))
			s.add(new Behavior("posture", 0, 0));
		if (Math.random() < m.value(s, shiftAtEnd(s)))
			s.add(new Behavior("posture", s.size(), s.size()));
	}
	
	private Param shiftAtStart(Sentence s) {
		// TODO is this wrong if a non-starting clause changes topic?
		boolean shift = any(s.get(CLAUSES), has_(TOPIC_SHIFT));
		if (s.is(Features.TURN_START))
			return shift ? 
				Param.Start_ChangeTopic_NewTurn : 
				Param.Start_KeepTopic_NewTurn;
		else
			return shift ? 
				Param.Start_ChangeTopic_KeepTurn :
				Param.Start_KeepTopic_KeepTurn;
	}
	
	private Param shiftAtEnd(Sentence s) {
		// TODO need external annotation: will our next turn shift the topic?
		return s.is(Features.TURN_END) ?
			Param.End_KeepTopic_NewTurn :
			Param.End_KeepTopic_KeepTurn;
	}
}
