#include <stdio.h>
#include <3ds.h>
#include "level.h"
#include "display.h"

char screen[ROWS][COLUMNS + 1];

void mvaddch(int y, int x, char c) {
	screen[y][x] = c;
}

void refresh() {
	display_clean();

	for (int i = 0; i < ROWS; i++) {
		screen[i][COLUMNS] = '\0';
		puts(screen[i]);
	}

}

static void
tile_print(struct tile *s, int x, int y) {
	switch (s->type) {
	case T_EMPTY:
		mvaddch(y, x, ' ');
		break;
	case T_WALL:
		mvaddch(y, x, '=');
		break;
	case T_DOOR:
		mvaddch(y, x, '+');
		break;
	case T_USTAIR:
		mvaddch(y, x, '<');
		break;
	case T_DSTAIR:
		mvaddch(y, x, '>');
		break;
	default:
		break;
	}
	if (s->c != NULL) {
		switch(s->c->race) {
		case R_HUMAN:
			mvaddch(y, x, '@');
			break;
		case R_DOG:
			mvaddch(y, x, 'd');
			break;
		}
	}
}

void
level_print(struct level *l) {
	int x, y;

	for (y = 0; y < ROWS; ++y)
		for (x = 0; x < COLUMNS; ++x)
			tile_print(&(l->tile[y][x]), x, y);
	refresh();
}

void
display_init(void) {
	gfxInitDefault();
	consoleInit(GFX_TOP, NULL);
}

void
display_clean(void) {
	printf("\e[1;1H\e[2J");
}

