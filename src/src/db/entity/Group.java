package db.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Group entity class
 * 
 * @author DesmondCobb
 *
 */
public class Group {

	private String groupname = null;
	private List<String> memberUsernameList = null;

	/**
	 * @param groupname
	 * @param memberUsernameList
	 */
	public Group(String groupname, ArrayList<String> memberUsernameList) {
		super();
		this.groupname = groupname;
		this.memberUsernameList = memberUsernameList;
	}

	/**
	 * @return the groupname
	 */
	public String getGroupname() {
		return groupname;
	}

	/**
	 * @param groupname
	 *            the groupname to set
	 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/**
	 * @return the memberUsernameList
	 */
	public List<String> getMemberUsernameList() {
		return memberUsernameList;
	}

	/**
	 * @param memberUsernameList
	 *            the memberUsernameList to set
	 */
	public void setMemberUsernameList(ArrayList<String> memberUsernameList) {
		this.memberUsernameList = memberUsernameList;
	}

}
