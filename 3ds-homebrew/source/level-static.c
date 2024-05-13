#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

#include "level.h"
#include "rng.h"

void
static_draw(struct level *l, void *arg) {
	int fd;
	int n;
	int x, y;
	char buf[ROWS * COLUMNS];
	const char *file = arg;

	(void)memset(buf, 0, sizeof buf);
	fd = open(file, O_RDONLY);
	if (fd == -1) {
		return;
	}
	n = read(fd, buf, sizeof buf);
	if (n == -1) {
		return;
	}
	(void)close(fd);

	for (y = 0; y < ROWS; ++y)
	for (x = 0; x < COLUMNS; ++x) {
		switch (buf[x + y * COLUMNS]) {
		case ' ':
			l->tile[y][x].type = T_EMPTY;
			break;
		case '1':
			l->tile[y][x].type = T_WALL;
			break;
		case '2':
			l->tile[y][x].type = T_DOOR;
			break;
		default:
			break;
		}
	}
}

