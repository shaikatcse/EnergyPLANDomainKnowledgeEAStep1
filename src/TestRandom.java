import jmetal.util.*;

public class TestRandom {
	public static void main(String args[]){
		
		PseudoRandom.setRandomGenerator(new RandomGenerator(1500));
		System.out.println(PseudoRandom.randInt());
		System.out.println(PseudoRandom.randDouble());
		System.out.println(PseudoRandom.randInt());
		System.out.println(PseudoRandom.randDouble());
		
	}
}
