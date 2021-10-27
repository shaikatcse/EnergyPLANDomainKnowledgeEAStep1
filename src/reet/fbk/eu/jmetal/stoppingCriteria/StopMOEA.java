package reet.fbk.eu.jmetal.stoppingCriteria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

/*
 * This class contains all the ingredients to make decision about stopping a MOEA
 */
public class StopMOEA {

	/*
	 * The following parameters are set for the GECCO 15 paper
	 */
	/*static final int nGenLT = 30, nGenUnCh = 10;
	static final double significanceValue = 0.05;*/
	
	/*
	 * New parameters for Aalborg Simulaiton
	 */
	/*static final int nGenLT = 20, nGenUnCh = 5;
	static final double significanceValue = 0.05;*/
	
	/*
	 * New parameters for Aalborg Simulaiton
	 */
	/*static final int nGenLT = 25, nGenUnCh = 5;
	static final double significanceValue = 0.05;*/
	
	int nGenLT, nGenUnCh;
	double significanceValue;
	
	
	AveragedHausdroffDistance ahd;
	CalculateDiversity cdv;
	List<Double> ahdList, dvList;

	List<Double> pValueHDList, pValueDVList;

	double ahdArray[];
	double dvArray[];

	double pValueHD = -1.0, pValueDV = -1.0;

	double[][] iMinus1SolFrontDouble;
	
	public StopMOEA(int nGenLT, int nGenUnCh, double alpha){
		this.nGenLT = nGenLT;
		this.nGenUnCh=nGenUnCh;
		this.significanceValue=alpha;
		
		ahd = new AveragedHausdroffDistance();
		cdv = new CalculateDiversity();
		ahdList = new ArrayList<Double>();
		dvList = new ArrayList<Double>();

		pValueHDList = new LinkedList<Double>();
		pValueDVList = new LinkedList<Double>();
	}
	
		
	public StopMOEA() {
		ahd = new AveragedHausdroffDistance();
		cdv = new CalculateDiversity();
		ahdList = new ArrayList<Double>();
		dvList = new ArrayList<Double>();

		pValueHDList = new LinkedList<Double>();
		pValueDVList = new LinkedList<Double>();
	}

	/*
	 * return true -> if it meets the stopping criteria
	 */
	public boolean isStopMOEA(int genNum, SolutionSet iSolFront,
			int numberOfObjectives, int numberOfVariables) throws JMException {
		double[][] iSolFrontDouble, iSolFrontDSDouble;
		iSolFrontDouble = convertSolutionSetToDoubleObjectives(iSolFront,
				numberOfObjectives);
		iSolFrontDSDouble = convertSolutionSetToDoubleDecisionVariables(
				iSolFront, numberOfVariables);

		Double ahdValue = -1.0, dvVAlue = -1.0;

		if (genNum != 1) {
			ahdValue = ahd.calclulate_ithGenerationAHD(iSolFrontDouble,
					iMinus1SolFrontDouble, numberOfObjectives);
			ahdList.add(ahdValue);
		}
		update_iMinus1SolFront(iSolFrontDouble);
		
		dvVAlue = cdv.calculateDiversityOf_ithGeneration(iSolFrontDSDouble,
				numberOfVariables);
		dvList.add(dvVAlue);

		if (genNum == nGenLT) {
			ahdArray = convertDoubles(ahdList, 0, genNum - 2);
			dvArray = convertDoubles(dvList, 0, genNum - 1);

			pValueHD = TtestOnSignificanceOfLinearTrend
					.doSignificanceTest(ahdArray);
			pValueDV = TtestOnSignificanceOfLinearTrend
					.doSignificanceTest(dvArray);

			pValueHDList.add(pValueHD);
			pValueDVList.add(pValueDV);
			
			checkNaN(ahdArray);
			checkNaN(dvArray);

		}
		if (genNum > nGenLT) {

			ahdArray = convertDoubles(ahdList, genNum - nGenLT - 1, genNum - 2);
			dvArray = convertDoubles(dvList, genNum - nGenLT, genNum - 1);

			pValueHD = TtestOnSignificanceOfLinearTrend
					.doSignificanceTest(ahdArray);
			pValueDV = TtestOnSignificanceOfLinearTrend
					.doSignificanceTest(dvArray);

			pValueHDList.add(pValueHD);
			pValueDVList.add(pValueDV);

			if (pValueDVList.size() > nGenUnCh)
				pValueDVList.remove(0);
			if (pValueHDList.size() > nGenUnCh)
				pValueHDList.remove(0);

			
			checkNaN(ahdArray);
			checkNaN(dvArray);
		}
		
		if (pValueDVList.size() >= nGenUnCh) {
			if (!checkSignificanceTwoList(pValueDVList, pValueHDList)) {
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	public void checkNaN(double [] array){
		for(int i=0;i<array.length;i++){
			if(Double.isNaN(array[i])){
				System.out.println("NaN");
				System.exit(0);
			}
				
		}
	}
	public void update_iMinus1SolFront(double[][] sol) {
		iMinus1SolFrontDouble = new double [sol.length][];
		for(int i =0;i<sol.length;i++){
			iMinus1SolFrontDouble[i] = sol[i];
		}
	}

	public boolean checkSignificanceTwoList(List<Double> pValueDVList,
			List<Double> pValueHDList) {
		boolean track = false;
		for (int i = 0; i < pValueDVList.size(); i++) {
			if (pValueDVList.get(i) < significanceValue
					|| pValueHDList.get(i) < significanceValue) {
				track = true;
				break;
			}

		}
		return track;
	}

	public double[] convertDoubles(List<Double> doubles, int start, int end) {
		double[] ret = new double[end - start + 1];
		int i = 0;
		for (int j = start; j <= end; j++)
			ret[i++] = doubles.get(j);
		return ret;
	}

	public double[][] convertSolutionSetToDoubleObjectives(SolutionSet sol,
			int numberOfObjectives) {
		double[][] front = new double[sol.size()][numberOfObjectives];
		for (int i = 0; i < sol.size(); i++) {
			for (int j = 0; j < numberOfObjectives; j++) {
				front[i][j] = sol.get(i).getObjective(j);
			}
		}
		return front;
	}

	public double[][] convertSolutionSetToDoubleDecisionVariables(
			SolutionSet sol, int numberOfVariable) throws JMException {
			 
			double[][] front = new double[sol.size()][numberOfVariable];
			for (int i = 0; i < sol.size(); i++) {
				XReal solution = new XReal(sol.get(i));
				for (int j = 0; j < numberOfVariable; j++) {
					front[i][j] = solution.getValue(j);
				}
			}
			return front;
		
	}
}
