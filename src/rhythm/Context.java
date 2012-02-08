package rhythm;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

public class Context extends Features {
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
	
	public boolean addDiscourseEntity(Token t) {
		return history.add(new DiscourseEntity(t));
	}
	
	public void reset() {
		history.clear();
		clearAllFeatures();
	}
}
