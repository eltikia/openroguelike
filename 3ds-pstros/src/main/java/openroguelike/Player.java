package openroguelike;

import nds.Key;
import nds.Video;

public class Player {
    /**
     * the player glyph
     */
    public static final char GLYPH_PLAYER = '@';
    private final LevelGenerator factory;

    private final KeyPressed keyPressed;
    /**
     * the current level
     */
    private final Level level;
    /**
     * the current coordinate X
     */
    private int currentX = LevelGenerator.PLAYER_POSITION;
    /**
     * the current coordinate Y
     */
    private int currentY = LevelGenerator.PLAYER_POSITION;
    private final Display display;

    private boolean endGame;

    private final CowSay cowSay;

    public Player(KeyPressed keyPressed) {
        super();
        this.keyPressed = keyPressed;
        factory = new LevelGenerator();
        cowSay = new CowSay(this);
        level = factory.getLevel();
        display = new Display(this);
    }

    public int getCurrentX() {
        return currentX;
    }

    public Display getDisplay() {
        return display;
    }

    public Level getLevel() {
        return level;
    }

    private void handleDirections() {
        int nextX = currentX;

        if (isKeyPressed(Key.RIGHT)) {
            nextX++;
        } else if (isKeyPressed(Key.LEFT)) {
            nextX--;
        }

        int nextY = currentY;

        if (isKeyPressed(Key.DOWN)) {
            nextY++;
        } else if (isKeyPressed(Key.UP)) {
            nextY--;
        }

        moveTo(nextX, nextY);
    }

    private void handleStairs() {
        char type = level.getTileType(currentX, currentY);
        boolean comingUp = type == Tile.GLYPH_STAIRS_UP;

        if (type == Tile.GLYPH_STAIRS_DOWN || comingUp) {
            LevelGenerator factory = level.getFactory();

            if (factory.changeLevel(comingUp)) {
                handleStairs(comingUp);
            } else {
                endGame = true;
            }

        }

    }

    private void handleStairs(boolean comingUp) {
        level.clear();
        display.clear();
        LevelGenerator factory = level.getFactory();
        factory.generate();
        char wayBack;

        if (comingUp) {
            wayBack = Tile.GLYPH_STAIRS_DOWN;
        } else {
            wayBack = Tile.GLYPH_STAIRS_UP;
        }

        Tile tile = level.findTileOfType(wayBack);
        tile.setCreature(new Character(GLYPH_PLAYER));
        currentX = tile.getCoordinateX();
        currentY = tile.getCoordinateY();
        display.clear();
    }

    private boolean isKeyPressed(int key) {
        int pressed = keyPressed.getKeyPressed();
        return (pressed & key) != 0;
    }

    private void moveTo(int nextX, int nextY) {

        if (level.moveTo(GLYPH_PLAYER, nextX, nextY)) {
            // Clean the previously occupied tile.
            level.moveTo(null, currentX, currentY);
            display.refresh(nextX, nextY);
            display.refresh(currentX, currentY);
            currentX = nextX;
            currentY = nextY;
            display.playerMoved(false);
        }

    }

    public boolean moveTo() {
        handleDirections();

        if (isKeyPressed(Key.A)) {
            handleStairs();
        } else if (isKeyPressed(Key.SELECT)) {
            Video.lcdSwap();
        }

        return endGame;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public LevelGenerator getFactory() {
        return factory;
    }

    public CowSay getCowSay() {
        return cowSay;
    }
}
