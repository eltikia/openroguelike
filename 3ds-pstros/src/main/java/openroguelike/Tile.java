package openroguelike;

public class Tile {
    public static final char GLYPH_CAVE = '=';
    /**
     * the door glyph
     */
    public static final char GLYPH_DOOR = '+';
    /**
     * the empty tile type
     */
    public static final char GLYPH_EMPTY = ' ';
    /**
     * the stairs up glyph
     */
    public static final char GLYPH_STAIRS_DOWN = '>';
    /**
     * the stairs up glyph
     */
    public static final char GLYPH_STAIRS_UP = '<';
    private final int coordinateX;
    private final int coordinateY;
    private Character creature;
    private char type = GLYPH_EMPTY;

    public Tile(int coordinateX, int coordinateY) {
        super();
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    Character getCreature() {
        return this.creature;
    }

    public void setCreature(Character creature) {
        this.creature = creature;
    }

    char getType() {
        return this.type;
    }

    void setType(char type) {
        this.type = type;
    }

    public boolean moveTo(Character newCreature) {
        // Empty fields, stairs and doors can be entered.
        boolean canEnter = type == GLYPH_EMPTY || type == GLYPH_STAIRS_UP || type == GLYPH_STAIRS_DOWN || type == GLYPH_DOOR;
        boolean canStep = creature == null || newCreature == null;

        if (canEnter && canStep) {
            creature = newCreature;
        }

        return canEnter && canStep;
    }

    public int manhattanDistance(Tile tile) {
        int yDiff = coordinateY - tile.getCoordinateY();
        int xDiff = coordinateX - tile.getCoordinateX();
        return Math.abs(yDiff) + Math.abs(xDiff);
    }
}