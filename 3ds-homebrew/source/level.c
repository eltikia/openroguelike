#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

#include "level.h"
#include "rng.h"

typedef unsigned int u_int;

static void
level_add_stair(struct level *l, enum tile_type tt) {
	unsigned int x = rng_uniform(COLUMNS);
	unsigned int y = rng_uniform(ROWS);

	if (l->tile[y][x].type == T_EMPTY) {
		l->tile[y][x].type = tt;
	} else {
		level_add_stair(l, tt);
	}
}

int
tile_is_diggable(struct tile *t) {
	return t->type == T_WALL;
}

void
level_dig(struct level *l, unsigned int x, unsigned int y) {
	if (tile_is_diggable(&l->tile[y][x]))
		l->tile[y][x].type = T_EMPTY;
}

void
level_init(struct level *l) {
	for (u_int y = 0; y < ROWS; y++)
		for (u_int x = 0; x < COLUMNS; x++) {
			(l->tile[y][x]).c = NULL;
			l->tile[y][x].type = T_EMPTY;
		}
}

void
level_generation(struct level *l, enum level_type type, void *arg) {
	level_init(l);
	switch (type) {
	case L_STATIC:
		static_draw(l, arg);
		break;
	case L_CAVE:
	default:
		cave_draw(l);
		break;
	}

	/* Static levels already have stairs on them */
	if (type != L_STATIC) {
		level_add_stair(l, T_USTAIR);
		level_add_stair(l, T_DSTAIR);
		// TODO: Level validation
	}
}

