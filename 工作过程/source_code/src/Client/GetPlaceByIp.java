package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.*;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * java根据url获取json对象
 * 
 * @author openks
 * @since 2013-7-16 需要添加java-json.jar才能运行
 */
public class GetPlaceByIp {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
			// System.out.println("同时 从这里也能看出 即便return了，仍然会执行finally的！");
		}
	}

	private static String getIP() {
		String urlStr = "http://www.chacha.cn/"; // 查询网址
		String encoding = "UTF-8";// 编码方式
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes("");// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();

			String reg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";// 匹配ip地址
			String str = buffer.toString();
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(str);
			String res = "";
			if (matcher.find()) {
				res = matcher.group();
			}
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	public String getPosition(){
		String cur="";
		String ip = getIP();
		JSONObject json;
		try {
			json = readJsonFromUrl(
					"http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip="+ip);
		
		cur=((JSONObject) json.get("content")).get("address").toString();} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cur;
	}
	
//	public static void main(String[] args) throws IOException, JSONException {
//
//		String ip = getIP();
//
//		// 这里调用百度的ip定位api服务 详见
//		// http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
//		JSONObject json = readJsonFromUrl(
//				"http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip="+ip);
////		System.out.println(json.toString());
//		System.out.println(((JSONObject) json.get("content")).get("address"));
//	}
}