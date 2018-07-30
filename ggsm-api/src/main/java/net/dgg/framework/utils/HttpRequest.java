package net.dgg.framework.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequest {
	/**
	 * Send Get to URL
	 * @param url Send Request URL
	 * @param param Request parameter , example: name1=value1&name2=value2 
	 * @return URL Response Result
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// The connection between open and URL
			URLConnection connection = realUrl.openConnection();
			// Set general request property
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.94 Safari/537.36");

			// Establish the actual connection
			connection.connect();
			// Get all the response header fields

			// Definition BufferedReader input stream to read the URL of the
			// response set the encoding format
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("GET requests error " + e);
            throw new RuntimeException("操作异常，请及时联系管理员修复");
		}
		// Use the finally block to close the input stream
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGetHpps(String url, String param) {
		HttpURLConnection urlCon = null;
		String httpsUrl = url + "?" + param;
		String result = "";
		try {
			urlCon = (HttpURLConnection) (new URL(httpsUrl)).openConnection();
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("GET");
			urlCon.setRequestProperty("Content-type", "text/html");
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * POST method to send a request to the specified URL
	 * 
	 * @param url URL transmission request
	 * @param param Request parameters, request parameters should be
	 *            name1=value1&name2=value2
	 * @return Represents the response of remote resources
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// The connection between open and URL
			URLConnection conn = realUrl.openConnection();
			// Set general request property
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.94 Safari/537.36");
			// Send POST request must set the following two lines
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// Get URLConnection object corresponding to the output stream
			out = new PrintWriter(conn.getOutputStream());
			// Send a request parameter
			out.print(param);
			// flush buffered output stream
			out.flush();
			// Definition BufferedReader input stream to read the URL response
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				result += line;
			}
		} catch (Exception e) {
			System.out.println("Send POST request an exception��" + e);
			e.printStackTrace();
		}
		// Use the finally block to close the output stream , the input stream
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 发起post请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求参数
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 * @throws UnknownHostException
	 *             未知主机异常
	 */
	public static String post(String url, Map<String, String> params,String charset, boolean pretty) {
		URL u = null;
		String paramString = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			paramString = sb.substring(0, sb.length() - 1);
		}
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(paramString);
			osw.flush();
			osw.close();
			// 读取返回内容
			br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
			br.close();
		} catch (Exception e) {
//			logger.warn(e.getMessage());
			return null;
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return buffer.toString();
	}

	public static String httpPostWithJSON(String url,JSONObject jsonParam ) throws Exception {

		HttpPost httpPost = new HttpPost(url);
		DefaultHttpClient client = new DefaultHttpClient();
		String respContent = null;

		StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);


		HttpResponse resp = client.execute(httpPost);
		if(resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he,"UTF-8");
		}
		return respContent;
	}

	//字符串传参
	public static String httpPostString(String url,String s,String au) throws Exception {

		HttpPost httpPost = new HttpPost(url);

		/*URL url1 = new URL(url);
		URI uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
		httpPost = new HttpPost(uri);*/

		DefaultHttpClient client = new DefaultHttpClient();
		String respContent = null;
		httpPost.setHeader("Authorization",au);
		httpPost.setHeader("Accept","application/json");
		//httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
		StringEntity entity = new StringEntity(s,"utf-8");//解决中文乱码问题
		/*entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");*/
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if(resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he,"UTF-8");
			System.out.println("respContent-------"+respContent);
		}
		return respContent;
	}

	//字符串传参
	public static String httpPostUnicomString(String url,String s,String au) throws Exception {

		HttpPost httpPost = new HttpPost(url);

		/*URL url1 = new URL(url);
		URI uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
		httpPost = new HttpPost(uri);*/

		DefaultHttpClient client = new DefaultHttpClient();
		String respContent = null;
		httpPost.setHeader("Authorization",au);
		httpPost.setHeader("Accept","application/json");
		httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
		StringEntity entity = new StringEntity(s,"utf-8");//解决中文乱码问题
		/*entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");*/
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if(resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he,"UTF-8");
			System.out.println("respContent-------"+respContent);
		}
		return respContent;
	}

	public static void main(String[] args) throws Exception {
		JSONObject params = new JSONObject();
		params.put("opportunityId","7616773852902854656");
		params.put("customerLevel","1");
		params.put("businessUnitId","7520740165925761024");
		System.out.println(HttpRequest.httpPostWithJSON("http://172.16.2.239:8001/autodispatch/opportunity",params));
	}
}