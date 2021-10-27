set terminal pdf enhanced 
set output 'Energy_Transition_analogy.pdf'

set termoption dashed
set arrow  from 5, 7 to 4, 0 nohead lw 2 lt 2 lc rgb "black"
set arrow  from 5, 7 to 7, 5 nohead lw 2  lt 2 lc rgb "blue"
set arrow  from 5, 7 to 3, 3 nohead lw 2 lt 2 lc rgb "black"
set arrow  from 5, 7 to 9, 6 nohead  lw 2  lt 2 lc rgb "black"
set arrow  from 5, 7 to 1, 9 nohead  lw 2  lt 2 lc rgb "black"

set xrange [0:10]
set yrange [0:10]
set xlabel "PV (KW_e)"
set ylabel "Heat pumps (KW_e)"

plot 'imaginaryScenarios.txt' i 0 using 1:2 lt 5 lc rgb "blue" t  columnheader(1) , \
						 ''   i 1 using 1:2 lt 9 lc rgb "red" t  columnheader(1)

unset arrow
#unset termoption

unset output
unset terminal
