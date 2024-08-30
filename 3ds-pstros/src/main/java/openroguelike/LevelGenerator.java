package openroguelike;

public class LevelGenerator {
    /**
     * the initial player X and Y coordinates for the player in the town
     */
    public static final int PLAYER_POSITION = 17;

    /**
     * the last level
     */
    public static final int END_GAME = 99;

    /**
     * the initial cow X and Y coordinates for the player in the town
     */
    private static final int COW_POSITION = 18;

    private int depth;

    private final LevelProducer levelProducer;

    private Level townLevel;

    public LevelGenerator(LevelProducer levelProducer) {
        super();
        this.levelProducer = levelProducer;
        generate();
    }

    public boolean changeLevel(boolean comingUp) {
        boolean canPlay = true;

        if (comingUp) {

            if (depth > 0) {
                depth--;
            }

        } else if (depth == END_GAME) {
            canPlay = false;
        } else {
            depth++;
        }

        return canPlay;
    }

    public Level generate() {
        Level level;

        if (depth == 0) {

            if (townLevel == null) {
                townLevel = new Level();
                townLevel.generate(Level.L_STATIC);
                // It's a cow in a ghost town.
                townLevel.moveTo('c', COW_POSITION, COW_POSITION);
                // It's a first step ever.
                townLevel.moveTo(Player.GLYPH_PLAYER, PLAYER_POSITION, PLAYER_POSITION);
            }

            level = townLevel;
        } else {
            level = levelProducer.generate(depth);
        }

        return level;
    }

    public int getDepth() {
        return depth;
    }

}
