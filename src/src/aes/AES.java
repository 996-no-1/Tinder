package aes;


public class AES {

	// Column confuses transformation matrix
	public static short[][] CX = { { 0x02, 0x03, 0x01, 0x01 }, { 0x01, 0x02, 0x03, 0x01 }, { 0x01, 0x01, 0x02, 0x03 },
			{ 0x03, 0x01, 0x01, 0x02 }, };
	// Column obfuscation transformation inverse matrix
	public static short[][] INVERSE_CX = { { 0x0E, 0x0B, 0x0D, 0x09 }, { 0x09, 0x0E, 0x0B, 0x0D },
			{ 0x0D, 0x09, 0x0E, 0x0B }, { 0x0B, 0x0D, 0x09, 0x0E }, };

	public static short[] LEFT_SHIFT_TABLE = { 1, 2, 3, 0 };

	// Array of wheel constants
	public static short[][] R_CON = { { 0x00, 0x00, 0x00, 0x00 }, { 0x01, 0x00, 0x00, 0x00 },
			{ 0x02, 0x00, 0x00, 0x00 }, { 0x04, 0x00, 0x00, 0x00 }, { 0x08, 0x00, 0x00, 0x00 },
			{ 0x10, 0x00, 0x00, 0x00 }, { 0x20, 0x00, 0x00, 0x00 }, { 0x40, 0x00, 0x00, 0x00 },
			{ 0x80, 0x00, 0x00, 0x00 }, { 0x1b, 0x00, 0x00, 0x00 }, { 0x36, 0x00, 0x00, 0x00 }, };

	// The matrix that determines the left shift rule of the interword byte loop in
	// the row shift transform of cryptographic operations
	public static short[][] SHIFTING_TABLE = { { 0, 1, 2, 3 }, { 1, 2, 3, 0 }, { 2, 3, 0, 1 }, { 3, 0, 1, 2 }, };

	// The matrix that determines the left shift rule of the interword byte loop in
	// the row shift transform of the decryption operation
	public static short[][] INVERSE_SHIFTING_TABLE = { { 0, 3, 2, 1 }, { 1, 0, 3, 2 }, { 2, 1, 0, 3 },
			{ 3, 2, 1, 0 }, };
	public static short[][] SUBSTITUTE_BOX = {
			{ 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76 },
			{ 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0 },
			{ 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15 },
			{ 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75 },
			{ 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84 },
			{ 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf },
			{ 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8 },
			{ 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2 },
			{ 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73 },
			{ 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb },
			{ 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79 },
			{ 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08 },
			{ 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a },
			{ 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e },
			{ 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf },
			{ 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 }, };

	public static short[][] INVERSE_SUBSTITUTE_BOX = {
			{ 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb },
			{ 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb },
			{ 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e },
			{ 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25 },
			{ 0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92 },
			{ 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84 },
			{ 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06 },
			{ 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b },
			{ 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73 },
			{ 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e },
			{ 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b },
			{ 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4 },
			{ 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f },
			{ 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef },
			{ 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61 },
			{ 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d }, };

	public static byte[] supplement = new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P' };
	
	/**
	 * used to generate roundKeys
	 * 
	 * @param key
	 * @return
	 */
	private short[][][] dealKey(String key) {
		short[][] initialKeyState = transfer(transferToShorts(key));
		short[][] rawRoundKeys = generateRoundKeys(initialKeyState);
		short[][][] roundKeys = transfer(rawRoundKeys);
		return roundKeys;
	}

	/**
	 * Convert the string to a one-dimensional short array
	 * 
	 * @param String
	 * @return short[]
	 */
	private short[] transferToShorts(String string) {
		byte[] bytes = string.getBytes();
		int length = bytes.length;
		byte[] newBytes = new byte[16];

		short[] shorts = new short[16];
		if (length <= 16) {
			System.arraycopy(bytes, 0, newBytes, 0, length);
		} else {
			System.arraycopy(bytes, 0, newBytes, 0, 16);
		}
		for (int i = 0; i < 16; i++) {
			shorts[i] = newBytes[i];
		}
//		for(int i=0; i<shorts.length; i++) {
//			System.out.print(shorts[i]+" ");
//		}
		return shorts;
	}

	/*
	 * Converts a 16-byte string into a 4-by-4 matrix store
	 */
	private short[][] transfer(short[] origin) {
		short[][] result = new short[origin.length / 4][4];
		for (int i = 0; i < result.length; i++) {
			System.arraycopy(origin, i * 4, result[i], 0, 4);
		}
//		System.out.println(result[0][3]);
		return result;
	}

	/*
	 * expand the key
	 */
	private static short[][] generateRoundKeys(short[][] originalKey) {
		short[][] roundKeys = new short[44][4];
		System.arraycopy(originalKey, 0, roundKeys, 0, 4);
		for (int i = 4; i < 44; i++) {
			short[] temp = roundKeys[i - 1];
			if (i % 4 == 0) {
				// temp = substituteWord(Leftshift(temp))⊕Rcon[i/4];
				temp = Converter.xor(Converter.substituteWord(Converter.leftShift(temp)), R_CON[i / 4]);
			}
			roundKeys[i] = Converter.xor(roundKeys[i - 4], temp);
		}
		return roundKeys;
	}

	/*
	 * used to get 11 roundkeys
	 */
	private short[][][] transfer(short[][] origin) {
		// [11][4][4]
		short[][][] result = new short[11][4][4];
		for (int i = 0; i < 11; i++) {
			short[][] temp = new short[4][4];
			System.arraycopy(origin, i * 4, temp, 0, 4);
			result[i] = temp;
		}
		return result;
	}

	/**
	 * This function is used to get format String[] array
	 * 
	 * @param p
	 * @param num
	 * @return String[]
	 */
	private String[] MyPart(byte[] p, int num) {
		// used to Encrytion
		if (num == 16) {
			int origin_length = p.length;
			int g_num;
			int r_num;
			r_num = num - (origin_length % num);
			byte[] p_padding;
			/**** fill ********/

			p_padding = new byte[origin_length + r_num];
			System.arraycopy(p, 0, p_padding, 0, origin_length);
			for (int i = 0; i < r_num; i++) {
				p_padding[origin_length + i] = supplement[r_num - 1];
			}

			g_num = p_padding.length / num;
			String[] temp = new String[g_num];
			byte[] f_p = new byte[num];
			for (int i = 0; i < g_num; i++) {
				System.arraycopy(p_padding, i * num, f_p, 0, num);
				temp[i] = new String(f_p);
			}
			return temp;
		} else {
			// used to Decyption
			int n = p.length / 32;
			String[] temp = new String[n];
			byte[] tp = new byte[32];
			for (int i = 0; i < n; i++) {
				System.arraycopy(p, i * 32, tp, 0, 32);
				temp[i] = new String(tp);
			}
			return temp;
		}

	}

	/**
	 * 
	 * @param temp
	 * @return short[]
	 */
	private short[] stringToHex(String[] temp) {
		short[] ciphertexts = new short[temp.length];
		for (int i = 0; i < temp.length; i++) {
			ciphertexts[i] = (short) Integer.parseInt(temp[i], 16);
		}
		return ciphertexts;
	}

	/**
	 * Restore the eventually decrypted short array to a string
	 * 
	 * @param decryptState
	 * @return
	 */
	private String getOriginString(short[][] decryptState) {
		StringBuilder builder = new StringBuilder();
		for (short[] shorts : decryptState) {
			for (short s : shorts) {
				builder.append(String.valueOf((char) s));
			}
		}
		return builder.toString();
	}

	/**
	 * Reverses the decrypted extended key array
	 * 
	 * @param roundKeys
	 * @return
	 */
	private short[][][] inverseRoundKeys(short[][][] roundKeys) {
		short[][][] result = new short[11][4][4];
		//int length = roundKeys.length;
		for (int i = 0; i < 11; i++) {
			result[i] = roundKeys[10 - i];
		}
		return result;
	}
	
	/**
	 * one 16 bytes encryption
	 * 
	 * @param initialPTState
	 * @param roundKeys
	 * @param substituteTable
	 * @param mixColumnTable
	 * @param shiftingTable
	 * @return
	 */
	private short[][] encryptOperation(short[][] initialPTState, short[][][] roundKeys, short[][] substituteTable,
			short[][] mixColumnTable, short[][] shiftingTable) {

		// Initial wheel key addition, xor operation
		short[][] state = Converter.xor(roundKeys[0], initialPTState);

		// Handle the first nine rounds
		for (int i = 0; i < 9; i++) {
			// Replaces the bytes of the state array with the bytes of the corresponding
			// position in the s-box
			state = Converter.substituteState(state, substituteTable);

			// Row shift transformation
			state = Converter.shiftRows(state, shiftingTable);

			// Column mixing transformation
			state = Converter.mixColumns(state, mixColumnTable);

			// Wheel key plus transform
			state = Converter.xor(roundKeys[i + 1], state);
		}

		// Deal with the last round
		state = Converter.substituteState(state, substituteTable);

		state = Converter.shiftRows(state, shiftingTable);

		state = Converter.xor(roundKeys[roundKeys.length - 1], state);
		return state;
	}

	/**
	 * one 16 bytes decryption
	 * 
	 * @param initialPTState
	 * @param roundKeys
	 * @param substituteTable
	 * @param mixColumnTable
	 * @param shiftingTable
	 * @return
	 */
	private short[][] decryptOperation(short[][] initialPTState, short[][][] roundKeys, short[][] substituteTable,
			short[][] mixColumnTable, short[][] shiftingTable) {

		short[][][] inverseRoundKeys = inverseRoundKeys(roundKeys);
		// Initial wheel key addition, xor operation
		short[][] state = Converter.xor(inverseRoundKeys[0], initialPTState);

		// Handle the first nine rounds
		for (int i = 0; i < 9; i++) {
			// Row shift transformation
			state = Converter.shiftRows(state, shiftingTable);

			// Replaces the bytes of the state array with the bytes of the corresponding
			// position in the s-box
			state = Converter.substituteState(state, substituteTable);

			// Wheel key plus transform
			state = Converter.xor(inverseRoundKeys[i + 1], state);

			// Column mixing transformation
			state = Converter.mixColumns(state, mixColumnTable);
		}

		// Deal with the last round
		state = Converter.shiftRows(state, shiftingTable);

		state = Converter.substituteState(state, substituteTable);

		state = Converter.xor(inverseRoundKeys[inverseRoundKeys.length - 1], state);
		return state;
	}

	/**
	 * This function is used to encode the plaintext to ciphertext. <If key is
	 * bigger than 16 bytes, only use first 16 bytes. If less than 16 bytes, use
	 * random to 16 bytes>
	 * 
	 * @param plaintext
	 * @param key
	 * @return ciphertext
	 */
	public String Encrypt(String plaintext, String key) {
		String ciphertext = "";
		short[][][] roundKeys = dealKey(key);
		String[] plaintexts = MyPart(plaintext.getBytes(), 16);
		for (int i = 0; i < plaintexts.length; i++) {
			short[][] initialPTState = transfer(transferToShorts(plaintexts[i]));
			short[][] finalState = encryptOperation(initialPTState, roundKeys, SUBSTITUTE_BOX,
					CX, SHIFTING_TABLE);
			String[][] hex = new String[finalState.length][4];
			for (int j = 0; j < finalState.length; j++) {
				for (int k = 0; k < finalState[j].length; k++) {
					hex[j][k] = Integer.toHexString(finalState[j][k]);
					if (hex[j][k].length() == 1) {
						hex[j][k] = "0" + hex[j][k];
					}
				}
			}
			for (int j = 0; j < hex.length; j++) {
				for (int k = 0; k < hex[j].length; k++) {
					ciphertext = ciphertext + hex[j][k] + " ";
				}
			}
		}
		return ciphertext;
	}

	/**
	 * This function is used to decode the ciphertext to plaintext. <If key is
	 * bigger than 16 bytes, only use first 16 bytes. If less than 16 bytes, use
	 * random to 16 bytes>
	 * 
	 * @param ciphertext
	 * @param key
	 * @return plaintext
	 */
	public String Decrypt(String ciphertext, String key) {
		ciphertext = ciphertext.replaceAll(" ", "");
		String finalplaintext = "";
		short[][][] roundKeys = dealKey(key);
		String[] myPart = MyPart(ciphertext.getBytes(), 32);
		for (int i = 0; i < myPart.length; i++) {
			String[] part = new String[16];
			for (int j = 0; j < part.length; j++) {
				part[j] = myPart[i].substring(j * 2, j * 2 + 2);
			}
			short[] ciphertexts = stringToHex(part);
			short[][] finalState = decryptOperation(transfer(ciphertexts), roundKeys,
					INVERSE_SUBSTITUTE_BOX, INVERSE_CX, INVERSE_SHIFTING_TABLE);
			String finalplaintext2 = getOriginString(finalState);
			finalplaintext += finalplaintext2;
		}
		byte[] a = finalplaintext.getBytes();
		byte b = a[a.length - 1];
		int num1 = 0;
		for (int t = 0; t < 16; t++) {
			if (b == supplement[t]) {
				num1 = t;
				break;
			}
		}
		finalplaintext = finalplaintext.substring(0, finalplaintext.length() - num1 - 1);
		return finalplaintext;
	}

	/*
	 * just used to test
	 */
	public static void main(String[] args) {
		AES aes = new AES();
		String key = "fashdf2216511567hgfyf,  ftf151";
		String plaintext = "QWERT. faei f a sdj!# YUI1111111L";
		String ciphertext = aes.Encrypt(plaintext, key);
		plaintext = aes.Decrypt(ciphertext, key);
		System.out.println(plaintext + '\n');
		System.out.println(ciphertext);
	}
}
