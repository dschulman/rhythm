package rhythm;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class BeatXmlCompiler implements Compiler {
	private final XMLOutputFactory xof = XMLOutputFactory.newFactory();

	public String apply(Sentence s) {
		StringWriter sw = new StringWriter();
		XMLStreamWriter w;
		try {
			w = xof.createXMLStreamWriter(sw);
			compile(s, w);
			w.flush();
			w.close();
			return sw.toString();
		} catch (XMLStreamException e) {
			// TODO better exception-handling strategy
			throw new RuntimeException(e);
		}
	}

	private void compile(Sentence s, XMLStreamWriter w)
			throws XMLStreamException {
		for (AnnotatedToken t : s.annotated()) {
			writeEnding(t.ending(), w);
			writeStarting(t.starting(), w);
			writeToken(t.token(), w);
		}
	}

	private void writeEnding(Iterable<Behavior> ending, XMLStreamWriter w)
			throws XMLStreamException {
		for (Behavior b : ending)
			if (!b.is(Features.FILTERED)) {
				if ("brows".equals(b.type()))
					writeEmpty(w, "EYEBROWS", a("DIR", "DOWN"));
				else if ("articulation-rate".equals(b.type()))
					w.writeEndElement();
			}
	}

	private void writeStarting(Iterable<Behavior> starting, XMLStreamWriter w)
			throws XMLStreamException {
		for (Behavior b : starting) {
			if (b.is(Features.FILTERED)) {
				if ("face".equals(b.type()))
					writeEmpty(w, "FACE", a("EXPR", affectToExpr(Affect.Neutral)));
			} else if ("beat".equals(b.type()))
				writeEmpty(w, "gesture", a("hand", "L"), a("cmd", "BEAT"));
			else if ("brows".equals(b.type()))
				writeEmpty(w, "EYEBROWS", a("DIR", "UP"));
			else if ("headnod".equals(b.type()))
				writeEmpty(w, "HEADNOD");
			else if ("posture".equals(b.type()))
				writeEmpty(w, "posture");
			else if ("articulation-rate".equals(b.type())) {
				w.writeStartElement("prosody");
				long rate = b.get(Features.RATE);
				w.writeAttribute("rate", (rate>0 ? "+" : "") + b.get(Features.RATE) + "%");
			} else if ("gaze".equals(b.type()) && b.has(Features.DIRECTION, "AWAY")) {
				writeEmpty(w, "gaze", a("dir", "AWAY"));
				writeEmpty(w, "delay", a("ms", "200")); //TODO configurable delay
				writeEmpty(w, "gaze", a("dir", "TOWARDS"));
			} else if ("face".equals(b.type()))
				writeEmpty(w, "FACE", a("EXPR", affectToExpr(b.get(Features.EXPRESSION))));
		}
	}
	
	private String affectToExpr(Affect a) {
		switch (a) {
		case Happy: return "SMILE";
		case Concern: return "CONCERN";
		default: return "WARM";
		}
	}
	
	private void writeToken(Token t, XMLStreamWriter w)
			throws XMLStreamException {
		if (t == null)
			return;
		if (t.get(Features.CLASS) != WordClass.Punctuation)
			w.writeCharacters(" ");
		w.writeCharacters(t.text());
	}
	
	private static final class Attrib {
		public final String name;
		public final String value;
		
		public Attrib(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
	
	private Attrib a(String name, String value) {
		return new Attrib(name, value);
	}
	
	private void writeEmpty(XMLStreamWriter w, String name, Attrib... attribs)
			throws XMLStreamException {
		w.writeEmptyElement(name);
		for (Attrib a : attribs)
			w.writeAttribute(a.name, a.value);
	}
}
