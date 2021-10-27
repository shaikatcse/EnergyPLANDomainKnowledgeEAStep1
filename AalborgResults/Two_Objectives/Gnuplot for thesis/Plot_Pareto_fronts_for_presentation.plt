set terminal pdf enhanced
set output 'AalborgParetoFrontsForPresentation.pdf'

set xrange [-0.6:0.6]
set yrange [2000:6000]
set y2range [265:806]

set xlabel 'CO_2 emissions (in Mton)'
set ylabel 'Annual Cost (in Million DKK)'

set y2label 'Annual Cost (in Million ERU)'
set ytics nomirror
set y2tics 50


plot "Aalborg_Pareto_fronts_results" using 1:2 title "Pareto-optimal front", "Aalborg_Pareto_fronts_results" using 5:6 lt 13 title "Aalborg \"manual\" configuration"

unset output
unset terminal
