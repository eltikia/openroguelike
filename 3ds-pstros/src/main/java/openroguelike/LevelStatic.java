package openroguelike;

class LevelStatic {
    /**
     * a town level
     */
    private static final String TOWN = "===============================================================================\n" +
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
                                       "===============================================================================";

    private final Level level;

    LevelStatic(Level level) {
        this.level = level;
    }

    void draw() {
        char[] buf = TOWN.toCharArray();

        for (int y = 0; y < Level.ROWS; y++) {

            for (int x = 0; x < Level.COLUMNS; x++) {
                char tile;
                int index = x + y * Level.COLUMNS;

                if (index < buf.length) {
                    tile = buf[index];
                } else {
                    // out of bounds
                    tile = Tile.GLYPH_EMPTY;
                }

                switch (tile) {
                    case Tile.GLYPH_EMPTY:
                        level.dig(x, y);
                        break;
                    case Tile.GLYPH_CAVE:
                    case Tile.GLYPH_DOOR:
                    case Tile.GLYPH_STAIRS_DOWN:
                    case Tile.GLYPH_STAIRS_UP:
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

    }

}
