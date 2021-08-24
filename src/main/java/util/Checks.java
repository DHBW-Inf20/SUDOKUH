package util;

/**
 *  Uninstantiable class with static utility methods for parameter checking.
 *
 * @author Luca Kellermann
 */
public final class Checks {

    public static int requireNonNegative(final int number, final String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    private Checks() {}
}
