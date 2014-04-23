postscript("NSGAII.Epsilon.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
filePolynomialMutation<-paste(resultDirectory, "PolynomialMutation", sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, indicator, sep="/")
PolynomialMutation<-scan(filePolynomialMutation)

fileDKMutation<-paste(resultDirectory, "DKMutation", sep="/")
fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileDKMutation<-paste(fileDKMutation, indicator, sep="/")
DKMutation<-scan(fileDKMutation)

algs<-c("PolynomialMutation","DKMutation")
boxplot(PolynomialMutation,DKMutation,names=algs, notch = FALSE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(2,3))
indicator<-"Epsilon"
qIndicator(indicator, "OptimizeElecEnergy_NSGAII")
