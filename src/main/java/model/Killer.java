package model;

import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Arrays.deepToString;
import static java.util.Collections.unmodifiableSet;
import static java.util.List.copyOf;
import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

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
    }

    /**
     * Result of group update operations like
     * {@link #putCellIntoExistingGroup(int, int, Group) putCellIntoExistingGroup()}.
     *
     * @param isSuccess whether the update operation was successful
     * @param groups    a {@link List} of {@link Group groups} that represent the state of all groups of a Killer after
     *                  the update operation (will be the same as before if {@code isSuccess} is {@code false})
     */
    public static final record GroupsUpdateResult(boolean isSuccess, List<Group> groups) {}


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


    private final List<Group> groups = new ArrayList<>();
    private final Map<Group, Integer> groupIndices = new HashMap<>();
    private final Map<Cell, Group> groupsForCells = new HashMap<>(getNumberOfCells());


    /**
     * Creates an empty Killer with {@link #SUB_GRID_SIZE} for its {@link #getSubGridSize() subGridSize} and
     * {@link #GRID_SIZE} for its {@link #getGridSize() gridSize}.
     */
    public Killer() {
        super(SUB_GRID_SIZE);
    }

    // package-private constructor for tests
    Killer(final int[][] grid, final List<Group> groups) {
        super(grid);
        this.groups.addAll(copyOf(groups)); // shallow copy ok, Group and Group#cells are immutable
        int index = 0;
        for (final Group group : this.groups) {
            groupIndices.put(group, index++);
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


    private GroupsUpdateResult createGroupsUpdateFailure() {
        return new GroupsUpdateResult(false, copyOf(groups));
    }

    private GroupsUpdateResult createGroupsUpdateSuccess() {
        return new GroupsUpdateResult(true, copyOf(groups));
    }

    private void updateGroup(final int groupIndex, final Group newGroup) {

        // replace in list and update index in map
        final Group oldGroup = groups.set(groupIndex, newGroup);
        groupIndices.remove(oldGroup);
        groupIndices.put(newGroup, groupIndex);

        // update group for all cells in newGroup (does not update them for oldGroup)
        for (final Cell cell : newGroup.cells) {
            groupsForCells.put(cell, newGroup);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean allCellsAreConnected(final Set<Cell> cells) {

        // a single cell is always connected
        final int cellCount = cells.size();
        if (cellCount <= 1) {
            return true;
        }

        final HashSet<Cell> connectedCells = new HashSet<>(cellCount);
        findConnectedCellsWithDepthFirstSearch(cells.iterator().next(), cells, connectedCells);
        return connectedCells.size() == cellCount;
    }

    private void findConnectedCellsWithDepthFirstSearch(final Cell cell, final Set<Cell> allCells,
                                                        final HashSet<Cell> connectedCells) {
        connectedCells.add(cell);

        for (int i = -2; i < 2; i++) {
            final int rowDelta = i % 2;          // these two values will produce the following tuples:
            final int columnDelta = (i + 1) % 2; // (0,-1); (-1,0); (0,1), (1,0)

            final Cell neighbor = new Cell(cell.row() + rowDelta, cell.column() + columnDelta);
            if (allCells.contains(neighbor) && !connectedCells.contains(neighbor)) {
                findConnectedCellsWithDepthFirstSearch(neighbor, allCells, connectedCells);
            }
        }
    }

    private boolean remove(final Cell cell) {

        final Group group = groupsForCells.get(cell);
        if (group == null) { // cell is in no group
            return false;
        } else if (group.cells.size() <= 1) { // group only consists of cell -> remove whole group
            return remove(group);
        }

        // keep all cells except cell
        final Set<Cell> newCells = group.cells.stream().filter(c -> !c.equals(cell)).collect(toSet());

        if (!allCellsAreConnected(newCells)) {
            return false;
        }

        final Group newGroup = new Group(unmodifiableSet(newCells), group.sum);

        // replace group with newGroup and update maps
        groupsForCells.remove(cell);
        groups.set(groupIndices.get(group), newGroup);
        for (final Cell c : newCells) {
            groupsForCells.put(c, newGroup);
        }

        return true;
    }

    private boolean remove(final Group group) {

        final Integer index = groupIndices.get(group);
        if (index == null) {
            return false;
        }

        // remove all references to group
        groups.remove(index.intValue()); // remove(index) would try to remove the index object
        groupIndices.remove(group);
        for (final Cell cell : group.cells) {
            groupsForCells.remove(cell);
        }

        // update every groupIndex >= index in the groupIndices map
        for (int groupIndex = index; index < groups.size(); groupIndex++) {
            groupIndices.put(groups.get(groupIndex), groupIndex);
        }

        return true;
    }

    /**
     * Returns an unmodifiable snapshot of the groups in this Killer.
     */
    public List<Group> getGroups() {
        return copyOf(groups);
    }

    /**
     * Returns the {@link Group group} that the cell in the specified {@code row} and {@code column} is part of or
     * {@code null} if the cell is in no group.
     */
    @Nullable
    public Group getGroupForCell(final int row, final int column) {
        return groupsForCells.get(new Cell(row, column));
    }

    /**
     * Trys to put the cell in the specified {@code row} and {@code column} into a new {@link Group group} and removes
     * it from the group it was previously part of (if any).
     * <p>This will not work if {@code sum} is not in the valid range from {@code 1} to
     * {@link Group#MAX_SUM Group#MAX_SUM} (both inclusive) or the removal of the cell from its previous group would
     * lead to a group with unconnected cells.</p>
     *
     * @param sum the {@link Group#sum sum} that the newly created {@link Group group} is supposed to have
     * @return a {@link GroupsUpdateResult GroupsUpdateResult}
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult putCellIntoNewGroup(final int row, final int column, final int sum) {

        // check for invalid sum
        if (sum < 1 || sum > Group.MAX_SUM) {
            return createGroupsUpdateFailure();
        }

        final Cell cell = new Cell(row, column);
        if (!remove(cell)) { // remove from previous group -> if not successful return failure
            return createGroupsUpdateFailure();
        }

        final Group group = new Group(Set.of(cell), sum);
        final int index = groups.size(); // will be added to end of groups

        // add the new group and update maps
        groups.add(group);
        groupIndices.put(group, index);
        groupsForCells.put(cell, group);

        return createGroupsUpdateSuccess();
    }

    /**
     * Trys to put the cell in the specified {@code row} and {@code column} into an existing {@link Group group} and
     * removes it from the group it was previously part of (if any).
     * <p>This will not work if {@code group} is no longer part of this Killer (also happens after updates to the
     * group) or the removal of the cell from its previous group / addition of the cell to {@code group} would lead to a
     * group with unconnected cells.</p>
     *
     * @return a {@link GroupsUpdateResult GroupsUpdateResult}
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult putCellIntoExistingGroup(final int row, final int column, final Group group) {

        // was removed, updated or did never exist
        if (groupIndices.get(group) == null) {
            return createGroupsUpdateFailure();
        }

        final Cell cell = new Cell(row, column);
        if (!remove(cell)) { // remove from previous group -> if not successful return failure
            return createGroupsUpdateFailure();
        }

        // add the cell to a copy of the group and replace the group
        final Set<Cell> newCells = new HashSet<>(group.cells);
        newCells.add(cell);
        if (!allCellsAreConnected(newCells)) {
            return createGroupsUpdateFailure();
        }
        // remove might have changed groupIndices -> get index again
        updateGroup(groupIndices.get(group), new Group(unmodifiableSet(newCells), group.sum));

        return createGroupsUpdateSuccess();
    }

    /**
     * Trys to set the sum of the specified {@code group} to {@code sum}.
     * <p>This will not work if {@code sum} is not in the valid range from {@code 1} to
     * {@link Group#MAX_SUM Group#MAX_SUM} (both inclusive) or if {@code group} is no longer part of this Killer (also
     * happens after updates to the group).</p>
     *
     * @return a {@link GroupsUpdateResult GroupsUpdateResult}
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult setSumForGroup(final Group group, final int sum) {

        // check for invalid sum
        if (sum < 1 || sum > Group.MAX_SUM) {
            return createGroupsUpdateFailure();
        }

        final Integer index = groupIndices.get(group);
        if (index == null) { // was removed, updated or did never exist
            return createGroupsUpdateFailure();
        }

        // replace with copy with updated sum
        updateGroup(index, new Group(group.cells, sum));

        return createGroupsUpdateSuccess();
    }

    /**
     * Trys to remove the cell in the specified {@code row} and {@code column} from the {@link Group group} it is part
     * of.
     * <p>This will not work if the cell is in no group or the removal of the cell would lead to a group with
     * unconnected cells.</p>
     *
     * @return a {@link GroupsUpdateResult GroupsUpdateResult}
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult removeCellFromGroup(final int row, final int column) {
        return remove(new Cell(row, column)) ? createGroupsUpdateSuccess() : createGroupsUpdateFailure();
    }

    /**
     * Trys to remove the specified {@code group}.
     * <p>This will not work if the group is no longer part of this Killer.</p>
     *
     * @return a {@link GroupsUpdateResult GroupsUpdateResult}
     * @see GroupsUpdateResult GroupsUpdateResult
     */
    public GroupsUpdateResult removeGroup(final Group group) {
        return remove(group) ? createGroupsUpdateSuccess() : createGroupsUpdateFailure();
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        final Cell currentCell = new Cell(row, column);
        final Group group = groupsForCells.get(currentCell);

        // an empty group cannot have any more conflicts
        if (group.cells.isEmpty()) {
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
     * (both inclusive), every cell is part of exactly one {@link Group group}, no group is empty, the
     * {@link Group#sum sum} of each group is in the valid range from {@code 1} to {@link Group#MAX_SUM Group#MAX_SUM}
     * (both inclusive), all cells in a group are connected, the total sum of all group sums is the same as
     * {@link #TOTAL_SUM}, there are at least {@link #MIN_GROUP_AMOUNT} groups and there are no
     * {@link #getConflictingCells(int, int, boolean) conflicts}.
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

            if (group.sum < 1
                    || group.sum > Group.MAX_SUM // sum is not in valid range
                    || group.cells.isEmpty() // group is empty
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
