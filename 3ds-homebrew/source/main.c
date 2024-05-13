#include <3ds.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

#include "level.h"
#include "creature.h"
#include "display.h"

int
main(void) {
	struct level *l, l1;
	struct creature player, dog;
	int next_level = 1;

	creature_init(&player);
	creature_generation(&player, R_HUMAN);
	creature_init(&dog);
	creature_generation(&dog, R_DOG);

	display_init();

	// Main loop
	while (aptMainLoop())
	{

		if (next_level) {
			level_generation(&l1, L_CAVE, NULL);
			l = &l1;
			creature_move_at(&player, NULL, &(l->tile[15][15]));
			creature_move_at(&dog, NULL, &(l->tile[16][15]));
			level_print(l);
		}

		//Scan all the inputs. This should be done once for each frame
		hidScanInput();

		//hidKeysDown returns information about which buttons have been just pressed (and they weren't in the previous frame)
		u32 kDown = hidKeysDown();

		if (kDown & KEY_START) break; // break in order to return to hbmenu

		next_level = kDown & KEY_A;

		// Flush and swap framebuffers
		gfxFlushBuffers();
		gfxSwapBuffers();

		//Wait for VBlank
		gspWaitForVBlank();
	}

	display_clean();
	gfxExit();
	return 0;
}
