#include <stdlib.h>
#include <time.h>

void
rng_init(void) {
	srandom(time(NULL));
}

size_t
rng_uniform(int mod) {
	return random() % mod;
}


