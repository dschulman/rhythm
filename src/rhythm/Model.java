package rhythm;

public interface Model<P extends Enum<P>> {
	double value(Sentence s, P param);
}
