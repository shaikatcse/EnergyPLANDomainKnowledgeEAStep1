#This is a R file that generate a boxplot for six different problem for NSGA-II.
#There are 6 rows and three columns. Each row presents a problem and 1st column present stop generation, 2nd column presents HVd and third column presents epsd.
#Someone needs to select appropriate path to work with.

#see "alltogather.NSGAII.R" for other parameters

postscript("All_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=11, width=7, pointsize=10)
#pdf("All_SPEA2.pdf", width=7, height=11,pointsize=10) 
#resultDirectory<-"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/NSGAIISC"
resultDirectory<-"C:/Users/mahbub/Desktop/shahriar/StoppingCriteriaStudies/data/SPEA2SC"

returnStdStGen <- function(problem)
{
if(problem =="ZDT1")
 return (200)
 else if(problem =="ZDT2")
 return (200)
 else if(problem =="ZDT3")
 return (200)
 else if(problem =="ZDT4")
 return (200)
 else if(problem =="DTLZ2")
 return (300)
 else if(problem =="DTLZ5")
 return (200)
}

funStPlot <- function(problem)
{
StgenHVD<-paste(resultDirectory, problem, sep="/")
StgenHVD<-paste(StgenHVD, "StGenHVDNew", sep="/")
StgenHVD<-scan(StgenHVD)

StgenAHD<-paste(resultDirectory, problem, sep="/")
StgenAHD<-paste(StgenAHD, "StGenAHDNew", sep="/")
StgenAHD<-scan(StgenAHD)

StgenDV<-paste(resultDirectory, problem, sep="/")
StgenDV<-paste(StgenDV, "StGenDVNew", sep="/")
StgenDV<-scan(StgenDV)

StgenOCD<-paste(resultDirectory, problem, sep="/")
StgenOCD<-paste(StgenOCD, "StopOCD", sep="/")
StgenOCD<-scan(StgenOCD)

ind<-c("AHD+Div", "AHD", "Div", "OCD" )
line<-returnStdStGen(problem)

if(problem =="DTLZ2"){
	boxplot(StgenHVD,StgenAHD,StgenDV,StgenOCD ,names=ind,  notch = FALSE, outline=FALSE, ylim=c(0,310))
}
else{

	boxplot(StgenHVD,StgenAHD,StgenDV,StgenOCD ,names=ind,  notch = FALSE, outline=FALSE)
}
#boxplot(StgenHVD,StgenAHD,StgenDV,names=ind, notch = FALSE, outline=FALSE, ylim=c(0,line)
abline(h=line)
titulo <-paste(problem, "StopGen", sep=":")
title(font.main = 1, main=titulo)
}

funHVDPlot <- function(problem)
{
HVD<-paste(resultDirectory, problem, sep="/")
HVD<-paste(HVD, "HVDNew", sep="/")
HVD<-scan(HVD)

HVDAHD<-paste(resultDirectory, problem, sep="/")
HVDAHD<-paste(HVDAHD, "HVDAHDNew", sep="/")
HVDAHD<-scan(HVDAHD)

HVDDV<-paste(resultDirectory, problem, sep="/")
HVDDV<-paste(HVDDV, "HVDDVNew", sep="/")
HVDDV<-scan(HVDDV)

HVDOCD<-paste(resultDirectory, problem, sep="/")
HVDOCD<-paste(HVDOCD, "HVDOCD", sep="/")
HVDOCD<-scan(HVDOCD)

#ind<-c(expression('HVD'[all]), expression('HVD'[AHD]), expression('HVD'[DV]) )
ind<-c("AHD+Div", "AHD", "Div", "OCD" )
boxplot(HVD,HVDAHD,HVDDV,HVDOCD, names=ind, notch = FALSE, outline=FALSE)
abline(h=0.0)
#titulo<-paste(problem, expression('HV'[D]), sep=":")
title( main=substitute(problem*':HV'[d], list(problem = problem)))
}

funEpsDPlot <- function(problem)
{
EpsD<-paste(resultDirectory, problem, sep="/")
EpsD<-paste(EpsD, "EpsDNew", sep="/")
EpsD<-scan(EpsD)

EpsAHD<-paste(resultDirectory, problem, sep="/")
EpsAHD<-paste(EpsAHD, "EpsDAHDNew", sep="/")
EpsAHD<-scan(EpsAHD)

EpsDDV<-paste(resultDirectory, problem, sep="/")
EpsDDV<-paste(EpsDDV, "EpsDDVNew", sep="/")
EpsDDV<-scan(EpsDDV)

EpsDOCD<-paste(resultDirectory, problem, sep="/")
EpsDOCD<-paste(EpsDOCD, "EpsDOCD", sep="/")
EpsDOCD<-scan(EpsDOCD)

#ind<-c(expression('EpsD'[all]), expression('EpsD'[AHD]), expression('EpsD'[DV]) )
ind<-c("AHD+Div", "AHD", "Div", "OCD" )
boxplot(EpsD,EpsAHD,EpsDDV,EpsDOCD, names=ind, notch = FALSE, outline=FALSE)
abline(h=0.0)
#titulo <-paste(problem,expression('HV'[D]), sep=":")
title( main=substitute(problem*':eps'[d], list(problem = problem)))
}


par(mfrow = c(6,3), mar=c(2, 2, 2, 2) + 0.1)
#par(mfrow=c(6,3))
prob1<-"ZDT1"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)

prob1<-"ZDT2"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)

prob1<-"ZDT3"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)

prob1<-"ZDT4"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)

prob1<-"DTLZ2"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)

prob1<-"DTLZ5"
funStPlot(prob1)
funHVDPlot(prob1)
funEpsDPlot(prob1)


dev.off()