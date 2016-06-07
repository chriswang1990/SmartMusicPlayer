//Project authors: Minquan Wang & Guanqing Hao

package musicPlayer;/*
 * Melody Player
 *
 * Authors: Allison Obourn and Marty Stepp
 * Version: Tue 2015/03/04
 * 
 * This instructor-provided file represents a musical accidental:
 * sharp, flat, or natural.
 */

/**
 * An musicPlayer.Accidental represents a musical accidental: sharp, flat, or natural.
 */
public enum Accidental {	
	SHARP, NATURAL, FLAT;

	/**
	 * Returns the musicPlayer.Accidental that is equivalent to the given string,
	 * such as musicPlayer.Accidental.SHARP for "SHARP", or null if the string does not
	 * match any musicPlayer.Accidental value.
	 */
	public static Accidental getValueOf(String s) {
		s = s.intern();
		if (s == "SHARP") {
			return Accidental.SHARP;
		}
		if (s == "FLAT") {
			return Accidental.FLAT;
		}
		if (s == "NATURAL") {
			return Accidental.NATURAL;
		}
		return null;
	}
}
