#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
pdf("indicators_NSGAII_SPEA2.pdf", onefile=FALSE, width=10)

NSGAIIresultDirectory<-"./MutationStudyNSGAII/data/"

NSGAIIqIndicator <- function(indicator, problem)
{
filePolynomialMutation<-paste(NSGAIIresultDirectory, "PolynomialMutation", sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, indicator, sep="/")
PolynomialMutation<-scan(filePolynomialMutation)

fileDKMutation<-paste(NSGAIIresultDirectory, "DKMutation", sep="/")
fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileDKMutation<-paste(fileDKMutation, indicator, sep="/")
DKMutation<-scan(fileDKMutation)

algs<-c("Polynomial","DKMutation")
boxplot(PolynomialMutation,DKMutation,names=algs, notch = FALSE)
titulo <-paste(indicator, "NSGAII", sep=":")
title(main=titulo)
}

SPEA2resultDirectory<-"./MutationStudySPEA2/data/"

SPEA2qIndicator <- function(indicator, problem)
{
filePolynomialMutation<-paste(SPEA2resultDirectory, "PolynomialMutation", sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, indicator, sep="/")
PolynomialMutation<-scan(filePolynomialMutation)

fileDKMutation<-paste(SPEA2resultDirectory, "DKMutation", sep="/")
fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileDKMutation<-paste(fileDKMutation, indicator, sep="/")
DKMutation<-scan(fileDKMutation)

algs<-c("Polynomial","DKMutation")
boxplot(PolynomialMutation,DKMutation,names=algs, notch = FALSE)
titulo <-paste(indicator, "SPEA2", sep=":")
title(main=titulo)
}

par(mfrow=c(2,4))
indicator<-"HV"
NSGAIIqIndicator(indicator, "OptimizeElecEnergy_NSGAII")
indicator<-"IGD"
NSGAIIqIndicator(indicator, "OptimizeElecEnergy_NSGAII")
indicator<-"GD"
NSGAIIqIndicator(indicator, "OptimizeElecEnergy_NSGAII")
indicator<-"Epsilon"
NSGAIIqIndicator(indicator, "OptimizeElecEnergy_NSGAII")
indicator<-"HV"
SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
indicator<-"IGD"
SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
indicator<-"GD"
SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
indicator<-"Epsilon"
SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")


