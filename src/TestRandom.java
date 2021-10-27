import java.util.Random;

import jmetal.util.*;

public class TestRandom {
	public static void main(String args[]){
		
		PseudoRandom.setRandomGenerator(new RandomGenerator(1500));
		Random rm=new Random();
		
		System.out.println(PseudoRandom.randInt());
		System.out.println(PseudoRandom.randDouble());
		System.out.println(rm.nextInt(100));
		System.out.println(PseudoRandom.randInt());
		System.out.println(PseudoRandom.randDouble());
		
	}
}
