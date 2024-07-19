package openroguelike;

import nds.Console;

/**
 * A speaking cow that says about the game end.
 */
public class CowSay {
    private final Player player;

    public CowSay(Player player) {
        super();
        this.player = player;
    }

    public void printCow() {
        Console.cls();
        Console.setpos(0, 0);

        if (player.isEndGame()) {
            printCow("$$", " ");
        } else {
            printCow("xx", "U");
        }

    }

    private String formatDepth() {
        LevelGenerator factory = player.getFactory();
        String depth = String.valueOf(factory.getDepth());

        if (factory.getDepth() < 10) {
            depth += " ";
        }

        return depth;
    }

    private void printCow(String eyes, String mouth) {
        String cow = " __________\n" +
                     "< Level " + formatDepth() + " >\n" +
                     " ----------\n" +
                     "        \\   ^__^\n" +
                     "         \\  (" + eyes + ")\\_______\n" +
                     "            (__)\\       )\\/\\\n" +
                     "             " + mouth + "  ||----w |\n" +
                     "                ||     ||\n\n\n\n\n\n";
        System.out.println(cow);
    }
}
