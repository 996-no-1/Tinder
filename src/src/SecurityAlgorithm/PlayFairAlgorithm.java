package SecurityAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * this class implement the play fair algorithm
 * 
 * @author Zhichao Wang 2019/06/03
 * @version 1.0
 */

public class PlayFairAlgorithm {
	private final char[][] matrix = { { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' },
			{ 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a' },
			{ 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' }, { 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's' },
			{ 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', ':' }, { '}', '~', '!', '"', '#', '$', '%', '&', '\'' },
			{ '<', '>', '*', '+', ',', '_', '-', '/', '0' }, { '1', '2', '3', '4', '5', '6', '7', '8', '9' } };// the
																												// palyfair's
																												// matrix
	private String key;

	/**
	 * @param key
	 */
	public PlayFairAlgorithm(String key) {
		this.key = key;
	}

	/**
	 * get the current key
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * in term of the key, we generate the matrix we gonna use in play fair
	 * 
	 * @return Matrix
	 */
	private char[][] getFairMatrix() {
		char[][] matrixCur = new char[9][9];
		StringBuffer temp = new StringBuffer();
		for (int i = 0; i < key.length(); i++) {
			String cur = key.substring(i, i + 1);
			if (temp.lastIndexOf(cur) == -1) {
				temp.append(cur);
			}
		}
		int num = 0;
		for (int i = 0; i < temp.length(); i++) {
			matrixCur[i / 9][i % 9] = temp.charAt(i);
			num++;
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (temp.lastIndexOf("" + matrix[i][j]) == -1) {
					matrixCur[num / 9][num % 9] = matrix[i][j];
					num++;
				}
			}

		}
		return matrixCur;
	}

	/**
	 * this function uses playfair to encrypt message with 9*9 matrix
	 * 
	 * @param msg
	 *            the plaintext
	 * @return the ciphertext
	 */
	public String encryptMsg(String msg) {
		char[][] matrixCur = getFairMatrix();
		for (int i = 0; i < msg.length(); i++) {// replace the space with "BMW"
			String sub = msg.substring(i, i + 1);
			if (sub.equals(" ")) {
				msg = msg.substring(0, i) + "BMW" + msg.substring(i + 1, msg.length());
			}
		}
		String cyphertext = "";
		if (msg.length() % 2 != 0) {
			// if the number of characters is odd .add "AOX"
			msg += "AOX";
		}
		List<String> pairs = new ArrayList<>();
		for (int i = 0; i <= msg.length() - 2; i = i + 2) {
			String curPair = msg.substring(i, i + 2);
			if (curPair.charAt(0) == curPair.charAt(1)) {
				String pair1 = "AO";
				String pair2 = "X" + curPair.charAt(1);
				pairs.add(pair1);
				pairs.add(pair2);
			} else {
				pairs.add(curPair);
			}
		}
		for (String cur : pairs) {
			cyphertext += matrixTrick(cur, 0, matrixCur);
		}
		return cyphertext;
	}

	/**
	 * this function uses playfair to decrypt message with 9*9 matrix
	 * 
	 * @return the plaintext
	 */
	public String decryptMsg(String msg) {
		String plaintext = "";
		char[][] matrixCur = getFairMatrix();
		List<String> pairs = new ArrayList<>();
		for (int i = 0; i <= msg.length() - 2; i = i + 2) {
			String curPair = msg.substring(i, i + 2);
			pairs.add(curPair);
		}
		String temp = "";
		for (String cur : pairs) {
			temp += matrixTrick(cur, 1, matrixCur);
		}
		for (int i = temp.length(); i >= 3;) {
			// System.err.println(temp);
			String sub = temp.substring(i - 3, i);
			// .err.println(sub);
			if (sub.equals("BMW")) {
				temp = temp.substring(0, i - 3) + " " + temp.substring(i, temp.length());
				i = i - 3;
			} else if (sub.equals("AOX")) {
				if (i != temp.length()) {
					temp = temp.substring(0, i - 3) + temp.substring(i, i + 1) + temp.substring(i, temp.length());
					i = i - 3;
				} else {
					temp = temp.substring(0, i - 3);
					i = i - 3;
					// System.err.println(temp);
				}
			} else
				i--;
		}
		plaintext = temp;
		return plaintext;
	}

	/**
	 * use to convert primitive letter pair to other pair
	 * 
	 * @param letterPair
	 * @return
	 */
	private String matrixTrick(String letterPair, int mark, char[][] matrix) {
		String res = "";
		int row1 = 0, col1 = 0, row2 = 0, col2 = 0;
		char letter1 = letterPair.charAt(0);
		char letter2 = letterPair.charAt(1);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == letter1) {
					row1 = i;
					col1 = j;
				} else if (matrix[i][j] == letter2) {
					row2 = i;
					col2 = j;
				}
			}
		}
		if (row1 != row2 && col1 != col2) {
			res += matrix[row1][col2];
			res += matrix[row2][col1];
		} else if (row1 == row2 && col1 != col2) {
			if (mark == 0) {
				res += matrix[row1][(col1 + 1) % matrix[0].length];
				res += matrix[row2][(col2 + 1) % matrix[0].length];
			} else {
				res += matrix[row1][(col1 - 1 + matrix[0].length) % matrix[0].length];
				res += matrix[row2][(col2 - 1 + matrix[0].length) % matrix[0].length];
			}
		} else {
			if (mark == 0) {
				res += matrix[(row1 + 1) % matrix[0].length][col1];
				res += matrix[(row2 + 1) % matrix[0].length][col2];
			} else {
				res += matrix[(row1 - 1 + matrix.length) % matrix.length][col1];
				res += matrix[(row2 - 1 + matrix.length) % matrix.length][col2];
			}
		}
		return res;
	}
}
