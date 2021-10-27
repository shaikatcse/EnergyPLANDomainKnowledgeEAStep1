package reet.fbk.eu.jmetal.encodings.variable;

//BinaryReal.java
//
//Author:
//   Antonio J. Nebro <antonio@lcc.uma.es>
//   Juan J. Durillo <durillo@lcc.uma.es>
//
//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.


import java.util.BitSet;

import jmetal.core.Variable;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

import jmetal.encodings.variable.Binary;

/**
* This class extends the Binary class to represent a Real variable encoded by a
* binary string
*/
public class BinaryInt extends Binary {

/**
 * Stores the real value of the variable
 */
private int value_;

/**
 * Stores the lower limit for the variable
 */
private int lowerBound_;

/**
 * Stores the upper limit for the variable
 */
private int upperBound_;

/**
 * Constructor.
 */
public BinaryInt() {
	super();
} // BinaryInt

/**
 * Constructor
 * 
 * @param numberOfBits
 *            Length of the binary string.
 * @param lowerBound
 *            The lower limit for the variable
 * @param upperBound
 *            The upper limit for the variable.
 */
public BinaryInt(int numberOfBits, int lowerBound, int upperBound) {
	super(numberOfBits);
	lowerBound_ = lowerBound;
	upperBound_ = upperBound;

	decode() ;
	if(this.value_ > upperBound){
		this.setValue(upperBound);
	}else if(value_<lowerBound){
		this.setValue(lowerBound);
	}
} // BinaryReal

/**
 * @param bits
 * @param nbBits
 * @param lowerBound
 * @param upperBound
 */
public BinaryInt(BitSet bits, int nbBits, int lowerBound, int upperBound) {
	super(nbBits);
	bits_ = bits;
	lowerBound_ = lowerBound;
	upperBound_ = upperBound;
	decode();
	
	if(this.value_ > upperBound){
		this.setValue(upperBound);
	}else if(value_<lowerBound){
		this.setValue(lowerBound);
	}
} // BinaryReal

/**
 * Copy constructor
 * 
 * @param variable
 *            The variable to copy
 */
public BinaryInt(BinaryInt variable) {
	super(variable);

	lowerBound_ = variable.lowerBound_;
	upperBound_ = variable.upperBound_;
	/*
	 * numberOfBits_ = variable.numberOfBits_;
	 * 
	 * bits_ = new BitSet(numberOfBits_); for (int i = 0; i < numberOfBits_;
	 * i++) bits_.set(i,variable.bits_.get(i));
	 */
	value_ = variable.value_;
} // BinaryReal

/**
 * Decodes the real value encoded in the binary string represented by the
 * <code>BinaryReal</code> object. The decoded value is stores in the
 * <code>value_</code> field and can be accessed by the method
 * <code>getValue</code>.
 */
public void decode() {
	int value = 0;
	//String binaryBits = "";
	for (int i = 0; i < numberOfBits_; i++) {
		if (bits_.get(i)) {
			value+= Math.pow(2,i);
		}
		
	}
	//value = Integer.parseInt(binaryBits);

	value_ = value;
} // decode

/**
 * Returns the double value of the variable.
 * 
 * @return the double value.
 */
public double getValue() {
	return value_;
} // getValue

/**
 * This implementation is efficient for binary string of length up to 24
 * bits, and for positive intervals.
 * 
 * @see jmetal.core.Variable#setValue(double)
 * 
 * @author jl hippolyte
 */
@Override
public void setValue(double value) //throws JMException 
{
	int intValue = (int) value;
/*		if (intValue < lowerBound_ || intValue > upperBound_) {

		throw new JMException("BinaryInt: the value cross the limit");
	} else {*/
		BitSet bitSet;
		bitSet = new BitSet(numberOfBits_);
		bitSet.clear();
		String binaryString = Integer.toBinaryString(intValue);
		String tmpString = new StringBuilder(binaryString).reverse().toString();
		for(int i=tmpString.length()-1;i>=0;i--){
			if(tmpString.charAt(i)=='1'){
				bitSet.set(i);
			}
		}
		this.bits_ = bitSet;
		this.decode();
	//}
}// setValue

/**
 * Creates an exact copy of a <code>BinaryReal</code> object.
 * 
 * @return The copy of the object
 */
public Variable deepCopy() {
	return new BinaryInt(this);
} // deepCopy

/**
 * Returns the lower bound of the variable.
 * 
 * @return the lower bound.
 */
public double getLowerBound() {
	return lowerBound_;
} // getLowerBound

/**
 * Returns the upper bound of the variable.
 * 
 * @return the upper bound.
 */
public double getUpperBound() {
	return upperBound_;
} // getUpperBound

/**
 * Sets the lower bound of the variable.
 * 
 * @param lowerBound
 *            the lower bound.
 */
public void setLowerBound(int lowerBound) {
	lowerBound_ = lowerBound;
} // setLowerBound

/**
 * Sets the upper bound of the variable.
 * 
 * @param upperBound
 *            the upper bound.
 */
public void setUpperBound(int upperBound) {
	upperBound_ = upperBound;
} // setUpperBound

/**
 * Returns a string representing the object.
 * 
 * @return the string.
 */
public String toString() {
	return value_ + "";
} // toString
} // BinaryReal
