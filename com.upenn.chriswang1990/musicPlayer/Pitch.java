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
        switch (s) {
            case "A":
                return Pitch.A;
            case "B":
                return Pitch.B;
            case "C":
                return Pitch.C;
            case "D":
                return Pitch.D;
            case "E":
                return Pitch.E;
            case "F":
                return Pitch.F;
            case "G":
                return Pitch.G;
            case "R":
                return Pitch.R;
            default:
                return null;
        }
    }
}
