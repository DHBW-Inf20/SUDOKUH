package model;

import model.Killer.GroupsUpdateResult.FailureReason;

import java.util.*;

import static java.util.Arrays.deepToString;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;
import static java.util.Set.copyOf;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

/**
 * Concrete final implementation of {@link AbstractSudoku} with the additional constraint of groups of cells that must
 * have a certain sum and consist of unique values.
 * <p>
 * This class provides the following public interface in addition to {@link AbstractSudoku}:
 * <ul>
 * <li>{@link #SUB_GRID_SIZE}, {@link #GRID_SIZE}, {@link #MIN_GROUP_AMOUNT} and {@link #TOTAL_SUM} - constants</li>
 * <li>
 * {@link Group Group}, {@link GroupsUpdateResult GroupsUpdateResult}, {@link #getGroups()},
 * {@link #getGroupForCell(int, int) getGroupForCell()}, {@link #putCellsIntoNewGroup(Set, int) putCellsIntoNewGroup()}
 * and {@link #removeGroup(Group) removeGroup()} - for managing the groups of a Killer
 * </li>
 * </ul>
 *
 * @author Luca Kellermann
 */
public final class Killer extends AbstractSudoku {

    /**
     * A group in a Killer.
     * <p>Groups are defined by:</p>
     *
     * @param cells a {@link Set} of all cells that are part of this group
     * @param sum   the sum that all cell values of {@code cells} must have in the solved state
     */
    public static final record Group(Set<Cell> cells, int sum) {

        /**
         * The maximum {@link #sum} that a Group is allowed to have.
         */
        public static final int MAX_SUM = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9; // 45, no duplicates allowed

        /**
         * The maximum amount of cells a Group is allowed to have.
         */
        public static final int MAX_CELLS = 9;
    }

    /**
     * Result of group update operations like {@link #putCellsIntoNewGroup(Set, int) putCellsIntoNewGroup()}.
     *
     * @param isSuccess     whether the update operation was successful
     * @param failureReason the {@link FailureReason reason} why this update operation failed, {@code null} otherwise
     * @param groups        a {@link Set} of {@link Group groups} that represent the state of all groups of a Killer
     *                      after the update operation (will be the same as before if {@code isSuccess} is
     *                      {@code false})
     */
    public static final record GroupsUpdateResult(boolean isSuccess, FailureReason failureReason, Set<Group> groups) {

        /**
         * Reason why a group update operation failed.
         */
        public enum FailureReason {
            GROUP_NOT_PART_OF_KILLER,
            GROUP_VALUES_NOT_UNIQUE,
            GROUP_SUM_NOT_VALID,
            GROUP_IS_EMPTY,
            GROUP_HAS_TOO_MANY_CELLS,
            GROUP_CELLS_ARE_NOT_CONNECTED,
        }
    }


    public static final int SUB_GRID_SIZE = 3;
    public static final int GRID_SIZE = 9;

    /**
     * The minimum amount of {@link Group groups} that a Killer needs to have to be valid (but not all Killers with this
     * amount of groups are valid).
     */
    public static final int MIN_GROUP_AMOUNT = 9; // group can have 9 cells at most -> at least 9 groups

    /**
     * The sum of {@link Group#sum sums} that all {@link #getGroups() groups} of a Killer must have for the Killer to be
     * valid.
     */
    public static final int TOTAL_SUM = (1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9) * 9; // 405


    private final Set<Group> groups = new HashSet<>();
    private final Map<Cell, Group> groupsForCells = new HashMap<>(getNumberOfCells());


    /**
     * Creates an empty Killer with {@link #SUB_GRID_SIZE} for its {@link #getSubGridSize() subGridSize} and
     * {@link #GRID_SIZE} for its {@link #getGridSize() gridSize}.
     */
    public Killer() {
        super(SUB_GRID_SIZE);
    }

    // package-private constructor for tests
    Killer(final int[][] grid, final Set<Group> groups) {
        super(grid);
        this.groups.addAll(groups);
        for (final Group group : this.groups) {
            for (final Cell cell : group.cells) {
                groupsForCells.put(cell, group);
            }
        }
        if (gridSize != GRID_SIZE || isInvalid()) {
            throw new IllegalArgumentException("Input killer is not valid!");
        }
    }


    @Override
    protected Killer getCopy() {
        return new Killer(grid, groups);
    }


    private static int getMinSumForGroupWithNCells(final int n) {
        return (n * (n + 1)) / 2; // Gauss' method
    }

    private static int getMaxSumForGroupWithNCells(final int n) {
        return Group.MAX_SUM - getMinSumForGroupWithNCells(GRID_SIZE - n);
    }

    private FailureReason reasonWhyGroupIsInvalid(final Group group) {
        return group.cells.isEmpty()
                ? FailureReason.GROUP_IS_EMPTY
                : reasonWhyGroupIsInvalidWithoutCells(group, emptySet());
    }

    private FailureReason reasonWhyGroupIsInvalidWithoutCells(final Group group, final Set<Cell> withoutCells) {

        final Set<Cell> cells = group.cells.stream().filter(not(withoutCells::contains)).collect(toSet());

        if (cells.size() > Group.MAX_CELLS) {
            return FailureReason.GROUP_HAS_TOO_MANY_CELLS;
        }

        if (group.sum < getMinSumForGroupWithNCells(cells.size())
                || group.sum > getMaxSumForGroupWithNCells(cells.size())) {
            return FailureReason.GROUP_SUM_NOT_VALID;
        }

        if (!allCellsAreConnected(cells)) {
            return FailureReason.GROUP_CELLS_ARE_NOT_CONNECTED;
        }

        // occurrences[n] == true -> cell value n + 1 already occurred in this group
        final boolean[] occurrences = new boolean[gridSize];
        int sum = 0;
        boolean hasEmptyCells = false;

        // calculate sum and check for double occurrences
        for (final Cell cell : cells) {
            final int cellValue = grid[cell.row()][cell.column()];

            if (cellValue == EMPTY_CELL) {
                hasEmptyCells = true;

            } else {
                sum += cellValue;

                // cellValue already occurred in this group
                if (occurrences[cellValue - 1]) {
                    return FailureReason.GROUP_VALUES_NOT_UNIQUE;
                }
                occurrences[cellValue - 1] = true;
            }
        }

        final boolean sumIsValid = (hasEmptyCells && sum < group.sum) || (!hasEmptyCells && sum == group.sum);

        return sumIsValid ? null : FailureReason.GROUP_SUM_NOT_VALID;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean allCellsAreConnected(final Set<Cell> cells) {
        return allCellsAreConnectedWithoutCells(cells, emptySet());
    }

    private static boolean allCellsAreConnectedWithoutCells(Set<Cell> cells, final Set<Cell> withoutCells) {

        cells = cells.stream().filter(not(withoutCells::contains)).collect(toSet());

        // a single cell is always connected
        final int cellCount = cells.size();
        if (cellCount <= 1) {
            return true;
        }

        final HashSet<Cell> connectedCells = new HashSet<>(cellCount);
        findConnectedCellsWithDepthFirstSearch(cells.iterator().next(), cells, connectedCells);
        return connectedCells.size() == cellCount;
    }

    private static void findConnectedCellsWithDepthFirstSearch(final Cell cell, final Set<Cell> allCells,
                                                               final HashSet<Cell> connectedCells) {
        connectedCells.add(cell);

        for (int i = -2; i < 2; i++) {
            final int rowDelta = i % 2;          // these two values will produce the following tuples:
            final int columnDelta = (i + 1) % 2; // (0,-1); (-1,0); (0,1); (1,0)

            final Cell neighbor = new Cell(cell.row() + rowDelta, cell.column() + columnDelta);
            if (allCells.contains(neighbor) && !connectedCells.contains(neighbor)) {
                findConnectedCellsWithDepthFirstSearch(neighbor, allCells, connectedCells);
            }
        }
    }

    private void remove(final Group group) {
        // remove all references to group
        groups.remove(group);
        for (final Cell cell : group.cells) {
            groupsForCells.remove(cell);
        }
    }

    /**
     * Returns an unmodifiable snapshot of the groups in this Killer.
     */
    public Set<Group> getGroups() {
        return copyOf(groups);
    }

    private GroupsUpdateResult newGroupsUpdateFailure(final FailureReason failureReason) {
        return new GroupsUpdateResult(false, failureReason, getGroups());
    }

    private GroupsUpdateResult newGroupsUpdateSuccess() {
        return new GroupsUpdateResult(true, null, getGroups());
    }

    /**
     * Returns the {@link Group group} that the cell in the specified {@code row} and {@code column} is part of or
     * {@code null} if the cell is in no group.
     */
    public Group getGroupForCell(final int row, final int column) {
        return groupsForCells.get(new Cell(row, column));
    }

    /**
     * Trys to put the specified {@link Set} of {@link AbstractPuzzle.Cell cells} into a new {@link Group group}
     * and removes them from the groups they were previously part of (if any).
     *
     * @param sum the {@link Group#sum sum} that the newly created {@link Group group} is supposed to have
     * @return a {@link GroupsUpdateResult GroupsUpdateResult} that provides information about the result of this
     * operation
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult putCellsIntoNewGroup(final Set<Cell> cells, final int sum) {

        final Group newGroup = new Group(copyOf(cells), sum);
        FailureReason reason = reasonWhyGroupIsInvalid(newGroup);
        if (reason != null) {
            return newGroupsUpdateFailure(reason);
        }

        final Set<Group> groups = cells.stream().map(groupsForCells::get).filter(Objects::nonNull).collect(toSet());

        for (final Group group : groups) {
            // check if old groups would be valid without cells
            reason = reasonWhyGroupIsInvalidWithoutCells(group, cells);
            if (reason != null) {
                return newGroupsUpdateFailure(reason);
            }
        }

        // remove cells after checking if valid
        for (final Group group : groups) {

            final Set<Cell> oldCells = group.cells;
            final Set<Cell> updatedCells = oldCells.stream().filter(not(cells::contains)).collect(toSet());

            if (updatedCells.isEmpty()) {
                remove(group);
            } else {
                final Group updatedGroup = new Group(unmodifiableSet(updatedCells), group.sum);

                // replace old group with newGroup
                this.groups.remove(group);
                this.groups.add(updatedGroup);
                for (final Cell cell : updatedCells) {
                    groupsForCells.put(cell, updatedGroup);
                }

                // remove group mappings for removed cells
                oldCells.stream().filter(cells::contains).forEach(groupsForCells::remove);
            }
        }

        this.groups.add(newGroup);
        for (final Cell cell : cells) {
            groupsForCells.put(cell, newGroup);
        }

        return newGroupsUpdateSuccess();
    }

    /**
     * Trys to remove the specified {@code group}.
     * <p>This will not work if the group is no longer part of this Killer.</p>
     *
     * @return a {@link GroupsUpdateResult GroupsUpdateResult} that provides information about the result of this
     * operation
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult removeGroup(final Group group) {
        if (groups.contains(group)) {
            remove(group);
            return newGroupsUpdateSuccess();
        } else {
            return newGroupsUpdateFailure(FailureReason.GROUP_NOT_PART_OF_KILLER);
        }
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        final Cell currentCell = new Cell(row, column);
        final Group group = groupsForCells.get(currentCell);

        // no group / an empty group cannot have any more conflicts
        if (group == null || group.cells.isEmpty()) {
            return conflicts;
        }

        // occurrences[n] == true -> cell value n + 1 already occurred in this group
        final boolean[] occurrences = new boolean[gridSize];
        int sum = 0;
        boolean hasEmptyCells = false;

        // calculate sum and check for double occurrences
        for (final Cell cell : group.cells) {
            final int cellValue = grid[cell.row()][cell.column()];

            if (cellValue == EMPTY_CELL) {
                hasEmptyCells = true;

            } else {
                sum += cellValue;

                // cellValue already occurred in this group
                if (occurrences[cellValue - 1]) {

                    if (cell.row() == row && cell.column() == column) {
                        // there is exactly one other cell with same number (other would have been rejected before)
                        // -> find it with findFirst()
                        conflicts.add(group.cells.stream()
                                .filter(c -> (c.row() != row || c.column() != column)
                                        && grid[c.row()][c.column()] == cellValue)
                                .findFirst().orElseThrow());
                    } else {
                        conflicts.add(cell);
                    }

                    if (!getAll) {
                        return conflicts;
                    }

                } else {
                    occurrences[cellValue - 1] = true;
                }
            }
        }

        if ((hasEmptyCells && sum >= group.sum) || (!hasEmptyCells && sum != group.sum)) {

            if (!getAll) {
                if (group.cells.size() > 1) {
                    conflicts.add(group.cells.stream()
                            .filter(c -> c.row() != row || c.column() != column)
                            .findFirst().orElseThrow());
                } else {
                    conflicts.add(group.cells.iterator().next());
                }
                return conflicts;
            }

            conflicts.addAll(group.cells);
            if (group.cells.size() > 1) {
                conflicts.remove(currentCell); // don't include cell that is being checked
            }
        }

        return conflicts;
    }


    @Override
    protected boolean hasToValidateBeforeSolve() {
        // All cells need to be part of a group in a valid Killer.
        // Since this won't be the case in the beginning, it should be validated before solving.
        return true;
    }

    /**
     * Returns {@code false} if and only if {@link #grid} is a square with {@link #gridSize} {@code *} {@link #gridSize}
     * cells, every cell is either {@link #EMPTY_CELL} or in the valid range from {@code 1} to {@link #gridSize}
     * (both inclusive), every cell is part of exactly one {@link Group group}, no group is empty, all groups have at
     * most {@link Group#MAX_CELLS Group#MAX_CELLS} cells, the {@link Group#sum sum} of each group is in the valid range
     * from {@link #getMinSumForGroupWithNCells(int) getMinSumForGroupWithNCells()} to
     * {@link #getMaxSumForGroupWithNCells(int) getMaxSumForGroupWithNCells()} (both inclusive), all cells in a group
     * are connected, the total sum of all group sums is the same as {@link #TOTAL_SUM}, there are at least
     * {@link #MIN_GROUP_AMOUNT} groups and there are no {@link #getConflictingCells(int, int, boolean) conflicts}.
     */
    @Override
    protected boolean isInvalid() {

        if (groups.size() < MIN_GROUP_AMOUNT || super.isInvalid()) {
            return true; // not enough groups or grid is invalid
        }

        // occurrences[row][column] == true -> cell in row and column is part of a group
        final boolean[][] occurrences = new boolean[gridSize][gridSize];
        int totalSum = 0;

        // register occurrences and calculate totalSum
        for (final Group group : groups) {

            if (group.cells.isEmpty() // group is empty
                    || group.cells.size() > Group.MAX_CELLS // too many cells
                    || group.sum < getMinSumForGroupWithNCells(group.cells.size())
                    || group.sum > getMaxSumForGroupWithNCells(group.cells.size()) // sum is not in valid range
                    || !allCellsAreConnected(group.cells)) { // cells are not connected
                return true;
            }

            totalSum += group.sum;
            for (final Cell cell : group.cells) {
                if (occurrences[cell.row()][cell.column()]) {
                    return true; // cell twice in group or in two groups
                }
                occurrences[cell.row()][cell.column()] = true;
            }
        }

        if (totalSum != TOTAL_SUM) {
            return true; // wrong total sum
        }

        // check for every cell to be in a group
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (!occurrences[row][column]) {
                    return true; // cell is in no group
                }
            }
        }

        return false; // no rule was violated -> valid
    }


    @Override
    protected boolean isEqualTo(final AbstractPuzzle other) {
        return super.isEqualTo(other) && this.groups.equals(((Killer) other).groups);
    }

    @Override
    public int hashCode() {
        return hash(super.hashCode(), groups);
    }

    @Override
    public String toString() {
        return "Killer{" +
                "subGridSize=" + subGridSize +
                ", gridSize=" + gridSize +
                ", grid=" + deepToString(grid) +
                ", groups=" + groups +
                '}';
    }
}
