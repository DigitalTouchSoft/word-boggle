package dtsoft.main.wordboggle.util;

public class Alphas {
	public static final String[] TRUE_ALPHAS = new String[] {
		"a", "b", "c", "d",
		"e", "f", "g", "h",
		"i", "j", "k", "l",
		"m", "n", "o", "p",
		"q", "r", "s", "t",
		"u", "v", "w", "x",
		"y", "z" };
	public static final String[] VOWELS = new String[] {"a", "e", "i", "o", "u"};
	public static final String[] CONSONANT = new String[] {
		"b", "c", "d",
		"f", "g", "h",
		"j", "k", "l",
		"m", "n", "p",
		"q", "r", "s", 
		"t", "v", "w", 
		"x", "z" };	
	public static final int[][] ALPHASCORES = new int[][] { {3, 1}, {4, 1}, {5, 2}, {6, 3}, {7, 5}, {8, 11} };
	
	public static boolean isConsonant(String letter) {
		for(String con: CONSONANT) {
			if (letter.equalsIgnoreCase(con))
				return true;
		}
		return false;
	}
	
	public static class WeightedAlphas {
		public static final String[] WEIGHTED_ALPHAS = new String[] {
			"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	"D", 	
			"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	"E", 	
			"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	"F", 	
			"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	"G", 	
			"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	"A", 	
			"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	"B", 	
			"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	"C", 	
			"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	"L", 	
			"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	"M", 	
			"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	"N", 	
			"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	"O", 	
			"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	"H", 	
			"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	"I", 	
			"J", 	"J", 	
			"K", 	"K", 	"K", 	"K", 	"K", 	"K", 	"K", 	"K", 	"K", 	"K", 	
			"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	"U", 	
			"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	"T", 	
			"W", 	"W", 	"W", 	"W", 	"W", 	"W", 	"W", 	"W", 	
			"V", 	"V", 	"V", 	"V", 	"V", 	"V", 	"V", 	"V", 	"V", 	"V", 	
			"Qu", 	"Qu", 	
			"Q", 	"Q",
			"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	"P", 	
			"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	"S", 	
			"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	"R", 	
			"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	"Y", 	
			"X", 	"X", 	"X", 	
			"Z", 	"Z", 	"Z", 	"Z", 	"Z"};
	}
}
