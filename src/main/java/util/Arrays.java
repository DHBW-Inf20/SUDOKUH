package util;

import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.copyOf;

public final class Arrays {

    public static int[][] deepCopyOf(final int[][] original) {
        final int[][] copy = new int[original.length][];

        for (int i = 0; i < original.length; i++) {
            final int[] subArray = original[i];
            if (subArray == null) {
                copy[i] = null;
            } else {
                copy[i] = copyOf(subArray, subArray.length);
            }
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[][] twoLevelCopyOf(final T[][] original) {
        final Class<? extends Object[][]> originalClass = original.getClass();

        final T[][] copy = (originalClass == Object[][].class)
                ? (T[][]) new Object[original.length][]
                : (T[][]) newInstance(originalClass.getComponentType(), original.length);

        for (int i = 0; i < original.length; i++) {
            final T[] subArray = original[i];
            if (subArray == null) {
                copy[i] = null;
            } else {
                copy[i] = copyOf(subArray, subArray.length);
            }
        }
        return copy;
    }

    private Arrays() {}
}
