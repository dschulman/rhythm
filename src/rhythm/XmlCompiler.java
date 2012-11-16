package rhythm;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class XmlCompiler implements Compiler {
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

	protected abstract void compile(Sentence s, XMLStreamWriter w)
		throws XMLStreamException;

	protected void writeToken(Token t, XMLStreamWriter w)
			throws XMLStreamException {
		if (t == null)
			return;
		if (! t.is(Features.MERGE_LEFT))
			w.writeCharacters(" ");
		w.writeCharacters(t.text());
	}
	
	protected static final class Attrib {
		public final String name;
		public final String value;
		
		public Attrib(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
	
	protected Attrib a(String name, String value) {
		return new Attrib(name, value);
	}

	protected void writeEmpty(XMLStreamWriter w, String name, Attrib... attribs)
			throws XMLStreamException {
		w.writeEmptyElement(name);
		for (Attrib a : attribs)
			w.writeAttribute(a.name, a.value);
	}
}
