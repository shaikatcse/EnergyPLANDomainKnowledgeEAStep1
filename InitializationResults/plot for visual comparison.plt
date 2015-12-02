set terminal pdf enhanced
set output 'visual comparison1.pdf'

set xrange [-0.6:0.7]
set xlabel "CO_2 emission (in million  tons)"
set ylabel "Annual cost (in  million DKK)"

plot "SPEA2WithoutTrack\\FUN_seed_447514" using 1:2 title "SPEA2 with RI" , "SPEA2WithoutTrackWithSI\\WithoutRM\\FUN_Init_545454_seed_637384" using 1:2 title "SPEA2 with SI"

unset output