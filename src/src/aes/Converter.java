package aes;

public class Converter {

	/**
	 * MixColumn: State array and polynomial equivalent matrix are multiplied by
	 * matrix multiplication matrix on GF(2) in finite domain
	 * 
	 * @param state state matrix
	 * @param table Polynomial equivalent matrix
	 * @return new state of the column mix
	 */
	public static short[][] mixColumns(short[][] state, short[][] table) {
		short[][] result = new short[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result[i][j] = 0;
				for (int k = 0; k < 4; k++) {
					result[i][j] = (short) (result[i][j] ^ (multiply(table[i][k],state[k][j])));
				}
			}
		}

		return result;
	}
	
	public static short multiply2(short a, short b) {
	short result = 0;
	if (a == 0x02) {
		if ((b & 0x80) == 0x80) {
			b <<= 1;
			b = (short) (b & 0xff);
			result = (short) (b ^ 0x1b);
		} else {
			b <<= 1;
			b = (short) (b & 0xff);
			result = b;
		}
	} else if (a == 0x03) {
		result = (short) (multiply2((short) 0x02,b) ^ b);
	}
	else {
		result = b;
	}

	return (short) (result & 0xff);
}
	

	/**
	 * The multiplication operation on GF(2) in the finite domain is transformed
	 * into the multiplication operation on GF(2) in the finite domain by
	 * decomposing the operands
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static short multiply(short a, short b) {
		short temp = 0;
		while (b != 0) {
			if ((b & 0x01) == 1) {
				temp ^= a;
			}
			a <<= 1;
			if ((a & 0x100) > 0) {
				a ^= 0x1b;
			}
			b >>= 1;
		}
		return (short) (temp & 0xff);
	}

	/**
	 * Row shift: loops the state rows to the left
	 * 
	 * @param state
	 * @param shiftingTable
	 * @return
	 */
	public static short[][] shiftRows(short[][] state, short[][] shiftingTable) {
		short[][] result = new short[4][4];
		for (int j = 0; j < 4; j++) { // local byte in a word
			for (int i = 0; i < 4; i++) { // local word
				result[i][j] = state[shiftingTable[i][j]][j];
			}
		}
		return result;
	}

	/**
	 * Addition to GF(2) in a finite domain
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static short[][] xor(short[][] first, short[][] second) {
		short[][] result = new short[first.length][4];
		int length = first.length;
		for (short i = 0; i < length; i++) {
			for (short j = 0; j < length; j++) {
				result[i][j] = (short) (first[i][j] ^ second[i][j]);
			}
		}
		return result;
	}

	/**
	 * An xor operation between two words
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static short[] xor(short[] first, short[] second) {
		short[] result = new short[4];
		for (short i = 0; i < 4; i++) {
			result[i] = (short) (first[i] ^ second[i]);
		}
		return result;
	}

	/**
	 * State substitution: word substitution for each word in a state
	 * 
	 * @param state
	 * @param substituteTable
	 * @return
	 */
	public static short[][] substituteState(short[][] state, short[][] substituteTable) {
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = substituteByte(state[i][j], substituteTable);
			}
		}
		return state;
	}

	/**
	 * Word substitution: byte substitution for each byte in a word take the high
	 * and low four bits of a word as the row number and column number of S box
	 * respectively, and replace the original bytewith the byte in S box by the
	 * column number
	 * 
	 * @param aWord
	 * @return
	 */
	public static short[] substituteWord(short[] aWord) {
		for (int i = 0; i < 4; i++) {
			aWord[i] = substituteByte(aWord[i], MyConverter.SUBSTITUTE_BOX);
//			int low4 = aWord[i] & 0x000f;
//			int high4 = (aWord[i] >> 4) & 0x000f;
//			aWord[i] = MyConverter.SUBSTITUTE_BOX[low4][high4];
		}
		return aWord;
	}

	/**
	 * Byte substitution: take the high and low four bits of a word as the row
	 * number and column number of S box respectively, and replace the original byte
	 * with the byte in S box by the column number
	 * 
	 * @param originalByte
	 * @param substituteTable
	 * @return
	 */
	public static short substituteByte(short originalByte, short[][] substituteTable) {
		int low4Bits = originalByte & 0x000f;
		int high4Bits = (originalByte >> 4) & 0x000f;
		return substituteTable[high4Bits][low4Bits];
	}

	/**
	 * The loop moves left by 1 in bytes
	 * 
	 * @param aWord
	 * @return
	 */
	public static short[] leftShift(short[] aWord) {
		short[] result = new short[4];
		for (int i = 0; i < 4; i++) {
			result[i] = aWord[MyConverter.LEFT_SHIFT_TABLE[i]];
		}
		return result;
	}
}
