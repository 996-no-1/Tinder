package SecurityAlgorithm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * 3DES algorthm
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */

public class TDES {
	private List<String> keys;

	/**
	 * split key to 3 subkeys
	 * 
	 * @param key
	 */
	public TDES(String key) {
		List<String> keys = new ArrayList<>();
		for (int i = 0; i < key.length(); i = i + 8) {
			if (i > key.length() - 8) {
				keys.add(key.substring(i, key.length()));
			} else {
				keys.add(key.substring(i, i + 8));
			}
		}
		while (keys.size() != 3)
			keys.add("");
		this.keys = keys;
	}

	/**
	 * encrypt plaintext
	 * 
	 * @param msg
	 * @return
	 */
	public List<BitSet> encryptMsg(String msg) {
		DES des1 = new DES(keys.get(0));
		List<BitSet> firstSet = des1.encryptText(msg);
		DES des2 = new DES(keys.get(1));
		String secondString = des2.decryptText(firstSet);
		DES des3 = new DES(keys.get(2));
		List<BitSet> thirdSet = des3.encryptText(secondString);
		return thirdSet;
	}

	/**
	 * decrypt cyphertext
	 * 
	 * @param cypherText
	 * @return
	 */
	public String decryptMsg(List<BitSet> cypherText) {
		DES des3 = new DES(keys.get(2));
		String thirdString = des3.decryptText(cypherText);
		DES des2 = new DES(keys.get(1));
		List<BitSet> secondSet = des2.encryptText(thirdString);
		DES des1 = new DES(keys.get(0));
		return des1.decryptText(secondSet);
	}

}
