package algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * this DES is used to encrypt plain text
 * 
 * @author Zhichao Wang 2019/06/05
 * @version 1.0
 */

public class DES {
	private List<BitSet> allSubKey = new ArrayList<>();
	private String key;
	BitSet keyBitSet = new BitSet(64);

	private final int[] initialPlainText = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46,
			38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11,
			3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
	private final int[] keyPermutation = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35,
			27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37,
			29, 21, 13, 5, 28, 20, 12, 4 };
	private final int[] compressionPermutation = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16,
			7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36,
			29, 32 };

	private final int[] expansionPerm = {

			32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21,
			20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

	private final int[][][] SBOX = {
			{ { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
					{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
					{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
					{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }, },
			{ { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
					{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
					{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
					{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
			{ { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
					{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
					{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
					{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }, },
			{ { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
					{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
					{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
					{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
			{ { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
					{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
					{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
					{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
			{ { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
					{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
					{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
					{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
			{ { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
					{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
					{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
					{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
			{ { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
					{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
					{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
					{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };

	private final int[] PBOX = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3,
			9, 19, 13, 30, 6, 22, 11, 4, 25 };

	private final int[] FinalPermutation = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46,
			14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59,
			27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

	/**
	 * 初始化key
	 * 
	 * @param key
	 */
	public DES(String key) {
		if (key.length() > 8) {
			this.key = key.substring(0, 8);
		} else {
			this.key = key;
		}
		key = this.key;
		String curbit = "";
		for (int i = 0; i < key.length(); i++) {
			curbit += charTobit8(key.charAt(i));
		}
		while (curbit.length() < 64)
			curbit += '0';
		//curbit="0001001100110100010101110111100110011011101111001101111111110001";
		for (int i = 0; i < 64; i++) {

			if (i < curbit.length()) {
				char cur = curbit.charAt(i);
				if (cur == '1') {
					keyBitSet.set(i, true);
				} else {
					keyBitSet.set(i, false);
				}
			} else {
				keyBitSet.set(i, false);
			}
		}
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 用于去除校验位和进行相关变化
	 */
	private BitSet permuteKey(BitSet keyset) {
		BitSet bitSet = new BitSet(56);
		for (int i = 0; i < 56; i++) {
			bitSet.set(i, keyset.get(keyPermutation[i]-1));

		}
			
		return bitSet;
	}

	/**
	 * 用于加密分片后的左移
	 * 
	 * @param set
	 * @param round
	 * @return
	 */
	private BitSet circularShit(BitSet bitset, int round) {
		int shift = 2;
		if (round == 2 || round == 1 || round == 9 || round == 16) {
			shift = 1;
		}
		BitSet shiftedSet = new BitSet(28);
		for (int i = 0; i < 28; i++) {
			shiftedSet.set(((i - shift) + 28) % 28, bitset.get(i));
		}
		return shiftedSet;
	}

	/**
	 * 对生成的56位密钥进行转换
	 * 
	 * @param set
	 * @return
	 */
	private BitSet compressionPrem(BitSet bitset) {
		BitSet subkey = new BitSet(48);
		for (int i = 0; i < 48; i++) {
			subkey.set(i, bitset.get(compressionPermutation[i]-1));
		}
		return subkey;
	}

	/**
	 * 通过十六轮生成子钥 0用于加密 1用于解密
	 */
	public void getSubKey(int type) {
		BitSet keyBit = new BitSet(56);
		BitSet leftSet = new BitSet(28), rightSet = new BitSet(28);
		keyBit = permuteKey(keyBitSet);
		for (int round = 1; round <= 16; round++) {

			for (int i = 0; i < 28; i++) {
				leftSet.set(i, keyBit.get(i));
				rightSet.set(i, keyBit.get(i + 28));
			}
			// 左移右移
				leftSet = circularShit(leftSet, round);
				rightSet = circularShit(rightSet, round);

			// 合并
			keyBit = new BitSet(56);
			for (int i = 0; i < 56; i++) {
					keyBit.set(i, leftSet.get(i));
					keyBit.set(i+28, rightSet.get(i));
				
			}
			allSubKey.add(compressionPrem((BitSet) keyBit.clone()));
		}
	}

	private int BTOD(BitSet bitSet, int length) {
		int num = 0;
		for (int i = 0; i < length; i++) {
			if (bitSet.get(i)) {
				num += Math.pow(2, length - i - 1);
			}
		}
		return num;
	}

	private BitSet DTOB(int num) {
		BitSet bitSet4 = new BitSet(4);
		for (int i = 3; i >= 0; i--) {
			int cur = num >>> i & 1;
			if (cur == 1) {
				bitSet4.set(3 - i, true);
			}
		}
		return bitSet4;
	}

	/**
	 * S-Box tested
	 * 
	 * @param bitSet48
	 * @return
	 */
	private BitSet SBox(BitSet bitSet48) {
		BitSet bitSet32 = new BitSet(32);
		for (int round = 1; round <= 8; round++) {
			BitSet fE = new BitSet(2), middle = new BitSet(4);
			for (int i = 0; i < 6; i++) {
				if (i == 0) {
					fE.set(0, bitSet48.get((round - 1) * 6 + i));
				} else if (i == 5) {
					fE.set(1, bitSet48.get((round - 1) * 6 + i));
				} else
					middle.set(i - 1, bitSet48.get((round - 1) * 6 + i));
			}
			int row = 0, column = 0;
			row = BTOD(fE, 2);
			column = BTOD(middle, 4);
			int res = SBOX[round - 1][row][column];
			BitSet bitSet4 = DTOB(res);
			for (int i = 0; i < 4; i++) {
				bitSet32.set((round - 1) * 4 + i, bitSet4.get(i));
			}
		}
		return bitSet32;
	}

	/**
	 * 处理64位的比特集，对这64位进行加密
	 * 
	 * @param msg
	 * @return
	 */
	public BitSet encryptText(BitSet msg) {

		// initial permutation
		BitSet tempMsg = (BitSet) msg.clone();
		for (int i = 0; i < 64; i++) {
			msg.set(i, tempMsg.get(initialPlainText[i]-1));
		}
		BitSet leftSet = new BitSet(32), rightSet = new BitSet(32);

		for (int round = 1; round <= 16; round++) {

			// 生成左右两个部分
			for (int i = 0; i < 32; i++) {
				leftSet.set(i, msg.get(i));
				rightSet.set(i, msg.get(i + 32));

			}
			BitSet leftFinal = (BitSet) rightSet.clone();
			// EP
			BitSet rightExpand = new BitSet(48);
			// System.err.println("E: "+rightExpand.size());
			for (int i = 0; i < 48; i++) {
				rightExpand.set(i, rightSet.get(expansionPerm[i]-1));
			}
			// XOR
			rightExpand.xor(allSubKey.get(round - 1));

			// S-box
			BitSet bitSet32 = SBox(rightExpand);
			// PBOX
			BitSet pBitSet32 = new BitSet(32);
			for (int i = 0; i < 32; i++) {
				pBitSet32.set(i, bitSet32.get(PBOX[i]-1));
			}
			// XOR
			pBitSet32.xor(leftSet);
			// 连接
			for (int i = 0; i < 32; i++) {
				msg.set(i, leftFinal.get(i));
				msg.set(i + 32, pBitSet32.get(i));
			}
			if (round == 16) {
				for (int i = 0; i < 32; i++) {
					msg.set(i, pBitSet32.get(i));
					msg.set(i + 32, leftFinal.get(i));
				}
			}
		}

		// final permutation
		BitSet clone = (BitSet) msg.clone();
		for (int i = 0; i < 64; i++) {
			msg.set(i, clone.get(FinalPermutation[i]-1));
		}
		return msg;
	}

	public BitSet decryptText(BitSet msg) {
		// initial permutation
		BitSet tempMsg = (BitSet) msg.clone();
		for (int i = 0; i < 64; i++) {
			msg.set(i, tempMsg.get(initialPlainText[i]-1));
		}
		BitSet leftSet = new BitSet(32);
		BitSet rightSet = new BitSet(32);
		for (int round = 1; round <= 16; round++) {
			// 生成左右两个部分
			for (int i = 0; i < 32; i++) {
				leftSet.set(i, msg.get(i));
				rightSet.set(i, msg.get(i + 32));
			}
			BitSet leftFinal = (BitSet) rightSet.clone();
			// EP
			BitSet rightExpand = new BitSet(48);
			for (int i = 0; i < 48; i++) {
				rightExpand.set(i, rightSet.get(expansionPerm[i]-1));
			}
			// XOR
			rightExpand.xor(allSubKey.get(15 - round + 1));

			// S-box
			BitSet bitSet32 = SBox(rightExpand);
			// PBOX
			BitSet pBitSet32 = new BitSet(32);
			for (int i = 0; i < 32; i++) {
				pBitSet32.set(i, bitSet32.get(PBOX[i]-1));
			}
			// XOR
			pBitSet32.xor(leftSet);
			// 连接
			for (int i = 0; i < 32; i++) {
				msg.set(i, leftFinal.get(i));
				msg.set(i + 32, pBitSet32.get(i));
			}
			if (round == 16) {
				for (int i = 0; i < 32; i++) {
					msg.set(i, pBitSet32.get(i));
					msg.set(i + 32, leftFinal.get(i));
				}
			}
		}
		// final permutation
		BitSet clone = (BitSet) msg.clone();
		for (int i = 0; i < 64; i++) {
			msg.set(i, clone.get(FinalPermutation[i]-1));
		}
		return msg;
	}

	/**
	 * 将一个字符转换成8位比特 tested
	 * 
	 * @param word
	 * @return
	 */
	private String charTobit8(char word) {
		String bite = new BigInteger(1, new byte[] { (byte) word }).toString(2);
		StringBuffer strb = new StringBuffer();
		for (int i = 0; i < 8 - bite.length(); i++) {
			strb.append(0);
		}
		strb.append(bite);
		return strb.toString();
	}

	/**
	 * 用于外部直接输入字符串然后分为64位的比特集 tested
	 * 
	 * @param msg
	 * @return
	 */
	public List<BitSet> encryptText(String msg) {
		getSubKey(0);
		List<BitSet> msgSet = new ArrayList<>();
		List<String> fragment = new ArrayList<>();
		for (int i = 0; i < msg.length(); i = i + 8) {
			if (i > msg.length() - 8) {
				fragment.add(msg.substring(i, msg.length()));
			} else {
				fragment.add(msg.substring(i, i + 8));
			}
		}
		for (String string : fragment) {
			// 将每个字串转化成64位比特
			BitSet bitSet64 = new BitSet(64);
			String curbit = "";
			for (int i = 0; i < string.length(); i++) {
				curbit += charTobit8(string.charAt(i));
			}
			while (curbit.length() != 64)
				curbit += '0';
			//System.err.println(curbit);
			for (int i = 0; i < 64; i++) {
				char cur = curbit.charAt(i);
				if (cur == '1') {
					bitSet64.set(i, true);
				} else {
					bitSet64.set(i, false);
				}

			}
			msgSet.add(encryptText(bitSet64));
		}
		return msgSet;
	}

	public String decryptText(List<BitSet> text) {
		getSubKey(0);
		List<BitSet> plainBit = new ArrayList<>();
		for (BitSet bitSet : text) {
			plainBit.add(decryptText(bitSet));
		}

		String result = "";
		for (BitSet bitSet : plainBit) {
			// 8组
			for (int i = 0; i < 8; i++) {
				BitSet bitSet8 = new BitSet(8);
				// 每组8gebit
				for (int j = 0; j < 8; j++) {
					bitSet8.set(j, bitSet.get(j + i * 8));
				}
				int num = 0;
				BitSet right = new BitSet(4);
				BitSet left = new BitSet(4);
				for (int j = 0; j < 4; j++) {
					left.set(j, bitSet8.get(j));
					right.set(j, bitSet8.get(j + 4));
				}
				num=BTOD(left, 4)*16+BTOD(right, 4);
				if (num==0) {
					continue;
				}
				result += (char) num;
			}
		}
		return result;

	}

	/**
	 * 
	 * 
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		DES desE = new DES("a");
		DES desD = new DES("a");
		List<BitSet> bitSets = desE.encryptText("Your lips are smoother than vaseline");
		System.err.println(desD.decryptText(bitSets));

	}
}
