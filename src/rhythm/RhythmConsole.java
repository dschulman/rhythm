package rhythm;

import java.io.IOException;

import jline.ConsoleReader;

public class RhythmConsole {
	private final Rhythm rhythm;
	private final ConsoleReader console = new ConsoleReader();
	
	public RhythmConsole(Rhythm rhythm) throws IOException {
		this.rhythm = rhythm;
	}
	
	public void run() throws IOException {
		String input;
		while ((input = console.readLine("> ")) != null) {
			Iterable<Sentence> output = rhythm.process(input);
			System.out.println(output);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Rhythm r = new Rhythm(new Configuration(args));
		RhythmConsole rc = new RhythmConsole(r);
		rc.run();
	}
}
