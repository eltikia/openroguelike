#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "level.h"
#include "rng.h"

static void rooms_init(struct level *);
static void rooms_split(struct level *, int, int, int, int, int, int);
static int rooms_ten_percent_of(int);

/* BSP tree rooms generation */
void
rooms_draw(struct level *l) {
	rooms_init(l);
	rooms_split(l, 0, COLUMNS, 0, ROWS, 0, 0);
}

static void
rooms_split(struct level *l, int minx, int x, int miny, int y,
	    int dir, int count) {
	int lx, ly;

	dir = dir ^ 1;

	if (count == 4)
		return;

	if (dir == 1) {
		//int ten = rooms_ten_percent_of(y);
		lx = minx;
		ly = miny + rng_uniform(y - miny);
		for (; lx < x; ++lx)
			l->tile[ly][lx].type = T_EMPTY;
		lx = 0;
	} else {
		int ten = rooms_ten_percent_of(x);
		ly = miny;
		lx = ten + rng_uniform(x - (ten * 2));
		for (; ly < y; ++ly)
			l->tile[ly][lx].type = T_EMPTY;
		ly = 0;
	}
	if (lx == 0)
		rooms_split(l, minx, x, miny, ly, dir, count + 1);
	else
		rooms_split(l, minx, lx, miny, y, dir, count + 1);
	if (ly == 0)
		rooms_split(l, lx, x, miny, y, dir, count + 1);
	else
		rooms_split(l, minx, x, ly, y, dir, count + 1);
}

static int 
rooms_ten_percent_of(int x) {
	return (10 * x / 100);
}

static void
rooms_init(struct level *l) {
	int x, y;

	/* fill the map with walls */
	for (y = 0; y < ROWS; ++y)
		for (x = 0; x < COLUMNS; ++x)
			l->tile[y][x].type = T_WALL;
}

