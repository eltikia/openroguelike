package openroguelike;

class Cave {
    private static final int CAVE_STEP = 3;
    private static final int MAX_RATIO = 10;
    private static final int MIN_WALL = 5;
    private static final int WALL_RATIO = 4;
    private final Level level;

    Cave(Level level) {
        this.level = level;
    }

    private void caveInit() {

        for (int y = 1; y < Level.ROWS - 1; y++) {

            for (int x = 1; x < Level.COLUMNS - 1; x++) {
                level.setTileType(x, y, randPick());
            }

        }

        level.makeWalls();
    }

    private void caveReduceNoise() {

        for (int y = 1; y < Level.ROWS - 1; y++) {

            for (int x = 1; x < Level.COLUMNS - 1; x++) {
                int nwall = getNWall(x, y);

                if (nwall >= MIN_WALL) {
                    // tile type: wall
                    level.setTileType(x, y, Tile.GLYPH_CAVE);
                } else {
                    level.dig(x, y);
                }

            }

        }

    }

    void draw() {
        caveInit();

        for (int i = 0; i < CAVE_STEP; i++) {
            caveReduceNoise();
        }

    }

    private int getNWall(int x, int y) {
        int nwall = 0;

        for (int iy = -1; iy <= 1; iy++) {

            for (int ix = -1; ix <= 1; ix++) {

                if (level.isDiggable(x + ix, y + iy)) {
                    nwall++;
                }

            }

        }

        return nwall;
    }

    private char randPick() {
        LevelGenerator factory = level.getFactory();
        int x = factory.getRandom(MAX_RATIO);
        char tileType;

        if (x < WALL_RATIO) {
            tileType = Tile.GLYPH_CAVE;
        } else {
            tileType = Tile.GLYPH_EMPTY;
        }

        return tileType;
    }

}
