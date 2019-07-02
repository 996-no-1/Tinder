package SecurityAlgorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

import java.util.List;
/**
 * MD5 algorthm
 * 
 * @author Wang Zhichao 2019/06/25
 * @version 1.0
 */
public class MD5 {
	List<BitSet> block512=new ArrayList<>();
	private final String A="00000001001000110100010101100111";
	private final String B="10001001101010111100110111101111";
	private final String C="11111110110111001011101010011000";
	private final String D="01110110010101000011001000010000";
	private final int S=12;
	
	public MD5(String msg) {
		int msgLength=msg.length();
		String length=""+msgLength;
		for (char cur : length.toCharArray()) {
			length+=charTobit8(cur);
		}
		while(length.length()!=64){
			length="0"+length;
		}
		BitSet bitSet=new BitSet();
		int count=0;
		for (char cur : msg.toCharArray()) {
			String bit8=charTobit8(cur);
			for (char curbit : bit8.toCharArray()) {
				if (curbit=='1') {
					bitSet.set(count, true);
				}else {
					bitSet.set(count, false);
				}
				count++;
			}
		}
		if ((count+64)%512!=0) {
			bitSet.set(count, true);
			count++;
		}
		while((count+64)%512!=0){
			bitSet.set(count, false);
			count++;
		}
		for(int i=0;i<64;i++){
			if (length.charAt(i)=='0') {
				bitSet.set(i+count,false);
			}else {
				bitSet.set(i+count,true);
			}
		}
		count+=64;
		BitSet bitSet512=new BitSet(512);
		for (int i = 0; i < count; i++) {
			bitSet512.set(i%512, bitSet.get(i));
			
			if ((i+1)%512==0) {
				block512.add(bitSet512);
				bitSet512=new BitSet(512);
			}
		}
	}
	
/**
 * convert 0/1 string to bit array
 * @param string
 * @return
 */
	private BitSet stringToBitSet(String string) {
		BitSet bitSet=new BitSet(string.length());
		for(int i=0;i<string.length();i++){
			if (string.charAt(i)=='0') {
				bitSet.set(i,false);
			}else {
				bitSet.set(i,true);
			}
		}
		return bitSet;
	}
	/**
	 * convert char to 8 bit
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
	 * type F 
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private BitSet F(BitSet b,BitSet c,BitSet d) {
		BitSet firstSet=(BitSet)b.clone();
		BitSet secondSet=(BitSet)b.clone();
		firstSet.and(c);
		BitSet allOne=new BitSet(32);
		for (int i = 0; i < 32; i++) {
			allOne.set(i, true);
		}
		secondSet.xor(allOne);
		secondSet.and(d);
		firstSet.or(secondSet);
		return firstSet;
	}
/**
 * type G
 * @param b
 * @param c
 * @param d
 * @return
 */
	private BitSet G(BitSet b,BitSet c,BitSet d) {
		BitSet firstSet=(BitSet)b.clone();
		BitSet secondSet=(BitSet)d.clone();
		firstSet.and(d);
		BitSet allOne=new BitSet(32);
		for (int i = 0; i < 32; i++) {
			allOne.set(i, true);
		}
		secondSet.xor(allOne);
		secondSet.and(c);
		firstSet.or(secondSet);
		return firstSet;
	}
/**
 * Type	H
 * @param b
 * @param c
 * @param d
 * @return
 */
	private BitSet H(BitSet b,BitSet c,BitSet d) {
		BitSet firstSet=(BitSet)d.clone();
		firstSet.xor(c);
		firstSet.xor(b);
		return firstSet;
	}
/**
 * Type I	
 * @param b
 * @param c
 * @param d
 * @return
 */
	private BitSet I(BitSet b,BitSet c,BitSet d) {
		BitSet firstSet=(BitSet)d.clone();
		BitSet allOne=new BitSet(32);
		for (int i = 0; i < 32; i++) {
			allOne.set(i, true);
		}
		firstSet.xor(allOne);
		firstSet.or(b);
		firstSet.xor(c);
		return firstSet;
	}
	/**
	 * add two bitset
	 * @param s1
	 * @param s2
	 * @return
	 */
	private BitSet addBitSet(BitSet s1,BitSet s2) {
		BitSet result=new BitSet(32);
		Boolean flag=false;
		for(int i=0;i<32;i++){
			Boolean cur=false;
			if (s1.get(i)&&s2.get(i)) {//1+1
				cur=true;
				result.set(i,false);
				if (flag) {
					result.set(i,true);
				}
				flag=cur;
			}else if ((!s1.get(i))&&(!s2.get(i))) {//0+0
				cur=false;
				result.set(i, false);
				if (flag) {
					result.set(i, true);
				}
				flag=cur;
			}else {//1+0
				cur=false;
				result.set(i,true);
				if (flag) {
					result.set(i,false);
					flag=true;
				}else {
					flag=cur;
				}
			}
		}
		return result;
	}
	/**
	 * use in circular shift after split into pieces
	 * 
	 * @param set
	 * @param round
	 * @return
	 */
	private BitSet circularShit(BitSet bitset) {
		int shift = S;
		BitSet shiftedSet = new BitSet(32);
		for (int i = 0; i < 32; i++) {
			shiftedSet.set((i - shift + 32) % 32, bitset.get(i));
		}
		return shiftedSet;
	}	
	
	/**
	 * 用于处理将二进制转换成十六进制
	 * 
	 * @param bitSet
	 * @param length
	 * @return
	 */
	private char BTOH(BitSet bitSet, int length) {
		int num = 0;
		for (int i = 0; i < length; i++) {
			if (bitSet.get(i)) {
				num += Math.pow(2, length - i - 1);
			}
		}
		if (num<=9) {
			return (char)(num+48);
		}
		return (char)(num+55);
	}
	/**
	 * get the MD5
	 * 
	 * @return
	 */
	public String processMD5() {
		String result="";
		BitSet A=stringToBitSet(this.A);
		BitSet B=stringToBitSet(this.B);
		BitSet C=stringToBitSet(this.C);
		BitSet D=stringToBitSet(this.D);
		for(BitSet bitSet512:block512){	
			
			List<BitSet> bitSets32=new ArrayList<>();
			BitSet each32=new BitSet(32);//16组
			for (int i = 0; i < 512; i++) {
				each32.set(i%32, bitSet512.get(i));
				if ((i+1)%32==0) {
					bitSets32.add(each32);
					each32=new BitSet(32);
				}
			}
			
			
			for (int round = 1; round <= 64; round++) {
				BitSet eachSet=bitSets32.get((round-1)%16);
				BitSet BCD;//表示第一次的bcd运算
				if (round<=16) {
					BCD=F(B, C, D);
				}else if (round>16&&round<=32) {
					BCD=G(B, C, D);
				}else if (round>32&&round<=48) {
					BCD=H(B, C, D);
				}else {
					BCD=I(B, C, D);
				}
				 BitSet step1=addBitSet(BCD, A);
				 BitSet step2=addBitSet(step1, eachSet);
				String kI=""+(round*1001)%10000;
				String kF="";
				for(char cur:kI.toCharArray()){
					kF+=charTobit8(cur);
				}
				BitSet step3=addBitSet(stringToBitSet(kF), step2);
				BitSet step4=circularShit(step3);
				BitSet step5=addBitSet(B, step4);
				A=(BitSet)D.clone();
				D=(BitSet)C.clone();
				C=(BitSet)B.clone()	;
				B=step5;
			}
			BitSet bitSet=new BitSet(128);
			for (int i = 0; i < 32; i++) {
				bitSet.set(i, A.get(i));
				bitSet.set(i+32, B.get(i));
				bitSet.set(i+64, C.get(i));
				bitSet.set(i+96, D.get(i));	
			}
			for (int i = 0; i < 32; i++) {
				BitSet bitSet4 = new BitSet(4);
				// 每组4gebit
				for (int j = 0; j < 4; j++) {
					bitSet4.set(j, bitSet.get(j + i * 4));
				}
				result += BTOH(bitSet4, 4);
			}
		}
		return result;
	}
	public static void main(String[] args) {
		MD5 md5=new MD5("123");
		System.err.println(md5.processMD5());
		 
	}
	
}
