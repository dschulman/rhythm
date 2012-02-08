package rhythm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;

public class RhythmConsole {
	private final Rhythm rhythm;
	private final Context context;
	private final BufferedReader reader;

	public RhythmConsole(Rhythm rhythm) throws IOException {
		this.rhythm = rhythm;
		this.context = new Context();
		this.reader = new BufferedReader(
			new InputStreamReader(System.in, Charsets.UTF_8));
	}

	public void run() throws IOException {
		String input;
		while ((input = readLine()) != null)
			if (!processAsCommand(input))
				for (String output : rhythm.process(context, input))
					System.out.println(output);
	}

	private String readLine() throws IOException {
		System.out.print("> ");
		System.out.flush();
		return reader.readLine();
	}
	
	private boolean processAsCommand(String input) {
		input = input.trim();
		if (!input.startsWith(":"))
			return false;

		input = input.substring(1).trim();
		String[] parts = input.split("\\s+");
		if (parts.length == 0)
			System.out.println("missing command!");
		else if (parts[0].equalsIgnoreCase("context"))
			cmdContext(parts);
		else if (parts[0].equalsIgnoreCase("output"))
			cmdOutput(parts);
		else if (parts[0].equalsIgnoreCase("reset"))
			context.reset();
		else
			System.out.println("unknown command: " + parts[0]);
		return true;
	}

	private void cmdContext(String[] args) {
		System.out.println(context);
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
