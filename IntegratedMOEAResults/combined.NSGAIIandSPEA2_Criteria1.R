#postscript("indicators_NSGAII_SPEA2.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
#pdf("indicators_NSGAII_NSGAII_DKM.pdf", onefile=FALSE, width=10)
postscript("NSGAII_SPEA2_Int_test.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=11)

NSGAIIresultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
	fileNSGAII<-paste(NSGAIIresultDirectory, "NSGAII", sep="/")
	#fileNSGAII<-paste(fileNSGAII,"With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
	NSGAII_results<-scan(fileNSGAII)

fileNSGAII_Int<-paste(NSGAIIresultDirectory, "NSGAII_int", sep="/")
fileNSGAII_Int<-paste(fileNSGAII_Int, "WithoutRM", sep="/")
fileNSGAII_Int<-paste(fileNSGAII_Int, "With_mutation_pr_0.1", sep="/")
fileNSGAII_Int<-paste(fileNSGAII_Int, "Criteria1", sep="/")

#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileNSGAII_Int<-paste(fileNSGAII_Int, indicator, sep="/")
NSGAII_Int_results<-scan(fileNSGAII_Int)

algs<-c("NSGAII","NSGAII_Int")
boxplot(NSGAII_results,NSGAII_Int_results,names=algs, notch = FALSE)
titulo <-indicator
title(main=titulo, cex.main=1.5)
}

SPEA2resultDirectory<-"."

SPEA2qIndicator <- function(indicator)
{
	fileSPEA2<-paste(SPEA2resultDirectory, "SPEA2", sep="/")
	#fileSPEA2<-paste(fileSPEA2, "With_mutation_pr_0.2", sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
	fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
	SPEA2_results<-scan(fileSPEA2)

fileSPEA2_Int<-paste(SPEA2resultDirectory, "SPEA2_Int", sep="/")
fileSPEA2_Int<-paste(fileSPEA2_Int, "WithoutRM", sep="/")
fileSPEA2_Int<-paste(fileSPEA2_Int, "With_mutation_pr_0.1", sep="/")
fileSPEA2_Int<-paste(fileSPEA2_Int, "Criteria1", sep="/")

#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileSPEA2_Int<-paste(fileSPEA2_Int, indicator, sep="/")
SPEA2_Int_results<-scan(fileSPEA2_Int)

algs<-c("SPEA2","SPEA2_Int")
boxplot(SPEA2_results,SPEA2_Int_results,names=algs, notch = FALSE)
titulo <-indicator
title(main=titulo, cex.main=1.5)
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
par(mfrow=c(2,4), cex.axis=1.0, cex.lab=2.0)
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


