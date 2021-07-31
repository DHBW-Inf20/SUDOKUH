package model;

import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Arrays.deepToString;
import static java.util.Objects.hash;
import static java.util.stream.Collectors.toSet;

public final class Killer extends AbstractSudoku {

    public static final record Group(int sum, Set<Cell> cells) {}

    public static final record GroupsUpdateResult(boolean isSuccess, List<Group> groups) {}


    public static final int SUB_GRID_SIZE = 3;
    public static final int GRID_SIZE = 9;
    public static final int MIN_GROUP_AMOUNT = 9; // group can have 9 cells at most -> at least 9 groups
    public static final int MAX_SUM = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9; // 45
    public static final int TOTAL_SUM = (1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9) * 9; // 405


    private final List<Group> groups = new ArrayList<>();
    private final Map<Group, Integer> groupIndices = new HashMap<>();
    private final Map<Cell, Group> groupsForCells = new HashMap<>(getNumberOfCells());


    public Killer() {
        super(SUB_GRID_SIZE);
    }

    // package-private for tests
    Killer(final int[][] grid, final List<Group> groups) {
        super(grid);
        this.groups.addAll(groups.stream().map(group -> new Group(group.sum, Set.copyOf(group.cells))).toList());
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
        return new GroupsUpdateResult(false, List.copyOf(groups));
    }

    private GroupsUpdateResult createGroupsUpdateSuccess() {
        return new GroupsUpdateResult(true, List.copyOf(groups));
    }

    public GroupsUpdateResult putCellIntoNewGroup(final int row, final int column, final int sum) {
        if (sum < 1 || sum > MAX_SUM) {
            return createGroupsUpdateFailure();
        }
        final Cell cell = new Cell(row, column);
        remove(cell);
        final Group group = new Group(sum, Set.of(cell));
        final int index = groups.size();
        groups.add(group);
        groupIndices.put(group, index);
        groupsForCells.put(cell, group);
        return createGroupsUpdateSuccess();
    }

    private void replaceGroup(final int index, final Group oldGroup, final Group newGroup) {
        groups.set(index, newGroup);
        groupIndices.remove(oldGroup);
        groupIndices.put(newGroup, index);
        for (final Cell cell : newGroup.cells) {
            groupsForCells.put(cell, newGroup);
        }
    }

    public GroupsUpdateResult putCellIntoExistingGroup(final int row, final int column, final Group group) {
        if (groupIndices.get(group) == null) {
            return createGroupsUpdateFailure();
        }
        final Cell cell = new Cell(row, column);
        remove(cell);
        final Set<Cell> newCells = new HashSet<>(group.cells);
        newCells.add(cell);
        replaceGroup(groupIndices.get(group), group, new Group(group.sum, newCells));
        return createGroupsUpdateSuccess();
    }

    public List<Group> getGroups() {
        return List.copyOf(groups);
    }

    @Nullable
    public Group getGroupForCell(final int row, final int column) {
        return groupsForCells.get(new Cell(row, column));
    }

    public GroupsUpdateResult setSumForGroup(final Group group, final int sum) {
        if (sum < 1 || sum > MAX_SUM) {
            return createGroupsUpdateFailure();
        }
        final Integer index = groupIndices.get(group);
        if (index == null) {
            return createGroupsUpdateFailure();
        }
        replaceGroup(index, group, new Group(sum, group.cells));
        return createGroupsUpdateSuccess();
    }

    public GroupsUpdateResult removeCellFromGroup(final int row, final int column) {
        return remove(new Cell(row, column)) ? createGroupsUpdateSuccess() : createGroupsUpdateFailure();
    }

    private boolean remove(final Cell cell) {
        final Group group = groupsForCells.get(cell);
        if (group == null) {
            return false;
        } else if (group.cells.size() <= 1) {
            return remove(group);
        }

        final Set<Cell> newCells = group.cells.stream().filter(c -> !c.equals(cell)).collect(toSet());
        final Group newGroup = new Group(group.sum, newCells);
        final int index = groupIndices.get(group);
        groups.set(index, newGroup);
        for (final Cell c : newCells) {
            groupsForCells.put(c, newGroup);
        }
        return true;
    }

    public GroupsUpdateResult removeGroup(final Group group) {
        return remove(group) ? createGroupsUpdateSuccess() : createGroupsUpdateFailure();
    }

    private boolean remove(final Group group) {
        final Integer index = groupIndices.get(group);
        if (index == null) {
            return false;
        }
        groups.remove(index.intValue());
        groupIndices.remove(group);
        for (final Cell cell : group.cells) {
            groupsForCells.remove(cell);
        }
        for (int i = index; index < groups.size(); i++) {
            final Group g = groups.get(i);
            groupIndices.put(g, groupIndices.get(g) - 1);
        }
        return true;
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        final Cell currentCell = new Cell(row, column);
        final Group group = groupsForCells.get(currentCell);

        if (group.cells.isEmpty()) {
            return conflicts;
        }

        final boolean[] occurrences = new boolean[gridSize];
        int sum = 0;
        boolean hasEmptyCells = false;

        for (final Cell cell : group.cells) {
            final int cellValue = grid[cell.row()][cell.column()];

            if (cellValue == EMPTY_CELL) {
                hasEmptyCells = true;

            } else {
                sum += cellValue;

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
        return true;
    }

    @Override
    protected boolean isInvalid() {

        if (groups.size() < MIN_GROUP_AMOUNT || super.isInvalid()) {
            return true; // grid is invalid
        }

        final boolean[][] occurrences = new boolean[gridSize][gridSize];
        int totalSum = 0;

        for (final Group group : groups) {
            if (group.sum < 1 || group.sum > MAX_SUM || group.cells.isEmpty()) {
                return true; // sum is not in valid range / group is empty
            }
            totalSum += group.sum;
            for (Cell cell : group.cells) {
                if (occurrences[cell.row()][cell.column()]) {
                    return true; // cell twice in group or in two groups
                }
                occurrences[cell.row()][cell.column()] = true;
            }
        }

        if (totalSum != TOTAL_SUM) {
            return true; // wrong total sum
        }

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
