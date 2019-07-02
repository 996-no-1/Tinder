package data;
/**
 * send message
 */
import java.io.Serializable;
import java.util.List;

public class Envelope implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private String sourceName;
	private List<Object> msg;
	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}
	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * @return the msg
	 */
	public List<Object> getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(List<Object> msg) {
		this.msg = msg;
	}
	
	
}
