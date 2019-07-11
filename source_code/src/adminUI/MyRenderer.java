package adminUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

class MyRenderer extends DefaultListCellRenderer {
 
	private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
	
	List<String> curOnlineUserNameList;
	
    public MyRenderer(List<String> curOnlineUserNameList) {
    	this.curOnlineUserNameList = curOnlineUserNameList;
    }
 
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        String curUserName = (String) value;
 
        System.err.println("curNnnn: " + curUserName);
        if(curOnlineUserNameList.contains(curUserName)) {
        	System.err.println("GREENGREEN");
        	setForeground(Color.GREEN);
        }else {
        	System.err.println("REDREDRED");
        	setForeground(Color.blue);
        }
        
        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        
        return this;
    }
}
