This is the results of 10 different runs with 10 different seed.
Here mutation based on domain knowledge is applies with SBX crossover.

It mutation is designed to work in 3 stages.

25% times, mutation favor RE sources.
25% times mutation favor conventation energies.
rest of the time Ploynomial mutation is used.

Population: 100
Evolution: 5000
crossover Probabilty: 0.9
Mutation probability: 0.2
Distribution index: 4
Algorithm: NSGA-II
selection: Binary Tournament.

seed [] = {545782, 455875, 547945, 458478, 981354, 652262, 562366, 365652, 456545, 549235 };

This will be the almost same experiment but GeneralizedMutation is updated to not inturrept the sequence of random call from jmetal. So, it is now very fair to compare.