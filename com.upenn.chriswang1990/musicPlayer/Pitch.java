//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;/*

/**
 * A musicPlayer.Pitch represents a musical pitch. R represents a rest, no pitch.
 */
public enum Pitch {
	A, B, C, D, E, F, G, R;
	
	/**
	 * Returns the musicPlayer.Pitch that is equivalent to the given string,
	 * such as musicPlayer.Pitch.D for "D", or null if the string does not
	 * match any musicPlayer.Pitch value.
	 */
	public static Pitch getValueOf(String s) {
		s = s.intern();
		if (s.equals("A")) {
			return Pitch.A;
		}
		if (s.equals("B")) {
			return Pitch.B;
		}
		if (s.equals("C")) {
			return Pitch.C;
		}
		if (s.equals("D")) {
			return Pitch.D;
		}
		if (s.equals("E")) {
			return Pitch.E;
		}
		if (s.equals("F")) {
			return Pitch.F;
		}
		if (s.equals("G")) {
			return Pitch.G;
		}
		if (s.equals("R")) {
			return Pitch.R;
		}
		return null;
	}
}
