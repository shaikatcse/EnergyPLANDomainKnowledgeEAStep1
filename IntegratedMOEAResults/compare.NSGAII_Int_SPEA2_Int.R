#this scipt is used to generate a boxplot with four indicator (hypervolume, IGD, Epsilon, spead)

postscript("compare_NSGAII_Int_SPEA2_Int.eps", horizontal=FALSE, onefile=FALSE, height=6, width=8.5, pointsize=10)
#pdf("combined_comparison.pdf", onefile=FALSE, width=8.5, height = 6)
#jpeg(filename = "comNSGAII_SIandSPEA2_SI.jpeg", width = 8.5, height = 6.0, units = "in", pointsize = 10, res = 1000)


NSGAIIresultDirectory<-"."
SPEA2resultDirectory<-"."

NSGAIIqIndicator <- function(indicator)
{
fileNSGAII_Int<-paste(NSGAIIresultDirectory, "NSGAII_Int", sep="/")
fileNSGAII_Int<-paste(fileNSGAII_Int, indicator, sep="/")
NSGAII_Int_results<-scan(fileNSGAII_Int)

fileSPEA2_Int<-paste(SPEA2resultDirectory, "SPEA2_Int", sep="/")
fileSPEA2_Int<-paste(fileSPEA2_Int, indicator, sep="/")
SPEA2_Int_results<-scan(fileSPEA2_Int)

algs<-c("NSGAII_Int","SPEA2_Int")
boxplot(NSGAII_Int_results,SPEA2_Int_results,names=algs, notch = FALSE)
titulo <-paste(indicator)
title(main=titulo)
}


par(mfrow=c(2,4))
indicator<-"HV"
NSGAIIqIndicator(indicator)
indicator<-"IGD"
NSGAIIqIndicator(indicator)
indicator<-"Epsilon"
NSGAIIqIndicator(indicator)
indicator<-"Spread"
NSGAIIqIndicator(indicator)


dev.off()
