package openroguelike;

class LevelStatic {
    /**
     * a town level
     */
    private static final char[] TOWN = ("===============================================================================\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=             ============= =============                                     =\n" +
                                            "=             =           = =           =>                                    =\n" +
                                            "=             =           = =           =                                     =\n" +
                                            "=             =           = =           =                                     =\n" +
                                            "=             =           = =           =                                     =\n" +
                                            "=  ==+======= =           = =           =                                     =\n" +
                                            "=  =    =   = =           = =           =                                     =\n" +
                                            "=  =    +   + +           + +           =                                     =\n" +
                                            "=  =    =   = =           = =           =                                     =\n" +
                                            "=  ========== ============= =============                                     =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "=                                                                             =\n" +
                                            "===============================================================================").toCharArray();

    private final Level level;

    private final Stairs stairs;

    LevelStatic(Level level) {
        this.level = level;
        stairs = new Stairs();
    }

    Stairs draw() {

        for (int y = 0; y < Level.ROWS; y++) {

            for (int x = 0; x < Level.COLUMNS; x++) {
                draw(x, y);
            }

        }

        return stairs;
    }

    private char getTile(int x, int y) {
        char tile;
        int index = x + y * Level.COLUMNS;

        if (index < TOWN.length) {
            tile = TOWN[index];
        } else {
            // out of bounds
            tile = Tile.GLYPH_EMPTY;
        }

        return tile;
    }

    private void draw(int x, int y) {
        char tile = getTile(x, y);

        switch (tile) {
            case Tile.GLYPH_EMPTY:
                level.dig(x, y);
                break;
            case Tile.GLYPH_STAIRS_DOWN:
                stairs.setStairsDown(level.getTile(x, y));
                level.setTileType(x, y, tile);
                break;
            case Tile.GLYPH_STAIRS_UP:
                stairs.setStairsUp(level.getTile(x, y));
            case Tile.GLYPH_CAVE:
            case Tile.GLYPH_DOOR:
                level.setTileType(x, y, tile);
                break;
            case '\0':
            case '\r':
            case '\n':
                break;
            default:
                System.err.println(tile + ": unknown tile");
                break;
        }

    }

}
