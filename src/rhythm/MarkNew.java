package rhythm;

import static rhythm.WordClass.Adjective;
import static rhythm.WordClass.Noun;
import static rhythm.WordClass.Verb;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class MarkNew implements Processor {
	private static final class DiscourseEntity {
		private final String lemma;
		private final WordNumber number;
		
		public DiscourseEntity(Token t) {
			String lemma = t.get(Features.LEMMA);
			this.lemma = lemma!=null ? lemma : t.text();
			this.number = t.get(Features.NUMBER);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DiscourseEntity))
				return false;
			DiscourseEntity de = (DiscourseEntity) obj;
			return lemma.equals(de.lemma) && (number==de.number);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(lemma, number);
		}
	}
	
	private final Set<DiscourseEntity> history = Sets.newHashSet();
	
	@Override
	public void process(Sentence s) {
		for (Token t : s.tokens()) {
			WordClass wc = t.get(Features.CLASS);
			if ((wc==Noun) || (wc==Verb) || (wc==Adjective))
				if (history.add(new DiscourseEntity(t)))
					t.put(Features.NEW);
		}
	}

}
