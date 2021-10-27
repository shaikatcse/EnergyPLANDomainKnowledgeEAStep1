set terminal pdf enhanced font "Helvetica,20"
set output 'demandProjections.pdf'

red = "#FF0000"; green = "#00FF00"; blue = "#0000FF"; skyblue = "#87CEEB";
set yrange [100:300]
set style data histogram
set style histogram cluster gap 1
set style fill solid
set boxwidth 0.9
set xtics format ""
set grid ytics
set xlabel "sub-sectors"
set ylabel "Demand (GWh)"

#set title "A Sample Bar Chart"
plot "demandProjections.txt" using 2:xtic(1) title "2008" linecolor rgb red, \
            '' using 3 title "2020" linecolor rgb blue, \
            '' using 4 title "2030" linecolor rgb green, \
            '' using 5 title "2050" linecolor rgb skyblue
			
unset terminal
unset output