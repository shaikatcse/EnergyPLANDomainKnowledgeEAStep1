write("", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex",append=FALSE)
resultDirectory<-"C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/data"
latexHeader <- function() {
  write("\\documentclass{article}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\maketitle", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\caption{", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(problem, "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(".GD.}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(problem, "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(".GD.}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)

  write("\\centering", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(tabularString, "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\hline ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  write("\\end{table}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("OptimizeElecEnergy_NSGAII") 
algorithmList <-c("PolynomialMutation", "DKMutation") 
tabularString <-c("lc") 
latexTableFirstLine <-c("\\hline  & DKMutation\\\\ ") 
indicator<-"GD"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "DKMutation") {
      write(i , "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
      write(" & ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
          }
          if (j != "DKMutation") {
            write(" & ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | p{0.15cm}   | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{1}{c|}{DKMutation} \\\\") 

# Step 3. Problem loop 
latexTableHeader("OptimizeElecEnergy_NSGAII ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "DKMutation") {
    write(i , "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
    write(" & ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
          } 
          if (problem == "OptimizeElecEnergy_NSGAII") {
            if (j == "DKMutation") {
              write(" \\\\ ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "C:/Users/mahbub/Documents/GitHub/EnergyPLANDomainKnowledgeEAStep1/Results/MutationStudyNSGAII/R/NSGAII.GD.Wilcox.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

