package rhythm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;

public class RhythmConsole {
	private Rhythm rhythm;
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
		else if ("context".startsWith(parts[0]))
			cmdContext(parts);
		else if ("generation".startsWith(parts[0]))
			cmdGeneration(parts);
		else if ("output".startsWith(parts[0]))
			cmdOutput(parts);
		else if ("reset".startsWith(parts[0]))
			context.reset();
		else
			System.out.println("unknown command: " + parts[0]);
		return true;
	}

	private void cmdContext(String[] args) {
		System.out.println(context);
	}

	private void cmdGeneration(String[] args) {
		if (args.length != 2)
			System.out.println("Usage: generation (basic|longitudinal)");
		else if ("basic".startsWith(args[1]))
			rhythm = rhythm.setGeneration(Rhythm.basicGeneration());
		else if ("longitudinal".startsWith(args[1]))
			rhythm = rhythm.setGeneration(Rhythm.longitudinalGeneration());
		else
			System.out.println("unknown generation type: " + args[1]);
	}
	
	private void cmdOutput(String[] args) {
		if (args.length != 2)
			System.out.println("Usage: output (beat|string)");
		else if ("beat".startsWith(args[1]))
			rhythm = rhythm.setOutput(new BeatXmlCompiler());
		else if ("string".startsWith(args[1]))
			rhythm = rhythm.setOutput(Compiler.ToString);
		else
			System.out.println("Unknown output type: " + args[1]);
	}

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration(args);
		Rhythm r = Rhythm.createStd(conf, Rhythm.basicGeneration());
		RhythmConsole rc = new RhythmConsole(r);
		rc.run();
	}
}
