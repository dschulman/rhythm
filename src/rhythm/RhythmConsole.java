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
		while ((input = console.readLine("> ")) != null)
			if ( ! processAsCommand(input))
				for (String output : rhythm.process(input))
					System.out.println(output);
	}
	
	private boolean processAsCommand(String input) {
		input = input.trim();
		if (! input.startsWith(":"))
			return false;
		
		input = input.substring(1).trim();
		String[] parts = input.split("\\s+");
		if (parts.length == 0)
			System.out.println("missing command!");
		else if (parts[0].equalsIgnoreCase("output"))
			cmdOutput(parts);
		else if (parts[0].equalsIgnoreCase("reset"))
			rhythm.reset();
		else
			System.out.println("unknown command: " + parts[0]);
		return true;
	}
	
	private void cmdOutput(String[] args) {
		if (args.length != 2)
			System.out.println("Usage: output (beat|string)");
		else if ("beat".equalsIgnoreCase(args[1]))
			rhythm.setOutput(new BeatXmlCompiler());
		else if ("string".equalsIgnoreCase(args[1]))
			rhythm.setOutput(Compiler.ToString);
		else
			System.out.println("Unknown output type: " + args[1]);
	}
	
	public static void main(String[] args) throws IOException {
		Rhythm r = new Rhythm(new Configuration(args));
		RhythmConsole rc = new RhythmConsole(r);
		rc.run();
	}
}
