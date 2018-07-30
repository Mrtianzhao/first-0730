package net.dgg.framework.https;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * <p>@Title https</p>
 * <p>@Description </p>
 * <p>@Version 1.0.0</p>
 * <p>@author rebin</p>
 * <p>@date 2017年7月20日</p>
 * <p>xiefangjian@163.com</p>
 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
 */
public class HttpTools {
	private RequestConfig requestConfig;//请求配置
	private String charset = "UTF-8";
	private String defaulturl = null;//默认服务器地址

	
	/**
	 * *******************************************************
	 * <p>@Description 下载文件</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 上午11:18:54</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	public InputStream DoFile(String url){
		PoolingHttpClientConnectionManager secureConnectionManager = null;
		InputStream instream = null;
		url=durl(url);
		try {
			secureConnectionManager =createManager(url);
			HttpClient httpclient = createHttpClient(secureConnectionManager);
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(requestConfig);
			HttpResponse httpResponse = httpclient.execute(httpget);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				instream = entity.getContent();
			}
		} catch (Exception se) {
			se.printStackTrace();
		}finally{
			if(null!=secureConnectionManager){
				secureConnectionManager.shutdown();
				secureConnectionManager.close();
			}
		}
		return instream;
    }
	/**
	 * *******************************************************
	 * <p>@Description 下载文件</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 上午11:21:34</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	public Boolean DoFile(File file, String url){
		PoolingHttpClientConnectionManager secureConnectionManager = null;
		try {
			url=durl(url);
			secureConnectionManager =createManager(url);
			HttpClient httpclient = createHttpClient(secureConnectionManager);
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(requestConfig);
			HttpResponse httpResponse = httpclient.execute(httpget);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				OutputStream out = new FileOutputStream(file);
				byte tmp[] = new byte[1024];
				int len;
				while ((len = instream.read(tmp, 0, 1024)) != -1) {
					out.write(tmp, 0, len);
				}
				out.close();
				instream.close();
			}
			return true;
		} catch (Exception se) {
		}finally{
			if(null!=secureConnectionManager){
				secureConnectionManager.shutdown();
				secureConnectionManager.close();
			}
		}
		return false;
	}

	/**
	 * *******************************************************
	 * <p>@Description 上传文件</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 下午3:01:10</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	public HttpResult UpFile(File f, String url) {
		PoolingHttpClientConnectionManager secureConnectionManager = null;
		try {
			url=durl(url);
			secureConnectionManager =createManager(url);
			HttpClient httpclient = createHttpClient(secureConnectionManager);
			HttpPost httpost = new HttpPost(url);
			httpost.setConfig(requestConfig);
			FileBody file = new FileBody(f);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("media", file);
			httpost.setEntity(builder.build());
			HttpResponse httpResponse = httpclient.execute(httpost);
			return new HttpResult(httpResponse.getStatusLine().getStatusCode(),EntityUtils.toString(httpResponse.getEntity()));
		} catch (Exception se) {
			return new HttpResult(900, se.getMessage());
		}finally{
			if(null!=secureConnectionManager){
				secureConnectionManager.shutdown();
				secureConnectionManager.close();
			}
		}
	}

	/**
	 * *******************************************************
	 * <p>@Description 上传文件</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 下午3:01:23</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	public HttpResult UpFile(InputStream in, String filename, String url) {
		PoolingHttpClientConnectionManager secureConnectionManager = null;
		try {
			url=durl(url);
			secureConnectionManager =createManager(url);
			HttpClient httpclient = createHttpClient(secureConnectionManager);
			HttpPost httpost = new HttpPost(url);
			HFileBody file = new HFileBody(in, filename);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("media", file);
			httpost.setEntity(builder.build());
			HttpResponse httpResponse = httpclient.execute(httpost);
			return new HttpResult(httpResponse.getStatusLine().getStatusCode(),EntityUtils.toString(httpResponse.getEntity()));
		} catch (Exception se) {
			return new HttpResult(900, se.getMessage());
		}finally{
			if(null!=secureConnectionManager){
				secureConnectionManager.shutdown();
				secureConnectionManager.close();
			}
		}
	}

	/**
	 * *******************************************************
	 * <p>@Description post地址</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 上午11:08:02</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	public HttpResult PostJson(String url)
    {
        return post(url, null, null);
    }

	/**
	 * *******************************************************
	 * <p>@Description post json数据</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 上午11:08:14</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
    public HttpResult PostJson(String url, String json)
    {
        return post(url, json, null);
    }

    /**
     * *******************************************************
     * <p>@Description post 参数</p>
     * <p>@Version 1.0.0</p>
     * <p>@author rebinrebin</p>
     * <p>@time 2017年7月20日 上午11:08:32</p>
     * <p>xiefangjian@163.com</p>
     * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
     * *****************************************************
     */
    public HttpResult PostJson(String url, Map<String,String> params)
    {
        return post(url, null,params);
    }
	/**
	 * *******************************************************
	 * <p>@Description 请iu参数</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月19日 上午11:01:02</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HttpResult post(String url,String json,Map<String,String> pars){
		 url=durl(url);
		 HttpPost httpPost = new HttpPost(url);// 创建httpPost    
		 httpPost.setConfig(requestConfig);
		 PoolingHttpClientConnectionManager secureConnectionManager = null;
		 try{
			 if(json != null && json.trim().length() > 0 && pars != null && !pars.isEmpty())
		        {
		            if(url.indexOf("?") == -1)
		            {
		                url = (new StringBuilder(String.valueOf(url))).append("?").toString();
		            }
		            for(Iterator iterator = pars.keySet().iterator(); iterator.hasNext();)
		            {
		                String key = (String)iterator.next();
		                url = (new StringBuilder(String.valueOf(url))).append("&").append(key).append("=").append((String)pars.get(key)).toString();
		            }
		
		        }
		        HttpPost httpost = new HttpPost(url);
		        if(json == null && pars != null && !pars.isEmpty())
		        {
		            List nvps = new ArrayList();
		            String key;
		            for(Iterator iterator1 = pars.keySet().iterator(); iterator1.hasNext(); nvps.add(new BasicNameValuePair(key, String.valueOf(pars.get(key)))))
		            {
		                key = (String)iterator1.next();
		            }
		
		            httpost.setEntity(new UrlEncodedFormEntity(nvps,charset));
		        }
		        if(json != null && json.trim().length() > 0)
		        {
		            StringEntity reqEntity = new StringEntity(json,charset);
		            httpost.setEntity(reqEntity);
		        }
		        secureConnectionManager =createManager(url);
		        HttpClient httpclient  = createHttpClient(secureConnectionManager);
		        HttpResponse httpResponse = httpclient.execute(httpost);
		        if(httpResponse.getStatusLine().getStatusCode() == 200)
		        {
		        	return  new HttpResult(200, EntityUtils.toString(httpResponse.getEntity()));
		        }else{
		        	return  new HttpResult(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
		        }
		 }catch(Exception se){
			 se.printStackTrace();
			 return  new HttpResult(900, se.getMessage());
		 }finally{
			 if(null!=secureConnectionManager){
				 secureConnectionManager.shutdown();
				 secureConnectionManager.close();
			 }
		 }
	}
	/**
	 * *******************************************************
	 * <p>@Description </p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月19日 下午1:31:00</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	private HttpClient createHttpClient(PoolingHttpClientConnectionManager secureConnectionManager){
		return  HttpClients.custom().setConnectionManager(secureConnectionManager).build();
	}
	/**
	 * *******************************************************
	 * <p>@Description createManager</p>
	 * <p>@Version 1.0.0</p>
	 * <p>@author rebinrebin</p>
	 * <p>@time 2017年7月20日 下午2:38:09</p>
	 * <p>xiefangjian@163.com</p>
	 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
	 * *****************************************************
	 */
	private PoolingHttpClientConnectionManager createManager(String url){
		try {
			url=durl(url);
			if(url.toLowerCase().startsWith("https")){
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, new TrustManager[] {
					new X509TrustManager() {
						public void checkClientTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {}
						public void checkServerTrusted(X509Certificate ax509certificate[], String s)throws CertificateException {}
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
					}
			    }, null);
	            ConnectionSocketFactory plainSocketFactory = new PlainConnectionSocketFactory();
	            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslContext);
				Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
		                    .register("http", plainSocketFactory)
		                    .register("https", ssf)
		                    .build();
				return new PoolingHttpClientConnectionManager(r);
			}
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
		return new PoolingHttpClientConnectionManager();
	}
	//==========================================================
	public String durl(String url){
		if(url==null){
			return defaulturl;
		}
		return url;
	}
	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getDefaulturl() {
		return defaulturl;
	}
	public void setDefaulturl(String defaulturl) {
		this.defaulturl = defaulturl;
	}
}
