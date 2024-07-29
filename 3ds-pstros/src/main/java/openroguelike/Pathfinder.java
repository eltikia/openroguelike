package openroguelike;

public class Pathfinder {
    private final boolean[] visited = new boolean[Level.ROWS * Level.COLUMNS];

    private final Level level;

    public Pathfinder(Level level) {
        super();
        this.level = level;
    }

    public boolean isTrapped() {
        Stairs stairs = level.getStairs();
        return !isPassable(stairs.getStairsDown());
    }

    private boolean isPassable(Tile current) {
        Stairs stairs = level.getStairs();
        boolean passed = current.equals(stairs.getStairsUp());

        if (!passed && !visited[current.getCoordinateX() * Level.ROWS + current.getCoordinateY()]
            && current.getType() != Tile.GLYPH_CAVE) {
            visited[current.getCoordinateX() * Level.ROWS + current.getCoordinateY()] = true;
            passed = canPass(current);
        }

        return passed;
    }

    private boolean canPass(Tile current) {
        boolean passed = false;

        for (int x = current.getCoordinateX() - 1; x <= current.getCoordinateX() + 1; x++) {

            for (int y = current.getCoordinateY() - 1; y <= current.getCoordinateY() + 1; y++) {
                Tile nextTile = level.getTile(x, y);

                if (nextTile != null && isPassable(nextTile)) {
                    passed = true;
                    break;
                }

            }

        }

        return passed;
    }
}
