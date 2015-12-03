#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
#pdf("indicators_NSGAII_NSGAII_DKM.pdf", onefile=FALSE, width=10)
postscript("indicators_NSGAII_SPEA2_test.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=12)

NSGAIIresultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
	fileNSGAII<-paste(NSGAIIresultDirectory, "NSGAII", sep="/")
	#fileNSGAII<-paste(fileNSGAII,"With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
	NSGAII_results<-scan(fileNSGAII)

fileNSGAIIDKM<-paste(NSGAIIresultDirectory, "NSGAIIWithDKMutation", sep="/")
fileNSGAIIDKM<-paste(fileNSGAIIDKM,"With_mutation_pr_0.1", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileNSGAIIDKM<-paste(fileNSGAIIDKM, indicator, sep="/")
NSGAIIDKM_results<-scan(fileNSGAIIDKM)

algs<-c("PM","SM")
boxplot(NSGAII_results,NSGAIIDKM_results,names=algs, notch = FALSE)
titulo <-paste(indicator, "NSGAII", sep=":")
title(main=titulo)
}

SPEA2resultDirectory<-"."

SPEA2qIndicator <- function(indicator)
{
	fileSPEA2<-paste(SPEA2resultDirectory, "SPEA2", sep="/")
	#fileSPEA2<-paste(fileSPEA2, "With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
	SPEA2_results<-scan(fileSPEA2)

fileSPEA2DKM<-paste(SPEA2resultDirectory, "SPEA2WithDKMutation", sep="/")
fileSPEA2DKM<-paste(fileSPEA2DKM,"With_mutation_pr_0.1", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileSPEA2DKM<-paste(fileSPEA2DKM, indicator, sep="/")
SPEA2DKM_results<-scan(fileSPEA2DKM)

algs<-c("PM","SM")
boxplot(SPEA2_results,SPEA2DKM_results,names=algs, notch = FALSE)
titulo <-paste(indicator, "SPEA2", sep=":")
title(main=titulo)
}

#SPEA2resultDirectory<-"./MutationStudySPEA2/data/"

#SPEA2qIndicator <- function(indicator, problem)
#{
#filePolynomialMutation<-paste(SPEA2resultDirectory, "PolynomialMutation", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, indicator, sep="/")
#PolynomialMutation<-scan(filePolynomialMutation)

#fileDKMutation<-paste(SPEA2resultDirectory, "DKMutation", sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
#fileDKMutation<-paste(fileDKMutation, indicator, sep="/")
#DKMutation<-scan(fileDKMutation)

#algs<-c("Polynomial","DKMutation")
#boxplot(PolynomialMutation,DKMutation,names=algs, notch = FALSE)
#titulo <-paste(indicator, "SPEA2", sep=":")
#title(main=titulo)
#}
par(mfrow=c(2,4),  cex.lab=2.0, cex.axis=1.5)
indicator<-"HV"
NSGAIIqIndicator(indicator)
indicator<-"IGD"
NSGAIIqIndicator(indicator)
indicator<-"Epsilon"
NSGAIIqIndicator(indicator)
indicator<-"Spread"
NSGAIIqIndicator(indicator)

indicator<-"HV"
SPEA2qIndicator(indicator)
indicator<-"IGD"
SPEA2qIndicator(indicator)
indicator<-"Epsilon"
SPEA2qIndicator(indicator)
indicator<-"Spread"
SPEA2qIndicator(indicator)

#indicator<-"HV"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"IGD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"GD"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")
#indicator<-"Epsilon"
#SPEA2qIndicator(indicator, "OptimizeElecEnergy_SPEA2")

dev.off()
