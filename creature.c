/*
 * Copyright (c) 2013,2018 Tristan Le Guern <tleguern@bouledef.eu>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

#include <stdbool.h>
#include <stdlib.h>
#include <sys/param.h>

#include "level.h"
#include "creature.h"
#include "rng.h"

#define AGGRO_RANGE	2
#define RETRY_LIMIT 1000

static void human_init(struct creature *);
static void goblin_init(struct creature *);
static int creature_try_place(struct creature *, struct level *, int, int);
static struct creature *creature_find_nearby_player(struct creature *,
    struct level *, int);

void
creature_init(struct creature *c, enum race race)
{
	c->race = race;
	c->actionpoints = 0;
	c->level = NULL;
	switch (race) {
	case R_HUMAN:
		human_init(c);
		break;
	case R_GOBLIN:
		goblin_init(c);
		break;
	case R__MAX:
	default:
		return; /* XXX error in some way */
	}
}

static int
creature_try_place(struct creature *c, struct level *l, int y, int x)
{

	if (y < 0 || y >= MAXROWS || x < 0 || x >= MAXCOLS) {
		return -1;
	}

	if (!tile_is_empty(&(l->tile[y][x]))) {
		return -1;
	}

	c->y = y;
	c->x = x;
	c->level = l;
	l->tile[y][x].creature = c;
	return 0;
}

int
creature_place_randomly(struct creature *c, struct level *l)
{

	for (int try = 0; try < RETRY_LIMIT; try++) {
		int y = rng_rand_uniform(MAXROWS);
		int x = rng_rand_uniform(MAXCOLS);

		if (l->tile[y][x].type == T_EMPTY && creature_try_place(c, l, y, x) == 0) {
			return 0;
		}

	}

	for (int y = 0; y < MAXROWS; y++) {

		for (int x = 0; x < MAXCOLS; x++) {

			if (l->tile[y][x].type == T_EMPTY && creature_try_place(c, l, y, x) == 0) {
				return 0;
			}

		}

	}

	return -1;
}

int
creature_place_at_stair(struct creature *c, struct level *l, bool up) {
	struct coordinate coord;
	enum tile_type stair = up ? T_UPSTAIR : T_DOWNSTAIR;

	if (level_find(l, stair, &coord) == -1) {
		return -1;
	}

	if (creature_try_place(c, l, coord.y, coord.x) == 0) {
		return 0;
	}

	for (int dy = -1; dy <= 1; dy++) {

		for (int dx = -1; dx <= 1; dx++) {

			if (creature_try_place(c, l, coord.y + dy, coord.x + dx) == 0) {
				return 0;
			}

		}

	}

	return creature_place_randomly(c, l);
}

int
creature_damroll(struct creature *c, struct creature *attacker) {
	double strength = (double) attacker->hp / (double) c->maxhp;
	int max_attack = (int) (double) c->maxhp * strength;
	int max_damage = MAX(max_attack, 1);
	int damage_roll = rng_rand_uniform(max_damage + 1);
	return MIN(damage_roll, c->hp);
}

int
creature_move(struct creature *c, struct level *l, int row, int col)
{
	int			 ny, nx;
	struct tile const	*nt;

	ny = c->y + row;
	nx = c->x + col;
	if (ny < 0 || ny >= MAXROWS) {
		return(-1);
	}
	if (nx < 0 || nx >= MAXCOLS) {
		return(-1);
	}
	nt = &(l->tile[ny][nx]);
	if (tile_is_wall(nt)) {
		return(-1);
	}
	if (!tile_is_empty(nt)) {

		if (NULL != nt->creature && nt->creature->race != c->race) {
			int damage = creature_damroll(nt->creature, c);
			nt->creature->hp -= damage;
			return(0);
		}

		return(-1);
	}

	l->tile[c->y][c->x].creature = NULL;
	c->y = ny;
	c->x = nx;
	l->tile[c->y][c->x].creature = c;
	return(0);
}

int
creature_move_left(struct creature *c, struct level* l)
{
	return(creature_move(c, l, 0, -1));
}

int
creature_move_down(struct creature *c, struct level* l)
{
	return(creature_move(c, l, 1, 0));
}

int
creature_move_up(struct creature *c, struct level* l)
{
	return(creature_move(c, l, -1, 0));
}

int
creature_move_right(struct creature *c, struct level* l)
{
	return(creature_move(c, l, 0, 1));
}

int
creature_move_upleft(struct creature *c, struct level* l)
{
	return(creature_move(c, l, -1, -1));
}

int
creature_move_downleft(struct creature *c, struct level* l)
{
	return(creature_move(c, l, 1, -1));
}

int
creature_move_upright(struct creature *c, struct level* l)
{
	return(creature_move(c, l, -1, 1));
}

int
creature_move_downright(struct creature *c, struct level* l)
{
	return(creature_move(c, l, 1, 1));
}

int
creature_climb_upstair(struct creature *c, struct level *f, struct level *t)
{
	if (f->tile[c->y][c->x].type != T_UPSTAIR) {
		return(-1);
	}
	f->tile[c->y][c->x].creature = NULL;

	if (creature_place_at_stair(c, t, false) == -1) {
		f->tile[c->y][c->x].creature = c;
		return(-1);
	}

	return(0);
}

int
creature_climb_downstair(struct creature *c, struct level *f, struct level *t)
{
	if (f->tile[c->y][c->x].type != T_DOWNSTAIR) {
		return(-1);
	}
	f->tile[c->y][c->x].creature = NULL;

	if (creature_place_at_stair(c, t, true) == -1) {
		f->tile[c->y][c->x].creature = c;
		return(-1);
	}

	return(0);
}

int
creature_rest(struct creature *c)
{

	if (c->hp > 0 && c->hp < c->maxhp) {
		c->hp += 1;
	}

	return(0);
}

static struct creature *
creature_find_nearby_player(struct creature *c, struct level *l, int radius)
{
	struct creature	*target = NULL;

	for (int dy = -radius; dy <= radius; dy++) {

		for (int dx = -radius; dx <= radius; dx++) {
			int ny, nx;
			struct creature *other;

			if (dy == 0 && dx == 0) {
				continue;
			}

			ny = c->y + dy;
			nx = c->x + dx;

			if (ny < 0 || ny >= MAXROWS || nx < 0 || nx >= MAXCOLS) {
				continue;
			}

			other = l->tile[ny][nx].creature;

			if (other != NULL && other->race == R_HUMAN) {

				if (rng_rand_uniform(100) > 20) {
					// attack the player
					target = other;
				}

				break;
			}

		}

	}
	return target;
}

void
creature_do_something(struct creature *c, struct level *l)
{
	uint32_t choice;
	struct creature *enemy;

	if (c->hp == -1) {
		return;
	}

	if (c->hp == 0) {
		c->hp = -1;
		l->tile[c->y][c->x].creature = NULL;
		return;
	}

	enemy = creature_find_nearby_player(c, l, AGGRO_RANGE);

	if (NULL != enemy) {
		int dy = (enemy->y > c->y) - (enemy->y < c->y);
		int dx = (enemy->x > c->x) - (enemy->x < c->x);

		creature_move(c, l, dy, dx);
		return;
	}

	choice = rng_rand_uniform(8);
	switch (choice) {
	case 0:
		creature_move_left(c, l);
		break;
	case 1:
		creature_move_down(c, l);
		break;
	case 2:
		creature_move_up(c, l);
		break;
	case 3:
		creature_move_right(c, l);
		break;
	case 4:
		creature_move_upleft(c, l);
		break;
	case 5:
		creature_move_downleft(c, l);
		break;
	case 6:
		creature_move_upright(c, l);
		break;
	case 7:
		creature_move_downright(c, l);
		break;
	}
}

static void
creature_hp_init(struct creature *c, int maxhp) {
	c->maxhp = maxhp;
	c->hp = c->maxhp;

}

static void
human_init(struct creature *c)
{
	creature_hp_init(c, 10);
	c->speed = 5;
}

static void
goblin_init(struct creature *c)
{
	creature_hp_init(c, 2);
	c->speed = 7;
}

