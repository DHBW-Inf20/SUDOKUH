package util;

import static java.util.Arrays.copyOf;

public class ArrayUtilities {

    public static int[][] deepCopyOf(final int[][] grid) {
        final int[][] result = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            result[i] = copyOf(grid[i], grid[i].length);
        }
        return result;
    }

    private ArrayUtilities() {}
}
