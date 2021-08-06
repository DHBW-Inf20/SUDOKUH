package util;

public class Checks {

    public static int requireNonNegative(final int number, final String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    private Checks() {}
}
