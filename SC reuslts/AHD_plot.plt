set terminal postscript eps #size 3.5,2.62 enhanced color font 'Helvetica,20' 
set output 'AhdAndDiv.eps'

#set terminal pdf  
#set output 'AHDAndDiv.pdf'

#set terminal epslatex size 3.5,2.62 color colortext
#set output 'AHDAndDiv.tex'

#reset
set label 1 "Stopping generation for Diversity" at 460, 0.7  right norotate back nopoint
set arrow from 470,0.7 to 490, 0.7 nohead lt 1 lw 2 lc rgb '#BB00BB'

set label 2 "Stopping generation for AHD" at 460, 0.65  right norotate back nopoint
set arrow from 470,0.65 to 490, 0.65 nohead lt 1 lw 2 lc rgb 'black'
 
set label 3 "Stopping generation for both" at 460, 0.6  right norotate back nopoint
set arrow from 470,0.6 to 490, 0.6 nohead lt 1 lw 2 lc rgb '#BB5E00'

set xrange[1:500]
set yrange[0:1]

#set style
set style line 1 lt 4 lw 2
set style line 2  lt 0 lw 2 lc 'blue'
 
yMax=0.3
#value for cut-off using only AHD
xValueHD=143
xValueDiv =121
xValueR=170

#set ylabel "Average Haussdorff Distance"
set xlabel "Generations"

plot 'AHD_ZDT1_run0_NSGAII' using 1:3 w l lc rgb 'black' lt 1 lw 1 title "Average Haussdorff Distance",'Div_ZDT1_run0_NSGAII' using 1:3 w l lc rgb 'blue' lt 1 lw 1 title "Diversity" 
set arrow from xValueHD,0 to xValueHD, yMax nohead lt 1 lw 2 lc rgb 'black' 
set arrow from xValueR,0 to xValueR, yMax nohead lt 1 lw 2 lc rgb '#BB5E00' 
set arrow from xValueDiv,0 to xValueDiv, yMax nohead lt 1 lw 2 lc rgb '#BB00BB'

 
#unset arrow
unset xrange
unset yrange
unset xlabel
unset ylabel
set output

#unset output
