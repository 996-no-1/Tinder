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
 * java����url��ȡjson����
 * 
 * @author openks
 * @since 2013-7-16 ��Ҫ���java-json.jar��������
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
			// System.out.println("ͬʱ ������Ҳ�ܿ��� ����return�ˣ���Ȼ��ִ��finally�ģ�");
		}
	}

	private static String getIP() {
		String urlStr = "http://www.chacha.cn/"; // ��ѯ��ַ
		String encoding = "UTF-8";// ���뷽ʽ
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// �½�����ʵ��
			connection.setConnectTimeout(2000);// �������ӳ�ʱʱ�䣬��λ����
			connection.setReadTimeout(2000);// ���ö�ȡ���ݳ�ʱʱ�䣬��λ����
			connection.setDoOutput(true);// �Ƿ������� true|false
			connection.setDoInput(true);// �Ƿ��������true|false
			connection.setRequestMethod("POST");// �ύ����POST|GET
			connection.setUseCaches(false);// �Ƿ񻺴�true|false
			connection.connect();// �����Ӷ˿�
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// ����������Զ˷�����д����
			out.writeBytes("");// д����,Ҳ�����ύ��ı� name=xxx&pwd=xxx
			out.flush();// ˢ��
			out.close();// �ر������
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// ���Զ�д�����ݶԶ˷�������������
			// ,��BufferedReader������ȡ
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();

			String reg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";// ƥ��ip��ַ
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
				connection.disconnect();// �ر�����
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
//		// ������ðٶȵ�ip��λapi���� ���
//		// http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
//		JSONObject json = readJsonFromUrl(
//				"http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip="+ip);
////		System.out.println(json.toString());
//		System.out.println(((JSONObject) json.get("content")).get("address"));
//	}
}