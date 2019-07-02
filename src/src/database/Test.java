package database;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.List;

import database.*;
import bean.*;
import data.FileSaved;
import data.FileSend;

public class Test {

	public static void main(String[] args) throws IOException {
//		FileDao fd = new FileDao();
//		List<FileSend> flist = fd.getFile("dc", "who");
//		byte[] bytes = flist.get(0).getFile();
//		String s = new String(bytes, "ISO-8859-1");
//		System.out.println(s);
		
//		UserDao userdao = new UserDao();
//		User u = new User();
//		u.setUsername("hpj");
//		u.setPassword("134134134");
//		userdao.addUser(u);
//		u.setPublickey("ahfauiofhasf-aifeigfmegm");
////		userdao.updatePublicKey(u);
//		byte[] buffer = null;
//		FileInputStream fis = new FileInputStream(new File("C:\\Users\\15360\\Desktop\\ROA——Test_Hpj.docx"));
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
//        byte[] b = new byte[1000];  
//        int n;  
//        while ((n = fis.read(b)) != -1) {  
//            bos.write(b, 0, n);  
//        }  
//        fis.close();  
//        bos.close();  
//        buffer = bos.toByteArray();
//        
//        BufferedOutputStream bbos = null;  
//        FileOutputStream fos = null;  
//        File file = null;
//        try {  
//            File dir = new File("C:\\Users\\15360\\Desktop");  
//            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
//                dir.mkdirs();  
//            }  
//            file = new File("C:\\Users\\15360\\Desktop\\RRRR.docx");  
//            fos = new FileOutputStream(file);  
//            bbos = new BufferedOutputStream(fos);  
//            bbos.write(buffer);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        } finally {  
//            if (bos != null) {  
//                try {  
//                    bos.close();  
//                } catch (IOException e1) {  
//                    e1.printStackTrace();  
//                }  
//            }  
//            if (fos != null) {  
//                try {  
//                    fos.close();  
//                } catch (IOException e1) {  
//                    e1.printStackTrace();  
//                }  
//            }  
//        } 
		
		FileSaved fs = new FileSaved();
		fs.setFile("aifhaiopfeiqfjqfniweqfnwief-----afaidflnadfaf");
		fs.setReceiver("hpj");
		fs.setSender("asdasd");
		HistoryDao hdao = new HistoryDao();
		hdao.uploadHistory(fs);
		
	}


}
