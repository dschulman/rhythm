package rhythm;

import java.util.Map;

import com.google.common.collect.Maps;

public class Features {
	private final Map<Feature<?>, Object> features = Maps.newHashMap();
	
	@SuppressWarnings("unchecked")
	public <T> T put(Feature<T> key, T value) {
		return (T) features.put(key, value);
	}
	
	public <T> void maybePut(Feature<T> key, T value) {
		if (value != null)
			put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Feature<T> key) {
		return (T) features.get(key);
	}
	
	public boolean put(Feature<Void> key) {
		boolean wasSet = features.containsKey(key);
		features.put(key, null);
		return wasSet;
	}
	
	public boolean is(Feature<Void> key) {
		return features.containsKey(key);
	}
	
	@Override
	public String toString() {
		return features.toString();
	}
	
	public static final Feature<String> TAG = Feature.named("TAG");
	public static final Feature<String> LEMMA = Feature.named("LEMMA");
	public static final Feature<WordClass> CLASS = Feature.named("CLASS");
	public static final Feature<WordNumber> NUMBER = Feature.named("NUMBER");
	public static final Feature<WordPerson> PERSON = Feature.named("PERSON");
	public static final Feature<PhraseType> PHRASE_TYPE = Feature.named("PHRASE_TYPE");
	public static final Feature<Void> NEW = Feature.named("NEW");
	
	public static final Feature<Intervals> PHRASES = Feature.named("PHRASES");
	public static final Feature<Intervals> CLAUSES = Feature.named("CLAUSES");
	public static final Feature<Intervals> THEMES = Feature.named("THEMES");
	public static final Feature<Intervals> RHEMES = Feature.named("RHEMES");
}
