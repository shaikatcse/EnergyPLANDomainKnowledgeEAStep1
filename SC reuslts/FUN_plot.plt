#This is a gnuplot file that plot FUN for two 140th and 141th generations.
#And a small box that provide zoomed view of a particular interesting region

set terminal postscript eps enhanced color #font 'Helvetica,10'
set output 'FUNGen.eps'

set multiplot

#bigger plot option

set xrange [0:1.02]
set yrange [0:1.05]
set xlabel "f1"
set ylabel "f2"

set key font ",18"

set object 1 rect from 0.24,0.45 to 0.29,0.55 lw 1     
#set object 2 rect from 0.36,0.35 to 0.40,0.43 lw 1     
#set object 3 rect from 0.81,0.02 to 0.94,0.14 lw 1     
plot 'FUN140' using 1:2 lt 1 lc rgb 'black' title "140th generation", 'FUN141' using 1:2 lt 4 lc rgb 'black' title "141th generation"

#now set option for the smaller plot
set size 0.6,0.35
set origin 0.3,0.5
set xtics 0.01
set xrange [0.24:0.29]
set yrange[0.45:0.55]
set xlabel ""
set ylabel ""

plot 'FUN140' using 1:2 lt 1 lc rgb 'black' notitle, 'FUN141' using 1:2 lt 4 lc rgb 'black' notitle 

unset multiplot
unset size
unset origin
unset xtics

unset output
  
