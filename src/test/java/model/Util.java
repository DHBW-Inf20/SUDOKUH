package model;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;

class Util {

    static List<Arguments> allCellRowsAndColumnsForGridWithSize(final int gridSize) {
        final List<Arguments> cellRowsAndColumnsArguments = new ArrayList<>();
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                cellRowsAndColumnsArguments.add(Arguments.of(row, column));
            }
        }
        return cellRowsAndColumnsArguments;
    }

    private Util() {}
}
