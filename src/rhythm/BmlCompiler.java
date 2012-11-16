package rhythm;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class BmlCompiler extends XmlCompiler {
	private int idcount = 0;
	
	@Override
	protected void compile(Sentence s, XMLStreamWriter w) throws XMLStreamException {
		w.writeStartDocument();
		w.writeStartElement("bml");
		w.writeDefaultNamespace("http://www.bml-initiative.org/bml/bml-1.0");
		w.writeAttribute("id", "rhythm" + (++idcount));
		writeSpeech(s, w);
		for (Behavior b : s.behaviors())
			writeBehavior(b, w);
		w.writeEndDocument();
	}
	
	private void writeSpeech(Sentence s, XMLStreamWriter w) throws XMLStreamException {
		w.writeStartElement("speech");
		w.writeAttribute("id", "speech");
		w.writeStartElement("text");
		for (AnnotatedToken t : s.annotated()) {
			if (anyToSync(t.starting()) || anyToSync(t.ending()))
				writeEmpty(w, "sync", a("id", "word" + t.index())); 
			writeToken(t.token(), w);
		}
		w.writeEndElement();
		w.writeEndElement();
	}
	
	private boolean anyToSync(Iterable<Behavior> bs) {
		for (Behavior b : bs)
			if (!b.is(Features.FILTERED) || "face".equals(b.type()))
				return true;
		return false;
	}
	
	private void writeBehavior(Behavior b, XMLStreamWriter w) throws XMLStreamException {
		if ("beat".equals(b.type())) {
			writeEmpty(w, "gesture",
				a("start", asSync(b.low())),
				a("lexeme", "BEAT"));
		} else if ("brows".equals(b.type())) {
			writeEmpty(w, "faceLexeme",
				a("start", asSync(b.low())),
				a("end", asSync(b.high())),
				a("lexeme", "RAISE_BROWS"));
		} else if ("face".equals(b.type())) {
			writeFace(b, w);
		} else if ("gaze".equals(b.type()) && b.has(Features.DIRECTION, "AWAY")) {
			writeEmpty(w, "gaze",
				a("start", asSync(b.low())),
				a("end", asSync(b.low(), b.low()==0? 0.5 : 0.3)), // TODO configurable
				a("target", "away")); // TODO configurable
		} else if ("headnod".equals(b.type())) {
			writeEmpty(w, "head",
				a("start", asSync(b.low())),
				a("lexeme", "NOD"));
		} else if ("posture".equals(b.type())) {
			writeEmpty(w, "postureShift",
				a("start", asSync(b.low())));
		}
	}
	
	private void writeFace(Behavior b, XMLStreamWriter w) throws XMLStreamException {
		boolean smile = !b.is(Features.FILTERED) && b.has(Features.EXPRESSION, Affect.Happy);
		boolean frown = !b.is(Features.FILTERED) && b.has(Features.EXPRESSION, Affect.Concern);
		w.writeStartElement("faceShift");
		w.writeAttribute("start", asSync(b.low()));
		writeEmpty(w, "lexeme", 
			a("lexeme", "RAISE_MOUTH_CORNERS"),
			a("amount", smile ? "0.5" : "0"));
		writeEmpty(w, "lexeme",
			a("lexeme", "LOWER_MOUTH_CORNERS"),
			a("amount", frown ? "0.5" : "0"));
		w.writeEndElement();
	}
	
	private String asSync(int index) {
		return "speech:word" + index;
	}
	
	private String asSync(int index, double offset) {
		return asSync(index) + (offset>=0 ? (" + " + offset) : (" - " + (-offset)));
	}
}
