package rhythm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Configuration {
	private final ImmutableMap<String, String> params;

	public Configuration(String... args) {
		this(parseArgs(args));
	}
	
	private static final Map<String, String> parseArgs(String[] args) {
		if (args.length % 2 == 1)
			throw new IllegalArgumentException();
		
		Map<String, String> params = Maps.newHashMap();
		for (int n=0; n<args.length; n+=2) {
			if (args[n].startsWith("--"))
				params.put(args[n].substring(2), args[n+1]);
			else
				throw new IllegalArgumentException();
		}
		return params;
	}
	
	public Configuration(Map<String, String> params) {
		this.params = ImmutableMap.copyOf(params);
	}

	public InputStream openModelStream(String key, String defaultName) throws FileNotFoundException {
		return new FileInputStream(modelPath(key, defaultName));
	}

	public File modelPath(String key, String defaultName) {
		return getPath("model-"+key).or(new File(modelDir(), defaultName));
	}
	
	private static final File DEFAULT_MODEL_DIR = new File(".");
	
	private File modelDir() {
		return getPath("models").or(DEFAULT_MODEL_DIR);
	}

	private Optional<File> getPath(String key) {
		String path = params.get(key);
		if (path!=null)
			return Optional.of(new File(path));
		else
			return Optional.absent();
	}

}
