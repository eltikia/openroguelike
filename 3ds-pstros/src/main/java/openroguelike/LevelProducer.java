package openroguelike;

public class LevelProducer implements Runnable {

    private static final int CAVE_RATIO = 9;

    private static final int MAX_RATIO = 10;

    private final Level[] levelsBuffer = new Level[LevelGenerator.END_GAME];

    public LevelProducer() {
        super();
        Thread producerThread = new Thread(this);
        producerThread.start();
    }

    public Level generate(int depth) {
        Level level = null;

        try {
            level = consume(depth - 1);
        } catch (InterruptedException e) {
            // interrupted
        }

        return level;
    }

    private void produce(int depth) throws InterruptedException {

        synchronized (this) {
            Level level;
            int type = randPick();

            do {
                level = new Level();
                level.generate(type);
            } while (new Pathfinder(level).isTrapped());

            levelsBuffer[depth] = level;
            notify();
        }

    }

    public Level consume(int depth) throws InterruptedException {
        Level level = levelsBuffer[depth];

        if (level == null) {

            synchronized (this) {

                while (levelsBuffer[depth] == null) {
                    wait();
                }

                level = levelsBuffer[depth];
            }

        }

        return level;
    }

    public void run() {

        try {

            for (int i = 0; i < levelsBuffer.length; i++) {
                produce(i);
            }

        } catch (InterruptedException e) {
            // interrupted
        }

    }

    private int randPick() {
        int x = RandomNumberGenerator.RANDOM.getRandom(MAX_RATIO);
        int levelType;

        if (x < CAVE_RATIO) {
            levelType = Level.L_CAVE;
        } else {
            levelType = Level.L_ROOMS;
        }

        return levelType;
    }

}
