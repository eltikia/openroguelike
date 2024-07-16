package openroguelike;

public class Level {
    /**
     * level type: static
     */
    public static final int L_STATIC = 2;
    static final int COLUMNS = 80;
    /**
     * level type: cave
     */
    static final int L_CAVE = 0;
    /**
     * level type: rooms
     */
    static final int L_ROOMS = 1;
    static final int ROWS = 23;
    private final Tile[] tiles = new Tile[ROWS * COLUMNS];
    private final LevelGenerator factory;

    Level(LevelGenerator factory) {
        this.factory = factory;

        for (int y = 0; y < ROWS; y++) {

            for (int x = 0; x < COLUMNS; x++) {
                tiles[x * ROWS + y] = new Tile(x, y);
            }

        }

    }

    private void addStair(char tileType) {
        int x = factory.getRandom(COLUMNS);
        int y = factory.getRandom(ROWS);

        if (getTileType(x, y) == Tile.GLYPH_EMPTY) {
            // tile type: empty
            setTileType(x, y, tileType);
        } else {
            addStair(tileType);
        }

    }

    void clear() {

        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.clear();
        }

    }

    void dig(int x, int y) {
        // tile type: empty
        setTileType(x, y, Tile.GLYPH_EMPTY);
    }

    public Tile findTileOfType(char tileType) {
        Tile tile = null;

        for (int i = 0; i < tiles.length; i++) {
            tile = tiles[i];

            if (tileType == tile.getType()) {
                break;
            }

        }

        return tile;
    }

    void generate(int levelType) {
        clear();

        switch (levelType) {
            case L_CAVE:
                Cave cave = new Cave(this);
                cave.draw();
                break;
            case L_ROOMS:
                Rooms rooms = new Rooms(this);
                rooms.draw();
                break;
            case L_STATIC:
                LevelStatic levelStatic = new LevelStatic(this);
                levelStatic.draw();
                break;
        }

        if (levelType != L_STATIC) {
            addStair(Tile.GLYPH_STAIRS_UP);
            addStair(Tile.GLYPH_STAIRS_DOWN);
        }

    }

    public LevelGenerator getFactory() {
        return factory;
    }

    Tile getTile(int x, int y) {
        Tile tileXY = null;
        int index = x * ROWS + y;

        if (y >= 0 && x >= 0 && index < tiles.length) {
            // The coordinates are valid.
            tileXY = tiles[index];
        }

        return tileXY;
    }

    char getTileType(int x, int y) {
        Tile tile = getTile(x, y);
        return tile.getType();
    }

    boolean isDiggable(int x, int y) {
        return getTileType(x, y) == Tile.GLYPH_CAVE;
    }

    boolean moveTo(char creature, int x, int y) {
        return moveTo(new Character(creature), x, y);
    }

    boolean moveTo(Character creature, int x, int y) {
        Tile destination = getTile(x, y);
        return destination.moveTo(creature);
    }

    void setTileType(int x, int y, char type) {
        Tile tile = getTile(x, y);
        tile.setType(type);
    }

    void makeWalls() {

        for (int y = 0; y < Level.ROWS; y++) {
            // tile type: wall
            setTileType(0, y, Tile.GLYPH_CAVE);
            setTileType(Level.COLUMNS - 1, y, Tile.GLYPH_CAVE);
        }

        for (int x = 0; x < Level.COLUMNS; x++) {
            // tile type: wall
            setTileType(x, 0, Tile.GLYPH_CAVE);
            setTileType(x, Level.ROWS - 1, Tile.GLYPH_CAVE);
        }

    }

}