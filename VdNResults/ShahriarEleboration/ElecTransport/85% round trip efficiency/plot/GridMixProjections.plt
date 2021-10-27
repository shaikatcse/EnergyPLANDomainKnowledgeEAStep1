set terminal pdf enhanced 
set output 'GridMixProjections.pdf'

set key autotitle columnheader
set key outside top right

set yrange [0:100]
set ylabel "Percentage (%)"
set xlabel "Years"

# Define plot style 'stacked histogram'
# with additional settings
set style data histogram
set style histogram rowstacked
set style fill solid border -1
set boxwidth 0.75


# We are plotting columns 2, 3 and 4 as y-values,
# the x-ticks are coming from column 1
# Additionally to the graph above we need to specify
# the titles via 't 2' aso.
plot 'GridMixProjections.txt' using (100*$2/($2+$3+$4+$5)):xtic(1) t 'Coal','' using (100*$3/($2+$3+$4+$5)) t 'Oil','' using (100*$4/($2+$3+$4+$5)) t 'NGas','' using (100*$5/($2+$3+$4+$5)) t 'RE'