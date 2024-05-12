#include <stdlib.h>
#include <unistd.h>

#include "creature.h"
#include "level.h"

void
creature_init(struct creature *c) {
	c->x = 0;
	c->y = 0;
	c->hp = 0;
	c->race = R_HUMAN;
	c->on_move =  NULL;
}

void
creature_generation(struct creature *c, enum race race) {
	c->race = race;

	switch (race) {
	case R_DOG:
		dog_generation(c);
		break;
	default:
		return;
	}
}

void
creature_move_at(struct creature *c, struct tile *from, struct tile *to) {
	if (from != NULL)
		from->c = NULL;
	to->c = c;
}

