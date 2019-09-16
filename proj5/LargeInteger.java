//Celine Cui
//4.15.2019
import java.util.Random;
import java.math.BigInteger;

public class LargeInteger {
	
	private final byte[] ONE = {(byte) 1};

	private byte[] val;

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = b;
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}
	
	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/** 
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}
	//recursive call for reducing extra useless byte
	public LargeInteger adjust_extra_byte(LargeInteger other) {
		if (other.length() > 1 && (((other.getVal()[0] == 0) && (((other.getVal()[1] & 0x80) >> 7) == 0))
				|| ((other.getVal()[0] & 0xff) == 0xff && ((other.getVal()[1] & 0x80) >> 7) == 1))) {
			byte[] newLargeInteger = new byte[other.length() - 1];
			for (int i = 1; i <= newLargeInteger.length; i++)
				newLargeInteger[i - 1] = other.val[i];
			return adjust_extra_byte(new LargeInteger(newLargeInteger));
		}
		return other;
	}
	
	/**
	 * If this is negative, most significant bit will be 1 meaning most 
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);
	
		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's 
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);
	
		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}
	
	private LargeInteger shiftLeft() {
		byte[] newLargeInteger;
		int a = 0;
		//avoid negative number after shifting, so at the second msb position, needs to check if it is 1
		//since the first byte of the original number should start with 0, so 0x11000000 will make sure the first bit is 0.
		if((val[0] & 0xC0) == 0x40)
			newLargeInteger = new byte[val.length + 1];
		else newLargeInteger = new byte[val.length];
		int prev_msb = 0;
		int msb;
		int val_length = val.length;
		int new_length = newLargeInteger.length;
		for(int i = 0; i < val.length; i++) {
			//record the msb for byte i
			msb = (val[val_length - (i+1)] & 0x80) >> 7;
			newLargeInteger[new_length - (i+1)] = (byte) (val[val_length - (i+1)] << 1);
			newLargeInteger[new_length - (i+1)] |= prev_msb;
			prev_msb = msb;
		}
		return new LargeInteger(newLargeInteger);
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */
	public LargeInteger multiply(LargeInteger other) {
		LargeInteger multiplier = this;
		LargeInteger multiplicand = other;
		int is_negative = 0;
		if (this.isNegative()){
			multiplier = this.negate();
			is_negative++;
			}
		if (other.isNegative()){
			multiplicand = other.negate();
			is_negative++;
			}
		//create a LargeInteger which is large enough for the product
		LargeInteger product = new LargeInteger(new byte[this.length() + other.length()]);
		//for each byte in the multiplier
		for (int i = multiplier.length() - 1; i >= 0; i--) {
			int currentBit = 1; //bit counter
			//for each bit in byte i
			for (int j = 8; j > 0; j--) {
				if ((currentBit & multiplier.getVal()[i]) > 0)
					product = product.add(multiplicand);
				currentBit = currentBit << 1;
				multiplicand = multiplicand.shiftLeft();
			}
		}
		if (is_negative == 1) product = product.negate();
		return adjust_extra_byte(product);
	}
	
	public LargeInteger shiftRight() {
		byte[] newLargeInteger;
		int i;
		if(val[0] == 0 && ((val[1] & 0x80)>>7) == 1) {
			newLargeInteger = new byte[val.length - 1];
			i = 1;
		} else {
			newLargeInteger = new byte[val.length];
			i = 0;
		}
		int prev_lsb = 0;
		int lsb;
		if(this.isNegative()) prev_lsb = 1;

		for(int j = 0; j < newLargeInteger.length; j++, i++) {
			lsb = val[i] & 1; //get the lsb by and 1
			newLargeInteger[j] = (byte) ((val[i] >> 1) & 0x7f);//make sure msb of current byte is 0
			newLargeInteger[j] |= (prev_lsb << 7); //or the previous lsb
			prev_lsb = lsb;
		}
		return adjust_extra_byte(new LargeInteger(newLargeInteger));
	}
	
	public LargeInteger mod(LargeInteger other) {
	 	//this % other = this - other*(this divide other)
		return adjust_extra_byte(this.subtract(other.multiply(this.divide(other))));
	}
	
	public LargeInteger divide(LargeInteger other) {
		LargeInteger quotient = new LargeInteger(new byte[]{(byte)0});
		LargeInteger dividend = this;
		LargeInteger divisor = other;
		int is_negative = 0;
		if (this.isNegative()) {
			is_negative++;
			dividend = this.negate();
		}
		if (other.isNegative()) {
			divisor = other.negate();
			is_negative++;
		}
		int shift = 0;
		//while dividend > divisor
		while (!dividend.subtract(divisor).isNegative()) {
			divisor = divisor.shiftLeft();
			shift++;
		}
		divisor = divisor.shiftRight();

		while (shift > 0) {
			quotient = quotient.shiftLeft();
			if (!((dividend.subtract(divisor)).isNegative())) {
				dividend = dividend.subtract(divisor);
				quotient = quotient.add(new LargeInteger(ONE));
			}
			divisor = divisor.shiftRight();
			shift--;
		}
		if(is_negative == 1) quotient=quotient.negate();
		return adjust_extra_byte(quotient);
	}
	
	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public LargeInteger[] XGCD(LargeInteger other) {
		if (other.equals(new LargeInteger(new byte[]{(byte)0}))) {
		 	//gcd(23, 0) = 23*1+0*0 x=1, y=0
			 //return {3, 1, 0}
			 return new LargeInteger[]{this, new LargeInteger(ONE), new LargeInteger(new byte[]{(byte)0})};
		 } else {
			 //gcd(99, 78) before = 99, other = after = 78
			 //= gcd(78, 99 mod 78 = 21)
			 //= gcd(21, 78 mod 21 = 15)
			 //= gcd(15, 21 mod 15 = 6)
			 //= gcd(6, 15 mod 6 = 3)
			 //= gcd(3, 6 mod 3 = 0); gcd(3,0)=3*1+0*0 this = 3, other = 0
			 LargeInteger[] array = other.XGCD(this.mod(other));
			 //{3, 0, 1}: 3 = 6*0 + 3*1
			 //{3, 1, -2}: 3 = 15*1 + 6*(-2)
			 //{3, -2, 3}: 3 = 21*(-2) + 15*3
			 //{3, 3, -11}: 3 = 78*3 + 21*(-11)
			 //{3, -11, 14}: 3 = 99*(-11) + 78*14
			 //sub in and get the coefficient
			 return new LargeInteger[]{array[0], adjust_extra_byte(array[2]),
					 adjust_extra_byte(array[1].subtract(this.divide(other).multiply(array[2])))};
		 }
	 }

	 /**
	  * Compute the result of raising this to the power of y mod n
	  * @param y exponent to raise this to
	  * @param n modulus value to use
	  * @return this^y mod n
	  */
	 public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
		//d = e^-1 mod(φ(n)); if y = -1, d = xgcd()
		 if(y.equals(new LargeInteger(ONE).negate())) {
			 LargeInteger[] array = n.XGCD(this);
			 if (array[2].isNegative())
				 return n.add(array[2]);
			 else
				 return array[2];
		 } else {
		 	//this = 3; y = 7; n = 5
			 // 3^7 = 3^1*3^2*3^4 = 3*4*1 mod 5 = 2
			 LargeInteger result = new LargeInteger(ONE);
			 LargeInteger separate = this.mod(n); // 3 mod 5 = 3
			 LargeInteger power = y;
			 while (!power.isNegative() && !power.equals(new LargeInteger(new byte[]{(byte)0}))) {
			 	//when power is odd
				 if (power.mod(new LargeInteger(new byte[]{(byte)2})).equals(new LargeInteger(ONE))) {
					 //result = 1*3*4 (mod 5) = 2
					 result = (result.multiply(separate)).mod(n);
				 }
				 power = power.shiftRight(); //power divide by 2
				 //3^1 mod 5 = 3
				 //3^2 mod 5 = 9 ~ 4
				 //3^4 mod 5 = 4^2 = 16 ~ 1
				 separate = (separate.multiply(separate)).mod(n); //3^2; (3^2)^2;...
			 }
			 return result;
		 }
	 }
	 
	 public LargeInteger resize() {
		byte[] temp = val;
		while (temp.length > 64) {
			byte[] temptemp = new byte[this.length() - 1];
			for (int i = 1; i <= temptemp.length; i++)
				temptemp[i - 1] = temp[i];
			temp = temptemp;
		}
		return new LargeInteger(temp);
	}
	public boolean equals(LargeInteger other) {
		if (this.length() != other.length()) return false;
		for (int i = 0; i < this.length(); i++){
			if (this.getVal()[i] != other.getVal()[i])
				return false;
		}
		return true;
	}
}