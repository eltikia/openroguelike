#ifndef LEVEL_H__
#define LEVEL_H__

#include <sys/queue.h>

#define ROWS 29
#define COLUMNS 49

enum tile_type {T_EMPTY, T_WALL, T_DOOR, T_USTAIR, T_DSTAIR};

struct tile {
	enum tile_type type;
	/* list of objects */
	struct creature *c;
};

int tile_is_diggable(struct tile *);

enum level_type {L_CAVE, L_STATIC, L_MAZE, L_ROOMS, L_GRID, L_FOREST};
SLIST_HEAD(, level) dungeon;
struct level {
	struct tile tile[ROWS][COLUMNS];
	SLIST_ENTRY(level) next;
};

void level_dig(struct level *, unsigned int, unsigned int);
void static_draw(struct level *, void *);
void cave_draw(struct level *);
void level_print(struct level *);
void level_generation(struct level *, enum level_type, void *);

#endif
