package openroguelike;

import nds.Console;
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

    public Player(LevelProducer producer, KeyPressed keyPressed) {
        super();
        this.keyPressed = keyPressed;
        factory = new LevelGenerator(producer);
        cowSay = new CowSay(this);
        display = new Display(this);
    }

    public int getCurrentX() {
        return currentX;
    }

    public Display getDisplay() {
        return display;
    }

    public Level getLevel() {
        return factory.generate();
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
        Level level = getLevel();
        char type = level.getTileType(currentX, currentY);
        boolean comingUp = type == Tile.GLYPH_STAIRS_UP;

        if (type == Tile.GLYPH_STAIRS_DOWN || comingUp) {

            if (factory.changeLevel(comingUp)) {
                handleStairs(comingUp);
            } else {
                endGame = true;
            }

        }

    }

    private void handleStairs(boolean comingUp) {
        Console.cls();
        Level level = factory.generate();
        Stairs stairs = level.getStairs();
        Tile wayBack = stairs.getWayBack(comingUp);
        wayBack.setCreature(new Character(GLYPH_PLAYER));
        currentX = wayBack.getCoordinateX();
        currentY = wayBack.getCoordinateY();
        display.clear();
    }

    private boolean isKeyPressed(int key) {
        int pressed = keyPressed.getKeyPressed();
        return (pressed & key) != 0;
    }

    private void moveTo(int nextX, int nextY) {
        Level level = getLevel();

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
