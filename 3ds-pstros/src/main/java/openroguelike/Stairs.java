package openroguelike;

public class Stairs {
    private static final int SAFE_DISTANCE = 20;
    private Tile[] emptyTiles;
    private Tile stairsDown;
    private Tile stairsUp;

    private int top = -1;

    public Stairs() {
        super();
    }

    public Stairs(Tile[] tiles) {
        emptyTiles = new Tile[tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];

            if (tile.getType() == Tile.GLYPH_EMPTY) {
                // tile type: empty
                emptyTiles[++top] = tile;
            }

        }

        addStairs();
    }

    private void addStairs() {
        stairsDown = addStair(Tile.GLYPH_STAIRS_DOWN);

        do {
            stairsUp = addStair(Tile.GLYPH_STAIRS_UP);

            if (stairsDown.manhattanDistance(stairsUp) > SAFE_DISTANCE) {
                break;
            } else if (top >= 0) {
                stairsUp.setType(Tile.GLYPH_EMPTY);
            }

        } while (top >= 0);

    }

    private Tile addStair(char tileType) {
        int tileIndex = RandomNumberGenerator.RANDOM.getRandom(top + 1);
        Tile emptyTile = emptyTiles[tileIndex];
        emptyTile.setType(tileType);
        emptyTiles[tileIndex] = emptyTiles[top];
        top--;
        return emptyTile;
    }

    public Tile getStairsDown() {
        return stairsDown;
    }

    public Tile getStairsUp() {
        return stairsUp;
    }

    public Tile getWayBack(boolean comingUp) {
        return comingUp ? stairsDown : stairsUp;
    }

    public void setStairsDown(Tile stairsDown) {
        this.stairsDown = stairsDown;
    }

    public void setStairsUp(Tile stairsUp) {
        this.stairsUp = stairsUp;
    }
}
