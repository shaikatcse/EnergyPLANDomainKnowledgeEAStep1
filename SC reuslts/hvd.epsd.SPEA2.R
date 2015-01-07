postscript("SPEA2_SC_indicators.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
#pdf("SPEA2SC.pdf", width=12, height=8,pointsize=12) 
resultDirectory<-"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/StoppingCriteriaStudies/data/SPEA2SC"
qIndicator <- function(problem)
{
filePolynomialMutation<-paste(resultDirectory, problem, sep="/")
#filePolynomialMutation<-paste(filePolynomialMutation, problem, sep="/")
filePolynomialMutation<-paste(filePolynomialMutation, "HVD", sep="/")
PolynomialMutation<-scan(filePolynomialMutation)

fileDKMutation<-paste(resultDirectory, problem, sep="/")
#fileDKMutation<-paste(fileDKMutation, problem, sep="/")
fileDKMutation<-paste(fileDKMutation, "EpsD", sep="/")
DKMutation<-scan(fileDKMutation)

ind<-c(expression('HV'[d]), expression('eps'[d]))
boxplot(PolynomialMutation,DKMutation,names=ind, notch = FALSE, outline=FALSE)
abline(h=0.0)
titulo <-paste(problem)
title(main=titulo)
}

par(mfrow=c(2,3))
prob1<-"ZDT1"
qIndicator(prob1)
prob1<-"ZDT2"
qIndicator(prob1)
prob1<-"ZDT3"
qIndicator(prob1)
prob1<-"ZDT4"
qIndicator(prob1)
prob1<-"DTLZ2"
qIndicator(prob1)
prob1<-"DTLZ5"
qIndicator(prob1)


dev.off()