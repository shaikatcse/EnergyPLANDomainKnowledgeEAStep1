package reet.fbk.eu.jmetal.operators.crossover;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Int;
import jmetal.encodings.variable.Real;
import reet.fbk.eu.jmetal.operators.crossover.SBXIntegerCrossoverV2;
import jmetal.problems.singleObjective.Sphere;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class SBXCrossoverMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap hm = new HashMap();

		double distributionIndex = 4.0;

		hm.put("probability", 1.0);
		hm.put("distributionIndex", distributionIndex);

		Problem problem = new Sphere("Real", 10);

		Int p1[] = new Int[1];
		p1[0] = new Int();

		Int p2[] = new Int[1];
		p2[0] = new Int();

		Solution s1 = new Solution();
		Solution s2 = new Solution();

		try {
			File file = new File("SBXInteger" + distributionIndex + ".txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			SBXIntegerCrossoverV2 sb = new SBXIntegerCrossoverV2(hm);
			//PolynomialIntegerMutation mu = new PolynomialIntegerMutation(hm);
			for (int i = 0; i < 10000000; i++) {

				int a;
				int b;
				/*System.out.println(a = new BigDecimal(
						PseudoRandom.randDouble() * 10000).setScale(0,
						RoundingMode.HALF_UP).doubleValue());
				System.out.println(b =   (((a % 50) >= (50/2)) ? (a + (50- (a%50))):( a-(a%50))) );*/

				//b=PseudoRandom.randInt(0,10);
				
				int uB = (int )PseudoRandom.randInt(0,10000);
				int lB= PseudoRandom.randInt(0,uB-1);
				
				int p1gv=PseudoRandom.randInt(lB,uB);
				int p2gv=PseudoRandom.randInt(lB,uB);
				
				
				//b=4;
				p1[0].setUpperBound(uB);
				p1[0].setLowerBound(lB);
				p1[0].setValue(p1gv);

				/*System.out.println(a = new BigDecimal(
						PseudoRandom.randDouble() * 10000).setScale(0,
						RoundingMode.HALF_UP).doubleValue());
				System.out.println(b = a - (a % 50));*/
				
				//b=PseudoRandom.randInt(0,1000);;
				
				//b=8;
				p2[0].setUpperBound(uB);
				p2[0].setLowerBound(lB);
				p2[0].setValue(p2gv);

				s1.setDecisionVariables((Variable[]) p1);
				s1.setType(new RealSolutionType(problem));

				s2.setDecisionVariables((Variable[]) p2);
				s2.setType(new RealSolutionType(problem));

				Solution[] offSprings = new Solution[2];
				
				
				offSprings = sb.doCrossover(1, s1, s2);
				/*offSprings[0]=mu.doMutation(1,s1);
				offSprings[1]=mu.doMutation(1,s2);*/
				
				
				/*offSprings[0].getDecisionVariables()[0].setValue((new BigDecimal(
						offSprings[0].getDecisionVariables()[0]
								.getValue()).setScale(0,
						RoundingMode.HALF_UP).doubleValue() ) - (new BigDecimal(
						offSprings[0].getDecisionVariables()[0]
								.getValue()).setScale(0,
						RoundingMode.HALF_UP).doubleValue() % 50));
						
				offSprings[1].getDecisionVariables()[0].setValue((new BigDecimal(
						offSprings[1].getDecisionVariables()[0]
								.getValue()).setScale(0,
						RoundingMode.HALF_UP).doubleValue() ) - (new BigDecimal(
						offSprings[1].getDecisionVariables()[0]
								.getValue()).setScale(0,
						RoundingMode.HALF_UP).doubleValue() % 50));*/
				
				String str = ""
						+ p1[0].getValue()
						+ "\t"
						+ p2[0].getValue()
						+ "\t"
						+ offSprings[0].getDecisionVariables()[0]
										.getValue() + "\t";
				str = str
						+ 	offSprings[1].getDecisionVariables()[0]
										.getValue()
						+ "\t"
						+ (p1[0].getValue() + p2[0].getValue())
						/ 2
						+ "\t"
						+ (offSprings[0].getDecisionVariables()[0].getValue() + offSprings[1]
								.getDecisionVariables()[0].getValue()) / 2
						+ "\n";
				bw.write(str);

			}
			bw.close();

		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
