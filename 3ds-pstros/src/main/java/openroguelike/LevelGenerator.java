package openroguelike;

import java.util.Random;

public class LevelGenerator {
    /**
     * the initial player X and Y coordinates for the player in the town
     */
    public static final int PLAYER_POSITION = 16;
    private static final int CAVE_RATIO = 9;
    /**
     * the last level
     */
    private static final int END_GAME = 99;
    /**
     * the initial kitten X and Y coordinates for the player in the town
     */
    private static final int KITTEN_POSITION = 17;
    private static final int MAX_RATIO = 10;
    private final Level level = new Level(this);
    private final Random random = new Random();
    private int depth;

    private boolean firstStep = true;

    public LevelGenerator() {
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

    public void generate() {

        if (depth == 0) {
            generate(Level.L_STATIC);
        } else {
            generate(randPick());
        }

    }

    private void generate(int levelType) {
        level.generate(levelType);

        if (firstStep) {
            level.moveTo(Player.GLYPH_PLAYER, PLAYER_POSITION, PLAYER_POSITION);
        }

        if (levelType == Level.L_STATIC) {
            level.moveTo('k', KITTEN_POSITION, KITTEN_POSITION);
        }

        firstStep = false;
    }

    public int getDepth() {
        return depth;
    }

    public Level getLevel() {
        return level;
    }

    public int getRandom(int bound) {
        return Math.abs(random.nextInt()) % bound;
    }

    private int randPick() {
        int x = getRandom(MAX_RATIO);
        int levelType;

        if (x < CAVE_RATIO) {
            levelType = Level.L_CAVE;
        } else {
            levelType = Level.L_ROOMS;
        }

        return levelType;
    }
}
