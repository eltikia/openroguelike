#if !defined CREATURE_H_
#define CREATURE_H_

struct tile;

#define NAME_SIZE 20

enum race {
	R_HUMAN,
	R_DOG,
};

struct creature {
	unsigned int x;
	unsigned int y;
	unsigned int hp;
	enum race race;
	int (*on_move)(unsigned int x, unsigned int y);
	char name[NAME_SIZE];
};

void creature_init(struct creature *);
void creature_generation(struct creature *, enum race);
void creature_move_at(struct creature *, struct tile *, struct tile *);

void dog_generation(struct creature *);

#endif

