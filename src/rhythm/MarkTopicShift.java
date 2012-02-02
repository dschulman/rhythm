package rhythm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class MarkTopicShift extends Processor {
	private static final class Marker {
		public final TopicShiftType type;
		public final ImmutableList<String> words;
		
		public Marker(TopicShiftType type, Iterable<String> words) {
			this.type = type;
			this.words = ImmutableList.copyOf(words);
		}
	}
	
	private static final Ordering<Marker> CompareLength = new Ordering<Marker>() {
		public int compare(Marker lhs, Marker rhs) {
			return Ints.compare(lhs.words.size(), rhs.words.size());
		}
	};
	
	private final List<Marker> markers;
	
	public MarkTopicShift(Configuration conf) throws IOException {
		InputStream in = conf.openModelStream("discourse-markers", "discourse-markers");
		try {
			// sort so that we prefer the longest markers
			markers = CompareLength.reverse().sortedCopy(read(in));
		} finally {
			in.close();
		}
	}
	
	private Iterable<Marker> read(InputStream in) throws IOException {
		LineNumberReader r = new LineNumberReader(new InputStreamReader(in, "UTF8"));
		List<Marker> markers = Lists.newArrayList();
		String line;
		while ((line = r.readLine()) != null) {
			if (line.startsWith("#")) continue;
			String[] parts = line.split("\\s+");
			if (parts.length == 0) continue;
			if (parts.length==1)
				throw new IOException("Error in discourse markers file, line " + r.getLineNumber());
			try {
				markers.add(new Marker(
					TopicShiftType.valueOf(parts[0]),
					Arrays.asList(parts).subList(1, parts.length)));
			} catch (IllegalArgumentException e) {
				throw new IOException("Bad discourse marker type, line " + r.getLineNumber());
			}
		}
		return markers;
	}

	public void process(Sentence s) {
		// we assume markers occur only at the beginning of clauses
		// TODO is this heuristic language-specific?
		for (Interval clause : s.get(Features.CLAUSES))
			for (Marker m : markers)
				if (testMarker(s, clause, m))
					break;
	}

	private boolean testMarker(Sentence s, Interval clause, Marker m) {
		Iterator<String> mit = m.words.iterator();
		for (Token t : s.tokensIn(clause)) {
			if (!t.text().equalsIgnoreCase(mit.next()))
				break;
			if (!mit.hasNext()) {
				clause.put(Features.TOPIC_SHIFT, m.type);
				return true;
			}
		}
		return false;
	}
}
