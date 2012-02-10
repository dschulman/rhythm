package rhythm;

// Extra math functions not found in java.util.Math
public class MathUtil {
	private MathUtil() {}
	
	public static double logit(double p) { return Math.log(p/(1-p)); }
	public static double invLogit(double q) { return 1/(1+Math.exp(-q)); }
}
