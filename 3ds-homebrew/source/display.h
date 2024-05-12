/*
 * Display:
 *  The high levels dungeon representation methods. 
 */

#ifndef DISPLAY_H
#define DISPLAY_H

#include "level.h"
#include "creature.h"

void level_print(struct level *);
void display_init(void);
void display_clean(void);

#endif
