package rhythm;

import java.io.StringWriter;
import java.util.Set;

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
		w.writeStartDocument();
		w.writeStartElement("speech");
		for (AnnotatedToken t : s.annotated()) {
			writeEnding(t.ending(), w);
			writeStarting(t.starting(), w);
			writeToken(t.token(), w);
		}
		w.writeEndElement();
		w.writeEndDocument();
	}

	private void writeEnding(Set<Behavior> ending, XMLStreamWriter w)
			throws XMLStreamException {
		for (Behavior b : ending)
			if ("brows".equals(b.type()))
				writeEmpty(w, "EYEBROWS", a("DIR", "DOWN"));
	}

	private void writeStarting(Set<Behavior> starting, XMLStreamWriter w)
			throws XMLStreamException {
		for (Behavior b : starting) {
			if ("beat".equals(b.type()))
				writeEmpty(w, "gesture", a("hand", "L"), a("cmd", "BEAT"));
			else if ("brows".equals(b.type()))
				writeEmpty(w, "EYEBROWS", a("DIR", "UP"));
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
