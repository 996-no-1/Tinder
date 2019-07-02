package SecurityAlgorithm;

/**
 * This class implement the caesar algorithm
 * 
 * @author Zhichao Wang 2019/06/04
 * @version 1.0
 */
public class CaesarAlgorithm {

	private Integer key;
	
	/**
	 * @param key
	 */
	public CaesarAlgorithm(String key) {
		super();
		this.key = (Integer.parseInt(key))%26;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key.toString();
	}

	/**
	 * this function is used to encrypt plaintext with caesar cipher.
	 * 
	 * @param msg the primitive message 
	 * @return the Ciphertext
	 */
	public String encryptMsg(String msg) {
		if (msg==null||msg=="")return "";
		String ciphertext="";
		for(char cur:msg.toCharArray()){
				if(cur<='z'&&cur>='a'){
				char temp=(char)((cur+key)%('z'+1));
				if (temp<'a') temp+='a';
				ciphertext+=(char)temp;
			}else if(cur<='Z'&&cur>='A'){
				char temp=(char)((cur+key)%('Z'+1));
				if (temp<'A') temp+='A';
				ciphertext+=(char)temp;
			}else{
				cur=(char)(cur+key);
				while(cur<='z'&&cur>='a'||cur<='Z'&&cur>='A')cur=(char)(cur+key);
				ciphertext+=cur;
			}
		}
		return ciphertext;
	}

	/**
	 * This function uses caesar cipher to decrypt cyphertext
	 * 
	 * @param msg the ciphertext
	 * @return the plaintext
	 */
	public String decryptMsg(String cypherText) {
		if (cypherText==null||cypherText=="")return "";
		String plaintext="";
		for(char cur:cypherText.toCharArray()){
			char res=(char) (cur-key);
				if(cur<='z'&&cur>='a'){
					if(res<'a')res+=26;
					plaintext+=res;
			}else if(cur<='Z'&&cur>='A'){
					if(res<'A')res+=26;
					plaintext+=res;
			}else{
				while(res<='z'&&res>='a'||res<='Z'&&res>='A')res=(char)(res-key);
				plaintext+=res;
			}
		}
		return plaintext;	
	}
public static void main(String [] args) {
	CaesarAlgorithm caesarAlgorithm=new CaesarAlgorithm("15");
	String msg="d456123";
	String cypherText=caesarAlgorithm.encryptMsg(msg);
	System.out.println(caesarAlgorithm.decryptMsg(cypherText));
}
}
