#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "level.h"
#include "rng.h"

/* Cave specific options */
static int cave_step = 3; /* Number of calls to cave_reduce_noise */

/* Cave functions */
static enum tile_type rand_pick(unsigned int);
static void cave_init(struct level *);
static void cave_reduce_noise(struct level *);

/* Cave level generation with the cellular automata algorithm */
void
cave_draw(struct level *l) {
	int s;

	cave_init(l);
	for (s = 0; s < cave_step; ++s)
		cave_reduce_noise(l);
}

static void
cave_reduce_noise(struct level *l) {
	int x, y;
	int ix, iy;

	for (y = 1; y < ROWS - 1; ++y)
	for (x = 1; x < COLUMNS - 1; ++x) {
		unsigned int nwall = 0;
		for (iy = -1; iy <= 1; ++iy)
		for (ix = -1; ix <= 1; ++ix)
			if (l->tile[y + iy][x + ix].type == T_WALL)
				++nwall;
		if (nwall >= 5)
			l->tile[y][x].type = T_WALL;
		else
			l->tile[y][x].type = T_EMPTY;
	}
}

static void
cave_init(struct level *l) {
	int x, y;

	/* randomly fill the map */
	for (y = 1; y < ROWS - 1; ++y)
		for (x = 1; x < COLUMNS - 1; ++x)
			l->tile[y][x].type = rand_pick(40);

	for (y = 0; y < ROWS; ++y) {
		l->tile[y][0].type = T_WALL;
		l->tile[y][COLUMNS - 1].type = T_WALL;
	}

	for (x = 0; x < COLUMNS; ++x) {
		l->tile[0][x].type = T_WALL;
		l->tile[ROWS - 1][x].type = T_WALL;
	}
}

static enum tile_type
rand_pick(unsigned int ratio) {
	unsigned int x = rng_uniform(100);
	return (x < ratio) ? T_WALL : T_EMPTY;
}

