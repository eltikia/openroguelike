#include <stdio.h>
#include <string.h>

#include "creature.h"
#include "rng.h"
#include "strlcat_strlcpy.h"

static char *names[] = {
	"Fido",
	"Idefix",
	"Chien",
	"Rex",
	"Medor",
};

void
dog_generation(struct creature *c) {
	char *name;

	c->hp = 5;
	name = names[rng_uniform(sizeof names / sizeof *names)];
	strlcpy(c->name, name, sizeof(c->name));
}

