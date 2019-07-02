package data;

import java.io.Serializable;
/**
 * Used to package related information when uploading files
 *
 */
public class FileSaved implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
String file;
String sender;
String receiver;
/**
 * @return the file
 */
public String getFile() {
	return file;
}
/**
 * @param file the file to set
 */
public void setFile(String file) {
	this.file = file;
}
/**
 * @return the sender
 */
public String getSender() {
	return sender;
}
/**
 * @param sender the sender to set
 */
public void setSender(String sender) {
	this.sender = sender;
}
/**
 * @return the receiver
 */
public String getReceiver() {
	return receiver;
}
/**
 * @param receiver the receiver to set
 */
public void setReceiver(String receiver) {
	this.receiver = receiver;
}

}
