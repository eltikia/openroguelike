import nds.Bios;
import nds.Console;
import nds.Key;
import openroguelike.*;

public class FightersGuild implements KeyPressed {

    private final LevelProducer levelProducer;
    private int keyPressed;

    public FightersGuild(LevelProducer levelProducer) {
        super();
        this.levelProducer = levelProducer;
    }

    public static void main(String[] args) {
        // Start a level producer thread.
        LevelProducer levelProducer = new LevelProducer();
        // Initialize the console, and play the game.
        int status;

        try {
            FightersGuild game = new FightersGuild(levelProducer);
            game.initConsole();
            game.play();
            status = 0;
        } catch (Throwable e) {
            Console.cls();
            System.err.println("error: " + e.getMessage());
            status = 1;
        }

        // Stop the level producer thread.
        System.exit(status);
    }

    public int getKeyPressed() {
        return keyPressed;
    }

    public void initConsole() {
        Console.cll();
        // Wait for the start key to be released unless it's not pressed at all.

        while ((Key.held() & Key.START) != 0) {
            Key.scan();
            Bios.swiWaitForVBlank();
        }

    }

    public void play() {
        Player player = new Player(levelProducer, this);
        Display display = player.getDisplay();
        display.clear();

        do {
            waitForKeyPressed();

            if (player.moveTo()) {
                // The player has won the game.
                break;
            }

            Key.scan();
        } while ((keyPressed & Key.START) == 0);

        CowSay cowSay = player.getCowSay();
        cowSay.printCow();
    }

    private void waitForKeyPressed() {
        // press it

        while ((keyPressed = Key.held()) == 0) {
            Key.scan();
            Bios.swiWaitForVBlank();
        }

        // release it
        int repeat = 10;

        while ((Key.held() & keyPressed) != 0 && repeat-- > 0) {
            Key.scan();
            Bios.swiWaitForVBlank();
        }

    }
}
