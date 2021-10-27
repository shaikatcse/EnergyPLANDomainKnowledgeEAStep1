In the simulation we want to replace all the individual boilers using district heating. District heating contains CHP, HP and boilers. There are alos eletroliser for district heating where excess electricity from PV can be converted to H2 and use the H2 as CHP fuel.
In the first stage, boiler capacity is very high (17500 KJ/s, KW). In the second stage, the boiler capacity is fixed by using maximul use of boiler.

Decision variables:
0-> chp2 1 -> hp2 2 -> pv capacity 
Objectives:
0 -> CO2 emission 1 -> annual cost 2 -> load following capccity

Constraints:
Biomass consumption<=56.78 GWh

EnergyPLAN configuration
CHP efficiency:
Elec eff: 0.2
Th eff: 0.7

Regulation: technical optimization
technical regulation: 1 (balacing heat demands)

New demands are:
Transport:
Diesel: 15.59 GWh
Petrol: 11.44 GWh

Heat demand: 42.37 GWh